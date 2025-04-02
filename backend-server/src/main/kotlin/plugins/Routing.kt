package com.ite393group5.plugins

import com.ite393group5.dao.documents.DocumentDAOImpl
import com.ite393group5.models.LocationInfo
import com.ite393group5.models.LoginRequest
import com.ite393group5.models.PersonalInfo
import com.ite393group5.models.Token
import com.ite393group5.models.TokenConfig
import com.ite393group5.models.User
import com.ite393group5.routes.staffRoutes
import com.ite393group5.routes.studentRoutes
import com.ite393group5.services.appointment.AppointmentService
import com.ite393group5.services.user.UserService
import com.ite393group5.utilities.JwtTokenService
import com.ite393group5.utilities.SHA256HashingService
import com.ite393group5.utilities.SaltedHash
import com.ite393group5.utilities.shaService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.withLock
import java.time.LocalDate

fun Application.configureRouting(
    userServiceImpl: UserService,
    appointmentServiceImpl: AppointmentService,
    documentDAOImpl: DocumentDAOImpl,
    tokenService: JwtTokenService,
    studentTokenConfig: TokenConfig,
    staffTokenConfig: TokenConfig,
    adminTokenConfig: TokenConfig
) {

    routing {
        //region login route for all users
        post("/login") {
            val user = call.receive<LoginRequest>()
            val password = user.password
            val username = user.username
            val findUser = userServiceImpl.findByUsername(username)
            println("User Role: ${findUser?.role}")
            if (findUser != null) {
                val saltedHash = SaltedHash(findUser.password, findUser.salt)

                if (shaService.verify(password, saltedHash)) {

                    val generatedToken = when (findUser.role) {
                        "student" -> Token(
                            bearerToken = tokenService.generateStudentToken(studentTokenConfig, findUser.username),
                            refreshToken = "refresh_Token",
                            expirationTimeDate = LocalDate.now().plusDays(30)
                        )
                        "staff" -> Token(
                            bearerToken = tokenService.generateStaffToken(staffTokenConfig, findUser.username, findUser.role),
                            refreshToken = "refresh_Token",
                            expirationTimeDate = LocalDate.now().plusDays(1)
                        )
                        "admin" -> Token(
                            bearerToken = tokenService.generateStaffToken(adminTokenConfig, findUser.username, findUser.role),
                            refreshToken = "refresh_Token",
                            expirationTimeDate = LocalDate.now().plusDays(1)
                        )
                        else -> {
                            call.respond(HttpStatusCode.Forbidden, mapOf("error" to "Unknown role"))
                            return@post
                        }
                    }

                    println(findUser.username + " has logged in")
                    call.respond(HttpStatusCode.OK, generatedToken)

                } else {
                    call.respond(HttpStatusCode.Unauthorized, mapOf("error" to "User not found"))
                }
            }
        }
        //endregion

        //region add routes from staff and student Routes
        staffRoutes(userServiceImpl,
            tokenService,
            studentTokenConfig,
            staffTokenConfig,
            appointmentServiceImpl)

        studentRoutes(
            userServiceImpl,
            appointmentServiceImpl,
            documentDAOImpl
        )
        //endregion

        //region websockets
        webSocket("/student-queue-channel")  {

            send("Connected to queue server")
            queueResponseFlow.emit("connected-queue")

            mutex.withLock {
                activeSessions.add(this)
                println("Active sessions size: ${activeSessions.size}")
            }

            queueFlow.collect { message ->
                send(message)
            }

            val job = launch {
                queueFlow.collect { message ->
                    println("Broadcasting: $message")
                    mutex.withLock {
                        activeSessions.forEach { session ->
                            session.send(message)
                        }
                    }
                }
            }

            try {
                println("Joining job...")
                job.join()  // If this is blocking indefinitely, check your flow emissions
                println("no errors")
            } catch (e: Exception) {
                println("Error: ${e.message}")
            } finally {
                println("Cleaning up...")
                mutex.withLock {
                    activeSessions.remove(this)
                }
                job.cancel()  // Cancel job explicitly before trying to join
                println("Job canceled")
            }
        }
        //endregion

        //region must-delete-route
        //delete this later on

        post("/createUsersForTesting") {
            try {
                val userService = userServiceImpl
                val hashingService = SHA256HashingService()

                val users = listOf(
                    // Students
                    Triple("student1@phinmaed.com.test", "student1@phinmaed.com.test", "student123"),
                    Triple("student2@phinmaed.com.test", "student2@phinmaed.com.test", "student123"),
                    Triple("student3@phinmaed.com.test", "student3@phinmaed.com.test", "student123"),
                    // Staff
                    Triple("staff1@phinmaed.com.test", "staff1@phinmaed.com.test", "staff123"),
                    Triple("staff2@phinmaed.com.test", "staff2@phinmaed.com.test", "staff123"),
                    Triple("staff3@phinmaed.com.test", "staff3@phinmaed.com.test", "staff123"),
                    // Admins
                    Triple("admin1@phinmaed.com.test", "admin1@phinmaed.com.test", "admin123"),
                    Triple("admin2@phinmaed.com.test", "admin2@phinmaed.com.test", "admin123")
                )

                val createdUsers = mutableListOf<User>()

                users.forEachIndexed { index, (username, email, password) ->
                    val role = when {
                        index < 3 -> "student"
                        index < 6 -> "staff"
                        else -> "admin"
                    }
                   log.info("Adding user $username to $role with email $email and password $password")
                    val saltedHash = hashingService.generateSaltedHash(password)
                    val user = User(
                        id = null,
                        username = username,
                        password = saltedHash.hash,
                        role = role,
                        salt = saltedHash.salt
                    )

                    val locationInfo = LocationInfo(
                        houseNumber = "${100 + index}",
                        street = "$role St.",
                        zone = "${index + 1}",
                        barangay = "$role Barangay",
                        cityMunicipality = "$role City",
                        province = "$role Province",
                        country = "PH",
                        postalCode = "100${index}"
                    )

                    val personalInfo = PersonalInfo(
                        firstName = "$role FirstName$index",
                        lastName = "$role LastName$index",
                        middleName = "M",
                        extensionName = "",
                        gender = if (index % 2 == 0) "Male" else "Female",
                        citizenship = "Filipino",
                        religion = "Catholic",
                        civilStatus = if (index % 2 == 0) "Single" else "Married",
                        email = email,
                        number = "0912345678${index}",
                        birthDate = LocalDate.of(2000 + index, (index % 12) + 1, (index % 28) + 1),
                        fatherName = "$role Father$index",
                        motherName = "$role Mother$index",
                        spouseName = if (role == "admin") "Admin Spouse$index" else null,
                        contactPersonNumber = "0912987654${index}"
                    )

                    val createdUser = userService.register(user, locationInfo, personalInfo)
                    createdUsers.add(createdUser)
                }

                call.respond(HttpStatusCode.Created, mapOf("users" to createdUsers))
            } catch (e: Exception) {
                e.printStackTrace()
                call.respond(HttpStatusCode.InternalServerError, "Error creating users: ${e.localizedMessage}")
            }
        }

        //endregion-later

        //this should be limited to 5 calls per restart and refilled every hour for the health-check
        get("/health-check") {
            call.respond(HttpStatusCode.OK, "Server is healthy")
        }

    }
}

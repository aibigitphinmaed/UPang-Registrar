package com.ite393group5.plugins

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
    tokenService: JwtTokenService,
    studentTokenConfig: TokenConfig,
    staffTokenConfig: TokenConfig
) {

    routing {
        //region login route for all users
        post("/login") {
            val user = call.receive<LoginRequest>()
            val password = user.password
            val username = user.username
            val findUser = userServiceImpl.findByUsername(username)
            if (findUser != null) {
                val saltedHash = SaltedHash(findUser.password, findUser.salt)
                if (shaService.verify(password, saltedHash)) {
                    val generatedToken = when (findUser.role) {
                        "student" -> Token(
                            bearerToken = tokenService.generateStudentToken(studentTokenConfig, findUser.username),
                            refreshToken = "refresh_Token",
                            expirationTimeDate = LocalDate.now().plusDays(30)
                        )
                        //not deleting this just in case we decided to create a mobile app for the staff user
                        "staff" -> Token(
                            bearerToken = tokenService.generateStaffToken(staffTokenConfig, findUser.username),
                            refreshToken = "refresh_Token",
                            expirationTimeDate = LocalDate.now().plusDays(1)
                        )else -> {
                            call.respond(HttpStatusCode.Forbidden, mapOf("error" to "Unknown role"))
                            return@post
                        }
                    }
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
            staffTokenConfig)

        studentRoutes(
            userServiceImpl,
            appointmentServiceImpl
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
                val studentSaltedHash = SHA256HashingService().generateSaltedHash("student123")
                val studentUser = User(
                    id = null,
                    username = "student123",
                    password = studentSaltedHash.hash,
                    role = "student",
                    salt = studentSaltedHash.salt,
                )

                val staffSaltedHash = SHA256HashingService().generateSaltedHash("staff123")

                val staffUser = User(
                    id = null,
                    username = "staff123",
                    password = staffSaltedHash.hash,
                    role = "staff",
                    salt = staffSaltedHash.salt,
                )

                val studentLocation = LocationInfo(
                    houseNumber = "123",
                    street = "Student St.",
                    zone = "1",
                    barangay = "Student Barangay",
                    cityMunicipality = "Student City",
                    province = "Student Province",
                    country = "PH",
                    postalCode = "1234"
                )

                val staffLocation = LocationInfo(
                    houseNumber = "456",
                    street = "Staff St.",
                    zone = "2",
                    barangay = "Staff Barangay",
                    cityMunicipality = "Staff City",
                    province = "Staff Province",
                    country = "PH",
                    postalCode = "5678"
                )

                val studentPersonalInfo = PersonalInfo(
                    firstName = "John",
                    lastName = "Doe",
                    middleName = "S",
                    extensionName = "",
                    gender = "Male",
                    citizenship = "Filipino",
                    religion = "Catholic",
                    civilStatus = "Single",
                    email = "student123@email.com",
                    number = "09123456789",
                    birthDate = LocalDate.of(2002, 5, 10),
                    fatherName = "Father Doe",
                    motherName = "Mother Doe",
                    spouseName = null,
                    contactPersonNumber = "09129876543"
                )

                val staffPersonalInfo = PersonalInfo(
                    firstName = "Jane",
                    lastName = "Smith",
                    middleName = "M",
                    extensionName = "",
                    gender = "Female",
                    citizenship = "Filipino",
                    religion = "Catholic",
                    civilStatus = "Married",
                    email = "staff123@email.com",
                    number = "09234567890",
                    birthDate = LocalDate.of(1990, 8, 20),
                    fatherName = "Father Smith",
                    motherName = "Mother Smith",
                    spouseName = "Mr. Smith",
                    contactPersonNumber = "09345678901"
                )

                // Register users
                val createdStudent = userServiceImpl.register(studentUser, studentLocation, studentPersonalInfo)
                val createdStaff = userServiceImpl.register(staffUser, staffLocation, staffPersonalInfo)

                call.respond(HttpStatusCode.Created, mapOf("student" to createdStudent, "staff" to createdStaff))
            } catch (e: Exception) {
                e.printStackTrace()
                call.respond(HttpStatusCode.InternalServerError, "Error creating users: ${e.localizedMessage}")
            }
        }
        //endregion-later
    }
}

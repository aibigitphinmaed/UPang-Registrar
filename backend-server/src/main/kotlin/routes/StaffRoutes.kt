package com.ite393group5.routes

import com.ite393group5.dto.StudentProfile
import com.ite393group5.models.LocationInfo
import com.ite393group5.models.PersonalInfo
import com.ite393group5.models.TokenConfig
import com.ite393group5.models.User
import com.ite393group5.models.UserSession
import com.ite393group5.services.UserService
import com.ite393group5.utilities.JwtTokenService
import com.ite393group5.utilities.SHA256HashingService
import com.ite393group5.utilities.SaltedHash
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.freemarker.*
import io.ktor.server.http.content.static
import io.ktor.server.http.content.staticResources
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.sessions
import java.time.LocalDate


fun Route.staffRoutes(
    userServiceImpl: UserService,
    tokenService: JwtTokenService,
    studentTokenConfig: TokenConfig,
    staffTokenConfig: TokenConfig
) {
    authenticate("staff-session") {



        staticResources("/static",
            "static"
        )

        get("hello-staff") {
            call.respondText("Hello Staff!")
        }

        get("/dashboard") {
            val userPrincipal = call.sessions.get("staff-session") as UserSession

            if (userPrincipal.userId != null) {
                val userID = userPrincipal.userId
                val personalInfo = userServiceImpl.retrieveProfileById(userID)
                val locationInfo = userServiceImpl.retrieveAddressById(userID)
                println("Welcome ${userPrincipal.username}")
                val data = mapOf(
                    "title" to "Staff Dashboard",
                    "username" to userPrincipal.username,
                    "personalInfo" to personalInfo,
                    "locationInfo" to locationInfo,
                )
                call.respond(FreeMarkerContent("dashboard.ftl", data))

            } else {
                println("User not authenticated")
                call.respondRedirect("/")
            }
        }

        get("/staff/add-student"){
            val staffSession = call.sessions.get("staff-session") as UserSession
            if (staffSession == null || staffSession.role != "staff") {
                call.respond(HttpStatusCode.Unauthorized, "Access Denied")
                return@get
            }
            call.respond(FreeMarkerContent("add_student.ftl", staffSession))
        }

        post("/staff/add-student") {
            val staffSession = call.sessions.get("staff-session") as UserSession
            if (staffSession == null || staffSession.role != "staff") {
                call.respond(HttpStatusCode.Unauthorized, "Access Denied")
                return@post
            }


            try {
                val studentRequest = call.receive<StudentProfile>()
                val studentPersonalInfo = studentRequest.studentPersonalInfo

                val plainPassword = studentPersonalInfo?.firstName+studentPersonalInfo?.middleName+studentPersonalInfo?.lastName

                // Hash the password before storing it
                val studentSaltedHash = SHA256HashingService().generateSaltedHash(plainPassword)

                val userToCreate = User(
                    password = studentSaltedHash.hash, salt = studentSaltedHash.salt,
                    id = null,
                    username = studentPersonalInfo?.email ?: "",
                    role = "student"
                )


                val createdStudent = userServiceImpl.registerStudent(userToCreate,studentRequest)

                call.respond(HttpStatusCode.Created, "Student ${createdStudent.username} created successfully")

            } catch (e: Exception) {
                e.printStackTrace()
                call.respond(HttpStatusCode.InternalServerError, "Error creating student: ${e.localizedMessage}")
            }
        }

        get("/staff/get-students") {
            val staffSession = call.sessions.get("staff-session") as UserSession
            if (staffSession == null || staffSession.role != "staff") {
                call.respond(HttpStatusCode.Unauthorized, "Access Denied")
                return@get
            }

            try {
                 val students = userServiceImpl.getStudents()
                 call.respond(HttpStatusCode.OK, students)
            } catch (e: Exception) {
                e.printStackTrace()
                call.respond(HttpStatusCode.InternalServerError, "Error fetching students")
            }
        }

    }


}
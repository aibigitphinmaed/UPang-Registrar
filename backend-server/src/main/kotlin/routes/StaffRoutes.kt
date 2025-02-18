package com.ite393group5.routes

import com.ite393group5.dto.UserProfile
import com.ite393group5.models.TokenConfig
import com.ite393group5.models.User
import com.ite393group5.models.UserSession
import com.ite393group5.services.UserService
import com.ite393group5.utilities.JwtTokenService
import com.ite393group5.utilities.SHA256HashingService
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.freemarker.*
import io.ktor.server.http.content.staticResources
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.sessions


fun Route.staffRoutes(
    userServiceImpl: UserService,
    tokenService: JwtTokenService,
    studentTokenConfig: TokenConfig,
    staffTokenConfig: TokenConfig
) {
    authenticate("staff-auth") {

        staticResources("/static",
            "static"
        )

        post("/staff/add-student") {
            val staffPrincipal = call.principal<JWTPrincipal>()!!
            val username = staffPrincipal.payload.getClaim("username").asString()
            val userid = userServiceImpl.findByUsername(username)?.id
            val staffUser = userServiceImpl.findByUsername(username)

            if (staffPrincipal == null || staffUser?.role != "staff") {
                call.respond(HttpStatusCode.Unauthorized, "Access Denied")
                return@post
            }

            try {
                val studentRequest = call.receive<UserProfile>()
                val studentPersonalInfo = studentRequest.userPersonalInfo

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

    }


}
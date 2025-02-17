package com.ite393group5.plugins

import com.ite393group5.models.LoginRequest
import com.ite393group5.models.Token
import com.ite393group5.models.TokenConfig
import com.ite393group5.models.UserSession
import com.ite393group5.routes.staffRoutes
import com.ite393group5.routes.studentRoutes
import com.ite393group5.services.StudentServiceImpl
import com.ite393group5.services.UserService
import com.ite393group5.utilities.JwtTokenService
import com.ite393group5.utilities.SaltedHash
import com.ite393group5.utilities.shaService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.freemarker.FreeMarkerContent
import io.ktor.server.http.content.files
import io.ktor.server.http.content.staticFiles
import io.ktor.server.http.content.staticResources
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.sessions
import java.time.LocalDate

fun Application.configureRouting(
    userServiceImpl: UserService,
    studentService: StudentServiceImpl,
    tokenService: JwtTokenService,
    studentTokenConfig: TokenConfig,
    staffTokenConfig: TokenConfig
) {

    routing {
        get("/logout") {
            call.sessions.clear("staff-session")
            if(call.sessions.get("staff-session") == null) {
                call.respondRedirect("/")
            }
        }

        get("/") {
            val maySession = call.sessions.get("staff-session")
            if(maySession != null){
                call.respondRedirect("/dashboard")
            }else{
                call.respondRedirect("/login")
            }
        }

        get("/login"){
            val maySession = call.sessions.get("staff-session")
            if(maySession != null){
                call.respondRedirect("/dashboard")
            }else{
                call.respond(FreeMarkerContent("login.ftl", null))
            }

        }

        post("/login") {
            val userAgent = call.request.headers["User-Agent"] ?: "unknown"
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
                        )
                        else -> {
                            call.respond(HttpStatusCode.Forbidden, mapOf("error" to "Unknown role"))
                            return@post
                        }
                    }

                    if (userAgent.contains("Students", ignoreCase = true)) {
                        call.respond(HttpStatusCode.OK, generatedToken)
                    } else {
                        println("Setting session for staff: ${findUser.username}")
                        call.sessions.set("staff-session" ,
                            UserSession(
                                username = findUser.username,
                                userId = findUser.id,
                                role = findUser.role
                            )
                        )
                        println("Redirecting to /dashboard")
                        call.respondRedirect("/dashboard")
                    }
                } else {
                    call.respond(HttpStatusCode.Unauthorized, mapOf("error" to "Invalid credentials"))
                }
            } else {
                call.respond(HttpStatusCode.Unauthorized, mapOf("error" to "User not found"))
            }
        }

        staffRoutes(userServiceImpl,
            tokenService,
            studentTokenConfig,
            staffTokenConfig)

        studentRoutes(userServiceImpl,studentService)


    }
}

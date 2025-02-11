package com.ite393group5.plugins

import com.ite393group5.models.LoginRequest
import com.ite393group5.models.Token
import com.ite393group5.models.TokenConfig
import com.ite393group5.routes.staffRoutes
import com.ite393group5.routes.studentRoutes
import com.ite393group5.services.UserService
import com.ite393group5.utilities.JwtTokenService
import com.ite393group5.utilities.SaltedHash
import com.ite393group5.utilities.shaService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.time.LocalDate

fun Application.configureRouting(
    userServiceImpl: UserService,
    tokenService: JwtTokenService,
    studentTokenConfig: TokenConfig,
    staffTokenConfig: TokenConfig
) {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        post("/login") {

            val user = call.receive<LoginRequest>()
            val password = user.password
            val username = user.username
            val findUser = userServiceImpl.findByUsername(username)



            if (findUser != null) {

                val saltedHash = SaltedHash(findUser.password, findUser.salt)
                if (shaService.verify(password, saltedHash)) {
                    if (findUser.role == "student") {
                        val generatedToken =
                            Token(
                                bearerToken = tokenService.generateStudentToken(studentTokenConfig, findUser.username),
                                refreshToken = "refresh_Token",
                                expirationTimeDate = LocalDate.now().plusDays(30)
                            )
                        call.respond(HttpStatusCode.OK, generatedToken)
                    }
                    else if (findUser.role == "staff") {
                        val generatedToken =
                            Token(
                                bearerToken = tokenService.generateStaffToken(staffTokenConfig, findUser.username),
                                refreshToken = "refresh_Token",
                                expirationTimeDate = LocalDate.now().plusDays(1)
                            )
                        call.respond(HttpStatusCode.OK, generatedToken)
                    }else {
                        call.respond(HttpStatusCode.Forbidden, mapOf("error" to "no role are you even a staff or a student in our school?"))
                    }
                }
            } else {
                call.respond(HttpStatusCode.Unauthorized, mapOf("error" to "no user exists go to SIS office for account concerns"))
            }
        }

        staffRoutes(userServiceImpl,
            tokenService,
            studentTokenConfig,
            staffTokenConfig)

        studentRoutes(userServiceImpl)

    }
}

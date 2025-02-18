package com.ite393group5.plugins

import com.ite393group5.models.LoginRequest
import com.ite393group5.models.Token
import com.ite393group5.models.TokenConfig
import com.ite393group5.routes.staffRoutes
import com.ite393group5.routes.studentRoutes
import com.ite393group5.services.StudentServiceImpl
import com.ite393group5.services.UserService
import com.ite393group5.utilities.JwtTokenService
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
    studentService: StudentServiceImpl,
    tokenService: JwtTokenService,
    studentTokenConfig: TokenConfig,
    staffTokenConfig: TokenConfig
) {

    routing {

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

        staffRoutes(userServiceImpl,
            tokenService,
            studentTokenConfig,
            staffTokenConfig)

        studentRoutes(userServiceImpl,studentService)

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

    }
}

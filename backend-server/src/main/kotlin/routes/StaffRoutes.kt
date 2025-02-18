package com.ite393group5.routes

import com.ite393group5.dto.UserProfile
import com.ite393group5.models.TokenConfig
import com.ite393group5.models.User
import com.ite393group5.plugins.currentQueueList
import com.ite393group5.plugins.previousQueueList
import com.ite393group5.plugins.queueResponseFlow
import com.ite393group5.services.UserService
import com.ite393group5.utilities.JwtTokenService
import com.ite393group5.utilities.SHA256HashingService
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.http.content.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Route.staffRoutes(
    userServiceImpl: UserService,
    tokenService: JwtTokenService,
    studentTokenConfig: TokenConfig,
    staffTokenConfig: TokenConfig
) {
    authenticate("staff-auth") {

        post("/test-staff-call"){
            call.respond(HttpStatusCode.OK,"staff called an api")
        }

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



        post("/reset-queue"){
            queueResponseFlow.emit("reset-queue")
            currentQueueList.clear()
            call.respond(HttpStatusCode.OK, "Queue reset successfully.")
        }

        post("/next-student"){

            if (currentQueueList.isNotEmpty()) {
                val student = currentQueueList.removeAt(0)

                val studentId = student.id
                if (studentId == null) {
                    call.respond(HttpStatusCode.NotFound, mapOf("error" to "user id not found"))
                    return@post
                }

                val studentProfile = userServiceImpl.retrieveProfileById(studentId)
                if (studentProfile == null) {
                    call.respond(HttpStatusCode.NotFound, mapOf("error" to "user profile not found"))
                    return@post
                }

                val fullName = "${studentProfile.lastName}, ${studentProfile.firstName} ${studentProfile.middleName} ${studentProfile.extensionName ?: ""}"



                queueResponseFlow.emit("Next Student: ${fullName}")

                previousQueueList.add(student)
                call.respond(HttpStatusCode.OK, student)

            } else {
                call.respond(HttpStatusCode.NotFound, "No students in the queue.")
            }
        }

        post("/previous-student"){
            if (previousQueueList.isNotEmpty()) {
                val student = previousQueueList.removeAt(previousQueueList.size - 1)
                currentQueueList.add(0, student)


                // Retrieve the full name of the student for better clarity in responses
                val studentProfile = userServiceImpl.retrieveProfileById(student.id ?: 0)

                if (studentProfile != null) {

                    val fullName = "${studentProfile.lastName}, ${studentProfile.firstName} ${studentProfile.middleName} ${studentProfile.extensionName ?: ""}"

                    println("Moved previous student back to the front: $fullName")

                    queueResponseFlow.emit("Previous Student: $fullName")

                    call.respond(HttpStatusCode.OK, student)

                } else {
                    call.respond(HttpStatusCode.NotFound, mapOf("error" to "Student profile not found"))
                }

            } else {
                call.respond(HttpStatusCode.NotFound, "No previous student to serve.")
            }
        }
    }


}
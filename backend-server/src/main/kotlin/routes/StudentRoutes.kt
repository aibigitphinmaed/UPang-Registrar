package com.ite393group5.routes

import com.ite393group5.dto.StudentProfile
import com.ite393group5.services.StudentServiceImpl
import com.ite393group5.services.UserService
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.engine.logError
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.*

fun Route.studentRoutes(userServiceImpl: UserService, studentService: StudentServiceImpl) {
    authenticate("student-auth") {

        get("student-profile") {
            val principal = call.principal<JWTPrincipal>()!!
            val username = principal.payload.getClaim("username").asString()
            val userid = userServiceImpl.findByUsername(username)?.id

            if (userid == null) {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to "user id not found"))
                return@get
            }

            val studentProfile = userServiceImpl.retrieveProfileById(userid)

            if (studentProfile != null) {
                call.respond(HttpStatusCode.OK, studentProfile)

            }else{
                call.respond(HttpStatusCode.NotFound, mapOf("error" to "Student profile not found"))
            }
        }
        get("student-address") {
            val principal = call.principal<JWTPrincipal>()!!
            val username = principal.payload.getClaim("username").asString()
            val userid = userServiceImpl.findByUsername(username)?.id
            if (userid == null) {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to "user id not found"))
                return@get
            }

            val studentAddress = userServiceImpl.retrieveAddressById(userid)

            if (studentAddress != null) {
                call.respond(HttpStatusCode.OK, studentAddress)
            }else{
                call.respond(HttpStatusCode.NotFound, mapOf("error" to "Student address not found"))
            }
        }
        get("hello-student"){
            call.respondText("Hello Student!")
        }
        post("student-id"){
            val principal = call.principal<JWTPrincipal>()!!
            val username = principal.payload.getClaim("username").asString()
            val userid = userServiceImpl.findByUsername(username)?.id
            if (userid != null) {
                call.respond(HttpStatusCode.OK, mapOf("id" to userid))
            }else{
                call.respond(HttpStatusCode.NotFound, mapOf("error" to "Student ID not found"))
            }
        }

        post("update-student-profile"){
            val principal = call.principal<JWTPrincipal>()
            if (principal == null) {
                call.respond(HttpStatusCode.Unauthorized, mapOf("error" to "Unauthorized access"))
                return@post
            }

            val username = principal.payload.getClaim("username").asString()

            val user = userServiceImpl.findByUsername(username)

            if (user == null) {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to "User not found"))
                return@post
            }

            val userid = userServiceImpl.findByUsername(username)?.id

            val studentProfile = try {
                call.receive<StudentProfile>()
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Invalid request body"))
                return@post
            }
            if(userid != null) {
                println(studentProfile)
                val b = userServiceImpl.updateProfile(username = username, data = studentProfile)
                if(b){
                    call.respond(HttpStatusCode.OK)
                }
                else{

                    call.respond(HttpStatusCode.InternalServerError)
                }
            }else{
                call.respond(HttpStatusCode.NotFound, mapOf("error" to "Student profile not found"))
            }
        }
    }
}
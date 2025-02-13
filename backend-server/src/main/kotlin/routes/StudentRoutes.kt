package com.ite393group5.routes

import com.ite393group5.services.UserService
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.*

fun Route.studentRoutes(userServiceImpl: UserService) {
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
    }
}
package com.ite393group5.routes

import com.ite393group5.dto.user.UserProfile
import com.ite393group5.dto.PasswordRequest
import com.ite393group5.models.User
import com.ite393group5.plugins.currentQueueList
import com.ite393group5.plugins.queueResponseFlow
import com.ite393group5.services.StudentServiceImpl
import com.ite393group5.services.UserService
import com.ite393group5.utilities.SHA256HashingService
import com.ite393group5.utilities.SaltedHash
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.http.content.streamProvider
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.request.receiveMultipart
import io.ktor.server.response.header
import io.ktor.server.response.respond
import io.ktor.server.response.respondFile
import io.ktor.server.response.respondText
import io.ktor.server.routing.*
import io.ktor.util.cio.writeChannel
import io.ktor.utils.io.copyAndClose
import java.io.File
import java.util.UUID

fun Route.studentRoutes(userServiceImpl: UserService, studentService: StudentServiceImpl) {
    authenticate("student-auth") {

        //region student queue
        post("/join-student-queue") {
            val principal = call.principal<JWTPrincipal>()!!
            val username = principal.payload.getClaim("username").asString()

            val studentUser = userServiceImpl.findByUsername(username)

            val userid = studentUser?.id
            if (userid == null) {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to "user id not found"))
                return@post
            }
            currentQueueList.add(studentUser)
            call.respond(HttpStatusCode.OK, studentUser)
        }

        //endregion
        // region student profile
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

            } else {
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
            } else {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to "Student address not found"))
            }
        }
        get("hello-student") {
            call.respondText("Hello Student!")
        }
        post("student-id") {
            val principal = call.principal<JWTPrincipal>()!!
            val username = principal.payload.getClaim("username").asString()
            val userid = userServiceImpl.findByUsername(username)?.id
            if (userid != null) {
                call.respond(HttpStatusCode.OK, mapOf("id" to userid))
            } else {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to "Student ID not found"))
            }
        }

        post("update-student-profile") {
            val principal = call.principal<JWTPrincipal>()
            if (principal == null) {
                call.respond(HttpStatusCode.Unauthorized, mapOf("error" to "Unauthorized access"))
                return@post
            }

            val requestProfile = call.receive<UserProfile>()

            val username = principal.payload.getClaim("username").asString()

            val user = userServiceImpl.findByUsername(username)

            if (user == null) {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to "User not found"))
                return@post
            } else {
                val isUpdated = userServiceImpl.updateProfile(requestProfile, username)
                if (isUpdated) {
                    call.respond(HttpStatusCode.OK)
                } else {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        mapOf("error" to "Student profile not found and unsuccessful update")
                    )
                }
            }

        }
        //endregion
        //region student change password
        post("/student-change-password") {
            val principal = call.principal<JWTPrincipal>()!!
            val passwordRequest = call.receive<PasswordRequest>()
            val username = principal.payload.getClaim("username").asString()
            println(passwordRequest)
            if (username.isNotBlank() and passwordRequest.newPassword.isNotBlank() and passwordRequest.currentPassword.isNotBlank()) {
                val jwtUser = userServiceImpl.findByUsername(username)

                if (jwtUser != null) {
                    val saltedHash = SaltedHash(jwtUser.password, jwtUser.salt)

                    val correctPassword = SHA256HashingService().verify(passwordRequest.currentPassword, saltedHash)
                    if (correctPassword) {

                        val generateNewSaltedHash =
                            SHA256HashingService().generateSaltedHash(passwordRequest.newPassword)
                        val updatedUser = User(
                            id = jwtUser.id,
                            username = jwtUser.username,
                            password = generateNewSaltedHash.hash,
                            role = jwtUser.role,
                            salt = generateNewSaltedHash.salt,
                        )
                        println(updatedUser)
                        val isUpdated = userServiceImpl.updateProfile(updatedUser, username)
                        if (isUpdated) {

                            call.respond(HttpStatusCode.OK)
                        } else {
                            call.respond(HttpStatusCode.InternalServerError)
                        }
                    } else {
                        println(correctPassword)
                        call.respond(HttpStatusCode.Unauthorized)
                    }
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            } else {
                call.respond(HttpStatusCode.BadRequest)
            }
        }
        //endregion

        //region student profile image

        post("/student-image-upload") {
            val multipart = call.receiveMultipart(formFieldLimit = 1024 * 1024 * 10)

            val username = call.principal<JWTPrincipal>()!!.payload.getClaim("username").asString()

            val user = userServiceImpl.findByUsername(username)

            if (user == null) {
                call.respond(HttpStatusCode.NotFound)
                return@post
            }

            var fileDescription = ""
            var fileName = ""


            //region uploading process
            multipart.forEachPart { part ->

                when (part) {
                    is PartData.FormItem -> {
                        fileDescription = part.value
                    }

                    is PartData.FileItem -> {
                        val extension = File(part.originalFileName!!).extension.lowercase()
                        if (extension !in listOf("jpg", "jpeg", "png", "webp")) {
                            call.respond(HttpStatusCode.BadRequest, "Invalid file type")
                            return@forEachPart
                        }

                        val userDir = File("uploads/profile_pictures/${user.id}")
                        if (!userDir.exists()) {
                            userDir.mkdirs()
                            println(userDir.toString())
                        }

                        val uuidFileName = "${UUID.randomUUID()}.$extension"
                        val file = File(userDir, uuidFileName)

                        part.provider().copyAndClose(file.writeChannel())

                        fileName = uuidFileName
                        println(fileName)
                    }

                    else -> {}
                }
                part.dispose()
            }

            if(fileName != null){
               val savedProfileImage = userServiceImpl.updateProfileImageRecords(fileName, username)
                if(savedProfileImage){
                    call.respond(HttpStatusCode.OK)
                }
                call.respond(HttpStatusCode.BadRequest)
            }else{
                call.respond(HttpStatusCode.BadRequest)
            }


            //endregion
        }

        get("/student-profile-image") {
            val principal = call.principal<JWTPrincipal>()!!
            val username = principal.payload.getClaim("username").asString()

            val user = userServiceImpl.findByUsername(username)

            val imageId = userServiceImpl.getCurrentUserProfileImageId(username)

            if (imageId == null) {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to "No profile image found"))
                return@get
            }

            val imageRecord = userServiceImpl.getImageRecordById(imageId)

            if (imageRecord == null) {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to "Image record not found"))
                return@get
            }
            if(user?.id == null){
                call.respond(HttpStatusCode.NotFound, mapOf("error" to "No User found"))
                return@get
            }

            val file = File("uploads/profile_pictures/${user.id}/${imageRecord.fileName}")
            val contentType = when (imageRecord.fileType?.lowercase()) {
                "png" -> ContentType.Image.PNG
                "jpeg", "jpg" -> ContentType.Image.JPEG
                else -> ContentType.Application.OctetStream
            }
            if (!file.exists()) {
                call.respond(HttpStatusCode.NotFound)
            } else {
                println(imageRecord)
                call.response.header(HttpHeaders.ContentType, contentType.toString())
                call.respondFile(file)
            }
        }
        //endregion

        //region appointment-feature-student

        //region student-appointment-request
        post("/student-appointment-request"){
            val principal = call.principal<JWTPrincipal>()!!
            val username = principal.payload.getClaim("username").asString()
            val userid = userServiceImpl.findByUsername(username)?.id

            if (userid == null) {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to "user id not found"))
                return@post
            }




        }
        //endregion
        //region student-get-appointments
        get("student-get-appointments"){

        }
        //endregion
        //region student-modify-appoint-request
        post("student-modify-appointments"){

        }
        //endregion
        //region student-cancel-appointment
        post("student-cancel-appointments"){

        }
        //endregion




        //endregion
    }
}



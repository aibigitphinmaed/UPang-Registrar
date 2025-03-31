package com.ite393group5.routes

import com.ite393group5.dto.appointment.AppointmentResponse
import com.ite393group5.dto.user.PasswordRequest
import com.ite393group5.dto.appointment.CancelAppointmentRequest
import com.ite393group5.dto.appointment.CreateAppointmentRequest
import com.ite393group5.dto.appointment.ModifyAppointmentRequest
import com.ite393group5.dto.appointment.UpdateAppointmentRequest
import com.ite393group5.dto.user.UserProfile
import com.ite393group5.models.Appointment
import com.ite393group5.models.User
import com.ite393group5.plugins.currentQueueList
import com.ite393group5.services.appointment.AppointmentService
import com.ite393group5.services.user.UserService
import com.ite393group5.utilities.SHA256HashingService
import com.ite393group5.utilities.SaltedHash
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.cio.*
import io.ktor.utils.io.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.io.File
import java.util.*

fun Route.studentRoutes(userServiceImpl: UserService,appointmentService: AppointmentService) {
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

            val studentPersonalInfo = userServiceImpl.getUserProfile(userid)?.userPersonalInfo

            if (studentPersonalInfo != null) {
                call.respond(HttpStatusCode.OK, studentPersonalInfo)

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

            val studentAddress = userServiceImpl.getUserProfile(userid)?.userAddressInfo

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
                val isUpdated = userServiceImpl.updateProfile(requestProfile, user)
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
                        val isUpdated = userServiceImpl.updateProfile(updatedUser, updatedUser)
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

                    else -> {

                    }
                }
                part.dispose()
            }

            if(fileName != null){
                println("profile image of user $username has been uploaded. file name is $fileName")
               val savedProfileImage = userServiceImpl.updateProfileImage(fileName, username)
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
            if(user == null){
                call.respond(HttpStatusCode.NotFound)
            }
            val userid = user?.id ?: -1
            println("looking for profile image of user id $userid")
            val imageId = userServiceImpl.getCurrentUserProfileImageId(userid)
            println("imageId is $imageId")
            if (imageId == null) {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to "No profile image found"))
                return@get
            }
            println("profile image id of user is $imageId")

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

            val jObject = call.receiveText()


            val appointmentRequest = Json.decodeFromString<CreateAppointmentRequest>(jObject)
            println(appointmentRequest)
            if(appointmentRequest == null) {
                call.respond(HttpStatusCode.InternalServerError, "Appointment Request body is missing")
                return@post
            }

            val appointmentResponse = appointmentService.createAppointment(appointmentRequest, userid)
            if(appointmentResponse == null) {
                call.respond(HttpStatusCode.InternalServerError, "Response from the server is null. Probably Under maintenance.")
                return@post
            }
            call.respond(HttpStatusCode.OK,appointmentResponse)
        }
        //endregion
        //region student-get-appointments
        get("student-get-appointments"){
            val principal = call.principal<JWTPrincipal>()!!
            val username = principal.payload.getClaim("username").asString()
            val userid = userServiceImpl.findByUsername(username)?.id

            if (userid == null) {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to "user id not found"))
                return@get
            }

            val studentsAppointments = appointmentService.getAllAppointments(userid)
            if(studentsAppointments == null) {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to "No appointments found for user $username"))
                return@get
            }

            call.respond(HttpStatusCode.Found, studentsAppointments)

        }
        //endregion

        //region student-modify-appoint-request
        post("student-modify-appointments"){
            val principal = call.principal<JWTPrincipal>()!!
            val username = principal.payload.getClaim("username").asString()
            val userid = userServiceImpl.findByUsername(username)?.id

            if (userid == null) {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to "user id not found"))
                return@post
            }

            val modifyAppointmentRequest = call.receive<ModifyAppointmentRequest>()

            if(modifyAppointmentRequest == null) {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to "Appointment request body is missing"))
                return@post
            }

            val modifyAppointmentResponse = appointmentService.modifyAppointmentRequest(modifyAppointmentRequest, userid)

            if(modifyAppointmentResponse == null) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Something went wrong with the server. Try again later"))
            }

            call.respond(HttpStatusCode.OK, modifyAppointmentResponse ?: mapOf("error" to "No appointments found for user $username"))

        }
        //endregion
        //region student-cancel-appointment
        post("student-cancel-appointment"){
            val principal = call.principal<JWTPrincipal>()!!
            val username = principal.payload.getClaim("username").asString()
            val userid = userServiceImpl.findByUsername(username)?.id
            println("student-cancel-appointments: $userid")
            if (userid == null) {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to "user id not found"))
                return@post
            }

            val reqBody = call.receive<Map<String, String>>()
            val appointmentId = reqBody["appointmentId"]

            val isAppointmentCancelled = appointmentService.cancelAppointment(appointmentId?.toInt() ?: -1, userid)
            if(isAppointmentCancelled == null) {
                call.respond(HttpStatusCode.NoContent, mapOf("error" to "No Appointment cancelled"))
            }else{
                call.respond(HttpStatusCode.OK, isAppointmentCancelled)
            }

        }

        post("current-appointment-request"){
            val principal = call.principal<JWTPrincipal>()!!
            val username = principal.payload.getClaim("username").asString()
            val userid = userServiceImpl.findByUsername(username)?.id

            if (userid == null) {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to "user id not found"))
                return@post
            }

            val bodyText = call.receiveText()
            val jsonObject = Json.parseToJsonElement(bodyText).jsonObject
            val appointmentId = jsonObject["appointmentId"]?.jsonPrimitive?.content?.toIntOrNull()
            if (appointmentId == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid appointmentId")
                return@post
            }
            val appointment: AppointmentResponse? = appointmentService.getAllAppointments(userid)
                ?.firstOrNull { it.id == appointmentId }
            if(appointment == null) {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to "No current appointment found for user $username"))
            }else{
                call.respond(HttpStatusCode.OK, appointment)
            }
        }

        post("current-appointment-status"){
            val principal = call.principal<JWTPrincipal>()!!
            val username = principal.payload.getClaim("username").asString()
            val userid = userServiceImpl.findByUsername(username)?.id

            if (userid == null) {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to "user id not found"))
                return@post
            }
            val bodyText = call.receiveText()
            val jsonObject = Json.parseToJsonElement(bodyText).jsonObject
            val appointmentId = jsonObject["appointmentId"]?.jsonPrimitive?.content?.toIntOrNull()
            if (appointmentId == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid appointmentId")
                return@post
            }
            val appointment: AppointmentResponse? = appointmentService.getAllAppointments(userid)
                ?.firstOrNull { it.id == appointmentId }
            if(appointment == null) {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to "No current appointment found for user $username"))
            }else{
                call.respond(mapOf("status" to appointment.status))
            }


        }
        //endregion

        //endregion

        //region create document request
        post("/student-document-request"){

        }
        post("/student-upload-requirements"){

        }
        //endregion
    }
}



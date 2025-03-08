package com.ite393group5.routes

import com.ite393group5.dto.appointment.AppointStatusRequest
import com.ite393group5.dto.appointment.UpdateAppointmentRequest
import com.ite393group5.dto.user.PasswordRequest
import com.ite393group5.dto.user.UserProfile
import com.ite393group5.models.Appointment
import com.ite393group5.models.LocationInfo
import com.ite393group5.models.PersonalInfo
import com.ite393group5.models.TokenConfig
import com.ite393group5.models.User
import com.ite393group5.plugins.currentQueueList
import com.ite393group5.plugins.previousQueueList
import com.ite393group5.plugins.queueResponseFlow
import com.ite393group5.services.appointment.AppointmentService
import com.ite393group5.services.user.UserService
import com.ite393group5.utilities.JwtTokenService
import com.ite393group5.utilities.SHA256HashingService
import com.ite393group5.utilities.SaltedHash
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
    staffTokenConfig: TokenConfig,
    appointmentServiceImpl: AppointmentService
) {

    authenticate("staff-auth") {



        post("/test-staff-call") {
            call.respond(HttpStatusCode.OK, "staff called an api")
        }

        staticResources(
            "/static",
            "static"
        )
        //region create student by staff
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

                val plainPassword =
                    studentPersonalInfo?.firstName + studentPersonalInfo?.middleName + studentPersonalInfo?.lastName

                // Hash the password before storing it
                val studentSaltedHash = SHA256HashingService().generateSaltedHash(plainPassword)

                val userToCreate = User(
                    password = studentSaltedHash.hash, salt = studentSaltedHash.salt,
                    id = null,
                    username = studentPersonalInfo?.email ?: "",
                    role = "student"
                )

                val createdStudent = userServiceImpl.register(user = userToCreate,personalInfo =studentRequest.userPersonalInfo ?: PersonalInfo(), locationInfo=studentRequest.userAddressInfo ?: LocationInfo())
                call.respond(HttpStatusCode.Created, "Student ${createdStudent.username} created successfully")

            } catch (e: Exception) {
                e.printStackTrace()
                call.respond(HttpStatusCode.InternalServerError, "Error creating student: ${e.localizedMessage}")
            }
        }

        //endregion

        //region Queue-feature
        post("/reset-queue") {
            queueResponseFlow.emit("reset-queue")
            currentQueueList.clear()
            call.respond(HttpStatusCode.OK, "Queue reset successfully.")
        }

        post("/next-student") {

            if (currentQueueList.isNotEmpty()) {
                val student = currentQueueList.removeAt(0)

                val studentId = student.id
                if (studentId == null) {
                    call.respond(HttpStatusCode.NotFound, mapOf("error" to "user id not found"))
                    return@post
                }

                val studentPersonalInfo = userServiceImpl.getUserProfile(studentId)?.userPersonalInfo
                if (studentPersonalInfo == null) {
                    call.respond(HttpStatusCode.NotFound, mapOf("error" to "user profile not found"))
                    return@post
                }

                val fullName =
                    "${studentPersonalInfo.lastName}, ${studentPersonalInfo.firstName} ${studentPersonalInfo.middleName} ${studentPersonalInfo.extensionName ?: ""}"



                queueResponseFlow.emit("Next Student: ${fullName}")

                previousQueueList.add(student)
                call.respond(HttpStatusCode.OK, student)

            } else {
                call.respond(HttpStatusCode.NotFound, "No students in the queue.")
            }
        }

        post("/previous-student") {
            if (previousQueueList.isNotEmpty()) {
                val student = previousQueueList.removeAt(previousQueueList.size - 1)
                currentQueueList.add(0, student)


                // Retrieve the full name of the student for better clarity in responses
                val studentPersonalInfo = userServiceImpl.getUserProfile(student.id ?: 0)?.userPersonalInfo

                if (studentPersonalInfo != null) {

                    val fullName =
                        "${studentPersonalInfo.lastName}, ${studentPersonalInfo.firstName} ${studentPersonalInfo.middleName} ${studentPersonalInfo.extensionName ?: ""}"

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
        //endregion

        //region appointment-feature

        //region create-student-appointment
        post("create-student-appointment"){

        }
        //endregion
        //region modify-student-appointment
        post("modify-student-appointment"){
            val staffPrincipal = call.principal<JWTPrincipal>()

            val username = staffPrincipal!!.payload.getClaim("username").asString()
            val userId = userServiceImpl.findByUsername(username)?.id
            if (userId == null) {
                call.respond(HttpStatusCode.Unauthorized, mapOf("error" to "You are not allowed"))
            }
            val appointment = call.receive<Appointment>()
            if( staffPrincipal == null ) {
                call.respond(HttpStatusCode.Unauthorized, "Access Denied")
                return@post
            }

            val modifiedAppointment = appointment.copy(
                staffId = userId,
            ).let {
                UpdateAppointmentRequest(
                    id = it.id,
                    studentId = it.studentId,
                    staffId = it.staffId,
                    scheduledDate = it.scheduledDate.toString(),
                    status = it.status,
                    isUrgent = it.isUrgent,
                    remarks = it.remarks,
                    cancellationReason = it.cancellationReason,
                )
            }

            val updateResponse = appointmentServiceImpl.updateAppointment(
                appointmentUpdateRequest = modifiedAppointment,
                studentId = modifiedAppointment.studentId,
            )

            if(updateResponse == null){
                call.respond(HttpStatusCode.NotFound, "Update Failed")
                return@post
            }
            call.respond(HttpStatusCode.OK, updateResponse)

         }
        // endregion
        //region get-all-appointments-by-status
        post("get-all-appointments-by-status"){
            val staffPrincipal = call.principal<JWTPrincipal>()
            val statusAppointmentRequest = call.receive<AppointStatusRequest>()
            //checking for user principal

            val status = statusAppointmentRequest.status.lowercase()
            if( staffPrincipal == null ) {
                call.respond(HttpStatusCode.Unauthorized, "Access Denied")
                return@post
            }
            //role checking
            val userRole = staffPrincipal.payload.getClaim("role").asString()

            if(userRole != "staff") {
                call.respond(HttpStatusCode.Unauthorized, "Access Denied Wrong Role")
                return@post
            }

            if (status == "" || status == null){
                call.respond(HttpStatusCode.BadRequest, "Access Denied")
                return@post
            }

            //proceed with the get appointments by status
            val listOfAppointmentByStatus = appointmentServiceImpl.retrieveAppointmentByStatus(status)

            if(listOfAppointmentByStatus == null || listOfAppointmentByStatus.isEmpty()){
                call.respond(HttpStatusCode.NotFound, "No appointment found by status")
                return@post
            }

            call.respond(HttpStatusCode.Found, listOfAppointmentByStatus)
        }
        //endregion

        //region get-all-appointments
        post("get-all-appointments"){
            val allAppointments = appointmentServiceImpl.retrieveAllAppointments() ?: emptyList()
            if(allAppointments.isEmpty() == true){
                call.respond(HttpStatusCode.NotFound, "No appointment found")
            }
            println(allAppointments)
            call.respond(HttpStatusCode.OK,allAppointments)
        }
        //endregion



        //region staff profile
        get("staff-personal"){
            val principal = call.principal<JWTPrincipal>()!!
            val username = principal.payload.getClaim("username").asString()
            val userid = userServiceImpl.findByUsername(username)?.id

            if (userid == null) {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to "user id not found"))
                return@get
            }

            val staffPersonalInfo = userServiceImpl.getUserProfile(userid)?.userPersonalInfo

            if (staffPersonalInfo != null) {
                call.respond(HttpStatusCode.OK, staffPersonalInfo)

            } else {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to "Student profile not found"))
            }
        }

        get("staff-address") {
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

        post("/staff-change-password") {
            val principal = call.principal<JWTPrincipal>()!!

            val passwordRequest = call.receive<PasswordRequest>()

            val username = principal.payload.getClaim("username").asString()
            println(principal)
            println(passwordRequest)
            println(username)
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
                        return@post
                    }
                } else {
                    call.respond(HttpStatusCode.NotFound)
                    return@post
                }
            } else {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
        }
        //endregion




        //region get-role of staff

        post("/get-role"){
            val jwtPrincipal = call.principal<JWTPrincipal>()
            println(jwtPrincipal!!.payload.getClaim("role").asString())
            if (jwtPrincipal == null) {
                call.respond(HttpStatusCode.Unauthorized, "No JWT Principal found")
                return@post
            }
            val username = jwtPrincipal.payload.getClaim("username").asString()
            if (username == null) {
                call.respond(HttpStatusCode.BadRequest, "No username found")
                return@post
            }
            val role = userServiceImpl.findByUsername(username)?.role
            if(role == null) {
                call.respond(HttpStatusCode.BadRequest, "No role found for this user contact admin for role assigning")
                return@post
            }
            return@post call.respond(HttpStatusCode.OK, mapOf("role" to role))
        }

        //endregion


    }



}
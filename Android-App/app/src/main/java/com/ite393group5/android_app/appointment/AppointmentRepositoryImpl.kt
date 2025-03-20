package com.ite393group5.android_app.appointment

import com.ite393group5.android_app.appointment.state.AppointmentRequest
import com.ite393group5.android_app.models.Appointment
import com.ite393group5.android_app.services.local.LocalService
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.serialization.Serializable
import timber.log.Timber
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppointmentRepositoryImpl @Inject constructor(
    private val localServiceImpl: LocalService,
    private val ktorClient: HttpClient
) : AppointmentRepository {

    override suspend fun requestNewAppointment(appointmentRequest: AppointmentRequest): HttpStatusCode {
        if (localServiceImpl.hasCurrentAppointment()) {
            return HttpStatusCode.Conflict
        }

        val token = localServiceImpl.getBearerToken()


        val serverResponse = ktorClient.post("student-appointment-request") {
            contentType(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
            }
            setBody(mapOf(
                "staffId" to appointmentRequest.staffId,
                "appointmentType" to appointmentRequest.appointmentType,
                "documentType" to appointmentRequest.documentType,
                "reason" to appointmentRequest.reason,
                "requestedDate" to appointmentRequest.requestedDate,
                "isUrgent" to appointmentRequest.isUrgent.toString()
            ))

        }

        if (serverResponse.status == HttpStatusCode.OK) {
            val newAppointment: AppointmentResponse = serverResponse.body()
            localServiceImpl.saveCurrentAppointment(newAppointment.toAppointment())
            return HttpStatusCode.OK
        }

        return HttpStatusCode.BadRequest
    }

    override suspend fun modifyCurrentAppointment(modifiedAppointment: Appointment): HttpStatusCode {
        val token = localServiceImpl.getBearerToken()
        val forServerBody = ModifyAppointmentRequest(
            id = modifiedAppointment.id,
            studentId = modifiedAppointment.studentId,
            staffId = modifiedAppointment.staffId,
            appointmentType = modifiedAppointment.appointmentType,
            documentType = modifiedAppointment.documentType,
            reason = modifiedAppointment.reason,
            requestedDate = modifiedAppointment.requestedDate.toString(),
        )
        val serverResponse = ktorClient.post("student-modify-appointments"){
            contentType(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
            }
            setBody(forServerBody)
        }
        when(serverResponse.status.value){
            200 -> {
                val responseAppointment: AppointmentResponse = serverResponse.body()
                localServiceImpl.saveCurrentAppointment(responseAppointment.toAppointment())
                return HttpStatusCode.OK
            }
            404 -> {
                return HttpStatusCode.NotFound
            }
            else -> {
                return HttpStatusCode.BadRequest
            }
        }
    }

    override suspend fun cancelCurrentAppointment(appointmentId: Int): HttpStatusCode {
        val token = localServiceImpl.getBearerToken()
        val serverResponse = ktorClient.post("student-cancel-appointment") {
            contentType(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
            }
            setBody(mapOf(
                "appointmentId" to appointmentId.toString()
            ))
        }
        if (serverResponse.status == HttpStatusCode.OK) {

            localServiceImpl.clearCurrentAppointment()
            return HttpStatusCode.OK
        }else{
            localServiceImpl.clearCurrentAppointment()
            return HttpStatusCode.NotFound
        }

    }

    override suspend fun getCurrentAppointment(): Appointment? {

        val localAppointment = localServiceImpl.getCurrentAppointment()
        val token = localServiceImpl.getBearerToken()

        if (localAppointment != null) {
            val checkStatusResponse = ktorClient.post("current-appointment-status") {
                contentType(ContentType.Application.Json)
                headers {
                    append(HttpHeaders.Authorization, "Bearer $token")
                }
                setBody(
                    mapOf(
                        "appointmentId" to localAppointment.id
                    )
                )
            }

            val checkStatus = try {
                checkStatusResponse.body<AppointmentStatusResponse>()
            }catch (e:Exception){
                localServiceImpl.clearCurrentAppointment()
                Timber.tag("ARPI:checkStatus:").e(e)
                return null
            }

            if (checkStatus.status.lowercase() == localAppointment.status.lowercase()){
                return localAppointment
            }else{
                val serverResponse = ktorClient.post("current-appointment-request") {
                    contentType(ContentType.Application.Json)
                    headers {
                        append(HttpHeaders.Authorization, "Bearer $token")
                    }
                    setBody(
                        mapOf(
                            "appointmentId" to localAppointment.id
                        )
                    )
                }
                return try {
                    serverResponse.body<Appointment>()
                } catch (e: Exception) {
                    null
                }
            }
        }
        return null
    }

    override suspend fun getHistoryAppointments(): List<Appointment> {
        TODO("Not yet implemented")
    }


}


@Serializable
data class AppointmentResponse(
    val id: Int,
    val studentId: Int,
    val staffId: Int?,
    val appointmentType: String,
    val documentType: String?,
    val reason: String?,
    val requestedDate: String,
    val scheduledDate: String?,
    val status: String,
    val isUrgent: Boolean,
    val remarks: String?,
    val cancellationReason: String?
){
    fun toAppointment(): Appointment {
        return Appointment(
            id = id,
            studentId = studentId,
            staffId = staffId,
            appointmentType = appointmentType,
            documentType = documentType,
            reason = reason,
            requestedDate = LocalDate.parse(requestedDate),
            scheduledDate = scheduledDate?.takeIf { it.isNotBlank() && it != "null" }?.let { LocalDate.parse(it) },
            status = status,
            isUrgent = isUrgent,
            remarks = remarks,
            cancellationReason = cancellationReason
        )
    }
}

@Serializable
data class AppointmentStatusResponse(val status: String)

@Serializable
data class ModifyAppointmentRequest(
    val id: Int,
    val studentId: Int,
    val staffId: Int?,
    val appointmentType: String,
    val documentType: String?,
    val reason: String?,
    val requestedDate: String,
)

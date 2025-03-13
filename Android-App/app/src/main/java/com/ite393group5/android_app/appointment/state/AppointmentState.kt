package com.ite393group5.android_app.appointment.state

import android.net.http.UrlRequest
import com.ite393group5.android_app.models.Appointment
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.format.DateTimeFormatter


data class AppointmentScreenState(
    val requestedDate: LocalDate? = LocalDate.now().plusDays(3),

    //ui state
    val hasAppointment: Boolean = false,
    val isCreatingAppointment: Boolean = false,
    val isModifyingAppointment: Boolean = false,
    val newAppointmentState: Appointment? = null,
    val firstConfirmation: Boolean = false,
    val secondConfirmation: Boolean = false,
    val hasInternet:Boolean = false,
    val appointmentRequest: AppointmentRequest = AppointmentRequest(),
    val cancellingCurrentAppointment: Boolean = false
)

val formatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE

@Serializable
data class AppointmentRequest(
    val staffId: Int? = null,
    val appointmentType: String? = "",
    val documentType: String? = "",
    val reason: String? = "",
    val requestedDate: String? = LocalDate.now().plusDays(3).format(formatter),
    val isUrgent: Boolean? = false
)

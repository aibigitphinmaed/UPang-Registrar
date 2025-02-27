package com.ite393group5.android_app.appointmentbooking.state

import com.ite393group5.android_app.models.Appointment
import java.time.LocalDate


data class AppointmentScreenState(
    val requestedDate: LocalDate? = LocalDate.now().plusDays(3),

    //ui state
    val hasAppointment: Boolean = false,
    val isCreatingAppointment: Boolean = false,
    val isModifyingAppointment: Boolean = false,
    val newAppointmentState: Appointment? = null,
    val firstConfirmation: Boolean = false,
    val secondConfirmation: Boolean = false
)
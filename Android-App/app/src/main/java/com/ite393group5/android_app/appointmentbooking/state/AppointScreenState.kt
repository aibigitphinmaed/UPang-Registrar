package com.ite393group5.android_app.appointmentbooking.state

import com.ite393group5.android_app.models.Appointment
import java.time.LocalDate


data class AppointScreenState(
    val selectedDate: LocalDate? = LocalDate.now(),
    val listOfAppointments: List<Appointment?> = emptyList(),
    val currentSelectedAppointment: Appointment? = null,
)
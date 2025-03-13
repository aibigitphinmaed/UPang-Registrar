package com.ite393group5.android_app.appointment

import com.ite393group5.android_app.appointment.state.AppointmentRequest
import com.ite393group5.android_app.models.Appointment
import io.ktor.http.HttpStatusCode

interface AppointmentRepository{
    suspend fun requestNewAppointment(appointmentRequest: AppointmentRequest): HttpStatusCode
    suspend fun modifyCurrentAppointment(modifiedAppointment:Appointment): HttpStatusCode
    suspend fun cancelCurrentAppointment(appointmentId:Int): HttpStatusCode
    suspend fun getCurrentAppointment():Appointment?
    suspend fun getHistoryAppointments():List<Appointment>
}
package com.ite393group5.services.appointment

import com.ite393group5.dto.appointment.AppointmentResponse
import com.ite393group5.dto.appointment.CancelAppointmentRequest
import com.ite393group5.dto.appointment.CreateAppointmentRequest
import com.ite393group5.dto.appointment.ModifyAppointmentRequest
import com.ite393group5.dto.appointment.UpdateAppointmentRequest
import com.ite393group5.models.Appointment

interface AppointmentService {
    suspend fun createAppointment(createAppointmentRequest: CreateAppointmentRequest, studentId: Int): AppointmentResponse?
    //getAllAppointments(userid)
    suspend fun getAllAppointments(userId:Int): List<AppointmentResponse>?
    suspend fun cancelAppointment(appointmentId: Int, studentId: Int): AppointmentResponse?
    suspend fun updateAppointment(appointmentUpdateRequest: UpdateAppointmentRequest, studentId: Int): AppointmentResponse?
    suspend fun modifyAppointmentRequest(modifyAppointmentRequest: ModifyAppointmentRequest, studentId: Int): AppointmentResponse?

    suspend fun retrieveAppointmentByStatus(status:String): List<Appointment>?

    suspend fun retrieveAllAppointments(): List<AppointmentResponse>?
}
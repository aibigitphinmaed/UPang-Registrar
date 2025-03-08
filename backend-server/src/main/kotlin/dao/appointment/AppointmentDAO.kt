package com.ite393group5.dao.appointment

import com.ite393group5.dto.appointment.AppointmentResponse
import com.ite393group5.dto.appointment.CancelAppointmentRequest
import com.ite393group5.dto.appointment.CreateAppointmentRequest
import com.ite393group5.dto.appointment.ModifyAppointmentRequest
import com.ite393group5.dto.appointment.UpdateAppointmentRequest
import com.ite393group5.models.Appointment

interface AppointmentDAO {

    fun createAppointment(appointmentRequest: CreateAppointmentRequest, studentId:Int): Appointment?
    fun readAllAppointments(): List<Appointment>?
    fun updateAppointment(appointmentUpdate: UpdateAppointmentRequest): Appointment?
    fun deleteAppointment(appointmentId: Int): Boolean
    fun findAppointmentByStudentId(userId: Int): List<Appointment>?
    fun findAppointmentById(appointmentId: Int): Appointment?
    fun modifyAppointmentRequest(modifyAppointmentRequest: ModifyAppointmentRequest, studentId: Int): Appointment?
}
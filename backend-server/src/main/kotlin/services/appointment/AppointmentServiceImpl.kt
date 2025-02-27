package com.ite393group5.services.appointment

import com.ite393group5.dao.appointment.AppointmentDAOImpl
import com.ite393group5.dto.appointment.*
import kotlinx.datetime.LocalDate

class AppointmentServiceImpl(

) : AppointmentService {
    private val appointmentDAO = AppointmentDAOImpl()

    override suspend fun createAppointment(
        createAppointmentRequest: CreateAppointmentRequest,
        studentId: Int
    ): AppointmentResponse? {
        val newAppointment = appointmentDAO.createAppointment(createAppointmentRequest, studentId)
        if (newAppointment != null) {
            return AppointmentResponse(
                studentId = newAppointment.studentId,
                id = newAppointment.id,
                staffId = newAppointment.staffId,
                appointmentType = newAppointment.appointmentType,
                documentType = newAppointment.documentType,
                reason = newAppointment.reason,
                requestedDate = newAppointment.requestedDate.toString(),
                scheduledDate = newAppointment.scheduledDate.toString(),
                status = newAppointment.status,
                isUrgent = newAppointment.isUrgent,
                remarks = newAppointment.remarks,
                cancellationReason = newAppointment.cancellationReason,
            )
        }
        return null
    }

    override suspend fun getAllAppointments(userId: Int): List<AppointmentResponse>? {
        val appointments = appointmentDAO.readAllAppointments()
            ?.filter { it.studentId == userId }
            ?.map { appointment ->
                AppointmentResponse(
                    id = appointment.id,
                    studentId = appointment.studentId,
                    staffId = appointment.staffId,
                    appointmentType = appointment.appointmentType,
                    documentType = appointment.documentType,
                    reason = appointment.reason,
                    requestedDate = appointment.requestedDate.toString(),
                    scheduledDate = appointment.scheduledDate.toString(),
                    status = appointment.status,
                    isUrgent = appointment.isUrgent,
                    remarks = appointment.remarks,
                    cancellationReason = appointment.cancellationReason,
                )
            }
        if (appointments == null) {
            return null
        }

        return if (appointments.isEmpty()) null else appointments
    }

    override suspend fun cancelAppointment(
        appointmentCancelRequest: CancelAppointmentRequest,
        studentId: Int
    ): AppointmentResponse? {
        val appointment = appointmentDAO.findAppointmentById(appointmentCancelRequest.id)
        if (appointment == null) {
            return null
        }
        val updateAppointmentRequest = UpdateAppointmentRequest(
            id = appointmentCancelRequest.id,
            studentId = appointmentCancelRequest.studentId,
            staffId = appointmentCancelRequest.staffId,
            scheduledDate = appointment.scheduledDate.toString(),
            status = "cancelled",
            isUrgent = appointment.isUrgent,
            remarks = "cancelled by student",
            cancellationReason = appointmentCancelRequest.cancellationReason,
        )
        val appointmentResponse = appointmentDAO.updateAppointment(updateAppointmentRequest)

        return appointmentResponse?.let { appointmentdto ->
            AppointmentResponse(
                id = appointmentdto.id,
                studentId = appointmentdto.studentId,
                staffId = appointmentdto.staffId,
                appointmentType = appointmentdto.appointmentType,
                documentType = appointmentdto.documentType,
                reason = appointmentdto.reason,
                requestedDate = appointmentdto.requestedDate.toString(),
                scheduledDate = appointmentdto.scheduledDate.toString(),
                status = appointmentdto.status,
                isUrgent = appointmentdto.isUrgent,
                remarks = appointmentdto.remarks,
                cancellationReason = appointmentdto.cancellationReason,
            )
        }
    }

    override suspend fun updateAppointment(
        appointmentUpdateRequest: UpdateAppointmentRequest,
        studentId: Int
    ): AppointmentResponse? {
        val appointment = appointmentDAO.findAppointmentById(appointmentUpdateRequest.id)
        if (appointment == null) {
            return null
        }
        val updatedAppointmentResponse = appointmentDAO.updateAppointment(appointmentUpdateRequest)

        return updatedAppointmentResponse?.let {
            AppointmentResponse(
                id = it.id,
                studentId = it.studentId,
                staffId = it.staffId,
                appointmentType = it.appointmentType,
                documentType = it.documentType,
                reason = it.reason,
                requestedDate = requireNotNull(it.requestedDate.toString()),
                scheduledDate = it.scheduledDate.toString(),
                status = it.status,
                isUrgent = it.isUrgent,
                remarks = it.remarks,
                cancellationReason = it.cancellationReason,
            )
        }
    }

    override suspend fun modifyAppointmentRequest(
        modifyAppointmentRequest: ModifyAppointmentRequest,
        studentId: Int
    ): AppointmentResponse? {
        val appointment = appointmentDAO.findAppointmentById(modifyAppointmentRequest.id)
        if (appointment == null) {
            return null
        }
        val modifyAppointmentResponse = appointmentDAO.modifyAppointmentRequest(
            modifyAppointmentRequest, studentId
        )
        return modifyAppointmentResponse?.let {
            AppointmentResponse(
                id = it.id,
                studentId = it.studentId,
                staffId = it.staffId,
                appointmentType = it.appointmentType,
                documentType = it.documentType,
                reason = it.reason,
                requestedDate = requireNotNull(it.requestedDate.toString()),
                scheduledDate = it.scheduledDate.toString(),
                status = it.status,
                isUrgent = it.isUrgent,
                remarks = it.remarks,
                cancellationReason = it.cancellationReason,
            )
        }
    }

}

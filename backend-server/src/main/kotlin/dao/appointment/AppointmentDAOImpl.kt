package com.ite393group5.dao.appointment

import com.ite393group5.db.AppointmentTable
import com.ite393group5.db.AppointmentTable.appointmentType
import com.ite393group5.db.AppointmentTable.cancellationReason
import com.ite393group5.db.AppointmentTable.createdAt
import com.ite393group5.db.AppointmentTable.documentType
import com.ite393group5.db.AppointmentTable.isUrgent
import com.ite393group5.db.AppointmentTable.notifiedAt
import com.ite393group5.db.AppointmentTable.reason
import com.ite393group5.db.AppointmentTable.remarks
import com.ite393group5.db.AppointmentTable.requestedDate
import com.ite393group5.db.AppointmentTable.scheduledDate
import com.ite393group5.db.AppointmentTable.staffId
import com.ite393group5.db.AppointmentTable.status
import com.ite393group5.db.AppointmentTable.studentId
import com.ite393group5.db.AppointmentTable.updatedAt
import com.ite393group5.dto.appointment.CreateAppointmentRequest
import com.ite393group5.dto.appointment.ModifyAppointmentRequest
import com.ite393group5.dto.appointment.UpdateAppointmentRequest
import com.ite393group5.models.Appointment
import kotlinx.datetime.LocalDate
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentTimestamp
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.time.format.DateTimeFormatter


class AppointmentDAOImpl : AppointmentDAO {

    override fun createAppointment(appointmentRequest: CreateAppointmentRequest, studentId: Int): Appointment? {
        return transaction {

            val appointmentId = AppointmentTable.insertAndGetId {
                it[AppointmentTable.studentId] = studentId
                it[staffId] = appointmentRequest.staffId
                it[appointmentType] = appointmentRequest.appointmentType
                it[documentType] = appointmentRequest.documentType
                it[reason] = appointmentRequest.reason
                it[requestedDate] = LocalDate.parse(appointmentRequest.requestedDate)
                it[scheduledDate] = null
                it[status] = "Pending"
                it[notifiedAt] = null
                it[isUrgent] = appointmentRequest.isUrgent == true
                it[remarks] = null
                it[cancellationReason] = null
            }

            println(appointmentRequest.requestedDate)
            println(LocalDate.parse(appointmentRequest.requestedDate))
            // Return the newly created appointment as an object
            return@transaction Appointment(
                id = appointmentId.value,
                studentId = studentId,
                staffId = appointmentRequest.staffId,
                appointmentType = appointmentRequest.appointmentType,
                documentType = appointmentRequest.documentType,
                reason = appointmentRequest.reason,
                requestedDate = LocalDate.parse(appointmentRequest.requestedDate),
                scheduledDate = null,
                status = "Pending",
                notifiedAt = null,
                isUrgent = appointmentRequest.isUrgent == true,
                remarks = null,
                cancellationReason = null,
                createdAt = System.currentTimeMillis().toString(),
                updatedAt = System.currentTimeMillis().toString()
            )

            return@transaction null
        }
    }

    override fun readAllAppointments(): List<Appointment>? {
        return transaction {
            AppointmentTable.selectAll().map {
                Appointment(
                    id = it[AppointmentTable.id].value,
                    studentId = it[studentId].value,
                    appointmentType = it[appointmentType],
                    documentType = it[documentType],
                    reason = it[reason],
                    requestedDate = LocalDate.parse(it[requestedDate].toString()),
                    staffId = it[staffId]?.value,
                    scheduledDate = runCatching {
                        it[scheduledDate]?.toString()?.let { dateStr -> LocalDate.parse(dateStr) }
                    }.getOrNull(),
                    status = it[status],
                    notifiedAt = it[notifiedAt].toString(),
                    isUrgent = it[isUrgent],
                    remarks = it[remarks],
                    cancellationReason = it[cancellationReason],
                    createdAt = it[createdAt].toString(),
                    updatedAt = it[updatedAt].toString(),
                )
            }.ifEmpty { return@transaction null }
        }
    }

    override fun updateAppointment(appointmentUpdate: UpdateAppointmentRequest): Appointment? {
        return transaction {
            val updatedRows = AppointmentTable.update({ AppointmentTable.id eq appointmentUpdate.id }) { row ->
                appointmentUpdate.staffId?.let { row[staffId] = it }
                appointmentUpdate.status?.let { row[status] = it }
                appointmentUpdate.isUrgent?.let { row[isUrgent] = it }
                appointmentUpdate.remarks?.let { row[remarks] = it }
                appointmentUpdate.cancellationReason?.let { row[cancellationReason] = it }
                row[updatedAt] = CurrentTimestamp // Assuming timestamp update

            }
            if (updatedRows > 0) {
                // Fetch and return the updated appointment
                AppointmentTable.selectAll().where { AppointmentTable.id eq appointmentUpdate.id }
                    .map {
                        Appointment(
                            id = it[AppointmentTable.id].value,
                            studentId = it[studentId].value,
                            appointmentType = it[appointmentType],
                            documentType = it[documentType],
                            reason = it[reason],
                             requestedDate = LocalDate.parse(it[requestedDate].toString()),
                            staffId = it[staffId]?.value,
                             scheduledDate =LocalDate.parse(it[requestedDate].toString()),
                            status = it[status],
                            notifiedAt = it[notifiedAt].toString(),
                            isUrgent = it[isUrgent],
                            remarks = it[remarks],
                            cancellationReason = it[cancellationReason],
                            createdAt = it[createdAt].toString(),
                            updatedAt = it[updatedAt].toString(),
                        )
                    }
                    .singleOrNull()
            } else {
                null
            }

        }
    }

    override fun deleteAppointment(appointmentId: Int): Boolean {
        return transaction{
            val deletedRows = AppointmentTable.deleteWhere { AppointmentTable.id eq appointmentId }
            deletedRows > 0
        }
    }

    override fun findAppointmentByStudentId(studentId: Int): List<Appointment>? {
        return transaction {
            AppointmentTable.selectAll().where { AppointmentTable.studentId eq studentId }
                .map {
                Appointment(
                    id = it[AppointmentTable.id].value,
                    studentId = it[AppointmentTable.studentId].value,
                    appointmentType = it[appointmentType],
                    documentType = it[documentType],
                    reason = it[reason],
                     requestedDate = LocalDate.parse(it[requestedDate].toString()),
                    staffId = it[staffId]?.value,
                     scheduledDate = LocalDate.parse(it[requestedDate].toString()),
                    status = it[status],
                    notifiedAt = it[notifiedAt].toString(),
                    isUrgent = it[isUrgent],
                    remarks = it[remarks],
                    cancellationReason = it[cancellationReason],
                    createdAt = it[createdAt].toString(),
                    updatedAt = it[updatedAt].toString(),
                )
            }.ifEmpty{ return@transaction null }
        }
    }

    override fun findAppointmentById(appointmentId: Int): Appointment? {
        return transaction {
            AppointmentTable
                .selectAll().where( AppointmentTable.id eq appointmentId )
                .map {
                    Appointment(
                        id = it[AppointmentTable.id].value,
                        studentId = it[studentId].value,
                        appointmentType = it[appointmentType],
                        documentType = it[documentType],
                        reason = it[reason],
                        requestedDate = LocalDate.parse(it[requestedDate].toString()),
                        staffId = it[staffId]?.value,
                        scheduledDate = it[scheduledDate],
                        status = it[status],
                        notifiedAt = it[notifiedAt].toString(),
                        isUrgent = it[isUrgent],
                        remarks = it[remarks],
                        cancellationReason = it[cancellationReason],
                        createdAt = it[createdAt].toString(),
                        updatedAt = it[updatedAt].toString(),
                    )
                }
                .singleOrNull()  // Ensures only one result is returned or null if not found
        }
    }

    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")



    override fun modifyAppointmentRequest(
        modifyAppointmentRequest: ModifyAppointmentRequest,
        studentId: Int
    ): Appointment? {
        return transaction {

            val updatedRows = AppointmentTable.update( { AppointmentTable.id eq modifyAppointmentRequest.id }){ row ->

               row[appointmentType] = modifyAppointmentRequest.appointmentType
               row[documentType] = modifyAppointmentRequest.documentType
               row[reason] = modifyAppointmentRequest.reason
               row[requestedDate] = LocalDate.parse(modifyAppointmentRequest.requestedDate)
               row[updatedAt] = CurrentTimestamp
               row[staffId] = modifyAppointmentRequest.staffId
            }

            if (updatedRows > 0) {
                // Fetch and return the updated appointment
                AppointmentTable.selectAll().where { AppointmentTable.id eq modifyAppointmentRequest.id }
                    .map {
                        Appointment(
                            id = it[AppointmentTable.id].value,
                            studentId = it[AppointmentTable.studentId].value,
                            appointmentType = it[appointmentType],
                            documentType = it[documentType],
                            reason = it[reason],
                             requestedDate = LocalDate.parse(it[requestedDate].toString()),
                            staffId = it[staffId]?.value,
                             scheduledDate =it[scheduledDate],
                            status = it[status],
                            notifiedAt = it[notifiedAt].toString(),
                            isUrgent = it[isUrgent],
                            remarks = it[remarks],
                            cancellationReason = it[cancellationReason],
                            createdAt = it[createdAt].toString(),
                            updatedAt = it[updatedAt].toString(),
                        )
                    }
                    .singleOrNull()
            } else {
                null
            }

        }
    }
}
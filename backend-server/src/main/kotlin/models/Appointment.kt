package com.ite393group5.models

import com.ite393group5.utilities.DateSerializer
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable


@Serializable
data class Appointment(
    val id: Int,
    val studentId: Int,
    val staffId: Int?,
    val appointmentType: String,
    val documentType: String?,
    val reason: String?,
    @Serializable(with = DateSerializer::class)
    val requestedDate: LocalDate? = null,
    @Serializable(with = DateSerializer::class)
    val scheduledDate: LocalDate? = null,
    val status: String,
    val notifiedAt: String?,
    val isUrgent: Boolean,
    val remarks: String?,
    val cancellationReason: String?,
    val createdAt: String,
    val updatedAt: String
)

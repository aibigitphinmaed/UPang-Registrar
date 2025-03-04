package com.ite393group5.models

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
    val requestedDate: LocalDate? = null,
    val scheduledDate: LocalDate? = null,
    val status: String,
    val notifiedAt: String?,
    val isUrgent: Boolean,
    val remarks: String? = null,
    val cancellationReason: String?,
    val createdAt: String,
    val updatedAt: String
)

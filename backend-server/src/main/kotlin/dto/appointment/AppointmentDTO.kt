package com.ite393group5.dto.appointment

import kotlinx.serialization.Serializable

@Serializable
data class AppointmentDTO(
    val id: Int,
    val studentId: Int,
    val staffId: Int?,
    val appointmentType: String,
    val documentType: String?,
    val reason: String?,
    val requestedDate: String,
    val scheduledDate: String?,
    val status: String,
    val isUrgent: Boolean,
    val remarks: String?,
    val cancellationReason: String?
)

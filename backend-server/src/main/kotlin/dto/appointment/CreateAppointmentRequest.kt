package com.ite393group5.dto.appointment

import kotlinx.serialization.Serializable

@Serializable
data class CreateAppointmentRequest(
    val studentId: Int,
    val staffId: Int? = null,
    val appointmentType: String,
    val documentType: String?,
    val reason: String?,
    val requestedDate: String,
    val scheduledDate: String?,
    val isUrgent: Boolean
)


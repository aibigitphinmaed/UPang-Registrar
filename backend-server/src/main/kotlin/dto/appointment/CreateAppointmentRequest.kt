package com.ite393group5.dto.appointment

import kotlinx.serialization.Serializable

@Serializable
data class CreateAppointmentRequest(
    val staffId: Int? = null,
    val appointmentType: String,
    val documentType: String?,
    val reason: String? = "",
    val requestedDate: String,
    val isUrgent: Boolean? = false
)


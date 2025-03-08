package com.ite393group5.dto.appointment

import kotlinx.serialization.Serializable

@Serializable
data class ModifyAppointmentRequest(
    val id: Int,
    val studentId: Int,
    val staffId: Int?,
    val appointmentType: String,
    val documentType: String?,
    val reason: String?,
    val requestedDate: String,
)

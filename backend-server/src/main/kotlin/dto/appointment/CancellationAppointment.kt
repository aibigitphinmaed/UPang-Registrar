package com.ite393group5.dto.appointment

import kotlinx.serialization.Serializable

@Serializable
data class CancelAppointmentRequest(
    val id: Int,
    val studentId: Int,
    val staffId: Int? = null,
    val cancellationReason: String?,
    val status: String?,
)
package com.ite393group5.dto.appointment

import kotlinx.serialization.Serializable

@Serializable
data class UpdateAppointmentRequest(
    val id: Int,
    val studentId: Int,
    val staffId: Int? = null,
    val scheduledDate: String? = null,
    val status: String? = null, // "Pending", "Approved", "Cancelled", "Rejected", "Completed".
    val isUrgent: Boolean? = null,
    val remarks: String? = null,
    val cancellationReason: String? = null
)

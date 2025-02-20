package com.ite393group5.android_app.models

import com.ite393group5.android_app.utilities.DateSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class Appointment(
    val id: Int,
    val studentId: Int,
    val staffId: Int? = null,
    val appointmentType: String,
    val documentType: String? = null,
    val reason: String? = null,
    @Serializable(with = DateSerializer::class)
    val requestedDate: LocalDate?,
    @Serializable(with = DateSerializer::class)
    val scheduledDate: LocalDate? = null,
    //Status: "pending", "approved", "completed", "cancelled", "no-Show"
    val status: String = "pending",
    @Serializable(with = DateSerializer::class)
    val notifiedAt: LocalDate? = null,
    val isUrgent: Boolean = false,
    val remarks: String? = null,
    val cancellationReason: String? = null
)

package com.ite393group5.dto.appointment

import com.ite393group5.utilities.DateSerializer
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class AppointmentSearchRequest(
    val searchQuery: String? = null,
    val status: String? = null,
    @Serializable(with = DateSerializer::class)
    val appointmentDate: LocalDate? = null,
    @Serializable(with = DateSerializer::class)
    val requestedDate: LocalDate? = null,
)
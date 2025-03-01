package com.ite393group5.dto.appointment

import kotlinx.serialization.Serializable



//probably add more later or change the object for something better
@Serializable
data class AppointStatusRequest(
    val status: String,
)
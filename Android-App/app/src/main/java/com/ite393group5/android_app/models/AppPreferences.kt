package com.ite393group5.android_app.models

import kotlinx.serialization.Serializable

@Serializable
data class AppPreferences(
    val userId: Int? = null,
    val token: Token? = null,
    val personalInfo: PersonalInfo? = null,
    val locationInfo: LocationInfo? = null,
    val profileImageLocation: String? = null,
    val currentAppointment: Appointment? = null
)




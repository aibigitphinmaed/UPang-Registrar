package com.ite393group5.android_app.models

import kotlinx.serialization.Serializable

@Serializable
data class AppPreferences(
    val token: Token? = null,
    val personalInfo: PersonalInfo? = null,
    val locationInfo: LocationInfo? = null,
)




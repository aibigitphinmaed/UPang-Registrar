package com.ite393group5.android_app.models

import kotlinx.serialization.Serializable

@Serializable
data class StudentProfile(
    val studentPersonalInfo: PersonalInfo? = null,
    val studentAddressInfo: LocationInfo? = null,
)
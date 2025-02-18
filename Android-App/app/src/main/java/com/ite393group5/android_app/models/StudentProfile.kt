package com.ite393group5.android_app.models

import kotlinx.serialization.Serializable

@Serializable
data class StudentProfile(
    val userPersonalInfo: PersonalInfo? = null,
    val userAddressInfo: LocationInfo? = null,
)
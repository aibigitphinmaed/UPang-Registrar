package com.ite393group5.dto

import com.ite393group5.models.LocationInfo
import com.ite393group5.models.PersonalInfo
import com.ite393group5.models.Updatable
import kotlinx.serialization.Serializable

@Serializable
data class StudentProfile(
    val studentPersonalInfo: PersonalInfo? = null,
    val studentAddressInfo: LocationInfo? = null,
) : Updatable
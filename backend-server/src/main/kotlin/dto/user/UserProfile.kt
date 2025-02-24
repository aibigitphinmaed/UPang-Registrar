package com.ite393group5.dto.user

import com.ite393group5.models.LocationInfo
import com.ite393group5.models.PersonalInfo
import com.ite393group5.models.Updatable
import com.ite393group5.models.User
import kotlinx.serialization.Serializable

@Serializable
data class UserProfile(
    val userPersonalInfo: PersonalInfo? = null,
    val userAddressInfo: LocationInfo? = null,
    val userAccount: User? = null
) : Updatable
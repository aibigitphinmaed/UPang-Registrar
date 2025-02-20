package com.ite393group5.android_app.profilemanagement.state

import com.ite393group5.android_app.models.LocationInfo
import com.ite393group5.android_app.models.PersonalInfo

data class ProfileScreenState(
    val personalInfo: PersonalInfo? = null,
    val locationInfo: LocationInfo? = null,
    val editMode:Boolean = false,
    val showConfirmWindow:Boolean = false,
    val confirmEditMode:Boolean = false,
    val changePasswordMode:Boolean = false,
    val confirmChangePasswordMode:Boolean = false,
    val profileImageLocation:String? = null
)
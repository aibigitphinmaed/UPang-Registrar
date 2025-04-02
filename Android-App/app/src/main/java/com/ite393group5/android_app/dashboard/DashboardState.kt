package com.ite393group5.android_app.dashboard

import com.ite393group5.android_app.models.PersonalInfo


data class DashboardState(
    val fullName: String = "",
    val personalInfo: PersonalInfo? = null
)
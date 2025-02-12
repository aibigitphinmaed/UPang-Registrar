package com.ite393group5.android_app.models

import kotlinx.serialization.Serializable

//created this class for the  user_id serialization
@Serializable
data class UserIdResponse(
    val id:Int
)

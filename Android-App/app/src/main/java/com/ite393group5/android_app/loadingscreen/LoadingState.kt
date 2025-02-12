package com.ite393group5.android_app.loadingscreen

import kotlinx.serialization.Serializable


@Serializable
data class LoadingState(
    val waitingUserId:Boolean = true,
    val waitingForProfileData:Boolean = true,
    val waitingForAddressData:Boolean = true
)
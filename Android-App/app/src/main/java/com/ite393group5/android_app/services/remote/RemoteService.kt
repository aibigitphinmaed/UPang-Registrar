package com.ite393group5.android_app.services.remote

import com.ite393group5.android_app.models.LocationInfo
import com.ite393group5.android_app.models.LoginRequest
import com.ite393group5.android_app.models.PersonalInfo
import com.ite393group5.android_app.models.Token

interface RemoteService {
    suspend fun login(loginRequest: LoginRequest):Boolean
    suspend fun logout()
    suspend fun refreshToken(): Token
    suspend fun getPersonalInformation(): PersonalInfo
    suspend fun getLocationInformation():LocationInfo
}
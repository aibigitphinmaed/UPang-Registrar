package com.ite393group5.android_app.services.remote

import android.graphics.Bitmap
import com.ite393group5.android_app.models.LocationInfo
import com.ite393group5.android_app.models.LoginRequest
import com.ite393group5.android_app.models.PersonalInfo
import com.ite393group5.android_app.models.Token
import io.ktor.http.HttpStatusCode

interface RemoteService {
    suspend fun login(loginRequest: LoginRequest):Boolean
    suspend fun logout()
    suspend fun refreshToken(): Token
    suspend fun getPersonalInformation(userId:Int): PersonalInfo
    suspend fun getLocationInformation(userId:Int): LocationInfo
    suspend fun retrievePreferences()
    suspend fun updatePersonalInformation(personalInfo: PersonalInfo,locationInfo: LocationInfo, profileImagePath:String) :  HttpStatusCode
    suspend fun changePassword(currentPassword: String, newPassword: String) : HttpStatusCode
    suspend fun getProfileImage(): Bitmap?
}
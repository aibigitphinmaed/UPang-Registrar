package com.ite393group5.android_app.services.local

import com.ite393group5.android_app.models.LocationInfo
import com.ite393group5.android_app.models.PersonalInfo
import com.ite393group5.android_app.models.Token
import java.time.LocalDate

interface LocalService {
    suspend fun saveToDateStore()
    suspend fun saveBearerToken(token: Token)
    suspend fun saveRefreshToken(token: Token)
    suspend fun getBearerToken(): String
    suspend fun getRefreshToken(): String
    suspend fun checkLocalDataStoreIfNotNull(): Boolean
    suspend fun clearEverythingOnLocal()
    suspend fun getTokenExpiration(): LocalDate?
    suspend fun checkSession(): Boolean
    suspend fun saveUserId(userId: Int)
    suspend fun getUserid(): Int?
    suspend fun savePersonalInfo(personalInfo: PersonalInfo)
    suspend fun saveAddressInfo(locationInfo: LocationInfo)
    suspend fun getPersonalInfo(): PersonalInfo
suspend fun  getAddressInfo():LocationInfo
suspend fun getUserId():Int?

}
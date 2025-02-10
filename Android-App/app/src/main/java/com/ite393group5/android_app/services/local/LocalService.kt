package com.ite393group5.android_app.services.local

import com.ite393group5.android_app.models.Token
import java.time.LocalDate

interface LocalService {
    suspend fun saveBearerToken(token: Token)
    suspend fun saveRefreshToken(token: Token)
    suspend fun getBearerToken():String
    suspend fun getRefreshToken():String
    suspend fun checkLocalDataStoreIfNotNull():Boolean
    suspend fun clearEverythingOnLocal()
    suspend fun getTokenExpiration():LocalDate?
}
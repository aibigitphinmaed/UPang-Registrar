package com.ite393group5.android_app.data.auth

import com.ite393group5.android_app.models.LoginRequest
import com.ite393group5.android_app.services.local.LocalServiceImpl
import com.ite393group5.android_app.services.remote.RemoteServiceImpl
import java.time.LocalDate
import javax.inject.Inject


class AuthRepositoryImpl @Inject constructor(
    private val localServiceImpl: LocalServiceImpl,
    private val remoteServiceImpl: RemoteServiceImpl
) : AuthRepository {

    override suspend fun login(loginRequest: LoginRequest): Boolean {
        return remoteServiceImpl.login(loginRequest)
    }

    override suspend fun logout() {
        localServiceImpl.clearEverythingOnLocal()
    }

    override suspend fun isAuthenticated(): Boolean {
        if (localServiceImpl.checkLocalDataStoreIfNotNull()){
            val currentLocalDate = LocalDate.now()
            val tokenExpirationDate = localServiceImpl.getTokenExpiration()
            return tokenExpirationDate?.isAfter(currentLocalDate) ?: false
        }else{
            return false
        }
    }


}
package com.ite393group5.android_app.profilemanagement.domain

import com.ite393group5.android_app.services.remote.RemoteService
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class ProfileChangePasswordUseCase @Inject constructor(
    private val remoteService: RemoteService
){
    suspend fun executeChangePassword(currentPassword: String, newPassword: String) : HttpStatusCode = withContext(Dispatchers.IO){
        return@withContext remoteService.changePassword(currentPassword, newPassword)
    }
}
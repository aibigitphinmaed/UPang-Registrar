package com.ite393group5.android_app.data.auth

import com.ite393group5.android_app.models.LoginRequest

interface AuthRepository {
    suspend fun login(loginRequest: LoginRequest):Boolean
    suspend fun logout()
    suspend fun isAuthenticated():Boolean
}
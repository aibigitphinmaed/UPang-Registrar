package com.ite393group5.dto.user

import kotlinx.serialization.Serializable

@Serializable
data class PasswordRequest(
    val currentPassword: String,
    val newPassword: String
)
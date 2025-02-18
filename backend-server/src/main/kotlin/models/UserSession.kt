package com.ite393group5.models

import io.ktor.server.auth.Principal
import kotlinx.serialization.Serializable

@Serializable
data class UserSession(
    val username: String?,
    val userId: Int?,
    val role: String?
) : Principal
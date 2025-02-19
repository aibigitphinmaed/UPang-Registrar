package com.ite393group5.models

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int? = null,
    val username: String,
    val password: String,
    val role: String,
    val salt: String,
    val imageId: Int? = null,
): Updatable

package com.ite393group5.models

data class TokenConfig(
    val issuer: String,
    val audience: List<String>,
    val expiresIn: Long,
    val secret: String,
    val realm: String
)

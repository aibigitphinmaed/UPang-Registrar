package com.ite393group5.models
import com.ite393group5.utilities.DateSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDate


@Serializable
data class Token(
    val bearerToken: String,
    val refreshToken: String,
    @Serializable(with = DateSerializer::class)
    val expirationTimeDate: LocalDate?
)




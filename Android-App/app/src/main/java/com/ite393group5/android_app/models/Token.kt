package com.ite393group5.android_app.models

import com.ite393group5.android_app.utilities.DateSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class Token(
    val bearerToken: String? = null,
    val refreshToken: String? = null,
    @Serializable(with = DateSerializer::class)
    val expirationTimeDate: LocalDate? = null
)




package com.ite393group5.utilities

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.ite393group5.models.TokenConfig
import java.util.Date
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days

class JwtTokenService {

    fun generateStudentToken(config: TokenConfig,username: String): String{


        var token = JWT.create()
            .withAudience(*config.audience.toTypedArray())
            .withIssuer(config.issuer)
            .withExpiresAt(Date(System.currentTimeMillis()  + config.expiresIn))
            .withClaim("username", username)
            .withClaim("role", "student")

        return token.sign(Algorithm.HMAC256(config.secret))
    }

    fun generateStaffToken(config: TokenConfig,username: String): String{


        var token = JWT.create()
            .withAudience(*config.audience.toTypedArray())
            .withIssuer(config.issuer)
            .withExpiresAt(Date(System.currentTimeMillis()  + config.expiresIn))
            .withClaim("username", username)
            .withClaim("role", "staff")

        return token.sign(Algorithm.HMAC256(config.secret))
    }

}
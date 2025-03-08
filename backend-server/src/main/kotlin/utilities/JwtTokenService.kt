package com.ite393group5.utilities

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.ite393group5.models.TokenConfig
import java.util.*

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

    fun generateStaffToken(config: TokenConfig,username: String, role: String): String{


        //this need to be revised since we have two web users admin and staff
        var token = JWT.create()
            .withAudience(*config.audience.toTypedArray())
            .withIssuer(config.issuer)
            .withExpiresAt(Date(System.currentTimeMillis()  + config.expiresIn))
            .withClaim("username", username)
            .withClaim("role", role)


        return token.sign(Algorithm.HMAC256(config.secret))
    }

}
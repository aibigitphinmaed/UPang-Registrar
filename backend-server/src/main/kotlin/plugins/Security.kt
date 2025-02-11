package com.ite393group5.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.ite393group5.models.TokenConfig
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.respond

fun Application.configureSecurity(studentTokenConfig: TokenConfig, staffTokenConfig: TokenConfig) {
    // Please read the jwt property from the config file if you are using EngineMain

    authentication {
        jwt("student-auth") {
            realm = studentTokenConfig.realm
            verifier(
                JWT
                    .require(Algorithm.HMAC256(studentTokenConfig.secret))
                    .withAnyOfAudience(*studentTokenConfig.audience.toTypedArray())
                    .withIssuer(studentTokenConfig.issuer)
                    .build()
            )
            validate { credential ->
                val tokenAudiences = credential.payload.audience

                if ("mobile-client" in tokenAudiences) {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
            challenge { defaultScheme, realm ->
                call.respond(HttpStatusCode.Unauthorized, mapOf("error" to  "Invalid or missing authentication token for student access."))
                }
        }

        jwt("staff-auth") {
            realm = staffTokenConfig.realm
            verifier(
                JWT
                    .require(Algorithm.HMAC256(staffTokenConfig.secret))
                    .withAnyOfAudience(*staffTokenConfig.audience.toTypedArray())
                    .withIssuer(staffTokenConfig.issuer)
                    .build()
            )
            validate{
                credential ->
                val tokenAudiences = credential.payload.audience
                if ("web-client" in tokenAudiences) {
                    JWTPrincipal(credential.payload)
                }else{
                    null
                }
            }
            challenge { defaultScheme, realm ->
                call.respond(HttpStatusCode.Unauthorized, mapOf("error" to  "Invalid or missing authentication token for staff access."))
            }
        }
    }
}

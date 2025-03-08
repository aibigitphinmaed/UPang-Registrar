package com.ite393group5.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.ite393group5.models.TokenConfig
import com.ite393group5.models.UserSession
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.respond
import io.ktor.server.response.respondRedirect
import io.ktor.server.sessions.SessionStorageMemory
import io.ktor.server.sessions.Sessions
import io.ktor.server.sessions.cookie

fun Application.configureSecurity(studentTokenConfig: TokenConfig, staffTokenConfig: TokenConfig) {


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

                if ("mobile-client" in tokenAudiences || credential.payload.getClaim("username").asString() != null) {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
            challenge { defaultScheme, realm ->
                call.respond(
                    HttpStatusCode.Unauthorized,
                    mapOf("error" to "Invalid or missing authentication token for student access.")
                )
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
            validate { credential ->
                val tokenAudiences = credential.payload.audience

                if ("web-client" in tokenAudiences || credential.payload.getClaim("username").asString() != null) {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }

            challenge { defaultScheme, realm ->
                call.respond(
                    HttpStatusCode.Unauthorized,
                    mapOf("error" to "Invalid or missing authentication token for staff access.")
                )
            }
        }

    }


}

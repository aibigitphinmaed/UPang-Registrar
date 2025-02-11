package com.ite393group5

import com.ite393group5.models.TokenConfig
import com.ite393group5.plugins.configureDatabases
import com.ite393group5.plugins.configureMonitoring
import com.ite393group5.plugins.configureRouting
import com.ite393group5.plugins.configureSecurity
import com.ite393group5.plugins.configureSerialization
import com.ite393group5.utilities.JwtTokenService
import io.ktor.server.application.*
import java.time.Duration

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {

    val tokenService = JwtTokenService()
    val studentTokenConfig = TokenConfig(
        issuer = environment.config.property("jwt.issuer").getString(),
        audience = environment.config.property("jwt.studentAudience").getList(),
        expiresIn = Duration.ofDays(30).toMillis(),
        secret = environment.config.property("jwt.secret").getString(),
        realm = environment.config.property("jwt.realm").getString(),
    )
    val staffTokenConfig = TokenConfig(
        issuer = environment.config.property("jwt.issuer").getString(),
        audience = environment.config.property("jwt.staffAudience").getList(),
        expiresIn = Duration.ofDays(30).toMillis(),
        secret = environment.config.property("jwt.secret").getString(),
        realm = environment.config.property("jwt.realm").getString(),
    )

    configureSecurity(studentTokenConfig,staffTokenConfig)
    configureMonitoring()
    configureSerialization()

   val userService = configureDatabases()
    configureRouting(userService, tokenService, studentTokenConfig,staffTokenConfig)

}


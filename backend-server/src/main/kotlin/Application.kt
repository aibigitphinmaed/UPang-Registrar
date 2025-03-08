package com.ite393group5

import com.ite393group5.models.TokenConfig
import com.ite393group5.plugins.*
import com.ite393group5.services.appointment.AppointmentServiceImpl
import com.ite393group5.services.user.UserServiceImpl
import com.ite393group5.utilities.JwtTokenService
import io.ktor.server.application.*
import io.ktor.server.netty.EngineMain
import java.time.Duration

fun main(args: Array<String>) {
    EngineMain.main(args)
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
    val adminTokenConfig = TokenConfig(
        issuer = environment.config.property("jwt.issuer").getString(),
        audience = environment.config.property("jwt.adminAudience").getList(),
        expiresIn = Duration.ofDays(30).toMillis(),
        secret = environment.config.property("jwt.secret").getString(),
        realm = environment.config.property("jwt.realm").getString(),
    )

    configureSecurity(studentTokenConfig,staffTokenConfig)
    configureMonitoring()
    configureSerialization()


    val database = configureDatabases()


    val userService = UserServiceImpl()
    val appointmentService = AppointmentServiceImpl()
    configureSockets(userService)
    configureRouting(userService, appointmentService,tokenService, studentTokenConfig,staffTokenConfig,adminTokenConfig)
    configureRateLimiter()


}


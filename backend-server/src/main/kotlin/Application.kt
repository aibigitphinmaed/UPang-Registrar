package com.ite393group5

import com.ite393group5.models.TokenConfig
import com.ite393group5.plugins.configureDatabases
import com.ite393group5.plugins.configureMonitoring
import com.ite393group5.plugins.configureRouting
import com.ite393group5.plugins.configureSecurity
import com.ite393group5.plugins.configureSerialization
import com.ite393group5.plugins.configureSession
import com.ite393group5.services.StudentServiceImpl
import com.ite393group5.services.UserServiceImpl
import com.ite393group5.utilities.JwtTokenService
import freemarker.cache.ClassTemplateLoader
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.server.application.*
import io.ktor.server.freemarker.FreeMarker
import io.ktor.server.plugins.cors.routing.CORS
import java.time.Duration

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {



    configureSession()

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


    val database = configureDatabases()


    val userService = UserServiceImpl(database)
    val studentService = StudentServiceImpl(database)
    configureRouting(userService,studentService, tokenService, studentTokenConfig,staffTokenConfig)

}


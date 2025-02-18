package com.ite393group5.plugins

import com.ite393group5.models.UserSession
import freemarker.cache.ClassTemplateLoader
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.freemarker.FreeMarker
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.sessions.SessionStorageMemory
import io.ktor.server.sessions.SessionTransportTransformerMessageAuthentication
import io.ktor.server.sessions.Sessions
import io.ktor.server.sessions.cookie
import io.ktor.util.hex


fun Application.configureSession() {
    val secretSignKey = hex("6819b57a326945c1968f45236589")
    install(Sessions) {
        cookie<UserSession>("staff-session", SessionStorageMemory()) {
            cookie.path = "/"
            cookie.maxAgeInSeconds = 60 * 60 * 24
            transform(SessionTransportTransformerMessageAuthentication(secretSignKey))
        }
    }
    install(FreeMarker) {
        templateLoader = ClassTemplateLoader(this::class.java.classLoader, "templates")
    }

    install(CORS) {
        allowHost("0.0.0.0:8080", schemes = listOf("http", "https"))

        allowHost("localhost:8080", schemes = listOf("http", "https"))
        allowCredentials = true
        allowHeader(HttpHeaders.ContentType)
        allowHeader(HttpHeaders.Accept)
        allowHeader(HttpHeaders.Authorization)
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Patch)
        allowMethod(HttpMethod.Delete)
        allowHeader("staff-session")
        exposeHeader("staff-session")
    }
}
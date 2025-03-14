package com.ite393group5.plugins

import com.ite393group5.utilities.DateSerializer
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json(
            Json{
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            }

        )
    }
}

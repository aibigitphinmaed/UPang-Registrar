package com.ite393group5.plugins

import com.ite393group5.services.UserService
import com.ite393group5.services.UserServiceImpl
import io.ktor.server.application.*
import java.sql.Connection
import java.sql.DriverManager


fun Application.configureDatabases(): Connection {
    return connectToPostgres()
}

fun Application.connectToPostgres(): Connection {
    return try {
        Class.forName("org.postgresql.Driver")
        val url = environment.config.propertyOrNull("database.url")?.getString()
            ?: "jdbc:postgresql://localhost:5432/postgresdb"
        val user = environment.config.propertyOrNull("database.user")?.getString() ?: "postgres"
        val password = environment.config.propertyOrNull("database.password")?.getString() ?: "postgres"

        DriverManager.getConnection(url, user, password).also {
            log.info("✅ Successfully connected to the PostgreSQL database.")
        }
    } catch (e: Exception) {
        log.error("❌ Database connection failed: ${e.message}", e)
        throw e
    }
}
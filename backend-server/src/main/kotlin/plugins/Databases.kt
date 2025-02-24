package com.ite393group5.plugins

import com.ite393group5.db.ImageRecordsTable
import com.ite393group5.db.LocationInformationTable
import com.ite393group5.db.PersonalInformationTable
import com.ite393group5.db.UserTable
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException


fun Application.configureDatabases() {
    connectToPostgres()
}

fun Application.connectToPostgres() {
    try{
        val url = environment.config.propertyOrNull("database.url")?.getString()
            ?: "jdbc:postgresql://localhost:5432/postgresdb"
        val user = environment.config.propertyOrNull("database.user")?.getString() ?: "postgres"
        val password = environment.config.propertyOrNull("database.password")?.getString() ?: "postgres"
        Database.connect(
            url = url,
            driver = "org.postgresql.Driver",
            user = user,
            password = password
        )

        transaction { SchemaUtils.create(
            UserTable,
            PersonalInformationTable,
            LocationInformationTable,
            ImageRecordsTable
        ) }

        log.info("✅ Successfully connected to PostgresSQL and initialized tables.")
    }catch(e: SQLException){
        log.error("❌ Database connection failed: ${e.message}", e)
        throw e
    }
}


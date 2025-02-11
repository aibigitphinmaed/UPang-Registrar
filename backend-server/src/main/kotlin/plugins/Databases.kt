package com.ite393group5.plugins

import com.ite393group5.services.UserService
import com.ite393group5.services.UserServiceImpl
import io.ktor.server.application.*
import java.sql.Connection
import java.sql.DriverManager


fun Application.configureDatabases() : UserService{
    val dbConnection = connectToPostgres(embedded = true)
    return UserServiceImpl(dbConnection)
}


fun Application.connectToPostgres(embedded:Boolean) : Connection{
    Class.forName("org.postgresql.Driver")
    return if(embedded){
        DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgresdb","postgres","postgres")
    }else{
        DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgresdb","postgres","postgres")
    }
}
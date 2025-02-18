package com.ite393group5.plugins

import com.ite393group5.models.User
import com.ite393group5.services.UserServiceImpl
import io.ktor.server.application.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.sync.Mutex
import kotlin.time.Duration.Companion.seconds


val queueResponseFlow = MutableSharedFlow<String>()
val queueFlow = queueResponseFlow.asSharedFlow()
val currentQueueList = mutableListOf<User>()
val previousQueueList = mutableListOf<User>()
 val activeSessions = mutableSetOf<WebSocketSession>()
 val mutex = Mutex()

fun Application.configureSockets(userServiceImpl: UserServiceImpl) {
    install(WebSockets){
        pingPeriod = 15.seconds
        timeout = 15.seconds
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }

}
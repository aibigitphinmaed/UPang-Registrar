package com.ite393group5.android_app.services.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import javax.inject.Inject


class QueueBroadcastReceiver : BroadcastReceiver() {
    @Inject
    lateinit var webSocketService: WebSocketService
    override fun onReceive(context: Context?, intent: Intent?) {

    }
}





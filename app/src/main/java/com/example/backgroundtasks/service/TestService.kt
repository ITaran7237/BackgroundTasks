package com.example.backgroundtasks.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.backgroundtasks.MainActivity.Companion.START_FOREGROUND_ACTION
import com.example.backgroundtasks.MainActivity.Companion.STOP_FOREGROUND_ACTION
import com.example.backgroundtasks.R

const val START_UPDATE_ACTION = "START_UPDATE_ACTION"
const val EXTRA_KEY_NUMBER = "EXTRA_KEY_STEP"
private const val NOTIFICATION_ID = 54

class TestService : Service() {
    private lateinit var notificationBuilder: NotificationCompat.Builder

    override fun onCreate() {
        super.onCreate()
        println("Service onCreate")
        val channelId =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel()
            } else {
                ""
            }
        createNotificationBuilder(channelId)
    }

    override fun onDestroy() {
        super.onDestroy()
        println("Service onDestroy")
        stopForegroundService()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action.equals(START_FOREGROUND_ACTION)) {
            createBroadcastIntent("service running")
        } else if (intent?.action.equals(STOP_FOREGROUND_ACTION)) {
            NotificationManagerCompat.from(this@TestService).cancelAll()
            stopForegroundService()
        }
        return START_NOT_STICKY
    }

    private fun createNotificationBuilder(channelId: String) {
        notificationBuilder = NotificationCompat.Builder(this, channelId).apply {
            setContentText("Service running...")
            setSmallIcon(R.mipmap.ic_launcher)
        }
        startForeground(NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun createNotificationChannel(): String {
        val channelId = "TestService"
        val channelName = "TestChannel"
        val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_NONE)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
        return channelId
    }

    private fun createBroadcastIntent(text: String) {
        val intent = Intent(START_UPDATE_ACTION)
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        intent.putExtra(EXTRA_KEY_NUMBER, text)
        sendBroadcast(intent)
    }

    private fun stopForegroundService() {
        stopForeground(true)
        stopSelf()
    }
}
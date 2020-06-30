package com.example.backgroundtasks.alarmManager

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import com.example.backgroundtasks.MainActivity.Companion.EVENT_ACTION
import com.example.backgroundtasks.broadcastReceiver.AlarmManagerReceiver

private var alarmManager: AlarmManager? = null
private lateinit var alarmIntent: PendingIntent

fun startAlarmManager(context: Context?, millisecondsToElapse: Long) {
    alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    alarmIntent = Intent(context, AlarmManagerReceiver::class.java).let { intent ->
        intent.action = EVENT_ACTION
        PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    alarmManager?.set(
        AlarmManager.ELAPSED_REALTIME_WAKEUP,
        SystemClock.elapsedRealtime() + millisecondsToElapse, alarmIntent
    )
}

fun stopAlarmManager(context: Context?) {
    alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    alarmIntent = Intent(context, AlarmManagerReceiver::class.java).let { intent ->
        intent.action = EVENT_ACTION
        PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    alarmManager?.cancel(alarmIntent)
}
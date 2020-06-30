package com.example.backgroundtasks.broadcastReceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.backgroundtasks.MainActivity.Companion.ALARM_EVENT
import com.example.backgroundtasks.MainActivity.Companion.EVENT_ACTION

class AlarmManagerReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action.equals(EVENT_ACTION)) {
            try {
                context?.sendBroadcast(Intent(ALARM_EVENT))
            } catch (ex: Exception) {
                println(ex.message)
            }
        }
    }
}
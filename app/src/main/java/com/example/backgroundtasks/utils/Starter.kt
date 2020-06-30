package com.example.backgroundtasks.utils

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.example.backgroundtasks.service.TestService

fun launchTestService(context: Context, action: String) {
    val intent = Intent(context, TestService::class.java)
    intent.action = action
    ContextCompat.startForegroundService(context, intent)
}
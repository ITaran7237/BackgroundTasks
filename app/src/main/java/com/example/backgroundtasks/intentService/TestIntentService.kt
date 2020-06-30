package com.example.backgroundtasks.intentService

import android.app.IntentService
import android.content.Intent
import java.util.concurrent.TimeUnit

class TestIntentService : IntentService("TestIntentService") {
    override fun onHandleIntent(intent: Intent?) {
        println(">>> onHandleIntentService start")
        try {
            TimeUnit.SECONDS.sleep(5)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        println(">>> onHandleIntentService end")
    }
}
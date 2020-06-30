package com.example.backgroundtasks.workManager

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class TestWorkManager constructor(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    override fun doWork(): Result {
        return try {
            for (i in 1..5) {
                Thread.sleep(500)
                println(">>> $i")
            }
            Result.success()
        } catch (throwable: Throwable) {
            println(">>> $throwable")
            Result.failure()
        }
    }
}
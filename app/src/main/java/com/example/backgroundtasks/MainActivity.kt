package com.example.backgroundtasks

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.backgroundtasks.alarmManager.startAlarmManager
import com.example.backgroundtasks.alarmManager.stopAlarmManager
import com.example.backgroundtasks.downloadManager.TestDownloadManager
import com.example.backgroundtasks.intentService.TestIntentService
import com.example.backgroundtasks.service.EXTRA_KEY_NUMBER
import com.example.backgroundtasks.service.START_UPDATE_ACTION
import com.example.backgroundtasks.utils.launchTestService
import com.example.backgroundtasks.workManager.TestWorkManager
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    companion object {
        const val DOWNLOAD_TEST_LINK = "https://github.com/username/projectname/archive/hello.zip"
        const val START_FOREGROUND_ACTION = "start_foreground"
        const val STOP_FOREGROUND_ACTION = "stop_foreground"
        const val EVENT_ACTION = "EVENT_ACTION"
        const val ALARM_EVENT = "ALARM_EVENT"
    }
    private val downloadManager by lazy { TestDownloadManager(applicationContext, DOWNLOAD_TEST_LINK, "hello.zip") }
    private val oneTimeWorkRequest = OneTimeWorkRequest.Builder(TestWorkManager::class.java).build()
    private val periodicWorkRequest = PeriodicWorkRequest.Builder(TestWorkManager::class.java, 15, TimeUnit.MINUTES).build()
    private val workManager = WorkManager.getInstance(application)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initButtonClickListener()
        observeWorkManager()
    }

    override fun onStart() {
        super.onStart()
        val timerIntentFilter = IntentFilter()
        timerIntentFilter.addAction(START_UPDATE_ACTION)
        timerIntentFilter.addCategory(Intent.CATEGORY_DEFAULT)
        registerReceiver(testServiceReceiver, timerIntentFilter)
        registerReceiver(alarmManagerReceiver, IntentFilter(ALARM_EVENT))
    }

    override fun onStop() {
        unregisterReceiver(testServiceReceiver)
        unregisterReceiver(alarmManagerReceiver)
        super.onStop()
    }

    private fun initButtonClickListener() {
        btnStartService.setOnClickListener { launchTestService(this, START_FOREGROUND_ACTION) }
        btnStopService.setOnClickListener { launchTestService(this, STOP_FOREGROUND_ACTION) }

        btnStartDownloadManager.setOnClickListener { downloadManager.downloadFile() }
        btnStopDownloadManager.setOnClickListener { downloadManager.stopDownloadAllFiles() }

        btnStartAlarmManager.setOnClickListener { startAlarmManager(applicationContext, 2000) }
        btnStopAlarmManager.setOnClickListener { stopAlarmManager(applicationContext) }

        btnStartWorkManager.setOnClickListener {
            workManager.enqueue(oneTimeWorkRequest)
//            workManager.enqueue(periodicWorkRequest)
        }
        btnStopWorkManager.setOnClickListener {
            workManager.cancelWorkById(oneTimeWorkRequest.id)
//            workManager.cancelWorkById(periodicWorkRequest.id)
        }

        btnStartIntentService.setOnClickListener { startService(Intent(this, TestIntentService::class.java)) }
    }

    private fun observeWorkManager(){
        workManager.getWorkInfoByIdLiveData(oneTimeWorkRequest.id)
            .observe(this, Observer<WorkInfo> {
                println(">>> isFinished oneTimeWorkRequest = ${it.state.isFinished}  status = ${it.state.name}")
            })

        workManager.getWorkInfoByIdLiveData(periodicWorkRequest.id)
            .observe(this, Observer<WorkInfo> {
                println(">>> isFinished periodicWorkRequest = ${it.state.isFinished}  status = ${it.state.name}")
            })
    }

    private val testServiceReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.getIntExtra(EXTRA_KEY_NUMBER, 1)
            Toast.makeText(this@MainActivity, "Service Triggered", Toast.LENGTH_LONG).show()
        }
    }

    private val alarmManagerReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            if (intent?.action == ALARM_EVENT) {
                Toast.makeText(this@MainActivity, "Alarm Triggered", Toast.LENGTH_LONG).show()
            }
        }
    }
}
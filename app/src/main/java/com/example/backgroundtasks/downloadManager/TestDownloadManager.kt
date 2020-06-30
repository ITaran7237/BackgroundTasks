package com.example.backgroundtasks.downloadManager

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment

class TestDownloadManager(private val context: Context, uri: String, outputFileName: String) {

    private val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

    private val downloadRequest = DownloadManager.Request(Uri.parse(uri)).apply {
        setTitle("Zip package")
        setDescription("A zip package with some files")
        setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, outputFileName)
    }

    private var downloadIDsList = arrayListOf<Long>()

    fun downloadFile() {
        val downloadID = downloadManager.enqueue(downloadRequest)
        downloadIDsList.add(downloadID)
        println(">>> downloadID = $downloadID    downloadIDsListSize = ${downloadIDsList.size}")
    }

    fun stopDownloadAllFiles() {
        if (downloadIDsList.isNotEmpty()) {
            for (downloadID in downloadIDsList) {
                downloadManager.remove(downloadID)
            }
        }
        downloadIDsList.clear()
    }
}


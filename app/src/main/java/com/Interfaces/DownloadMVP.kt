package com.Interfaces

import android.app.Activity
import android.app.DownloadManager
import android.content.BroadcastReceiver

interface DownloadMVP {

    interface View {

        fun onExceptionDownload(exception: String)

        fun updateProgress(progress: Int)

        fun updateText(text: String)

        fun setReceiver(downloadCompleteReceiver: BroadcastReceiver)

        fun setDownloadManager(downloadManager: DownloadManager, idDownload: Long)

        fun onSuccessDonwload()

    }

    interface Presenter {

        fun setView(view: View)

        fun onResponse(response: String)

        fun setProgress(progress: Int)

        fun setUpdateProgressText(text: String)

        fun setDownloadCompleteReceiver(downloadCompleteReceiver: BroadcastReceiver)

        fun setManager(downloadManager: DownloadManager, idDownload: Long)

        fun executeDonwload(activity: Activity)

    }

    interface Model {

        fun doDownloadUpdate(activity: Activity)
        fun responseReceiver(response: String)

    }


}
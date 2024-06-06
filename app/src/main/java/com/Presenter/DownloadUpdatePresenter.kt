package com.Presenter

import android.app.Activity
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.util.Log
import com.Interfaces.DownloadMVP
import javax.inject.Inject

class DownloadUpdatePresenter @Inject constructor(private var model: DownloadMVP.Model) :
    DownloadMVP.Presenter {

    private val TAG = "DownloadUpdatePresenter"
    private var view: DownloadMVP.View? = null

    override fun setView(view: DownloadMVP.View) {
        this.view = view
    }

    override fun onResponse(response: String) {

        view?.let {

            if (response == "Success") {
                it.onSuccessDonwload()
            } else {

                when (response.split(",")[0]) {
                    "Exception" -> view?.onExceptionDownload(response.split(",")[1])
                    else -> "Error desconocido"
                }

            }

        }
    }

    override fun setProgress(progress: Int) {
        view?.updateProgress(progress)
    }

    override fun setUpdateProgressText(text: String) {
        view?.updateText(text)
    }

    override fun setDownloadCompleteReceiver(downloadCompleteReceiver: BroadcastReceiver) {
        Log.i(TAG, "setDownloadCompleteReceiver: No null")
        view?.setReceiver(downloadCompleteReceiver)
    }

    override fun setManager(downloadManager: DownloadManager, idDownload: Long) {
        Log.i(TAG, "setDownloadManager: No null")
        view?.setDownloadManager(downloadManager, idDownload)
    }

    override fun executeDonwload(activity: Activity) {
        model.doDownloadUpdate(activity)
    }
}
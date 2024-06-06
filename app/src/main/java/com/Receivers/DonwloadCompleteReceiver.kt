package com.Receivers

import android.app.Activity
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import com.Constants.ConstantsUpdate
import com.Interfaces.DownloadMVP
import com.Utilities.FilesK
import com.Utilities.PreferenceHelper
import com.innovacion.eks.beerws.BuildConfig
import java.io.File
import java.lang.ref.WeakReference

class DonwloadCompleteReceiver constructor(
    private var model: DownloadMVP.Model,
    private var destination: String,
    private var uri: Uri,
    private var files: FilesK,
    private var preferenceHelper: PreferenceHelper,
    private var activity: WeakReference<Activity>
) : BroadcastReceiver() {

    private val TAG = "DonwloadCompleteReceive"

    override fun onReceive(context: Context?, intent: Intent?) {

        val action = intent?.action
        if (DownloadManager.ACTION_DOWNLOAD_COMPLETE == action) {
            model.responseReceiver("Success")
            installApk()
        }

    }

    private fun installApk() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            val contentUri = FileProvider.getUriForFile(
                activity.get()!!,
                BuildConfig.APPLICATION_ID + ConstantsUpdate.PROVIDER_PATH,
                File(destination)
            )
            val install = Intent(Intent.ACTION_VIEW)
            install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            install.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            install.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true)
            install.data = contentUri
            activity.get()?.let {

                it.startActivity(install)
                it.unregisterReceiver(this)

            }


        } else {

            val install = Intent(Intent.ACTION_VIEW)
            install.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            install.setDataAndType(uri, ConstantsUpdate.APP_INSTALL_PATH)
            activity.get()?.let {

                it.startActivity(install)
                it.unregisterReceiver(this)

            }

        }

    }


}
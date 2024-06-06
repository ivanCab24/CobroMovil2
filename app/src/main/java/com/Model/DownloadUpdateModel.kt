package com.Model

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DownloadManager
import android.content.Context
import android.content.IntentFilter
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.util.Log
import com.Constants.ConstantsPreferences
import com.Constants.ConstantsUpdate
import com.Constants.ConstantsUpdate.FILE_NAME
import com.Interfaces.DownloadMVP
import com.Receivers.DonwloadCompleteReceiver
import com.Utilities.FilesK
import com.Utilities.PreferenceHelper
import dagger.Lazy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.lang.ref.WeakReference
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class DownloadUpdateModel @Inject constructor(
    private var presenter: Lazy<DownloadMVP.Presenter>,
    private var preferenceHelper: PreferenceHelper,
    private var files: FilesK
) : DownloadMVP.Model, CoroutineScope {

    private val TAG = "DownloadUpdateModel"
    private var URL_TO_DOWNLOAD = ""
    private lateinit var activity: WeakReference<Activity>
    private var downloading = false
    private var statusDownload = 0
    private val job = Job()


    override fun doDownloadUpdate(activity: Activity) {

        this.activity = WeakReference<Activity>(activity)

        URL_TO_DOWNLOAD =
            "http://${preferenceHelper.getString(ConstantsPreferences.PREF_SERVER, null)}/apks/$FILE_NAME"
        Log.i(TAG, URL_TO_DOWNLOAD)

        enqueDownload()


    }

    override fun responseReceiver(response: String) {
        presenter.get().onResponse(response)
    }

    private fun enqueDownload() {

        var destination =
            activity.get()?.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString() + "/"
        destination += FILE_NAME


        val uri = Uri.parse("${ConstantsUpdate.FILE_BASE_PATH}$destination")

        val file = File(destination)

        if (file.exists()) file.delete()

        val donwloadManager =
            activity.get()?.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val downloadUri = Uri.parse(URL_TO_DOWNLOAD)

        val request = DownloadManager.Request(downloadUri)
        request.setMimeType(ConstantsUpdate.MIME_TYPE)
        request.setTitle("Descargando $FILE_NAME")
        request.setDescription(FILE_NAME)
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationUri(uri)

        val id: Long = donwloadManager.enqueue(request)
        presenter.get().setManager(donwloadManager, id)

        val onComplete =
            DonwloadCompleteReceiver(this, destination, uri, files, preferenceHelper, activity)
        this.activity.get()
            ?.registerReceiver(onComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        presenter.get().setDownloadCompleteReceiver(onComplete)

        launch { invokeQUery(donwloadManager, id) }

    }

    @SuppressLint("Range")
    private suspend fun invokeQUery(manager: DownloadManager, id: Long) {

        downloading = true

        while (downloading) {

            val query = DownloadManager.Query()
            query.setFilterById(id)

            val cursor = manager.query(query)
            cursor.moveToFirst()

            var downloadedSize: Int
            var totalSize: Int
            var progress: Int

            try {

                downloadedSize =
                    cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))
                totalSize =
                    cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))

                if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                    downloading = false
                }

                progress = ((downloadedSize * 100L) / totalSize).toInt()

                val finalProgress = progress

                statusMessage(cursor)

                withContext(Main) {

                    presenter.get().setProgress(finalProgress)
                    presenter.get().setUpdateProgressText("$finalProgress%")

                    if (statusDownload != DownloadManager.STATUS_SUCCESSFUL && statusDownload != DownloadManager.STATUS_RUNNING && statusDownload != DownloadManager.STATUS_PENDING) {

                        downloading = false
                        //servidor
                       presenter.get().onResponse("Exception,No se pudo conectar al servidor")

                    }

                }

                cursor.close()

            } catch (exception: Exception) {

                downloading = false
                withContext(Main) {
                    presenter.get()
                        .onResponse("Exception,Ocurrio un error en la descarga \n Presion el botón de reintentar")
                }

            }

        }


    }

    @SuppressLint("Range")
    private fun statusMessage(cursor: Cursor): String {

        var msg: String

        if (cursor.count != 0) {
            statusDownload = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
            Log.i(TAG, "statusMessage: $statusDownload")
            when (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))) {

                DownloadManager.STATUS_FAILED -> {
                    msg = "La descarga ha fallado $statusDownload"
                    downloading = false
                }

                DownloadManager.STATUS_PAUSED -> msg = "Descarga \n pausada"
                DownloadManager.STATUS_PENDING -> msg = "Descarga \n pendiente"
                DownloadManager.STATUS_RUNNING -> msg = "Descarga \n en progreso"
                DownloadManager.STATUS_SUCCESSFUL -> msg = "Descarga \n completada"
                else -> msg = "La descarga no \n esta a la vista"

            }
        } else {
            msg = "La descarga ha fallado"
        }

        Log.i(TAG, "statusMessage: $msg")
        return msg

    }

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

}
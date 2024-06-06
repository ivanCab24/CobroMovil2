package com.webservicestasks.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.Constants.ConstantsPreferences
import com.Constants.ConstantsPreferencesLogs
import com.Utilities.Files
import com.Utilities.Notifications.NotificationHelper
import com.Utilities.PreferenceHelper
import com.Utilities.PreferenceHelperLogs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.File
import java.io.IOException
import javax.inject.Inject

class WorkerEnviarLogs(
    appContext: Context,
    params: WorkerParameters,
    private val preferenceHelper: PreferenceHelper,
    private val preferenceHelperLogs: PreferenceHelperLogs,
    private val files: Files
) : CoroutineWorker(appContext, params) {

    private val TAG = "WorkerEnviarLogs"
    private var contadorSuccess = 0
    private var contadorTotalFiles = 0
    private val successFiles = mutableListOf<File>()
    private val notificationHelper = NotificationHelper(appContext)

    override suspend fun doWork(): Result {

        withContext(Dispatchers.IO){
            if (files.allFiles.isNotEmpty() || preferenceHelperLogs.getString(ConstantsPreferencesLogs.PREF_LOGS).isNotEmpty()){
                files.createLogFileFromPreferences(preferenceHelper, preferenceHelperLogs)
                sendLogs()
            }
        }


        return Result.success()

    }

    class Factory @Inject constructor(
        private val preferenceHelper: PreferenceHelper,
        private val preferenceHelperLogs: PreferenceHelperLogs,
        private val files: Files
    ): ChildWorkerFactory<WorkerEnviarLogs>{

        override fun create(
            appContext: Context,
            workerParameters: WorkerParameters
        ): WorkerEnviarLogs {
            return WorkerEnviarLogs(appContext,
                workerParameters,
                preferenceHelper,
                preferenceHelperLogs,
                files)
        }
    }

    private fun sendLogs(){

        {
            var mensaje = ""
            val files: Array<File?> = files.allFiles
            contadorTotalFiles = files.size

            for (item in files) {
                try {
                    Thread.sleep(500)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                    this.files.createFileException(
                        "Controller/UploadFilesTask/InterruptedException ${
                            Log.getStackTraceString(
                                e
                            )
                        }", preferenceHelper
                    )
                }

                mensaje = if (item?.let { uploadLog(it) } == true) {
                    "true"
                } else {
                    "false"
                }

            }

            if (mensaje == "true") {

                try {
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

                this.files.deleteAllFiles(ArrayList(successFiles))
                notificationHelper.sendHighPriorityNotification(
                    "Envío de logs",
                    "Logs envíados \n$contadorSuccess de $contadorTotalFiles enviados correctamente"
                )
            } else {
                notificationHelper.sendHighPriorityNotification(
                    "Envío de logs",
                    "Error al enviar logs\nSe volvera a intentar dentro de 30 minutos"
                )
            }
        }

    }

    private fun uploadLog(file: File) : Boolean{

        try {

            val requestBody: RequestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart(
                    "files",
                    file.name.replace(" ", ",").replace(":", ","),
                    RequestBody.create("text/*".toMediaTypeOrNull(),file))
                .addFormDataPart("files", "files")
                .addFormDataPart("submit", "submit")
                .build();

            val request: Request = Request.Builder().apply {
                url("http://${preferenceHelper.getString(ConstantsPreferences.PREF_SERVER, null)}/ws_pagomovil/upload.php")
                post(requestBody)
            }.build()

            val client = OkHttpClient()
            client.newCall(request).enqueue(object : Callback {

                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.body?.string() == "Archivo Subido"){
                        successFiles.add(file)
                        contadorSuccess++
                        Log.i(TAG, "onResponse: ${file.name}")
                    }
                }

            })

            return true

        }catch (exception: Exception){
            exception.printStackTrace()
            return false
        }

    }

}
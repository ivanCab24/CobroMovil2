package com.Model

import android.util.Log
import com.Constants.ConstantsPreferences.PREF_SERVER
import com.Interfaces.DialogWaitingFilesMVP
import com.Utilities.FilesK
import com.Utilities.PreferenceHelper
import dagger.Lazy
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.File
import java.io.IOException
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class DialogWaitingFilesModel @Inject constructor(
    private var presenter: Lazy<DialogWaitingFilesMVP.Presenter>,
    private var preferenceHelper: PreferenceHelper,
    private var filesK: FilesK
) : DialogWaitingFilesMVP.Model, CoroutineScope {

    private val TAG = "DialogWaitingFilesModel"
    private val job = Job()
    private val successFiles: ArrayList<File> = ArrayList()

    override fun executeUpdateFilesTask() {

        launch { doUploadFiles() }

    }

    private suspend fun doUploadFiles() {
        try {
            val files = filesK.getAllFiles()
            for ((contador, item) in files.withIndex()) {
                delay(500)
                withContext(Main) {
                    presenter.get()
                        .doUpdateProgressText(
                            "Subiendo archivo $contador de ${files.size}",
                            contador * 100 / files.size
                        )
                }
                uploadFile(item)
            }
            delay(1000)
            filesK.deleteAllFiles(successFiles)
            withContext(Main) {
                presenter.get().onResponseReceived("Success,Archivos subidos correctamente")
            }

        } catch (e: Exception) {
            e.printStackTrace()
            withContext(Main) {
                presenter.get().onResponseReceived("Exception,Error al subir los archivos")
            }
        }

    }

    private fun uploadFile(file: File) {

        try {
            val requestBody: RequestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart(
                    "files",
                    file.name.replace(" ", ",").replace(":", ","),
                    RequestBody.create("text/*".toMediaTypeOrNull(), file)
                )
                .addFormDataPart("files", "files")
                .addFormDataPart("submit", "submit")
                .build();

            val request: Request = Request.Builder()
                .url("http://${preferenceHelper.getString(PREF_SERVER, null)}/ws_pagomovil/upload.php")
                .post(requestBody)
                .build()

            val client = OkHttpClient()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.body?.string() == "Archivo Subido") {
                        successFiles.add(file)
                        Log.i(TAG, "onResponse: ${file.name}")
                    }
                }
            })

        } catch (e: Exception) {
            e.printStackTrace()
            presenter.get().onResponseReceived("Exception,${Log.getStackTraceString(e)}")
        }

    }

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO
}
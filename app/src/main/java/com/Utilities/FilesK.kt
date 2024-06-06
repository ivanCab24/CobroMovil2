package com.Utilities

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import com.Constants.ConstantsPreferences
import com.Constants.ConstantsPreferencesLogs
import okio.IOException
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class FilesK @Inject constructor(
    private val contentResolver: ContentResolver,
    private val preferenceHelper: PreferenceHelper,
    private val preferenceHelperLogs: PreferenceHelperLogs
) {

    private val TAG = "FilesK"

    private var date = ""
    private var hora = ""

    private val dirName = "CobroMovilLogs"

    private lateinit var directory: File
    private lateinit var contentValues: ContentValues


    fun append(fileName: String, text: String) {

        val finalName = fileName.replace(" ", ",").replace(":", ",").replace("_", ",")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, finalName)
                put(MediaStore.MediaColumns.MIME_TYPE, "text/plain")
                put(
                    MediaStore.MediaColumns.RELATIVE_PATH,
                    "${Environment.DIRECTORY_DOCUMENTS}/$dirName/"
                )
            }
            val uri: Uri? =
                contentResolver.insert(MediaStore.Files.getContentUri("external"), contentValues)
            Log.i(TAG, "append: ${uri?.path}")
            try {
                uri?.let {
                    val outputStream: OutputStream? = contentResolver.openOutputStream(uri)
                    outputStream?.let {
                        outputStream.apply {
                            write(text.toByteArray())
                            outputStream.close()
                        }
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }

        } else {
            directory = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
                dirName
            )
            if (!directory.exists())
                directory.mkdirs()
            val myExternalFile = File(directory, fileName)
            Log.i("ssssss", directory.path)
            try {
                FileOutputStream(myExternalFile).apply {
                    write(text.toByteArray())
                    close()
                }

            } catch (e: IOException) {
                e.printStackTrace()
            }

        }

    }

    @SuppressLint("Range")
    fun getAllFiles(): Array<File> {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

            val contentUri = MediaStore.Files.getContentUri("external")
            val selection = "${MediaStore.MediaColumns.RELATIVE_PATH}=?"
            val selectionArgs: Array<String> =
                arrayOf("${Environment.DIRECTORY_DOCUMENTS}/$dirName/")
            val cursor = contentResolver.query(contentUri, null, selection, selectionArgs, null)

            val files: MutableList<File> = mutableListOf()
            var filesF: Array<File> = arrayOf()

            cursor?.let {

                if (cursor.count != 0) {

                    while (cursor.moveToNext()) {
                        val fileName =
                            cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME))
                        val file = File("${Environment.DIRECTORY_DOCUMENTS}/$dirName/", fileName)
                        files.add(file)
                    }

                    Log.i(TAG, "getAllFiles: Q ${files.size}")
                    filesF = files.toTypedArray()
                    files.forEachIndexed { index, file ->
                        Log.i(TAG, "getAllFilesK: Q ${file.name}")
                        filesF[index] = file
                    }
                }
                cursor.close()
            }
            return filesF
        } else {

            var files = directory.listFiles()

            if (files != null) {
                for (item in files) {
                    Log.i(TAG, "getAllFiles: FileName ${item.name}")
                }
            } else {
                files = arrayOf()
            }

            return files

        }

    }

    @SuppressLint("Range")
    fun deleteAllFiles(fileArrayList: ArrayList<File>) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

            val contentUri = MediaStore.Files.getContentUri("external")
            val selection = "${MediaStore.MediaColumns.RELATIVE_PATH}=?"
            val selectionArgs = arrayOf("${Environment.DIRECTORY_DOCUMENTS}/$dirName/")
            val cursor = contentResolver.query(contentUri, null, selection, selectionArgs, null)

            var uri: Uri?

            for (file in fileArrayList) {
                cursor?.let {
                    if (cursor.count != 0) {
                        while (cursor.moveToNext()) {
                            val fileName =
                                cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME))
                            Log.i(TAG, "deleteAllFilesK: Q $fileName")

                            val id =
                                cursor.getLong(cursor.getColumnIndex(MediaStore.MediaColumns._ID))
                            uri = ContentUris.withAppendedId(contentUri, id)
                            uri?.let { uri ->
                                val deleted = contentResolver.delete(uri, null, null)
                                Log.i(TAG, "deleteAllFiles: after $deleted")
                            }
                        }
                    }

                }
            }

            cursor?.close()

        } else {

            for (file in fileArrayList) {
                val file = File("$directory/${file.name}")
                Log.i(TAG, "deleteAllFiles: ${file.path}")
                file.delete()
            }

        }

    }

    private fun createLogFile(fileName: String) {

        val log = preferenceHelperLogs.getString(ConstantsPreferencesLogs.PREF_LOGS)
        if (log.isNotEmpty()) {
            append(fileName, log)
            preferenceHelperLogs.removePreference(ConstantsPreferencesLogs.PREF_LOGS)
        }

    }

    fun createFileException(exception: String) {

        createTime()
        val fileName =
            "Unidad ${preferenceHelper.getString(ConstantsPreferences.PREF_UNIDAD, null)} Terminal ${
                preferenceHelper.getString(ConstantsPreferences.PREF_NUMERO_TERMINAL, null)
            } Exception $date $hora.txt"
        append(fileName, "Version ${Utils.versionName} $exception")

    }

    fun createLogFileFromPreferences() {

        createTime()
        val fileName =
            "Unidad ${preferenceHelper.getString(ConstantsPreferences.PREF_UNIDAD, null)} LogCobroMovil Terminal ${
                preferenceHelper.getString(ConstantsPreferences.PREF_NUMERO_TERMINAL, null)
            } $date $hora.txt"
        createLogFile(fileName)

    }

    fun registerLogs(descripcion: String, json: String) {
        val thread = Thread {
            try {
                var folio = ""
                if(json.contains("folio"))
                    folio = json.substring(json.indexOf("folio="),json.indexOf("folio=")+16)
                else if(json.contentEquals("FOLIOCTA"))
                    folio = json.substring(json.indexOf("FOLIOCTA"),json.indexOf("FOLIOCTA")+16)
                var params=HashMap<String,String>()
                params["Llave"] = "e936c17f7b54f02d32@@5463e0c3f749080b9"
                params["folio"] = folio
                params["proceso"] = descripcion!!
                params["Tipo"] = "PAGO"
                params["sjson"] = json!!
                Curl.sendPostRequest(params, "Graba_logs", preferenceHelper)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        thread.start()
        val log = preferenceHelperLogs.getString(ConstantsPreferencesLogs.PREF_LOGS)
        createTime()

        try {

            var jsonArray: JSONArray? = if (log.isEmpty()) JSONArray() else JSONArray(log)

            val jsonObject = JSONObject().apply {
                put("Fecha", date)
                put("Hora", hora)
                put("Version", Utils.versionName)
                put("Descripcion", descripcion)
                put("JSON", json)
            }

            jsonArray!!.put(jsonObject)
            preferenceHelperLogs.putString(ConstantsPreferencesLogs.PREF_LOGS, jsonArray.toString())

        } catch (e: JSONException) {
            e.printStackTrace()
            createFileException("Utilities/Files/registerLogs ${e.message}")
        }

    }

    fun createTime() {
        date = SimpleDateFormat("ddMMyyyy").format(Date())
        hora = SimpleDateFormat("HH:mm:ss:SS").format(Date())
    }

}
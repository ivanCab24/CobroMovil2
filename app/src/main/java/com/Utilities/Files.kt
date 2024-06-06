package com.Utilities

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import com.Constants.ConstantsPreferences
import com.Constants.ConstantsPreferencesLogs
import com.Utilities.Curl.Companion.sendPostRequest
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * Type: Class.
 * Access: Public.
 * Name: Files.
 *
 * @Description.
 * Esta clase es la encargada de generar los archivos locales como los logs.
 * @EndDescription.
 */
class Files(
    /**
     * The Context.
     */
    var context: Context
) {
    @JvmField
    @Inject
    var preferenceHelperLogs: PreferenceHelperLogs? = null

    @JvmField
    @Inject
    var preferenceHelper: PreferenceHelper? = null
    private var date = ""
    private var hora = ""
    private val dirName = "CobroMovilLogs"
    private val directory = File(
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
        dirName
    )
    private val contentResolver: ContentResolver
    private var contentValues: ContentValues? = null

    /**
     * Type: Method.
     * Parent: Files.
     * Name: append.
     *
     * @param fileName @PsiType:String.
     * @param text     @PsiType:String.
     * @Description.
     * Insertamos los valores para crear el archivo, como el nombre y contenido
     * @EndDescription.
     */
    fun append(fileName: String, text: String) {

        if(false){
            val finalName = fileName.replace(" ", ",").replace(":", ",").replace("_", ",")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                contentValues = ContentValues()
                contentValues!!.put(MediaStore.MediaColumns.DISPLAY_NAME, finalName)
                contentValues!!.put(MediaStore.MediaColumns.MIME_TYPE, "text/plain")
                contentValues!!.put(
                    MediaStore.MediaColumns.RELATIVE_PATH,
                    Environment.DIRECTORY_DOCUMENTS + "/" + dirName + "/"
                )
                val uri = contentResolver.insert(
                    MediaStore.Files.getContentUri("external"),
                    contentValues
                )
                Log.i("aaaaaaa", uri!!.path!!)
                try {
                    val outputStream = contentResolver.openOutputStream(uri)
                    outputStream!!.write(text.toByteArray())
                    outputStream.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                if (!directory.exists()) directory.mkdirs()
                Log.i("iiiiiiii", directory.path)
                val myExternalFile = File(directory, finalName)
                var fileOutputStream: FileOutputStream?
                try {
                    fileOutputStream = FileOutputStream(myExternalFile)
                    fileOutputStream.write(text.toByteArray())
                    fileOutputStream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    /**
     * Type: Method.
     * Parent: Files.
     * Name: getAllFiles.
     *
     * @return file [ ]
     * @Description.
     * Esta función retorna un arreglo de todos los archivos.
     * @EndDescription. file [ ].
     */
    val allFiles: Array<File?>
        @SuppressLint("Range")
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val contentUri = MediaStore.Files.getContentUri("external")
            val selection = MediaStore.MediaColumns.RELATIVE_PATH + "=?"
            val selectionArgs = arrayOf(Environment.DIRECTORY_DOCUMENTS + "/" + dirName + "/")
            val cursor = contentResolver.query(contentUri, null, selection, selectionArgs, null)
            val files = ArrayList<File>()
            var filesF: Array<File?>?
            if (cursor!!.count != 0) {
                while (cursor.moveToNext()) {
                    val fileName =
                        cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME))
                    val file = File(Environment.DIRECTORY_DOCUMENTS + "/" + dirName + "/", fileName)
                    files.add(file)
                }
                filesF = arrayOfNulls(files.size)
                Log.i(TAG, "getAllFiles: Q " + files.size)
                for (i in files.indices) {
                    Log.i(TAG, "getAllFiles: Q " + files[i].absolutePath)
                    filesF[i] = files[i]
                }
            } else {
                filesF = arrayOf()
            }
            cursor.close()
            filesF

        } else {
            var files = directory.listFiles()
            if (files != null) {
                for (file in files) {
                    Log.i(TAG, "getAllFiles: FileName " + file!!.name)
                }
            } else {
                files = arrayOf()
            }
            files
        }

    /**
     * Type: Method.
     * Parent: Files.
     * Name: deleteAllFiles.
     *
     * @param fileArrayList @PsiType:ArrayList<File>.
     * @Description.
     * Esta función elimina todos los archivos, cuando se suben los archivos, procede a borrarlos
     * @EndDescription.
    </File> */
    @SuppressLint("Range")
    fun deleteAllFiles(fileArrayList: ArrayList<File>) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val contentUri = MediaStore.Files.getContentUri("external")
            val selection = MediaStore.MediaColumns.RELATIVE_PATH + "=?"
            val selectionArgs = arrayOf(Environment.DIRECTORY_DOCUMENTS + "/" + dirName + "/")
            val cursor = contentResolver.query(contentUri, null, selection, selectionArgs, null)
            var uri: Uri?
            for (i in fileArrayList.indices) {
                if (cursor!!.count != 0) {
                    while (cursor.moveToNext()) {
                        val fileName =
                            cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME))
                        Log.i(
                            TAG,
                            "deleteAllFiles: Q " + fileName + " Array " + fileArrayList[i].name
                        )
                        val id = cursor.getLong(cursor.getColumnIndex(MediaStore.MediaColumns._ID))
                        uri = ContentUris.withAppendedId(contentUri, id)
                        val deleted = contentResolver.delete(uri, null, null)
                        Log.i(TAG, "deleteAllFiles: after$deleted")
                    }
                }
            }
            cursor!!.close()
        } else {
            for (i in fileArrayList.indices) {
                val file = File(directory.toString() + "/" + fileArrayList[i].name)
                Log.i(TAG, "deleteAllFiles: " + file.path)
                file.delete()
            }
        }
    }

    /**
     * Type: Method.
     * Parent: Files.
     * Name: createLogFile.
     *
     * @param fileName             @PsiType:String.
     * @param preferenceHelperLogs @PsiType:PreferenceHelperLogs.
     * @param preferenceHelper     @PsiType:PreferenceHelper.
     * @Description.
     * Este metofo crea el archivo, le da nombre
     * @EndDescription.
     */
    fun createLogFile(
        fileName: String,
        preferenceHelperLogs: PreferenceHelperLogs
    ) {
        val log = preferenceHelperLogs.getString(ConstantsPreferencesLogs.PREF_LOGS)
        if (log.isNotEmpty()) {
            append(fileName, log)
            preferenceHelperLogs.removePreference(ConstantsPreferencesLogs.PREF_LOGS)
        }
    }

    /**
     * Type: Method.
     * Parent: Files.
     * Name: createFileException.
     *
     * @param exception        @PsiType:String.
     * @param preferenceHelper @PsiType:PreferenceHelper.
     * @Description.
     * Este metodo crea los archivos de las excepciones
     * @EndDescription.
     */
    fun createFileException(exception: String, preferenceHelper: PreferenceHelper) {
        createTime()
        val fileName =
            ("E285 Unidad " + preferenceHelper.getString(ConstantsPreferences.PREF_UNIDAD, null) + " Terminal " + preferenceHelper.getString(
                    ConstantsPreferences.PREF_NUMERO_TERMINAL,
                    null
            )
                    + " Exception " + date + " " + hora + ".txt")
        append(fileName, "Version " + Utils.versionName + " " + exception)
    }

    /**
     * Type: Method.
     * Parent: Files.
     * Name: createInfoFile.
     *
     * @param metodo           @PsiType:String.
     * @param info             @PsiType:String.
     * @param preferenceHelper @PsiType:PreferenceHelper.
     * @Description.
     * Esta función crea un archivo de información.
     * @EndDescription.
     */
    fun createInfoFile(metodo: String, info: String, preferenceHelper: PreferenceHelper) {
        createTime()
        val fileName =
            ("E285 Unidad " + preferenceHelper.getString(ConstantsPreferences.PREF_UNIDAD, null) + " Terminal " + preferenceHelper.getString(
                    ConstantsPreferences.PREF_NUMERO_TERMINAL,
                    null
            )
                    + " " + metodo + " " + date + " " + hora + ".txt")
        append(fileName, "Version " + Utils.versionName + " " + info)
    }

    /**
     * Type: Method.
     * Parent: Files.
     * Name: createLogFileFromPreferences.
     *
     * @param preferenceHelper     @PsiType:PreferenceHelper.
     * @param preferenceHelperLogs @PsiType:PreferenceHelperLogs.
     * @Description.
     * Esta función crea un archivo de las preferencias guardadas
     * @EndDescription.
     */
    fun createLogFileFromPreferences(
        preferenceHelper: PreferenceHelper,
        preferenceHelperLogs: PreferenceHelperLogs
    ) {
        createTime()
        val fileName = ("E285 Unidad " + preferenceHelper.getString(ConstantsPreferences.PREF_UNIDAD, null) + " LogCobroMovil " + "Terminal " + preferenceHelper.getString(ConstantsPreferences.PREF_NUMERO_TERMINAL, null) + " " + date + " " + hora + ".txt")
        createLogFile(fileName, preferenceHelperLogs)
    }

    /**
     * Type: Method.
     * Parent: Files.
     * Name: registerLogs.
     *
     * @param descripcion          @PsiType:String.
     * @param json                 @PsiType:String.
     * @param preferenceHelperLogs
     * @param preferenceHelper
     * @Description.
     * Este método crea el archivo json de los logs.
     * @EndDescription.
     */
    fun registerLogs(
        descripcion: String?,
        json: String?,
        preferenceHelperLogs: PreferenceHelperLogs,
        preferenceHelper: PreferenceHelper
    ) {
        val log = preferenceHelperLogs.getString(ConstantsPreferencesLogs.PREF_LOGS)
        createTime()
        try {
            val jsonArray: JSONArray
            jsonArray = if (log.isEmpty()) JSONArray() else JSONArray(log)
            val jsonObject = JSONObject()
            jsonObject.put("Fecha", date)
            jsonObject.put("Hora", hora)
            jsonObject.put("Version", Utils.versionName)
            jsonObject.put("Terminal", "E285")
            jsonObject.put("Descripcion", descripcion)
            jsonObject.put("JSON", json)
            jsonArray.put(jsonObject)
            //preferenceHelperLogs.putString(ConstantsPreferencesLogs.PREF_LOGS, jsonArray.toString())
            val thread = Thread {
                try {
                    var params=HashMap<String,String>()
                    var folio = ""
                    if(json!!.contains("folio"))
                        folio = json!!.substring(json!!.indexOf("folio")+6,json!!.indexOf("folio")+16+6)
                    else if(json.contains("FOLIOCTA"))
                        folio = json!!.substring(json!!.indexOf("FOLIOCTA")+11,json!!.indexOf("FOLIOCTA")+16+10)
                    else if(json.contains("Folio"))
                        try{
                            folio = json!!.substring(json!!.indexOf("Folio")+6,json!!.indexOf("Folio")+16+5)
                        }catch (e:StringIndexOutOfBoundsException){
                            folio = json!!.substring(json!!.indexOf("Folio")+6,json!!.indexOf("Folio")+16)
                        }
                    Log.i("sssssss",folio)
                    params["Llave"] = "e936c17f7b54f02d32@@5463e0c3f749080b9"
                    params["folio"] = folio
                    params["proceso"] = descripcion!!
                    params["Tipo"] = "PAGO"
                    params["sjson"] = json!!
                    sendPostRequest(params,"Graba_logs",preferenceHelper)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            thread.start()
        } catch (e: JSONException) {
            e.printStackTrace()
            createFileException("Files/registerLogs " + e.message, preferenceHelper)
        }
    }

    /**
     * Type: Method.
     * Parent: Files.
     * Name: createTime.
     *
     * @Description.
     * Este método crea la fecha y hora para anexarlo a los logs.
     * @EndDescription.
     */
    private fun createTime() {
        date = SimpleDateFormat("ddMMyyyy").format(Date())
        hora = SimpleDateFormat("HH:mm:ss:SS").format(Date())
    }

    companion object {
        private const val TAG = "Files"
    }

    /**
     * Type: Method.
     * Parent: Files.
     * Name: Files.
     *
     * @param context @PsiType:Context.
     * @Description.
     * Este es el constructor del objeto File
     * @EndDescription.
     */
    init {
        contentResolver = context.contentResolver
    }
}
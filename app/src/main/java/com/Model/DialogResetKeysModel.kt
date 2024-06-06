package com.Model

import android.content.Context
import android.util.Log
import com.Constants.ConstantsPreferences
import com.DataModel.VtolResetKeys
import com.Interfaces.ResetKeysMVP
import com.Utilities.FilesK
import com.Utilities.PreferenceHelper
import com.Utilities.Utils
import com.Verifone.BtManager
import com.Verifone.CommandUtils
import com.Verifone.VerifonePinpadInterface
import com.Verifone.VerifonePinpadInterface.COM_03
import com.Verifone.VerifonePinpadInterface.MSG_Z10
import com.Verifone.VerifonePinpadInterface.MSG_Z11
import com.Verifone.VerifoneTaskManager
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonEncodingException
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.webservicestasks.ReadJsonFeadTaskK
import com.webservicestasks.ToksWebServiceExceptionRate
import com.webservicestasks.ToksWebServicesConnection
import com.webservicestasks.ToksWebServicesConnection.KEY
import com.webservicestasks.ToksWebServicesConnection.NAMESPACE
import com.webservicestasks.ToksWebServicesConnection.STRING
import com.webservicestasks.ToksWebServicesConnection.VTOL_RESETKYS
import dagger.Lazy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.apache.commons.lang3.ArrayUtils
import org.ksoap2.serialization.SoapObject
import java.lang.reflect.Type
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class DialogResetKeysModel @Inject constructor(
    var presenter: Lazy<ResetKeysMVP.Presenter>,
    var preferenceHelper: PreferenceHelper,
    var files: FilesK,
    var moshi: Moshi,
    var context: Context
) : ReadJsonFeadTaskK(), ResetKeysMVP.Model, VerifonePinpadInterface, ToksWebServicesConnection,
    CoroutineScope {

    private val job = Job()
    private val TAG = "DialogResetKeysModel"
    private var btManager: BtManager? = null

    override fun doResetKeys() {
        try {
            launch { initPinpadConnection() }
        } catch (e: Exception) {
            e.printStackTrace()
        }


    }

    //================================================================================
    // Iniciar conexión con la pinpad
    //================================================================================

    private suspend fun initPinpadConnection() {

        if (btManager == null) {
            Log.i(TAG, "initPinpadConnection: TCPNull")
            btManager = BtManager(
                context,
                preferenceHelper.getString(ConstantsPreferences.PREF_PINPAD, null)
            )
        }
        btManager?.let { btManager ->
            if (!btManager.isBtConected) {
                Log.i(TAG, "initPinpadConnection: No Conectada")
                if (btManager.BtSppWaitClient() != BtManager.BT_RETULT.SUCCESS) {
                    Log.i(TAG, "initPinpadConnection: Conexion Fallida")
                    withContext(Main) {
                        presenter.get()
                            .onResponse("Fail,No se pudo conectar a la pinpad\nSi el problema persiste reinicie el dispositivo")
                    }
                } else {
                    Log.i(TAG, "initPinpadConnection: Conexion Correcta")
                    withContext(Main) {
                        presenter.get().onResponse("Success,Generando Chip Tokens")
                    }
                    chipTokensTask()
                }
            } else {
                Log.i(TAG, "initPinpadConnection: Ya conectada")
                withContext(Main) { presenter.get().onResponse("Success,Generando Chip Tokens") }
                chipTokensTask()
            }
        } ?: run {
            withContext(Main) {
                presenter.get()
                    .onResponse("Fail,No se pudo conectar a la pinpad\nSi el problema persiste reinicie el dispositivo")
            }
        }
    }

    //================================================================================
    // Generación de chiptokens
    //================================================================================

    private suspend fun chipTokensTask() {
        files.registerLogs("Inicia ChipTokensTask", "")
        VerifoneTaskManager.limpiarTerminal()
        var regreso: ArrayList<String> = arrayListOf()
        val chDataIn = ByteArray(1024)
        val chAck = byteArrayOf(0x06)
        var key: String
        btManager?.let { btManager ->
            btManager.BtSendData(MSG_Z10)
            regreso.clear()
            regreso = btManager.BtRecvData(chDataIn, 10000)
            regreso = btManager.BtRecvData(chDataIn, 30000)
            btManager.BtSendData(chAck)
            val output = StringBuilder()
            for (item in regreso) {
                try {
                    output.append(Integer.parseInt(item, 16).toChar())
                } catch (e: NumberFormatException) {
                    e.printStackTrace()
                    withContext(Main) {
                        presenter.get()
                            .onResponse("Fail,Error al generar ChipTokens\nVuelva a intentar")
                    }
                    VerifoneTaskManager.limpiarTerminal()
                    return
                }
            }

            files.registerLogs("Termina ChipTokensTask", "")
            Log.i(TAG, "chipTokensTask: output $output")
            if (output.isNotEmpty()) {
                key = output.substring(8, output.length - 1)
                Log.i(TAG, "chipTokensTask: auxKey $key")
                withContext(Main) { presenter.get().onResponse("Success,Reseteando llaves") }
                //================================================================================
                // Inicia peticion de llaves
                //================================================================================
                try {
                    initResetKeysVtolRequest(key)
                } catch (e: JsonEncodingException) {
                    e.printStackTrace()
                    VerifoneTaskManager.limpiarTerminal(btManager)
                    withContext(Main) {
                        presenter.get()
                            .onResponse("Exception,Error al generar JSON\nPosible falla de BBVA")
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                    VerifoneTaskManager.limpiarTerminal(btManager)
                    files.createFileException(
                        "Model/chipTokensTask ${Log.getStackTraceString(e)}"
                    )
                    withContext(Main) {
                        presenter.get().onResponse("Exception,${Utils.getExceptionRated(e)}")
                    }

                }

            } else {

                withContext(Main) {
                    presenter.get()
                        .onResponse("Fail,Error al generar ChipTokens\nVuelva a intentar")
                }
                VerifoneTaskManager.limpiarTerminal()
            }
        } ?: run {
            withContext(Main) {
                presenter.get().onResponse("Exception,Error conexión no establecida con la pinpad")
            }
        }
    }

    //================================================================================
    // Realizar peticion de llave
    //================================================================================

    private suspend fun initResetKeysVtolRequest(chipTokens: String) {
        val request = createRequestResetKeysVtol(chipTokens)
        val result = this.doIn(request, VTOL_RESETKYS, preferenceHelper)
        withContext(Main) { postResultFromResetKeysVtol(result) }
    }

    private fun createRequestResetKeysVtol(chipTokens: String): SoapObject {
        val request = SoapObject(NAMESPACE, VTOL_RESETKYS)
        request.addProperty("Llave", KEY)
        request.addProperty("Cadena", STRING)
        request.addProperty("Unidad", preferenceHelper.getString(ConstantsPreferences.PREF_UNIDAD, null))
        request.addProperty("ChipTokens", chipTokens.replace("\u0003", ""))

        Log.i(TAG, "createRequestResetKeysVtol: $request")

        files.registerLogs(
            "Inicia VtolResetKeys",
            request.toString()
        )
        return request
    }

    private fun postResultFromResetKeysVtol(result: String) {

        files.registerLogs("Terminal VtolResetKeys", result)

        if (ToksWebServiceExceptionRate.rateError(result).isNotEmpty()) {

            VerifoneTaskManager.limpiarTerminal(btManager)
            presenter.get().onResponse("Exception,${ToksWebServiceExceptionRate.rateError(result)}")

        } else {

            val type: Type = Types.newParameterizedType(List::class.java, VtolResetKeys::class.java)
            val jsonAdapter: JsonAdapter<List<VtolResetKeys>> = moshi.adapter(type)
            val vtolResetKeys: List<VtolResetKeys>? = jsonAdapter.fromJson(result)

            vtolResetKeys?.let {
                val item = vtolResetKeys[0]
                if (item.CODIGO == "OK") {
                    if (item.CAMPO102 != "NA" && item.CAMPO102 != "N/A") {
                        launch { inyeccionDeLlavesTask(item) }
                    } else {
                        VerifoneTaskManager.limpiarTerminal(btManager)
                        val mensaje = "Error: ${item.CAMPO28} (${item.CAMPO27})"
                        presenter.get().onResponse("Fail,$mensaje")
                    }
                } else {
                    VerifoneTaskManager.limpiarTerminal(btManager)
                    val mensaje = "Error: ${item.CAMPO28} (${item.CAMPO27})"
                    presenter.get().onResponse("Fail,$mensaje")
                }
            } ?: run {
                VerifoneTaskManager.limpiarTerminal(btManager)
                presenter.get().onResponse("Exception,Error al generar JSON\nPosible falla de BBVA")
            }
        }
    }
    //================================================================================
    // Termina peticion de llaves
    //================================================================================

    //================================================================================
    // Inicia inyeccion de llaves
    //================================================================================

    private suspend fun inyeccionDeLlavesTask(vtolResetKeys: VtolResetKeys) {
        files.registerLogs(
            "Inicia InyeccionDeLlavesTask",
            ""
        )
        val chDataOut: ByteArray?
        var chDataIn = ByteArray(1024)
        var chAck = byteArrayOf(0x06)

        var chipTokens: String
        var newCadena: String
        var auxCadena: String
        var enviaLrc: String
        val auxLrc: Byte?
        val auxmsgcZ11: ByteArray?

        chipTokens = CommandUtils.toHex(vtolResetKeys.CAMPO102)
        val chip: ByteArray = CommandUtils.hexStringToByteArray(chipTokens)

        newCadena =
            CommandUtils.byteArrayToHexString(MSG_Z11) + chipTokens + CommandUtils.byteArrayToHexString(
                COM_03
            )
        auxCadena = newCadena.replace(" ", "")
        enviaLrc = CommandUtils.hexToAscii(auxCadena)

        auxLrc = CommandUtils.calculateLRC(enviaLrc)
        auxmsgcZ11 = ArrayUtils.addAll(MSG_Z11, *chip)

        val auxmsgZ11: ByteArray? = ArrayUtils.addAll(auxmsgcZ11, *COM_03)
        val msgcZ11: ByteArray? = ArrayUtils.addAll(auxmsgZ11, auxLrc)

        chDataOut = msgcZ11

        btManager?.let { btManager ->
            btManager.BtSendData(chDataOut)
            btManager.BtRecvData(chDataIn, 10000)
            btManager.BtRecvData(chDataIn, 30000)
            btManager.BtSendData(chAck)
            files.registerLogs(
                "Termina InyeccionDeLlavesTask",
                ""
            )

            if (vtolResetKeys.CAMPO28 == "APROBADA") {
                preferenceHelper.putString(ConstantsPreferences.PREF_CAMPO_ER, "0")
                VerifoneTaskManager.limpiarTerminal(btManager)
                withContext(Main) {
                    presenter.get().onResponse("Finish,Inyección de llaves exitosa")
                }

            } else {
                VerifoneTaskManager.limpiarTerminal(btManager)
                withContext(Main) { presenter.get().onResponse("Fail,${vtolResetKeys.CAMPO28}") }
            }

        } ?: run {
            withContext(Main) {
                presenter.get()
                    .onResponse("Fail,Conexión perdida con pinpad, vuelva a intentar\nSi el problema persiste reinicie la aplicación")
            }
        }
    }

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO
}
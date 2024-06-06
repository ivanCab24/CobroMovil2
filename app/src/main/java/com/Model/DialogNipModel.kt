package com.Model

import android.util.Log
import com.Constants.ConstantsPreferences
import com.DataModel.ACCException
import com.DataModel.NipACC

import com.Interfaces.DialogNipMVP
import com.Utilities.FilesK
import com.Utilities.PreferenceHelper
import com.Utilities.Utils
import com.View.Fragments.ContentFragment
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.webservicestasks.ReadJsonFeadTaskK
import com.webservicestasks.ToksWebServiceExceptionRate
import com.webservicestasks.ToksWebServicesConnection
import com.webservicestasks.ToksWebServicesConnection.*
import dagger.Lazy
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main
import org.ksoap2.serialization.SoapObject
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class DialogNipModel @Inject constructor(
    private val presenter: Lazy<DialogNipMVP.Presenter>,
    private val preferenceHelper: PreferenceHelper,
    private val files: FilesK


) : ReadJsonFeadTaskK(), DialogNipMVP.Model, ToksWebServicesConnection, CoroutineScope {

    private val job = Job()
    private val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()

    override fun executeNipRequest(membresia: String) {
        launch {
            try {
                initGeneraNip(membresia)
            } catch (e: Exception) {
                e.printStackTrace()
                files.createFileException(
                    "Model/executeNipRequest ${Log.getStackTraceString(e)}",
                )
                withContext(Main) { posExceptionFromAPI(e) }
            }
        }
    }

    override fun exexute_Reenvio_sms() {
        launch {
            try {
                initSMS()
            } catch (e: Exception) {
                e.printStackTrace()

            }
        }
    }

    private fun posExceptionFromAPI(e: Exception) {
        val exceptionRate = ToksWebServiceExceptionRate.rateError(Log.getStackTraceString(e))
        presenter.get().onResponseReceived("Exception,$exceptionRate")
    }



    //================================================================================
    // Methos for Nip
    //================================================================================

    private suspend fun initGeneraNip(membresia: String) {
        val request = createRequestNip(membresia)
        val result = this.doIn(request, GENERAR_NIP, preferenceHelper)
        withContext(Main) { postResultFromAPI(result) }
    }
    private suspend fun initSMS() {
        val request = Reenvio_sms_Request()
        val result = this.doIn(request, REENVIO_SMS, preferenceHelper)
        Log.i("SMS", result)
    }

    private fun createRequestNip(membresia: String): SoapObject {
        val marca =
            Utils.evaluarMarcaSeleccionada(preferenceHelper.getString(ConstantsPreferences.PREF_MARCA_SELECCIONADA, null))
        val request = SoapObject(NAMESPACE, GENERAR_NIP)
        request.addProperty("Llave", KEY)
        request.addProperty("Membresia", membresia)
        request.addProperty("ticket", preferenceHelper.getString(ConstantsPreferences.PREF_FOLIO, null))
        request.addProperty("marca", marca)
        request.addProperty("telefono",ContentFragment.copiaMiembro!!.response.membresia!!.telefono)
        request.addProperty("nombre",ContentFragment.copiaMiembro!!.response.membresia!!.nombre)
        files.registerLogs(
            "Inicia generacion de NIP",
            request.toString()
        )
        return request
    }

    private fun Reenvio_sms_Request(): SoapObject {

        val request = SoapObject(NAMESPACE, REENVIO_SMS)
        request.addProperty("llave", KEY)
        request.addProperty("folio", preferenceHelper.getString(ConstantsPreferences.PREF_FOLIO, null))
        request.addProperty("unidad", ContentFragment.cuenta.idLocal)

        files.registerLogs(
            "Accion de boton",
            request.toString()
        )
        return request
    }

    private fun postResultFromAPI(result: String) {
        files.registerLogs(
            "Termina Genera NIP",
            result
        )

        if (ToksWebServiceExceptionRate.rateError(result).isNotEmpty()) {
            presenter.get().onResponseReceived(
                "Exception,${
                    ToksWebServiceExceptionRate.rateError(result)
                }"
            )

        } else {

            val jsonAdapter: JsonAdapter<NipACC> = moshi.adapter(NipACC::class.java)
            val nipResponse: NipACC? = jsonAdapter.fromJson(result)
            if (result.contains("code")) {
                nipResponse?.let {
                    if (nipResponse.exceptionMessage.isEmpty() && nipResponse.success)
                        presenter.get().onResponseReceived("Success,${nipResponse.response?.nip}")
                    else
                        presenter.get().onResponseReceived("Fail,${nipResponse.exceptionMessage}")
                } ?: run {
                    presenter.get()
                        .onResponseReceived("Exception,Error al generar NIP vuelva a intentar")
                }
            } else {
                val jsonAdapter = moshi.adapter(ACCException::class.java)
                val exceptionResponse: ACCException? = jsonAdapter.fromJson(result)
                exceptionResponse?.let { accException ->
                    presenter.get().onResponseReceived("Exception,${accException.ExceptionMessage}")
                } ?: run {
                    presenter.get()
                        .onResponseReceived("Exception,Error al generar NIP vuelva a intentar")
                }
            }
        }
    }

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO
}
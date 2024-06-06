package com.Model

import android.util.Log
import com.Constants.ConstantsPreferences
import com.Constants.ConstantsPreferences.PREF_FOLIO_CUENTA_CERRADA
import com.Interfaces.CierraCuentaMVP
import com.Presenter.CierraCuentaPresenter
import com.Presenter.CierraCuentaPresenter.Companion.filesWeakReference
import com.Presenter.CierraCuentaPresenter.Companion.preferenceHelper
import com.Presenter.CierraCuentaPresenter.Companion.preferenceHelperLogs
import com.Presenter.CierraCuentaPresenter.Companion.view
import com.webservicestasks.ReadJSONFeedTask
import com.webservicestasks.ToksWebServiceExceptionRate
import com.webservicestasks.ToksWebServicesConnection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.json.JSONArray
import org.ksoap2.serialization.SoapObject
import kotlin.coroutines.CoroutineContext

class CierraCuentaModel : CierraCuentaMVP.Model, CoroutineScope {
    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    private object WebServiceTaskCaller {
        fun CallWSConsumptionTask(request: SoapObject, method: String) {
            val service: ReadJSONFeedTask = object : ReadJSONFeedTask() {
                override fun onResponseReceived(jsonResult: String) {
                    if (ToksWebServiceExceptionRate.rateError(jsonResult) != "") {
                        val exceptionRated = ToksWebServiceExceptionRate.rateError(jsonResult)
                        CierraCuentaPresenter.view!!.onExceptionCierraCuentaResult(exceptionRated)
                    } else {
                        cierraCuentaRequestResult(jsonResult)
                    }
                }
            }
            service.execute(request, method, CierraCuentaPresenter.preferenceHelper)
        }
    }

    companion object {
        private fun cierraCuentaRequest() {
            val request = SoapObject(
                ToksWebServicesConnection.NAMESPACE,
                ToksWebServicesConnection.CIERRA_CUENTA
            )
            request.addProperty("Llave", ToksWebServicesConnection.KEY)
            request.addProperty("Cadena", ToksWebServicesConnection.STRING)
            request.addProperty(
                "Folio",
                preferenceHelper!!.getString(ConstantsPreferences.PREF_FOLIO, null)
            )
            request.addProperty(
                "Unidad",
                preferenceHelper!!.getString(ConstantsPreferences.PREF_UNIDAD, null)
            )
            request.addProperty(
                "Usuario",
                preferenceHelper!!.getString(ConstantsPreferences.PREF_ESTAFETA, null)
            )
            Log.d(CierraCuentaPresenter.TAG, "cierraCuentaRequest: $request")
            filesWeakReference!!.get()!!.registerLogs(
                "Inicia CierraCuenta",
                request.toString(),
                preferenceHelperLogs!!,
                preferenceHelper!!
            )
            WebServiceTaskCaller.CallWSConsumptionTask(
                request,
                ToksWebServicesConnection.CIERRA_CUENTA
            )
        }

        private fun cierraCuentaRequestResult(jsonResult: String) {
            preferenceHelper!!.putString(
                PREF_FOLIO_CUENTA_CERRADA,
                preferenceHelper!!.getString(ConstantsPreferences.PREF_FOLIO, null)
            )
            try {
                filesWeakReference!!.get()!!.registerLogs(
                    "Termina CierraCuenta",
                    jsonResult,
                    preferenceHelperLogs!!,
                    preferenceHelper!!
                )
                val jsonArray = JSONArray(jsonResult)
                val jsonObject = jsonArray.getJSONObject(0)
                val codigo = jsonObject.getString("CODIGO").toUpperCase()
                if (codigo == "OK") {
                    val respuesta = jsonObject.getString("RESPUESTA")
                    val cerrada = jsonObject.getString("CERRADA")
                    if (respuesta == "Cuenta Cerrada" && cerrada == "1") {
                        view!!.onSuccessCierraCuentaResult(respuesta)
                    } else if (respuesta.contains("Posición Cerrada, cuenta abierta \r por posiciones pendientes")) {
                        view!!.onSuccessCierraCuentaResult(respuesta)
                    } else {
                        view!!.onFailCierraCuentaResult(respuesta)
                    }
                } else {
                    view!!.onFailCierraCuentaResult(codigo)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                filesWeakReference!!.get()!!.createFileException(
                    "Controller/CierraCuentaRequest/cierraCuentaRequestResult " + jsonResult +
                            " " + Log.getStackTraceString(e), preferenceHelper!!
                )
                view!!.onExceptionCierraCuentaResult(e.message)
                System.out.println("Despues de cerrar la cuenta--->" + jsonResult)
            }
        }
    }

    override fun executeCierraCuentaRequest() {
        cierraCuentaRequest()
    }
}
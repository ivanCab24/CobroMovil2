package com.Model

import android.util.Log
import com.Constants.ConstantsPreferences
import com.DataModel.Caja
import com.Interfaces.CajaMVP
import com.Presenter.CajaPresenter
import com.Presenter.CajaPresenter.Companion.filesWeakReference
import com.Presenter.CajaPresenter.Companion.preferenceHelper
import com.Presenter.CajaPresenter.Companion.preferenceHelperLogs
import com.Presenter.CajaPresenter.Companion.view
import com.webservicestasks.ReadJSONFeedTask
import com.webservicestasks.ToksWebServiceExceptionRate
import com.webservicestasks.ToksWebServicesConnection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.json.JSONArray
import org.json.JSONException
import org.ksoap2.serialization.SoapObject
import kotlin.coroutines.CoroutineContext

class CajaModel : CajaMVP.Model, CoroutineScope {
    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    object WebServiceTaskCaller {
        fun CallWSConsumptionTask(request: SoapObject, method: String) {
            val service: ReadJSONFeedTask = object : ReadJSONFeedTask() {
                override fun onResponseReceived(jsonResult: String) {
                    if (ToksWebServiceExceptionRate.rateError(jsonResult) != "") {
                        val exceptionRated = ToksWebServiceExceptionRate.rateError(jsonResult)
                        CajaPresenter.view!!.onExceptionCajaResult(exceptionRated)
                    } else {
                        cajaRequestResult(jsonResult)
                    }
                }
            }
            service.execute(request, method, preferenceHelper)
        }
    }

    companion object {
        private fun cajaRequest() {
            val request = SoapObject(
                ToksWebServicesConnection.NAMESPACE,
                ToksWebServicesConnection.REVISA_CAJA_ABIERTA
            )
            request.addProperty("Llave", ToksWebServicesConnection.KEY)
            request.addProperty("Cadena", ToksWebServicesConnection.STRING)
            request.addProperty(
                "Unidad",
                preferenceHelper!!.getString(ConstantsPreferences.PREF_UNIDAD, null)
            )
            request.addProperty(
                "Folio",
                preferenceHelper!!.getString(ConstantsPreferences.PREF_FOLIO, null)
            )
            Log.d(CajaPresenter.TAG, "cajaRequest: $request")
            filesWeakReference!!.get()!!.registerLogs(
                "Inicia CajaRequest",
                request.toString(),
                preferenceHelperLogs!!,
                preferenceHelper!!
            )
            WebServiceTaskCaller.CallWSConsumptionTask(
                request,
                ToksWebServicesConnection.REVISA_CAJA_ABIERTA
            )
        }

        private fun cajaRequestResult(jsonResult: String) {
            try {
                filesWeakReference!!.get()!!.registerLogs(
                    "Termina CajaRequest",
                    jsonResult,
                    preferenceHelperLogs!!,
                    preferenceHelper!!
                )
                preferenceHelper!!.putString(ConstantsPreferences.PREF_JSON_CAJA, jsonResult)
                val jsonArray = JSONArray(jsonResult)
                val jsonObject = jsonArray.getJSONObject(0)
                val codigo = jsonObject.getString("CODIGO").toUpperCase()
                if (codigo == "OK") {
                    val idCaja = jsonObject.getInt("ID_CAJA")
                    val idTerm = jsonObject.getInt("ID_TERM")
                    val responsable = jsonObject.getString("RESPONSABLE")
                    preferenceHelper!!.putString(
                        ConstantsPreferences.PREF_ID_CAJA,
                        idCaja.toString()
                    )
                    view!!.onSuccess(Caja(idCaja, idTerm, responsable))
                } else {
                    view!!.onFailCajaResult(codigo)
                }
            } catch (e: JSONException) {
                e.printStackTrace()
                filesWeakReference!!.get()!!.createFileException(
                    "Controller/CajaRequest/cajaRequestResult " + jsonResult + " " +
                            Log.getStackTraceString(e), preferenceHelper!!
                )
                view!!.onExceptionCajaResult(e.message)
            }
        }
    }

    override fun executeCajaRequest() {
        cajaRequest()
    }
}
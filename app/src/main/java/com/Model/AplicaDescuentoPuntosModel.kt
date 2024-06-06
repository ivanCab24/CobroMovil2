package com.Model

import android.util.Log
import com.Constants.ConstantsPreferences
import com.Interfaces.AplicaDescuentoPuntosMVP
import com.Presenter.AplicaDescuentoPuntosPresenter
import com.Presenter.AplicaDescuentoPuntosPresenter.Companion.preferenceHelper
import com.Presenter.AplicaDescuentoPuntosPresenter.Companion.preferenceHelperLogs
import com.Presenter.AplicaDescuentoPuntosPresenter.Companion.view
import com.View.Activities.ActividadPrincipal
import com.View.Fragments.ContentFragment
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

class AplicaDescuentoPuntosModel : AplicaDescuentoPuntosMVP.Model, CoroutineScope {
    private val job = Job()

    object WebServiceTaskCaller {
        fun CallWSConsumptionTask(request: SoapObject, method: String) {
            val service: ReadJSONFeedTask = object : ReadJSONFeedTask() {
                override fun onResponseReceived(jsonResult: String) {
                    if (ToksWebServiceExceptionRate.rateError(jsonResult) != "") {
                        val exceptionRated = ToksWebServiceExceptionRate.rateError(jsonResult)
                        view!!.onExceptionAplicaDescuentoResult(exceptionRated)
                    } else {
                        aplicaDescunetoPuntosRequestResult(jsonResult)
                    }
                }
            }
            service.execute(request, method, preferenceHelper)
        }
    }

    companion object {
        private fun aplicaDescuentoPuntosRequest(puntosARedimir: String?) {
            val request = SoapObject(
                ToksWebServicesConnection.NAMESPACE,
                ToksWebServicesConnection.APLICA_DESCUENTO
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
            request.addProperty("Tipo", "999")
            request.addProperty("Id_Desc", "997")
            request.addProperty("Descripcion", "PUNTOS A COMER")
            request.addProperty("Porcentaje", puntosARedimir)
            request.addProperty(
                "Referencia",
                ContentFragment.miembro!!.response.membresia!!.numeroMembresia
            )
            request.addProperty(
                "Usuario",
                preferenceHelper!!.getString(ConstantsPreferences.PREF_ESTAFETA, null)
            )
            request.addProperty(
                "Empleado",
                preferenceHelper!!.getString(ConstantsPreferences.PREF_NOMBRE_EMPLEADO, null)
            )
            Log.d(AplicaDescuentoPuntosPresenter.TAG, "aplicaDescuentoPuntosRequest: $request")
            AplicaDescuentoPuntosPresenter.filesWeakReference!!.get()!!.registerLogs(
                "Inicia AplicaDescuentoPuntos",
                request.toString(),
                preferenceHelperLogs!!,
                preferenceHelper!!
            )
            WebServiceTaskCaller.CallWSConsumptionTask(
                request,
                ToksWebServicesConnection.APLICA_DESCUENTO
            )
        }

        private fun aplicaDescunetoPuntosRequestResult(jsonResult: String) {
            try {
                AplicaDescuentoPuntosPresenter.filesWeakReference!!.get()!!.registerLogs(
                    "Termina AplicaDescuentoPuntos",
                    jsonResult,
                    preferenceHelperLogs!!,
                    preferenceHelper!!
                )
                val jsonArray = JSONArray(jsonResult)
                val jsonObject = jsonArray.getJSONObject(0)
                val codigo = jsonObject.getString("CODIGO").toUpperCase()
                if (codigo == "OK") {
                    ActividadPrincipal.isDescuentoGRG = false
                    val respuesta = jsonObject.getString("RESPUESTA")
                    view!!.onSuccessAplicaDescuentoResult(respuesta)
                } else {
                    view!!.onFailAplicaDescuentoResult(codigo)
                }
            } catch (e: JSONException) {
                e.printStackTrace()
                AplicaDescuentoPuntosPresenter.filesWeakReference!!.get()!!.createFileException(
                    "Controller/AplicaDescuentoPuntosRequest/aplicaDescunetoPuntosRequestResult " + jsonResult + " "
                            + Log.getStackTraceString(e),
                    preferenceHelper!!
                )
                view!!.onExceptionAplicaDescuentoResult(e.message)
            }
        }

    }


    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    override fun executeAplicaDescuentoRequest(puntosARedimir: String?) {
        aplicaDescuentoPuntosRequest(puntosARedimir)
    }

    override fun aplicaDescunetoPuntosResult(jsonResult: String) {
        aplicaDescunetoPuntosRequestResult(jsonResult)
    }
}
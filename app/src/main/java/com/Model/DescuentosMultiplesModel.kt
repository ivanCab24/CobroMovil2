package com.Model

import android.util.Log
import com.Constants.ConstantsPreferences
import com.Interfaces.DescuentosMultiplesMVP
import com.Presenter.DescuentosMultiplesPresenter.Companion.TAG
import com.Presenter.DescuentosMultiplesPresenter.Companion.filesWeakReference
import com.Presenter.DescuentosMultiplesPresenter.Companion.preferenceHelper
import com.Presenter.DescuentosMultiplesPresenter.Companion.preferenceHelperLogs
import com.Presenter.DescuentosMultiplesPresenter.Companion.view
import com.webservicestasks.ReadJSONFeedTask
import com.webservicestasks.ToksWebServiceExceptionRate
import com.webservicestasks.ToksWebServicesConnection
import org.json.JSONArray
import org.json.JSONException
import org.ksoap2.serialization.SoapObject

class DescuentosMultiplesModel:DescuentosMultiplesMVP.Model {
    private object WebServiceTaskCaller {
        fun CallWSConsumptionTask(request: SoapObject, method: String) {
            val service: ReadJSONFeedTask = object : ReadJSONFeedTask() {
                override fun onResponseReceived(jsonResult: String) {
                    if (ToksWebServiceExceptionRate.rateError(jsonResult) != "") {
                        val exceptionRated = ToksWebServiceExceptionRate.rateError(jsonResult)
                        view!!.onExceptionMultiplesDescuentosResult(exceptionRated)
                    } else {
                        descuentosMultiplesRequestResult(jsonResult)
                    }
                }
            }
            service.execute(request, method, preferenceHelper)
        }
    }

    companion object{
        private fun descuentosMultiplesRequest() {
            val request = SoapObject(
                ToksWebServicesConnection.NAMESPACE,
                ToksWebServicesConnection.APLICA_DESCUENTOS
            )
            request.addProperty(
                "sJson",
                preferenceHelper!!.getString(ConstantsPreferences.PREF_JSON_MULTIPLES_DESCUENTO, null)
            )
            Log.d(TAG, "descuentosMultiplesRequest: $request")
            filesWeakReference!!.get()!!.registerLogs(
                "Inicia AplicaDescuentos",
                request.toString(),
                preferenceHelperLogs!!,
                preferenceHelper!!
            )
            WebServiceTaskCaller.CallWSConsumptionTask(
                request,
                ToksWebServicesConnection.APLICA_DESCUENTOS
            )
        }

        private fun descuentosMultiplesRequestResult(jsonResult: String) {
            try {
                filesWeakReference!!.get()!!.registerLogs(
                    "Termina AplicaDescuentos",
                    jsonResult,
                    preferenceHelperLogs!!,
                    preferenceHelper!!
                )
                val jsonArray = JSONArray(jsonResult)
                val jsonObject = jsonArray.getJSONObject(0)
                val codigo = jsonObject.getString("CODIGO").toUpperCase()
                if (codigo == "OK") {
                    val respuesta = jsonObject.getString("RESPUESTA")
                    if (respuesta == "Descuento Aplicado" || respuesta.contains("Descuento Aplicado a ")) {
                        view!!.onSuccessMultiplesDescuentosResult(respuesta)
                    } else {
                        view!!.onFailMultiplesDescuentosResult(respuesta)
                    }
                } else {
                    view!!.onFailMultiplesDescuentosResult(codigo)
                }
            } catch (e: JSONException) {
                e.printStackTrace()
                filesWeakReference!!.get()!!.createFileException(
                    "Controller/DescuentosMultiplesRequest/descuentosMultiplesRequestResult " + jsonResult +
                            " " + Log.getStackTraceString(e), preferenceHelper!!
                )
                view!!.onExceptionMultiplesDescuentosResult(e.message)
            }
        }
    }

    override fun executeDescuentosMultiplesRequest() {
        descuentosMultiplesRequest()
    }
}
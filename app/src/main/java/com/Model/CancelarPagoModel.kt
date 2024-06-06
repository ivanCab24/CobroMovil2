package com.Model

import android.util.Log
import com.Constants.ConstantsPreferences
import com.Interfaces.CancelarPagoMVP
import com.Presenter.CancelarPagoPresenter
import com.View.Fragments.ContentFragment
import com.webservicestasks.ReadJSONFeedTask
import com.webservicestasks.ToksWebServiceExceptionRate
import com.webservicestasks.ToksWebServicesConnection
import org.json.JSONArray
import org.json.JSONException
import org.ksoap2.serialization.SoapObject

class CancelarPagoModel:CancelarPagoMVP.Model {
    private object WebServiceTaskCaller {
        fun CallWSConsumptionTask(request: SoapObject, method: String) {
            val service: ReadJSONFeedTask = object : ReadJSONFeedTask() {
                override fun onResponseReceived(jsonResult: String) {
                    if (ToksWebServiceExceptionRate.rateError(jsonResult) != "") {
                        val exceptionRated = ToksWebServiceExceptionRate.rateError(jsonResult)
                        CancelarPagoPresenter.view!!.onExceptionCancelarPagoResult(exceptionRated)
                    } else {
                        cancelarPagoRequestResult(jsonResult)
                    }
                }
            }
            service.execute(request, method, CancelarPagoPresenter.preferenceHelper)
        }
    }
    companion object{
        private fun cancelarPagoRequest() {
            val request = SoapObject(
                ToksWebServicesConnection.NAMESPACE,
                ToksWebServicesConnection.CANCELA_PAGO
            )
            request.addProperty("Llave", ToksWebServicesConnection.KEY)
            request.addProperty("Cadena", ToksWebServicesConnection.STRING)
            request.addProperty(
                "Folio",
                CancelarPagoPresenter.preferenceHelper!!.getString(ConstantsPreferences.PREF_FOLIO, null)
            )
            request.addProperty(
                "Unidad",
                CancelarPagoPresenter.preferenceHelper!!.getString(ConstantsPreferences.PREF_UNIDAD, null)
            )
            request.addProperty("Abono_Parcial", "0")
            request.addProperty("Abono_Mn", "" + ContentFragment.pagos!!.abonoMn)
            request.addProperty("Abono_Propina", "" + ContentFragment.pagos!!.abonoPropina)
            request.addProperty("Referencia", "0")
            request.addProperty("Contador", ContentFragment.pagos!!.contador)
            Log.d(CancelarPagoPresenter.TAG, "cancelarPagoRequest: $request")
            CancelarPagoPresenter.filesWeakReference!!.get()!!.registerLogs(
                "Inicia CancelaPago",
                request.toString(),
                CancelarPagoPresenter.preferenceHelperLogs!!,
                CancelarPagoPresenter.preferenceHelper!!
            )
            WebServiceTaskCaller.CallWSConsumptionTask(
                request,
                ToksWebServicesConnection.CANCELA_PAGO
            )
        }

        private fun cancelarPagoRequestResult(jsonResult: String) {
            try {
                CancelarPagoPresenter.filesWeakReference!!.get()!!.registerLogs(
                    "Termina CancelaPago",
                    jsonResult,
                    CancelarPagoPresenter.preferenceHelperLogs!!,
                    CancelarPagoPresenter.preferenceHelper!!
                )
                val jsonArray = JSONArray(jsonResult)
                val codigo = jsonArray.getJSONObject(0).getString("CODIGO").toUpperCase()
                if (codigo == "OK") {
                    val respuesta = jsonArray.getJSONObject(0).getString("RESPUESTA")
                    if (respuesta == "Cargo eliminado") {
                        CancelarPagoPresenter.view!!.onSuccessCancelarPagoResult(respuesta)
                    } else {
                        CancelarPagoPresenter.view!!.onFailCancelarPagoResult(respuesta)
                    }
                } else {
                    CancelarPagoPresenter.view!!.onFailCancelarPagoResult(codigo)
                }
            } catch (e: JSONException) {
                e.printStackTrace()
                CancelarPagoPresenter.filesWeakReference!!.get()!!.createFileException(
                    "Controller/CancelarPagoRequest/cancelarPagoRequestResult " + jsonResult +
                            " " + Log.getStackTraceString(e), CancelarPagoPresenter.preferenceHelper!!
                )
                CancelarPagoPresenter.view!!.onExceptionCancelarPagoResult(e.message)
            }
        }
    }

    override fun executeCancelarPagoRequest() {
        cancelarPagoRequest()
    }
}
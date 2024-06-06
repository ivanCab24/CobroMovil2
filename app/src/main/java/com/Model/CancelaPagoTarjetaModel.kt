package com.Model

import android.util.Log
import com.Constants.ConstantsPreferences
import com.Interfaces.CancelaPagoTarjetaMVP
import com.Presenter.CancelaPagoTarjetaPresenter
import com.Utilities.Utils
import com.View.Fragments.ContentFragment
import com.webservicestasks.ReadJSONFeedTask
import com.webservicestasks.ToksWebServiceExceptionRate
import com.webservicestasks.ToksWebServicesConnection
import org.json.JSONArray
import org.json.JSONException
import org.ksoap2.serialization.SoapObject

class CancelaPagoTarjetaModel:CancelaPagoTarjetaMVP.Model {
    private object WebServiceTaskCaller {
        fun CallWSConsumptionTask(request: SoapObject, method: String) {
            val service: ReadJSONFeedTask = object : ReadJSONFeedTask() {
                override fun onResponseReceived(jsonResult: String) {
                    if (ToksWebServiceExceptionRate.rateError(jsonResult) != "") {
                        val exceptionResult = ToksWebServiceExceptionRate.rateError(jsonResult)
                        CancelaPagoTarjetaPresenter.view!!.onCancelaPagoTarjetaExceptionResult(exceptionResult)
                    } else {
                        cancelaPagoRequestResult(jsonResult)
                    }
                }
            }
            service.execute(request, method, CancelaPagoTarjetaPresenter.preferenceHelper)
        }
    }

    companion object{
        private fun cancelaPagoRequest() {
            val request = SoapObject(
                ToksWebServicesConnection.NAMESPACE,
                ToksWebServicesConnection.CANCELA_PAGO
            )
            request.addProperty("Llave", ToksWebServicesConnection.KEY)
            request.addProperty("Cadena", ToksWebServicesConnection.STRING)
            request.addProperty(
                "Folio",
                CancelaPagoTarjetaPresenter.preferenceHelper!!.getString(ConstantsPreferences.PREF_FOLIO, null)
            )
            request.addProperty(
                "Unidad",
                CancelaPagoTarjetaPresenter.preferenceHelper!!.getString(ConstantsPreferences.PREF_UNIDAD, null)
            )
            request.addProperty("Abono_Parcial", "0")
            request.addProperty("Abono_Mn", "" + ContentFragment.pagos!!.abonoMn.toString())
            request.addProperty(
                "Abono_Propina",
                "" + Utils.round(ContentFragment.pagos!!.abonoPropina, 2).toString()
            )
            request.addProperty("Referencia", "0")
            request.addProperty("Contador", ContentFragment.pagos!!.contador.toString())
            Log.d(CancelaPagoTarjetaPresenter.TAG, "cancelaPagoRequest: $request")
            CancelaPagoTarjetaPresenter.filesWeakReference!!.get()!!.registerLogs(
                "Inicia CancelaPagoTarjeta",
                request.toString(),
                CancelaPagoTarjetaPresenter.preferenceHelperLogs!!,
                CancelaPagoTarjetaPresenter.preferenceHelper!!
            )
            WebServiceTaskCaller.CallWSConsumptionTask(
                request,
                ToksWebServicesConnection.Cancela_Pago
            )
        }

        private fun cancelaPagoRequestResult(jsonResult: String) {
            try {
                CancelaPagoTarjetaPresenter.filesWeakReference!!.get()!!.registerLogs(
                    "Termina CancelaPagoTarjeta",
                    jsonResult,
                    CancelaPagoTarjetaPresenter.preferenceHelperLogs!!,
                    CancelaPagoTarjetaPresenter.preferenceHelper!!
                )
                val jsonArray = JSONArray(jsonResult)
                val codigo = jsonArray.getJSONObject(0).getString("CODIGO").toUpperCase()
                val respuesta = jsonArray.getJSONObject(0).getString("RESPUESTA")
                if (codigo == "OK") {
                    if (respuesta == "Cargo eliminado") {
                        ContentFragment.contentFragment!!.cuenta
                        CancelaPagoTarjetaPresenter.preferenceHelper!!.putString(
                            ConstantsPreferences.PREF_FOLIO_BOUCHER,
                            CancelaPagoTarjetaPresenter.preferenceHelper!!.getString(
                                    ConstantsPreferences.PREF_FOLIO,
                                    null)
                        )
                        CancelaPagoTarjetaPresenter.view!!.onCancelaPagoTarjetaSuccessResult(respuesta)
                    } else {
                        CancelaPagoTarjetaPresenter.view!!.onCancelaPagoTarjetaFailResult(respuesta)
                    }
                } else {
                    CancelaPagoTarjetaPresenter.view!!.onCancelaPagoTarjetaFailResult(codigo)
                }
            } catch (e: JSONException) {
                e.printStackTrace()
                CancelaPagoTarjetaPresenter.filesWeakReference!!.get()!!.createFileException(
                    "Controller/CancelaPagoTarjetaRequest/cancelaPagoRequestResult " + jsonResult +
                            " " + Log.getStackTraceString(e), CancelaPagoTarjetaPresenter.preferenceHelper!!
                )
                CancelaPagoTarjetaPresenter.view!!.onCancelaPagoTarjetaExceptionResult(e.message)
            }
        }
    }

    override fun executeCancelaPagoRequest() {
        cancelaPagoRequest()
    }

}
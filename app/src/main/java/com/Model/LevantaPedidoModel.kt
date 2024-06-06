package com.Model

import android.util.Log
import com.Constants.ConstantsPreferences
import com.Interfaces.LevantaPedidoMVP
import com.Presenter.LevantaPedidoPresenter
import com.Presenter.LevantaPedidoPresenter.Companion.filesWeakReference
import com.Presenter.LevantaPedidoPresenter.Companion.preferenceHelper
import com.Presenter.LevantaPedidoPresenter.Companion.preferenceHelperLogs
import com.Presenter.LevantaPedidoPresenter.Companion.view
import com.View.Fragments.ContentFragment
import com.webservicestasks.ReadJSONFeedTask
import com.webservicestasks.ToksWebServiceExceptionRate
import com.webservicestasks.ToksWebServicesConnection
import org.json.JSONArray
import org.json.JSONException
import org.ksoap2.serialization.SoapObject

class LevantaPedidoModel:LevantaPedidoMVP.Model {
    private object WebServiceTaskCaller {
        fun CallWSConsumptionTask(request: SoapObject, method: String) {
            val service: ReadJSONFeedTask = object : ReadJSONFeedTask() {
                override fun onResponseReceived(jsonResult: String) {
                    filesWeakReference!!.get()!!.registerLogs(
                        "Termina LevantaPedido",
                        jsonResult,
                        preferenceHelperLogs!!,preferenceHelper!!
                    )
                    if (ToksWebServiceExceptionRate.rateError(jsonResult) != "") {
                        val exceptionResult = ToksWebServiceExceptionRate.rateError(jsonResult)
                        view!!.onLevantaPedidoExceptionResult(exceptionResult)
                    } else {
                        levantaPedidoRequestResult(jsonResult)
                    }
                }
            }
            service.execute(request, method, preferenceHelper)
        }
    }

    companion object{
        private fun levantaPedidoRequest() {
            val request = SoapObject(
                ToksWebServicesConnection.NAMESPACE,
                ToksWebServicesConnection.LEVANTA_PEDIDO_MP
            )
            request.addProperty("Llave", ToksWebServicesConnection.KEY)
            request.addProperty(
                "Cajamp",
                preferenceHelper!!.getString(ConstantsPreferences.PREF_CAJA_MP, null)
            )
            request.addProperty(
                "Comanda",
                preferenceHelper!!.getString(ConstantsPreferences.PREF_FOLIO, null)
            )
            request.addProperty("Importe", java.lang.String.valueOf(ContentFragment.montoCobro+ContentFragment.tipAmount))
            Log.d(LevantaPedidoPresenter.TAG, "levantaPedidoRequest: $request")
            filesWeakReference!!.get()!!.registerLogs(
                "Inicia LevantaPedido",
                request.toString(),
                preferenceHelperLogs!!,
                preferenceHelper!!
            )
            WebServiceTaskCaller.CallWSConsumptionTask(
                request,
                ToksWebServicesConnection.LEVANTA_PEDIDO_MP
            )
        }

        private fun levantaPedidoRequestResult(jsonResult: String) {
            try {
                if (jsonResult.contains("id")) {
                    Log.i("pppppppp", jsonResult)
                    val jsonArrayMain = JSONArray("[$jsonResult]")
                    val jsonObjectMain = jsonArrayMain.getJSONObject(0)
                    val id = jsonObjectMain.getString("id")
                    val clientID = jsonObjectMain.getString("client_id")
                    if (!id.isEmpty()) {
                        view!!.onSuccessLevantaPedidoResult(id, clientID)
                    } else {
                        view!!.onLevantaPedidoFailResult("No se encontro el id de orden")
                    }
                } else {
                    val jsonArrayError = JSONArray("[$jsonResult]")
                    val jsonObjectError = jsonArrayError.getJSONObject(0)
                    val error = jsonObjectError.getString("error")
                    val message = jsonObjectError.getString("message")
                    val status = jsonObjectError.getString("status")
                    view!!.onLevantaPedidoFailResult("Error $error Message $message Status $status")
                }
            } catch (exception: JSONException) {
                exception.printStackTrace()
                filesWeakReference!!.get()!!.createFileException(
                    "Controller/LevantaPedidoRequest/ " + Log.getStackTraceString(exception),
                    preferenceHelper!!
                )
                view!!.onLevantaPedidoExceptionResult(exception.message)
            }
        }
    }

    override fun executeLevantaPedidoRequest() {
        levantaPedidoRequest()
    }
}
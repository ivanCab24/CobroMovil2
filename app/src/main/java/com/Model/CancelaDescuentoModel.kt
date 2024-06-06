package com.Model

import android.util.Log
import com.Constants.ConstantsPreferences
import com.DataModel.DescuentosAplicados
import com.Interfaces.CancelaDescuentoMVP
import com.Presenter.CancelaDescuentoPresenter
import com.webservicestasks.ReadJSONFeedTask
import com.webservicestasks.ToksWebServiceExceptionRate
import com.webservicestasks.ToksWebServicesConnection
import org.json.JSONArray
import org.json.JSONException
import org.ksoap2.serialization.SoapObject

class CancelaDescuentoModel:CancelaDescuentoMVP.Model {
    private object WebServiceTaskCaller {
        fun CallWSConsumptionTask(request: SoapObject, method: String) {
            val service: ReadJSONFeedTask = object : ReadJSONFeedTask() {
                override fun onResponseReceived(jsonResult: String) {
                    if (ToksWebServiceExceptionRate.rateError(jsonResult) != "") {
                        val exceptionRated = ToksWebServiceExceptionRate.rateError(jsonResult)
                        CancelaDescuentoPresenter.view!!.onExceptionCancelaDescuentoResult(exceptionRated)
                    } else {
                        cancelaDescuentoRequestResult(jsonResult)
                    }
                }
            }
            service.execute(request, method, CancelaDescuentoPresenter.preferenceHelper)
        }
    }
    companion object{
        private fun cancelaDescuentoRequest(descuentosAplicados: DescuentosAplicados?) {
            val request = SoapObject(
                ToksWebServicesConnection.NAMESPACE,
                ToksWebServicesConnection.CANCELA_DESCUENTO
            )
            request.addProperty("Llave", ToksWebServicesConnection.KEY)
            request.addProperty("Cadena", ToksWebServicesConnection.STRING)
            request.addProperty("Folio", CancelaDescuentoPresenter.preferenceHelper!!.getString(
                    ConstantsPreferences.PREF_FOLIO,
                    null))
            request.addProperty("Unidad", descuentosAplicados!!.iD_LOCAL)
            request.addProperty("Tipo", descuentosAplicados.tipO_DESC)
            request.addProperty("Id_Desc", descuentosAplicados.iD_DESC)
            request.addProperty("id_pos", descuentosAplicados.iD_POS)
            request.addProperty("NumDscto", descuentosAplicados.contador)
            Log.d(CancelaDescuentoPresenter.TAG, "cancelaDescuentoRequest: $request")
            CancelaDescuentoPresenter.filesWeakReference!!.get()!!.registerLogs(
                "Inicia CancelaDescuento",
                request.toString(),
                CancelaDescuentoPresenter.preferenceHelperLogs!!,
                CancelaDescuentoPresenter.preferenceHelper!!
            )
            WebServiceTaskCaller.CallWSConsumptionTask(
                request,
                ToksWebServicesConnection.CANCELA_DESCUENTO
            )
        }

        private fun cancelaDescuentoRequestResult(jsonResult: String) {
            try {
                CancelaDescuentoPresenter.filesWeakReference!!.get()!!.registerLogs(
                    "Termina CancelaDescuento",
                    jsonResult,
                    CancelaDescuentoPresenter.preferenceHelperLogs!!,
                    CancelaDescuentoPresenter.preferenceHelper!!
                )
                val jsonArray = JSONArray(jsonResult)
                val codigo = jsonArray.getJSONObject(0).getString("CODIGO").toUpperCase()
                if (codigo == "OK") {
                    val respuesta = jsonArray.getJSONObject(0).getString("RESPUESTA")
                    if (respuesta == "Descuento Cancelado") {
                        CancelaDescuentoPresenter.view!!.onSuccessCancelaDescuentoResult(respuesta)
                    } else {
                        CancelaDescuentoPresenter.view!!.onFailCancelaDescuentoResult(respuesta)
                    }
                } else {
                    CancelaDescuentoPresenter.view!!.onFailCancelaDescuentoResult(codigo)
                }
            } catch (e: JSONException) {
                e.printStackTrace()
                CancelaDescuentoPresenter.filesWeakReference!!.get()!!.createFileException(
                    "Controller/CancelaDescuentoRequest/cancelaDescuentoRequestResult " + jsonResult +
                            " " + Log.getStackTraceString(e), CancelaDescuentoPresenter.preferenceHelper!!
                )
                CancelaDescuentoPresenter.view!!.onExceptionCancelaDescuentoResult(e.message)
            }
        }

    }

    override fun executeCancelaDescuentoRequest(descuentosAplicados: DescuentosAplicados?) {
        cancelaDescuentoRequest(descuentosAplicados)
    }
}
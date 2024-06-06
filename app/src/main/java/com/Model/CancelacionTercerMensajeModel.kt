package com.Model

import android.util.Log
import com.Constants.ConstantsPreferences
import com.DataModel.VtolCancelResponse
import com.Interfaces.CancelacionTercerMensajeMVP
import com.Presenter.CancelacionTercerMensajePresenter
import com.webservicestasks.ReadJSONFeedTask
import com.webservicestasks.ToksWebServiceExceptionRate
import com.webservicestasks.ToksWebServicesConnection
import org.json.JSONArray
import org.json.JSONException
import org.ksoap2.serialization.SoapObject

class CancelacionTercerMensajeModel:CancelacionTercerMensajeMVP.Model {
    private object WebServiceTaskCaller {
        fun CallWSConsumptionTask(request: SoapObject, method: String) {
            val service: ReadJSONFeedTask = object : ReadJSONFeedTask() {
                override fun onResponseReceived(jsonResult: String) {
                    if (ToksWebServiceExceptionRate.rateError(jsonResult) != "") {
                        val exceptionResult = ToksWebServiceExceptionRate.rateError(jsonResult)
                        CancelacionTercerMensajePresenter.view!!.onTercerMensajeCancelacionExceptionResult(exceptionResult)
                    } else {
                        tercerMensajeCancelacionRequestResult(
                            jsonResult
                        )
                    }
                }
            }
            service.execute(request, method, CancelacionTercerMensajePresenter.preferenceHelper)
        }
    }

    companion object{
        private fun tercerMensajeCancelacionRequest(vtolCancelResponse: VtolCancelResponse?) {
            val request = SoapObject(
                ToksWebServicesConnection.NAMESPACE,
                ToksWebServicesConnection.Tercermensaje_vitol
            )
            request.addProperty("Llave", ToksWebServicesConnection.KEY)
            request.addProperty("Cadena", ToksWebServicesConnection.STRING)
            request.addProperty(
                "Unidad",
                CancelacionTercerMensajePresenter.preferenceHelper!!.getString(ConstantsPreferences.PREF_UNIDAD, null)
            )
            request.addProperty("id_trans", vtolCancelResponse!!.campo24)
            request.addProperty("Accion", "Commit")
            request.addProperty("Extradata", "")
            Log.d(CancelacionTercerMensajePresenter.TAG, "tercerMensajeCancelacionRequest: $request")
            CancelacionTercerMensajePresenter.filesWeakReference!!.get()!!.registerLogs(
                "Inicia TercerMensajeCancelacion",
                request.toString(),
                CancelacionTercerMensajePresenter.preferenceHelperLogs!!,
                CancelacionTercerMensajePresenter.preferenceHelper!!
            )
            WebServiceTaskCaller.CallWSConsumptionTask(
                request,
                ToksWebServicesConnection.Tercermensaje_vitol
            )
        }

        private fun tercerMensajeCancelacionRequestResult(jsonResult: String) {
            try {
                CancelacionTercerMensajePresenter.filesWeakReference!!.get()!!.registerLogs(
                    "Termina TercerMensajeCancelacion",
                    jsonResult,
                    CancelacionTercerMensajePresenter.preferenceHelperLogs!!,
                    CancelacionTercerMensajePresenter.preferenceHelper!!
                )
                val jsonArray = JSONArray(jsonResult)
                val Codigo = jsonArray.getJSONObject(0).getString("CODIGO").toUpperCase()
                val mensaje = jsonArray.getJSONObject(0).getString("MENSAJE").toUpperCase()
                if (mensaje == "OK") {
                    if (Codigo == "OK") {
                        CancelacionTercerMensajePresenter.view!!.onSuccessTercerMensajeCancelacionResult()
                    } else {
                        CancelacionTercerMensajePresenter.view!!.onTercerMensajeCancelacionFailResult(Codigo)
                    }
                } else {
                    CancelacionTercerMensajePresenter.view!!.onTercerMensajeCancelacionFailResult(mensaje)
                }
            } catch (e: JSONException) {
                e.printStackTrace()
                CancelacionTercerMensajePresenter.filesWeakReference!!.get()!!.createFileException(
                    "Controller/CancelacionTercerMensajeRequest/tercerMensajeCancelacionRequestResult " + jsonResult +
                            " " + Log.getStackTraceString(e), CancelacionTercerMensajePresenter.preferenceHelper!!
                )
                CancelacionTercerMensajePresenter.view!!.onTercerMensajeCancelacionExceptionResult(e.message)
            }
        }
    }

    override fun executeCancelacionTercerMensajeRequest(vtolCancelResponse: VtolCancelResponse?) {
        tercerMensajeCancelacionRequest(vtolCancelResponse)
    }
}
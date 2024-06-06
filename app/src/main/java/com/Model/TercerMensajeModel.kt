package com.Model

import android.util.Log
import com.Constants.ConstantsPreferences
import com.DataModel.SaleVtol
import com.Interfaces.TercerMensajeMVP
import com.Presenter.TercerMensajePresenter
import com.Verifone.EMVResponse
import com.Verifone.VerifoneTaskManager
import com.webservicestasks.ReadJSONFeedTask
import com.webservicestasks.ToksWebServiceExceptionRate
import com.webservicestasks.ToksWebServicesConnection
import org.json.JSONArray
import org.json.JSONException
import org.ksoap2.serialization.SoapObject

class TercerMensajeModel:TercerMensajeMVP.Model {
    private object WebServiceTaskCaller {
        fun CallWSConsumptionTask(request: SoapObject, method: String) {
            val service: ReadJSONFeedTask = object : ReadJSONFeedTask() {
                override fun onResponseReceived(jsonResult: String) {
                    if (ToksWebServiceExceptionRate.rateError(jsonResult) != "") {
                        val exceptionRated = ToksWebServiceExceptionRate.rateError(jsonResult)
                        TercerMensajePresenter.view!!.onTercerMensajeExceptionResult(exceptionRated)
                    } else {
                        tercerMensajeRequestResult(jsonResult)
                    }
                }
            }
            service.execute(request, method, TercerMensajePresenter.preferenceHelper)
        }
    }

    companion object{
        private fun tercerMensajeRequest(
            accion: String?,
            response: EMVResponse?,
            saleVtol: SaleVtol?
        ) {
            val request = SoapObject(
                ToksWebServicesConnection.NAMESPACE,
                ToksWebServicesConnection.Tercermensaje_vitol
            )
            request.addProperty("Llave", ToksWebServicesConnection.KEY)
            request.addProperty("Cadena", ToksWebServicesConnection.STRING)
            request.addProperty(
                "Unidad",
                TercerMensajePresenter.preferenceHelper!!.getString(ConstantsPreferences.PREF_UNIDAD, null)
            )
            request.addProperty("id_trans", saleVtol!!.campo24)
            request.addProperty("Accion", accion)
            request.addProperty("Extradata", if (accion == "Rollback") response!!.paramE2 else "")
            request.addProperty(
                "id_term",
                TercerMensajePresenter.preferenceHelper!!.getString(ConstantsPreferences.PREF_NUMERO_TERMINAL, null)
            )
            TercerMensajePresenter.accion = accion
            Log.d(TercerMensajePresenter.TAG, "tercerMensajeRequest: $request")
            TercerMensajePresenter.filesWeakReference!!.get()!!.registerLogs(
                "Inicia TercerMensaje",
                request.toString(),
                TercerMensajePresenter.preferenceHelperLogs!!,
                TercerMensajePresenter.preferenceHelper!!
            )
            WebServiceTaskCaller.CallWSConsumptionTask(
                request,
                ToksWebServicesConnection.Tercermensaje_vitol
            )
        }

        private fun tercerMensajeRequestResult(jsonResult: String) {
            try {
                TercerMensajePresenter.filesWeakReference!!.get()!!.registerLogs(
                    "Termina TercerMensaje",
                    jsonResult,
                    TercerMensajePresenter.preferenceHelperLogs!!,
                    TercerMensajePresenter.preferenceHelper!!
                )
                val jsonArray = JSONArray(jsonResult)
                val Codigo = jsonArray.getJSONObject(0).getString("CODIGO").toUpperCase()
                val mensaje = jsonArray.getJSONObject(0).getString("MENSAJE").toUpperCase()
                if (mensaje == "OK") {
                    if (Codigo == "OK") {
                        if (TercerMensajePresenter.getTaf9f27 != "00") {
                            if (TercerMensajePresenter.transaccionExitosa) TercerMensajePresenter.view!!.onTercerMensajeSuccessResult(
                                TercerMensajePresenter.accion
                            )
                        } else {
                            TercerMensajePresenter.view!!.onTercerMensajeDeclinadaEMV(
                                TercerMensajePresenter.accion
                            )
                        }
                    } else {
                        TercerMensajePresenter.view!!.onTercerMensajeFailResult(Codigo)
                    }
                } else {
                    TercerMensajePresenter.view!!.onTercerMensajeFailResult(mensaje)
                }
                VerifoneTaskManager.limpiarTerminal()
            } catch (e: JSONException) {
                e.printStackTrace()
                TercerMensajePresenter.filesWeakReference!!.get()!!.createFileException(
                    "Controller/TercerMensajeRequest/tercerMensajeRequestResult " + jsonResult +
                            " " + Log.getStackTraceString(e), TercerMensajePresenter.preferenceHelper!!
                )
                TercerMensajePresenter.view!!.onTercerMensajeExceptionResult(e.message)
            }
        }
    }

    override fun executeTercerMensajeRequest(accion: String?,
                                             response: EMVResponse?,
                                             saleVtol: SaleVtol?) {
        tercerMensajeRequest(accion,response,saleVtol)

    }
}
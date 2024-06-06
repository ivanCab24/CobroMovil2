package com.Model

import android.util.Log
import com.Constants.ConstantsPreferences
import com.Interfaces.AplicaDescuentoMVP
import com.Presenter.AplicaDescuentoPresenter
import com.View.Fragments.ContentFragment
import com.webservicestasks.ReadJSONFeedTask
import com.webservicestasks.ToksWebServiceExceptionRate
import com.webservicestasks.ToksWebServicesConnection
import org.json.JSONArray
import org.json.JSONException
import org.ksoap2.serialization.SoapObject

class AplicaDescuentoModel:AplicaDescuentoMVP.Model {
    private object WebServiceTaskCaller {
        fun CallWSConsumptionTask(request: SoapObject, method: String) {
            val service: ReadJSONFeedTask = object : ReadJSONFeedTask() {
                override fun onResponseReceived(jsonResult: String) {
                    ContentFragment.isDescuentoRedimido=false
                    if (ToksWebServiceExceptionRate.rateError(jsonResult) != "") {
                        val exceptionRated = ToksWebServiceExceptionRate.rateError(jsonResult)
                        AplicaDescuentoPresenter.view!!.onExceptionAplicaDescuentoResult(exceptionRated)
                    } else {
                        aplicaDescuentoRequestResult(jsonResult)
                    }
                }
            }
            service.execute(request, method, AplicaDescuentoPresenter.preferenceHelper)
        }
    }

    companion object{
        private fun aplicaDescuentoRequest(referencia: String?) {
            val request = SoapObject(
                ToksWebServicesConnection.NAMESPACE,
                ToksWebServicesConnection.APLICA_DESCUENTO
            )
            request.addProperty("Llave", ToksWebServicesConnection.KEY)
            request.addProperty("Cadena", ToksWebServicesConnection.STRING)
            request.addProperty(
                "Folio",
                AplicaDescuentoPresenter.preferenceHelper!!.getString(ConstantsPreferences.PREF_FOLIO, null)
            )
            request.addProperty(
                "Unidad",
                AplicaDescuentoPresenter.preferenceHelper!!.getString(ConstantsPreferences.PREF_UNIDAD, null)
            )
            request.addProperty("Tipo", "" + ContentFragment.getDescuento!!.tipo)
            request.addProperty("Id_Desc", ContentFragment.getDescuento!!.idDesc)
            request.addProperty("Descripcion", ContentFragment.getDescuento!!.descripcion)
            request.addProperty(
                "Porcentaje",
                ContentFragment.getDescuento!!.porcentaje.toString() + ""
            )
            request.addProperty("Referencia", referencia)
            if(ContentFragment.getDescuento!!.idDesc==996)
                request.addProperty(
                    "Usuario",
                    ContentFragment.getDescuento!!.descuento
                )
            else
            request.addProperty(
                "Usuario",
                AplicaDescuentoPresenter.preferenceHelper!!.getString(ConstantsPreferences.PREF_ESTAFETA, null)
            )
            request.addProperty(
                "Empleado",
                AplicaDescuentoPresenter.preferenceHelper!!.getString(ConstantsPreferences.PREF_NOMBRE_EMPLEADO, null)
            )
            Log.d(AplicaDescuentoPresenter.TAG, "aplicaDescuentoRequest: $request")
            AplicaDescuentoPresenter.filesWeakReference!!.get()!!.registerLogs(
                "Inicia AplicaDescuento",
                request.toString(),
                AplicaDescuentoPresenter.preferenceHelperLogs!!,
                AplicaDescuentoPresenter.preferenceHelper!!
            )
            WebServiceTaskCaller.CallWSConsumptionTask(
                request,
                ToksWebServicesConnection.APLICA_DESCUENTO
            )
        }

        private fun aplicaDescuentoRequestResult(jsonResult: String) {
            try {
                AplicaDescuentoPresenter.filesWeakReference!!.get()!!.registerLogs(
                    "Termina AplicaDescuento",
                    jsonResult,
                    AplicaDescuentoPresenter.preferenceHelperLogs!!,
                    AplicaDescuentoPresenter.preferenceHelper!!
                )
                val jsonArray = JSONArray(jsonResult)
                val jsonObject = jsonArray.getJSONObject(0)
                val codigo = jsonObject.getString("CODIGO").toUpperCase()
                if (codigo == "OK") {
                    val respuesta = jsonObject.getString("RESPUESTA")
                    AplicaDescuentoPresenter.view!!.onSuccess(respuesta)
                } else {
                    AplicaDescuentoPresenter.view!!.onFailAplicaDescuentoResult(codigo)
                }
            } catch (e: JSONException) {
                e.printStackTrace()
                AplicaDescuentoPresenter.filesWeakReference!!.get()!!.createFileException(
                    "Controller/AplicaDescuentoRequest/aplicaDescuentoRequestResult " + jsonResult + " "
                            + Log.getStackTraceString(e), AplicaDescuentoPresenter.preferenceHelper!!
                )
                AplicaDescuentoPresenter.view!!.onExceptionAplicaDescuentoResult(e.message)
            }
        }
    }

    override fun executeAplicaDescuento(referencia: String?) {
        aplicaDescuentoRequest(referencia)
    }
}
package com.Model

import android.util.Log
import com.DataModel.Descuentos
import com.Interfaces.DescuentoFavoritoMVP
import com.Presenter.DescuentoFavoritoPresenter.Companion.TAG
import com.Presenter.DescuentoFavoritoPresenter.Companion.filesWeakReference
import com.Presenter.DescuentoFavoritoPresenter.Companion.preferenceHelper
import com.Presenter.DescuentoFavoritoPresenter.Companion.preferenceHelperLogs
import com.Presenter.DescuentoFavoritoPresenter.Companion.view
import com.webservicestasks.ReadJSONFeedTask
import com.webservicestasks.ToksWebServiceExceptionRate
import com.webservicestasks.ToksWebServicesConnection
import org.json.JSONArray
import org.json.JSONException
import org.ksoap2.serialization.SoapObject

class DescuentoFavoritoModel:DescuentoFavoritoMVP.Model {
    private object WebServiceTaskCaller {
        fun CallWSConsumptionTask(request: SoapObject, method: String) {
            val service: ReadJSONFeedTask = object : ReadJSONFeedTask() {
                override fun onResponseReceived(jsonResult: String) {
                    if (ToksWebServiceExceptionRate.rateError(jsonResult) != "") {
                        val exceptionRated = ToksWebServiceExceptionRate.rateError(jsonResult)
                        view!!.onDescuentoFavoritoExceptionResult(exceptionRated)
                    } else {
                        descuentoFavoritoRequestResult(jsonResult)
                    }
                }
            }
            service.execute(request, method, preferenceHelper)
        }
    }

    companion object{
        private fun descuentoFavoritoRequest(descuentos: Descuentos?) {
            val request = SoapObject(
                ToksWebServicesConnection.NAMESPACE,
                ToksWebServicesConnection.DESCUENTO_FAVORITO
            )
            request.addProperty("Llave", ToksWebServicesConnection.KEY)
            request.addProperty("Cadena", ToksWebServicesConnection.STRING)
            request.addProperty("Tipo", descuentos!!.tipo)
            request.addProperty("Id_Desc", descuentos.idDesc)
            request.addProperty("Bandera", if (descuentos.favorito == 1) 0 else 1)
            Log.d(TAG, "descuentoFavoritoRequest: $request")
            filesWeakReference!!.get()!!.registerLogs(
                "Inicia DescuentoFavorito",
                request.toString(),
                preferenceHelperLogs!!,
                preferenceHelper!!
            )
            WebServiceTaskCaller.CallWSConsumptionTask(
                request,
                ToksWebServicesConnection.DESCUENTO_FAVORITO
            )
        }

        private fun descuentoFavoritoRequestResult(jsonResult: String) {
            try {
                filesWeakReference!!.get()!!.registerLogs(
                    "Termina DescuentoFavorito",
                    jsonResult,
                    preferenceHelperLogs!!,
                    preferenceHelper!!
                )
                val jsonArray = JSONArray(jsonResult)
                val codigo = jsonArray.getJSONObject(0).getString("CODIGO").toUpperCase()
                if (codigo == "OK") view!!.onDescuentoFavoritoSuccess() else view!!.onDescuentoFavoritoFailResult(
                    codigo
                )
            } catch (e: JSONException) {
                e.printStackTrace()
                filesWeakReference!!.get()!!.createFileException(
                    "Controller/DescuentoFavoritoRequest/descuentoFavoritoRequestResult " + jsonResult + " " + Log.getStackTraceString(
                        e
                    ), preferenceHelper!!
                )
                view!!.onDescuentoFavoritoExceptionResult(e.message)
            }
        }
    }

    override fun executeDescuentoFavoritoRequest(descuentos: Descuentos?) {
        descuentoFavoritoRequest(descuentos)
    }
}
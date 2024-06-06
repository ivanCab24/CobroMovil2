package com.Model

import android.annotation.SuppressLint
import android.util.Log
import com.Constants.ConstantsPreferences
import com.DataModel.FormasDePago
import com.Interfaces.FormasDePagoMPV
import com.Presenter.FormasDePagoPresenter
import com.Presenter.FormasDePagoPresenter.Companion.TAG
import com.Presenter.FormasDePagoPresenter.Companion.filesWeakReference
import com.Presenter.FormasDePagoPresenter.Companion.formasDePagoArrayList
import com.Presenter.FormasDePagoPresenter.Companion.preferenceHelperLogs
import com.Presenter.FormasDePagoPresenter.Companion.preferencesHelper
import com.Presenter.FormasDePagoPresenter.Companion.view
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

class FormasDePagoModel : FormasDePagoMPV.Model, CoroutineScope {
    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    private object WebServiceTaskCaller {
        fun CallWSConsumptionTask(request: SoapObject, method: String) {
            val service: ReadJSONFeedTask = object : ReadJSONFeedTask() {
                @SuppressLint("StaticFieldLeak")
                override fun onResponseReceived(jsonResult: String) {
                    if (ToksWebServiceExceptionRate.rateError(jsonResult) != "") {
                        val exceptionRated = ToksWebServiceExceptionRate.rateError(jsonResult)
                        view!!.onExceptionFormasDePagoResult(exceptionRated)
                    } else {
                        formasDePagoRequestResult(jsonResult)
                    }
                }
            }
            service.execute(request, method, preferencesHelper)
        }
    }

    companion object {
        private fun formasDePagoRequest() {
            val request = SoapObject(
                ToksWebServicesConnection.NAMESPACE,
                ToksWebServicesConnection.CATALOGO_FORMAS_PAGO
            )
            request.addProperty("Llave", ToksWebServicesConnection.KEY)
            request.addProperty("Cadena", ToksWebServicesConnection.STRING)
            request.addProperty(
                "Unidad",
                preferencesHelper!!.getString(ConstantsPreferences.PREF_UNIDAD, null)
            )
            Log.d(TAG, "formasDePagoRequest: $request")
            filesWeakReference!!.get()!!.registerLogs(
                "Inicia CatalogoFormasPago",
                request.toString(),
                preferenceHelperLogs!!,
                preferencesHelper!!
            )
            WebServiceTaskCaller.CallWSConsumptionTask(
                request,
                ToksWebServicesConnection.CATALOGO_FORMAS_PAGO
            )
        }

        private fun formasDePagoRequestResult(jsonResult: String) {
            try {
                filesWeakReference!!.get()!!.registerLogs(
                    "Termina CatalogoFormasPago",
                    jsonResult,
                    preferenceHelperLogs!!,
                    preferencesHelper!!
                )
                val jsonArray = JSONArray(jsonResult)
                val codigo = jsonArray.getJSONObject(0).getString("CODIGO")
                formasDePagoArrayList.clear()
                if (codigo == "0") {
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val tipoFpgo = jsonObject.getInt("TIPO_FPGO")
                        val idFpgo = jsonObject.getInt("ID_FPGO")
                        val formaPago = jsonObject.getString("FORMA_PAGO")
                        val tipoCambio = jsonObject.getInt("TIPO_CAMBIO")
                        val comision = jsonObject.getString("COMISION")
                        val tipoComision = jsonObject.getString("TIPO_COMISION")
                        val propina = jsonObject.getString("FPROPINA")
                        formasDePagoArrayList.add(
                            FormasDePago(
                                tipoFpgo, idFpgo, formaPago, tipoCambio, comision,
                                tipoComision, propina
                            )
                        )
                    }
                    view!!.onSuccessFormasDePagoResult(formasDePagoArrayList)
                } else {
                    view!!.onFailFormasDePagpResult(codigo)
                }
            } catch (e: JSONException) {
                e.printStackTrace()
                filesWeakReference!!.get()!!.createFileException(
                    "Controller/FormasDePagoRequest/formasDePagoRequestResult " + jsonResult +
                            " " + Log.getStackTraceString(e),
                    FormasDePagoPresenter.preferencesHelper!!
                )
                view!!.onExceptionFormasDePagoResult(e.message)
            }
        }
    }

    override fun executeFormasPagoRequest() {
        formasDePagoRequest()
    }
}
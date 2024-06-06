package com.Model

import android.util.Log
import com.Constants.ConstantsPreferences
import com.DataModel.SaleVtol
import com.DataModel.VtolCancelResponse
import com.Interfaces.ImprimeVoucherMVP
import com.Presenter.ImprimeVoucherPresenter.Companion.TAG
import com.Presenter.ImprimeVoucherPresenter.Companion.filesWeakReference
import com.Presenter.ImprimeVoucherPresenter.Companion.preferenceHelper
import com.Presenter.ImprimeVoucherPresenter.Companion.preferenceHelperLogs
import com.Presenter.ImprimeVoucherPresenter.Companion.view
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

class ImprimeVoucherModel : ImprimeVoucherMVP.Model, CoroutineScope {
    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    private object WebServiceTaskCaller {
        fun CallWSConsumptionTask(request: SoapObject, method: String) {
            val service: ReadJSONFeedTask = object : ReadJSONFeedTask() {
                override fun onResponseReceived(jsonResult: String) {
                    if (ToksWebServiceExceptionRate.rateError(jsonResult) != "") {
                        val exceptionRated = ToksWebServiceExceptionRate.rateError(jsonResult)
                        view!!.onExceptionImprimeVoucherResult(exceptionRated)
                    } else {
                        imprimeVoucherRequestResult(jsonResult)
                    }
                }
            }
            service.execute(request, method, preferenceHelper)
        }
    }

    companion object {
        private fun imprimeVoucherRequest(
            saleVtol: SaleVtol?,
            vtolCancelResponse: VtolCancelResponse?,
            folio: String?,
            campo32: String?
        ) {
            val request = SoapObject(
                ToksWebServicesConnection.NAMESPACE,
                ToksWebServicesConnection.IMPRIME_BOUCHER
            )
            request.addProperty("Llave", ToksWebServicesConnection.KEY)
            request.addProperty("Cadena", ToksWebServicesConnection.STRING)
            request.addProperty(
                "Folio",
                if (folio!!.isEmpty()) preferenceHelper!!.getString(
                        ConstantsPreferences.PREF_FOLIO_BOUCHER,
                        null
                ) else folio
            )
            request.addProperty(
                "id_local",
                preferenceHelper!!.getString(ConstantsPreferences.PREF_UNIDAD, null)
            )
            request.addProperty(
                "estafeta",
                preferenceHelper!!.getString(ConstantsPreferences.PREF_ESTAFETA, null)
            )
            if (!campo32!!.isEmpty()) {
                request.addProperty("campo32", campo32)
            } else if (saleVtol == null && vtolCancelResponse == null) {
                request.addProperty("campo32", "")
            } else {
                request.addProperty(
                    "campo32",
                    if (saleVtol == null) vtolCancelResponse!!.campo32 else saleVtol.campo32
                )
            }
            request.addProperty("Reimpresion", "N")
            Log.d(TAG, "imprimeVoucherRequest: $request")
            filesWeakReference!!.get()!!.registerLogs(
                "Inicia ImprimeVoucher",
                request.toString(),
                preferenceHelperLogs!!,
                preferenceHelper!!
            )
            WebServiceTaskCaller.CallWSConsumptionTask(
                request,
                ToksWebServicesConnection.IMPRIME_BOUCHER
            )
        }

        private fun imprimeVoucherRequestResult(jsonResult: String) {
            try {
                filesWeakReference!!.get()!!.registerLogs(
                    "Termina ImprimeVoucher",
                    "",
                    preferenceHelperLogs!!,
                    preferenceHelper!!
                )
                preferenceHelper!!.putBoolean(ConstantsPreferences.PREF_IS_VOUCHER, true)
                val jsonArray = JSONArray(jsonResult)
                val arrayCodigo = ArrayList<String>()
                val arrayEmisor = ArrayList<String>()
                val arrayCliente = ArrayList<String>()
                val codigo = jsonArray.getJSONObject(0).getString("CODIGO").toUpperCase()
                if (codigo == "OK") {
                    for (i in 1 until jsonArray.length()) {
                        arrayCodigo.add(jsonArray.getJSONObject(i).getString("CODIGO"))
                        arrayEmisor.add(jsonArray.getJSONObject(i).getString("EMISOR"))
                        arrayCliente.add(jsonArray.getJSONObject(i).getString("CLIENTE"))
                    }
                    val arrayListAuxEmisor = ArrayList<String?>()
                    val arrayListAuxCliente = ArrayList<String?>()
                    for (j in arrayCodigo.indices) {
                        if (arrayEmisor[j] == "1") {
                            arrayListAuxEmisor.add(arrayCodigo[j])
                        }
                        if (arrayCliente[j] == "1") {
                            arrayListAuxCliente.add(arrayCodigo[j])
                        }
                    }
                    view!!.onSuccessImprimeVoucherResult(arrayListAuxEmisor, arrayListAuxCliente)
                } else {
                    view!!.onFailImprimeVoucherResult(codigo)
                }
            } catch (e: JSONException) {
                e.printStackTrace()
                filesWeakReference!!.get()!!.createFileException(
                    "Controller/ImprimeVoucherRequest/imprimeVoucherRequestResult " + jsonResult +
                            " " + Log.getStackTraceString(e), preferenceHelper!!
                )
                view!!.onExceptionImprimeVoucherResult(e.message)
            }
        }
    }

    override fun executeImprimeVoucherRequest(
        saleVtol: SaleVtol?,
        vtolCancelResponse: VtolCancelResponse?,
        folio: String?,
        campo32: String?
    ) {
        imprimeVoucherRequest(saleVtol, vtolCancelResponse, folio, campo32)
    }
}
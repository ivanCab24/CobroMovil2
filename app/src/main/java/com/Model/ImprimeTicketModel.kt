package com.Model

import android.util.Log
import com.Constants.ConstantsPreferences
import com.Interfaces.ImprimeTicketMVP
import com.Presenter.ImprimeTicketPresenter
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

class ImprimeTicketModel : ImprimeTicketMVP.Model, CoroutineScope {
    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    object WebServiceTaskCaller {
        fun CallWSConsumptionTask(request: SoapObject, method: String) {
            val service: ReadJSONFeedTask = object : ReadJSONFeedTask() {
                override fun onResponseReceived(jsonResult: String) {
                    if (ToksWebServiceExceptionRate.rateError(jsonResult) != "") {
                        val exceptionRated = ToksWebServiceExceptionRate.rateError(jsonResult)
                        ImprimeTicketPresenter.view!!.onExceptionImprimeTicketResult(exceptionRated)
                    } else {
                        imprimeTicketRequestResult(jsonResult)
                    }
                }
            }
            service.execute(request, method, ImprimeTicketPresenter.preferenceHelper)
        }
    }

    companion object {
        private fun imprimeTicketRequest(folio: String?) {
            val request = SoapObject(
                ToksWebServicesConnection.NAMESPACE,
                ToksWebServicesConnection.IMPRIME_TICKET
            )
            request.addProperty("Llave", ToksWebServicesConnection.KEY)
            request.addProperty("Cadena", ToksWebServicesConnection.STRING)
            request.addProperty(
                "Folio",
                if (folio!!.isEmpty()) ImprimeTicketPresenter.preferenceHelper!!.getString(
                        ConstantsPreferences.PREF_FOLIO_CUENTA_CERRADA,
                        null
                ) else folio
            )
            request.addProperty(
                "Unidad",
                ImprimeTicketPresenter.preferenceHelper!!.getString(ConstantsPreferences.PREF_UNIDAD, null)
            )
            Log.d(ImprimeTicketPresenter.TAG, "imprimeTicketRequest: $request")
            ImprimeTicketPresenter.filesWeakReference!!.get()!!.registerLogs(
                "Inicia ImprimeTicket",
                request.toString(),
                ImprimeTicketPresenter.preferenceHelperLogs!!,
                ImprimeTicketPresenter.preferenceHelper!!
            )
            WebServiceTaskCaller.CallWSConsumptionTask(
                request,
                ToksWebServicesConnection.IMPRIME_TICKET // IMPRIME_TICKET
            )
        }

        private fun imprimeTicketRequestResult(jsonResult: String) {
            System.out.println("resultado del json" + jsonResult)
            try {
                ImprimeTicketPresenter.filesWeakReference!!.get()!!.registerLogs(
                    "Termina ImprimeTicket",
                    "",
                    ImprimeTicketPresenter.preferenceHelperLogs!!,
                    ImprimeTicketPresenter.preferenceHelper!!
                )
                ImprimeTicketPresenter.preferenceHelper!!.putBoolean(
                    ConstantsPreferences.PREF_IS_VOUCHER,
                    false
                )
                val jsonArray = JSONArray(jsonResult)
                val Codigo = jsonArray.getJSONObject(0).getString("CODIGO").toUpperCase()
                val arrayTextoTicket = ArrayList<String?>()
                if (Codigo == "OK") {
                    val imprimeCodBarras = jsonArray.getJSONObject(0).getString("CODBARRAS")
                    ImprimeTicketPresenter.preferenceHelper!!.putString(
                        ConstantsPreferences.PREF_CODBARRAS,
                        imprimeCodBarras
                    )
                    for (i in 0 until jsonArray.length()) {
                        when (val texto = jsonArray.getJSONObject(i).getString("TEXTO")) {
                            "En caso de requerir Factura" -> {
                                arrayTextoTicket.add("Solicitar su factura al")
                            }
                            "Electronica lo podra  soli-" -> {
                                arrayTextoTicket.add("momento del pago o durante ")
                            }
                            "citar durante el mes en que" -> {
                                arrayTextoTicket.add("el mes en que se realizó el")
                            }
                            "se realizo su consumo o  en" -> {
                                arrayTextoTicket.add("consumo")
                            }
                            "el  momento  del  pago  del" -> {}
                            "consumo" -> {}
                            else -> arrayTextoTicket.add(texto)
                        }
                    }
                    arrayTextoTicket.add("")
                    arrayTextoTicket.add("Comprobante sin efectos fiscales")
                    arrayTextoTicket.add("")
                    println(arrayTextoTicket.toString())
                    ImprimeTicketPresenter.view!!.onSuccessImprimeTicketResult(arrayTextoTicket)
                } else {
                    ImprimeTicketPresenter.view!!.onFailExceptionImprimeTicketResult(Codigo)
                }
            } catch (e: JSONException) {
                e.printStackTrace()
                ImprimeTicketPresenter.filesWeakReference!!.get()!!.createFileException(
                    "Controller/ImprimeTicketRequest/imprimeTicketRequestResult " + jsonResult +
                            " " + Log.getStackTraceString(e),
                    ImprimeTicketPresenter.preferenceHelper!!
                )
                ImprimeTicketPresenter.view!!.onExceptionImprimeTicketResult(e.message)
            }
        }
    }

    override fun executeImprimeTicketRequest(folio: String?) {
        imprimeTicketRequest(folio)
    }
}
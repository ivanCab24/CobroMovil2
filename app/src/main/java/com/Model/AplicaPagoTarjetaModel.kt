package com.Model

import android.util.Log
import com.Constants.ConstantsPreferences
import com.Presenter.AplicaPagoTarjetaPresenter
import com.Utilities.Utils
import com.View.Fragments.ContentFragment
import com.webservicestasks.ReadJSONFeedTask
import com.webservicestasks.ToksWebServiceExceptionRate
import com.webservicestasks.ToksWebServicesConnection
import org.json.JSONArray
import org.json.JSONException
import org.ksoap2.serialization.SoapObject

class AplicaPagoTarjetaModel:com.Interfaces.AplicaPagoTarjetaPresenter.Model {
    private object WebServiceTaskCaller {
        fun CallWSConsumptionTask(request: SoapObject, method: String) {
            val service: ReadJSONFeedTask = object : ReadJSONFeedTask() {
                override fun onResponseReceived(jsonResult: String) {
                    if (ToksWebServiceExceptionRate.rateError(jsonResult) != "") {
                        val exceptionRated = ToksWebServiceExceptionRate.rateError(jsonResult)
                        AplicaPagoTarjetaPresenter.view!!.onExceptionAplicaPagoResult(exceptionRated)
                    } else {
                        aplicaPagoTarjetaRequestResult(jsonResult)
                    }
                }
            }
            service.execute(request, method, AplicaPagoTarjetaPresenter.preferenceHelper)
        }
    }
    companion object{
        private fun aplicaPagoTarjetaRequest() {
            val request = SoapObject(
                ToksWebServicesConnection.NAMESPACE,
                ToksWebServicesConnection.APLICA_PAGO
            )
            request.addProperty("Llave", ToksWebServicesConnection.KEY)
            request.addProperty("Cadena", ToksWebServicesConnection.STRING)
            request.addProperty(
                "Folio",
                AplicaPagoTarjetaPresenter.preferenceHelper!!.getString(ConstantsPreferences.PREF_FOLIO, null)
            )
            request.addProperty(
                "id_local",
                AplicaPagoTarjetaPresenter.preferenceHelper!!.getString(ConstantsPreferences.PREF_UNIDAD, null)
            )
            request.addProperty(
                "id_caja",
                AplicaPagoTarjetaPresenter.preferenceHelper!!.getString(ConstantsPreferences.PREF_ID_CAJA, null)
            )
            request.addProperty(
                "id_emp",
                AplicaPagoTarjetaPresenter.preferenceHelper!!.getString(ConstantsPreferences.PREF_ESTAFETA, null)
            )
            request.addProperty("tipo_fpgo", AplicaPagoTarjetaPresenter.getTipoFpgo)
            request.addProperty("id_fpgo", AplicaPagoTarjetaPresenter.getIdFpgo)
            request.addProperty("abono_parcial", "" + ContentFragment.montoCobro)
            request.addProperty("abono_mn", "" + ContentFragment.montoCobro)
            request.addProperty("abono_propina", "" + Utils.round(
                ContentFragment.getTipAmount(),
                2
            )
            )
            request.addProperty("Abono_cancelado", "0")
            request.addProperty("vencimiento", "0")
            request.addProperty("recnumapr", AplicaPagoTarjetaPresenter.getSaleVtol!!.campo22)
            request.addProperty("tipotarjeta", "1")
            request.addProperty("track1", AplicaPagoTarjetaPresenter.getResponse!!.trackI)
            request.addProperty("track2", AplicaPagoTarjetaPresenter.getResponse!!.trackII)
            request.addProperty("cuotas", AplicaPagoTarjetaPresenter.getSaleVtol!!.campo94)
            request.addProperty("txn", AplicaPagoTarjetaPresenter.getSaleVtol!!.campo24)
            request.addProperty("ticket", AplicaPagoTarjetaPresenter.getSaleVtol!!.campo32)
            request.addProperty("lote", AplicaPagoTarjetaPresenter.getSaleVtol!!.campo31)
            request.addProperty("abono_pto", "0")
            request.addProperty("arqc", AplicaPagoTarjetaPresenter.getResponse!!.tag9F27)
            request.addProperty("autorizacion", AplicaPagoTarjetaPresenter.getSaleVtol!!.campo22)
            request.addProperty("Total", Utils.round(
                ContentFragment.montoCobro + ContentFragment.getTipAmount(),
                2
            ).toString())
            request.addProperty("cvm", AplicaPagoTarjetaPresenter.getResponse!!.tag9F34)
            request.addProperty("signature", AplicaPagoTarjetaPresenter.getResponse!!.tagC2)
            request.addProperty("qps", AplicaPagoTarjetaPresenter.getSaleVtol!!.qps)
            request.addProperty("nombretarjeta", AplicaPagoTarjetaPresenter.getResponse!!.tarjetahabiente)
            request.addProperty("saldoantpuntos", AplicaPagoTarjetaPresenter.getSaleVtol!!.puntosAnterior)
            request.addProperty("numpuntos", AplicaPagoTarjetaPresenter.getSaleVtol!!.puntosRedimidos)
            request.addProperty("saldodisppuntos", AplicaPagoTarjetaPresenter.getSaleVtol!!.puntosDisponibles)
            request.addProperty("aid", AplicaPagoTarjetaPresenter.getResponse!!.tag4F)
            request.addProperty("producto", AplicaPagoTarjetaPresenter.getResponse!!.tag50)
            request.addProperty("pan", AplicaPagoTarjetaPresenter.getResponse!!.pan.replace("f", ""))
            Log.d(AplicaPagoTarjetaPresenter.TAG, "aplicaPagoTarjetaRequest: $request")
            AplicaPagoTarjetaPresenter.filesWeakReference!!.get()!!.registerLogs(
                "Inicia AplicaPagoTarjeta",
                request.toString(),
                AplicaPagoTarjetaPresenter.preferenceHelperLogs!!,
                AplicaPagoTarjetaPresenter.preferenceHelper!!
            )
            WebServiceTaskCaller.CallWSConsumptionTask(
                request,
                ToksWebServicesConnection.APLICA_PAGO
            )
        }

        private fun aplicaPagoTarjetaRequestResult(jsonResult: String) {
            try {
                AplicaPagoTarjetaPresenter.filesWeakReference!!.get()!!.registerLogs(
                    "Termina AplicaPagoTarjeta",
                    jsonResult,
                    AplicaPagoTarjetaPresenter.preferenceHelperLogs!!,
                    AplicaPagoTarjetaPresenter.preferenceHelper!!
                )
                val jsonArray = JSONArray(jsonResult)
                val codigo = jsonArray.getJSONObject(0).getString("CODIGO")
                if (codigo == "Pago aplicado") {
                    AplicaPagoTarjetaPresenter.view!!.onSuccessAplicaPagoResult()
                } else {
                    AplicaPagoTarjetaPresenter.view!!.onFailAplicaPagoResult(codigo)
                }
            } catch (e: JSONException) {
                e.printStackTrace()
                AplicaPagoTarjetaPresenter.filesWeakReference!!.get()!!.createFileException(
                    "Controller/AplicaPagoTarjetaRequest/aplicaPagoTarjetaRequestResult " + jsonResult + " "
                            + Log.getStackTraceString(e), AplicaPagoTarjetaPresenter.preferenceHelper!!
                )
                AplicaPagoTarjetaPresenter.view!!.onExceptionAplicaPagoResult(e.message)
            }
        }
    }

    override fun executeAplicaPagoRequest() {
        aplicaPagoTarjetaRequest()
    }
}
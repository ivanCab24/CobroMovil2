package com.Model

import android.util.Log
import com.Constants.ConstantsPreferences
import com.Interfaces.AplicaPagoMVP
import com.Presenter.AplicaPagoPresenter
import com.View.Fragments.ContentFragment
import com.webservicestasks.ReadJSONFeedTask
import com.webservicestasks.ToksWebServiceExceptionRate
import com.webservicestasks.ToksWebServicesConnection
import org.json.JSONArray
import org.json.JSONException
import org.ksoap2.serialization.SoapObject

class AplicaPagoModel:AplicaPagoMVP.Model {
    private object WebServiceTaskCaller {
        fun CallWSConsumptionTask(request: SoapObject, method: String) {
            val service: ReadJSONFeedTask = object : ReadJSONFeedTask() {
                override fun onResponseReceived(jsonResult: String) {
                    if (ToksWebServiceExceptionRate.rateError(jsonResult) != "") {
                        val exceptionRated = ToksWebServiceExceptionRate.rateError(jsonResult)
                        AplicaPagoPresenter.view!!.onExceptionAplicaPagoResult(exceptionRated)
                    } else {
                        aplicaPagoRequestResult(jsonResult)
                    }
                }
            }
            service.execute(request, method, AplicaPagoPresenter.preferenceHelper)
        }
    }
    companion object{
        private fun aplicaPagoRequest(autorizacion: String?) {
            val request = SoapObject(
                ToksWebServicesConnection.NAMESPACE,
                ToksWebServicesConnection.APLICA_PAGO
            )
            request.addProperty("Llave", ToksWebServicesConnection.KEY)
            request.addProperty("Cadena", ToksWebServicesConnection.STRING)
            request.addProperty(
                "Folio",
                AplicaPagoPresenter.preferenceHelper!!.getString(ConstantsPreferences.PREF_FOLIO, null)
            )
            request.addProperty(
                "id_local",
                AplicaPagoPresenter.preferenceHelper!!.getString(ConstantsPreferences.PREF_UNIDAD, null)
            )
            request.addProperty(
                "id_caja",
                AplicaPagoPresenter.preferenceHelper!!.getString(ConstantsPreferences.PREF_ID_CAJA, null)
            )
            request.addProperty(
                "id_emp",
                AplicaPagoPresenter.preferenceHelper!!.getString(ConstantsPreferences.PREF_ESTAFETA, null)
            )
            request.addProperty("tipo_fpgo", ContentFragment.formasDePago!!.tipoFpgo)
            request.addProperty("id_fpgo", ContentFragment.formasDePago!!.idFpgo)
            request.addProperty("abono_parcial", "" + ContentFragment.montoCobro)
            request.addProperty("abono_mn", "" + ContentFragment.montoCobro)
            request.addProperty("abono_propina", "" + ContentFragment.tipAmount)
            request.addProperty("Abono_cancelado", "0")
            request.addProperty("vencimiento", "0")
            request.addProperty("recnumapr", "")
            request.addProperty("tipotarjeta", "")
            request.addProperty("track1", "")
            request.addProperty("track2", "")
            request.addProperty("cuotas", "")
            request.addProperty("txn", "")
            request.addProperty("ticket", "")
            request.addProperty("lote", "")
            request.addProperty("abono_pto", "0")
            request.addProperty("arqc", "")
            request.addProperty("autorizacion", autorizacion)
            request.addProperty("Total", (ContentFragment.montoCobro + ContentFragment.tipAmount).toString())
            request.addProperty("cvm", "")
            request.addProperty("signature", "")
            request.addProperty("qps", "")
            request.addProperty("nombretarjeta", "")
            request.addProperty("saldoantpuntos", "")
            request.addProperty("numpuntos", "")
            request.addProperty("saldodisppuntos", "")
            request.addProperty("aid", "")
            request.addProperty("producto", "")
            request.addProperty("pan", "")
            Log.d(AplicaPagoPresenter.TAG, "aplicaPagoRequest: $request")
            AplicaPagoPresenter.filesWeakReference!!.get()!!.registerLogs(
                "Inicia AplicaPago",
                request.toString(),
                AplicaPagoPresenter.preferenceHelperLogs!!,
                AplicaPagoPresenter.preferenceHelper!!
            )
            WebServiceTaskCaller.CallWSConsumptionTask(
                request,
                ToksWebServicesConnection.APLICA_PAGO
            )
        }

        private fun aplicaPagoRequestResult(jsonResult: String) {
            System.out.println("---->entrando al metodo de pago<-----")
            try {
                AplicaPagoPresenter.filesWeakReference!!.get()!!.registerLogs(
                    "Termina AplicaPago",
                    jsonResult,
                    AplicaPagoPresenter.preferenceHelperLogs!!,
                    AplicaPagoPresenter.preferenceHelper!!
                )
                val jsonArray = JSONArray(jsonResult)
                val codigo = jsonArray.getJSONObject(0).getString("CODIGO")
                if (codigo == "Pago aplicado") {
                    System.out.println("---->despues de aplicar el pago<-----")
                    AplicaPagoPresenter.view!!.onSuccessAplicaPagoResult(codigo)
                } else {
                    System.out.println("---->despues de fallar al aplicar el pago<-----")
                    AplicaPagoPresenter.view!!.onFailAplicaPagoResult(codigo)
                }
            } catch (e: JSONException) {
                e.printStackTrace()
                AplicaPagoPresenter.filesWeakReference!!.get()!!.createFileException(
                    "Controller/AplicaPagoRequest/aplicaPagoRequestResult " + jsonResult + " "
                            + Log.getStackTraceString(e), AplicaPagoPresenter.preferenceHelper!!
                )
                AplicaPagoPresenter.view!!.onExceptionAplicaPagoResult(e.message)
            }
        }
    }

    override fun executeAplicaPagoRequest(autorizacion: String?) {
        aplicaPagoRequest(autorizacion)
    }
}
package com.Model

import android.util.Log
import com.Constants.ConstantsPreferences
import com.DataModel.SaleVtol
import com.Interfaces.SaleVtolMVP
import com.Presenter.SaleVtolModel
import com.Utilities.Utils
import com.Verifone.EMVResponse
import com.View.Fragments.ContentFragment
import com.webservicestasks.ReadJSONFeedTask
import com.webservicestasks.ToksWebServiceExceptionRate
import com.webservicestasks.ToksWebServicesConnection
import org.json.JSONArray
import org.json.JSONException
import org.ksoap2.serialization.SoapObject

class SaleVtolModel:SaleVtolMVP.Model {
    private object WebServiceTaskCaller {
        fun CallWSConsumptionTask(request: SoapObject, method: String) {
            val service: ReadJSONFeedTask = object : ReadJSONFeedTask() {
                override fun onResponseReceived(jsonResult: String) {
                    if (ToksWebServiceExceptionRate.rateError(jsonResult) != "") {
                        val exceptionRated = ToksWebServiceExceptionRate.rateError(jsonResult)
                        SaleVtolModel.view!!.onExceptionSaleVtolResult(exceptionRated)
                    } else {
                        saleVtolRequestResult(jsonResult)
                    }
                }
            }
            service.execute(request, method, SaleVtolModel.preferenceHelper)
        }
    }
    companion object{
        private fun saleVtolRequest(
            response: EMVResponse?,
            modoLectura: String?,
            transactionType: String?,
            year: String?,
            month: String?,
            day: String?
        ) {
            val request = SoapObject(
                ToksWebServicesConnection.NAMESPACE,
                ToksWebServicesConnection.Sale_vitol
            )
            request.addProperty("Llave", ToksWebServicesConnection.KEY)
            request.addProperty("Cadena", ToksWebServicesConnection.STRING)
            request.addProperty(
                "Unidad",
                SaleVtolModel.preferenceHelper!!.getString(ConstantsPreferences.PREF_UNIDAD, null)
            )
            request.addProperty(
                "Id_term",
                SaleVtolModel.preferenceHelper!!.getString(ConstantsPreferences.PREF_NUMERO_TERMINAL, null)
            )
            request.addProperty("Id_Coma", "2")
            request.addProperty("tipotarjeta", response!!.transactionType)
            request.addProperty("emvData", "")
            request.addProperty("chipTokens", response.chipTokens)
            request.addProperty("codigoSeguridad", "")
            request.addProperty("ctCZ", if (transactionType == "BANDA") "" else response.ctCZ)
            request.addProperty("ctEY", response.ctEY)
            request.addProperty("ctR1", response.ctR1)
            request.addProperty("message", response.message)
            request.addProperty("modoLectura", modoLectura)
            request.addProperty("pan", response.pan.replace("f", ""))
            request.addProperty("paramE2", response.paramE2)
            request.addProperty("transactionType", response.transactionType)
            request.addProperty("tag4F", response.tag4F)
            request.addProperty("tag50", response.tag50)
            request.addProperty("tag5F30", response.tag5F30)
            request.addProperty("tag5F34", response.tag5F34)
            request.addProperty("tag8A", response.tag8A)
            request.addProperty("tag95", response.tag95)
            request.addProperty("tag99", response.tag99)
            request.addProperty("tag9B", response.tag9B)
            request.addProperty("tag9F12", response.tag9F12)
            request.addProperty("tag9F26", response.tag9F26)
            request.addProperty("tag9F27", response.tag9F27)
            request.addProperty("tag9F34", response.tag9F34)
            request.addProperty("tag9F39", response.tag9F39)
            request.addProperty("tagC2", response.tagC2)
            request.addProperty(
                "tarjetahabiente",
                if (transactionType == "BANDA") "" else response.tarjetahabiente
            )
            request.addProperty("trackI", response.trackI)
            request.addProperty("trackII", response.trackII.replace("f", ""))
            request.addProperty("Monto", "" + ContentFragment.montoCobro)
            request.addProperty("Propina", "" + Utils.round(ContentFragment.getTipAmount(), 2))
            request.addProperty("OriginalDate", year + month + day)
            request.addProperty("Campo32", "")
            Log.d(SaleVtolModel.TAG, "saleVtolRequest: $request")
            SaleVtolModel.filesWeakReference!!.get()!!.registerLogs(
                "Inicia SaleVitol",
                request.toString(),
                SaleVtolModel.preferenceHelperLogs!!,
                SaleVtolModel.preferenceHelper!!
            )
            WebServiceTaskCaller.CallWSConsumptionTask(
                request,
                ToksWebServicesConnection.Sale_vitol
            )
        }

        private fun saleVtolRequestResult(jsonResult: String) {
            try {
                SaleVtolModel.filesWeakReference!!.get()!!.registerLogs(
                    "Termina SaleVitol",
                    jsonResult,
                    SaleVtolModel.preferenceHelperLogs!!,
                    SaleVtolModel.preferenceHelper!!
                )
                val jsonArray = JSONArray(jsonResult)
                val jsonObject = jsonArray.getJSONObject(0)
                val codigo = jsonObject.getString("CODIGO").toUpperCase()
                val campo22 = jsonObject.getString("CAMPO22")
                val campo24 = jsonObject.getString("CAMPO24")
                val campo25 = jsonObject.getString("CAMPO25")
                val campo27 = jsonObject.getString("CAMPO27")
                val campo28 = jsonObject.getString("CAMPO28")
                val campo31 = jsonObject.getString("CAMPO31")
                val campo32 = jsonObject.getString("CAMPO32")
                val campo33 = jsonObject.getString("CAMPO33")
                val campo58 = jsonObject.getString("CAMPO58")
                val campo94 = jsonObject.getString("CAMPO94")
                val puntosRedimidos = jsonObject.getString("PUNTOSREDIMIDOS")
                val puntosDisponibles = jsonObject.getString("PUNTOSDISPONIBLES")
                val puntosAnterior = jsonObject.getString("PUNTOSANTERIOR")
                val qPS = jsonObject.getString("QPS")
                val campo1022 = jsonObject.getString("CAMPO1022") /*"! ER00002 10"*/
                val saleVtol = SaleVtol(
                    codigo,
                    campo22,
                    campo25,
                    campo27,
                    campo32,
                    campo24,
                    campo31,
                    campo33,
                    campo94,
                    puntosAnterior,
                    puntosDisponibles,
                    puntosRedimidos,
                    qPS,
                    campo28,
                    campo58,
                    campo1022
                )
                if (codigo == "OK") {
                    if (campo1022 != "NA" || campo1022 == null) {
                        val campoER = campo1022.substring(campo1022.indexOf("! ER"))
                        val campoERAux = campoER.substring(10, 11)
                        SaleVtolModel.preferenceHelper!!.putString(ConstantsPreferences.PREF_CAMPO_ER, campoERAux)
                    }
                    SaleVtolModel.view!!.onSuccessVtolResult(saleVtol)
                } else {
                    if (campo25.toUpperCase().contains("ERROR") || campo27.toUpperCase()
                            .contains("ERROR")
                    ) {
                        SaleVtolModel.view!!.onFailSaleVtolResult("$codigo $campo28")
                    } else {
                        SaleVtolModel.view!!.onSuccessVtolResult(saleVtol)
                    }
                }
            } catch (e: JSONException) {
                e.printStackTrace()
                SaleVtolModel.filesWeakReference!!.get()!!.createFileException(
                    "Controller/SaleVtolRequest/saleVtolRequestResult " + jsonResult +
                            " " + Log.getStackTraceString(e), SaleVtolModel.preferenceHelper!!
                )
                SaleVtolModel.view!!.onExceptionSaleVtolResult(e.message)
            }
        }
    }

    override fun executeSaleVtolRequest(
        response: EMVResponse?,
        modoLectura: String?,
        transactionType: String?,
        year: String?,
        month: String?,
        day: String?
    ) {
        saleVtolRequest(response,modoLectura,transactionType,year,month,day)
    }
}
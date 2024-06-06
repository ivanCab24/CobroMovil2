package com.Model

import android.util.Log
import com.Constants.ConstantsPreferences
import com.DataModel.VtolCancelResponse
import com.Interfaces.CancelacionVtolMVP
import com.Presenter.CancelacionVtolPresenter
import com.Utilities.Utils
import com.Verifone.EMVResponse
import com.View.Fragments.ContentFragment
import com.webservicestasks.ReadJSONFeedTask
import com.webservicestasks.ToksWebServiceExceptionRate
import com.webservicestasks.ToksWebServicesConnection
import org.json.JSONArray
import org.json.JSONException
import org.ksoap2.serialization.SoapObject

class CancelacionVtolModel:CancelacionVtolMVP.Model {
    private object WebServiceTaskCaller {
        fun CallWSConsumptionTask(request: SoapObject, method: String) {
            val service: ReadJSONFeedTask = object : ReadJSONFeedTask() {
                override fun onResponseReceived(jsonResult: String) {
                    if (ToksWebServiceExceptionRate.rateError(jsonResult) != "") {
                        val exceptionResult = ToksWebServiceExceptionRate.rateError(jsonResult)
                        CancelacionVtolPresenter.view!!.onCancelacionVtolExceptionResult(exceptionResult)
                    } else {
                        cancelacionVtolRequestResponse(jsonResult)
                    }
                }
            }
            service.execute(request, method, CancelacionVtolPresenter.preferenceHelper)
        }
    }
    companion object{
        private fun cancelacionVtolRequest(
            response: EMVResponse?,
            setYear: String?,
            setMonth: String?,
            setDay: String?,
            setTicket: String?
        ) {
            val request = SoapObject(
                ToksWebServicesConnection.NAMESPACE,
                ToksWebServicesConnection.Cancelacion_vitol
            )
            request.addProperty("Llave", ToksWebServicesConnection.KEY)
            request.addProperty("Cadena", ToksWebServicesConnection.STRING)
            request.addProperty(
                "Unidad",
                CancelacionVtolPresenter.preferenceHelper!!.getString(ConstantsPreferences.PREF_UNIDAD, null)
            )
            request.addProperty("Id_term", "2")
            request.addProperty("Id_Coma", "2")
            request.addProperty("tipotarjeta", response!!.transactionType)
            request.addProperty("emvData", "")
            request.addProperty("chipTokens", response.chipTokens)
            request.addProperty("codigoSeguridad", "")
            request.addProperty("ctCZ", response.ctCZ)
            request.addProperty("ctEY", response.ctEY)
            request.addProperty("ctR1", response.ctR1)
            request.addProperty("message", response.message)
            request.addProperty("modoLectura", "0")
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
            request.addProperty("tarjetahabiente", response.tarjetahabiente)
            request.addProperty("trackI", response.trackI)
            request.addProperty("trackII", response.trackII)
            request.addProperty(
                "Monto",
                "" + Utils.round(
                    ContentFragment.pagos!!.abonoMn + ContentFragment.pagos!!.abonoPropina,
                    2
                ).toString()
            )
            request.addProperty("Propina", "0")
            request.addProperty("OriginalDate", setYear + setMonth + setDay)
            request.addProperty("Campo32", setTicket)
            Log.d(CancelacionVtolPresenter.TAG, "cancelacionVtolRequest: $request")
            CancelacionVtolPresenter.filesWeakReference!!.get()!!.registerLogs(
                "Inicia CancelacionVtol",
                request.toString(),
                CancelacionVtolPresenter.preferenceHelperLogs!!,
                CancelacionVtolPresenter.preferenceHelper!!
            )
            WebServiceTaskCaller.CallWSConsumptionTask(
                request,
                ToksWebServicesConnection.Cancelacion_vitol
            )
        }

        private fun cancelacionVtolRequestResponse(jsonResult: String) {
            try {
                CancelacionVtolPresenter.filesWeakReference!!.get()!!.registerLogs(
                    "Termina CancelacionVtol",
                    jsonResult,
                    CancelacionVtolPresenter.preferenceHelperLogs!!,
                    CancelacionVtolPresenter.preferenceHelper!!
                )
                val jsonArray = JSONArray(jsonResult)
                val jsonObject = jsonArray.getJSONObject(0)
                val codigo = jsonObject.getString("CODIGO").toUpperCase()
                val campo28 = jsonObject.getString("CAMPO28")
                if (codigo == "OK") {
                    val codigoLoc = jsonArray.getJSONObject(0).getString("CODIGO")
                    val campo24 = jsonArray.getJSONObject(0).getString("CAMPO24")
                    val campo22 = jsonArray.getJSONObject(0).getString("CAMPO22")
                    val campo25 = jsonArray.getJSONObject(0).getString("CAMPO25")
                    val campo27 = jsonArray.getJSONObject(0).getString("CAMPO27")
                    val campo32 = jsonArray.getJSONObject(0).getString("CAMPO32")
                    val campo28Loc = jsonArray.getJSONObject(0).getString("CAMPO28")
                    val vtolCancelResponse = VtolCancelResponse(
                        codigoLoc,
                        campo24,
                        campo22,
                        campo25,
                        campo27,
                        campo32,
                        campo28Loc
                    )
                    CancelacionVtolPresenter.view!!.onCancelacionVtolSuccessResult(vtolCancelResponse)
                } else {
                    CancelacionVtolPresenter.view!!.onCancelacionVtolFailResult(campo28)
                }
            } catch (e: JSONException) {
                e.printStackTrace()
                CancelacionVtolPresenter.filesWeakReference!!.get()!!.createFileException(
                    "Controller/CancelacionVtolRequest/cancelacionVtolRequestResponse " + jsonResult +
                            " " + Log.getStackTraceString(e), CancelacionVtolPresenter.preferenceHelper!!
                )
                CancelacionVtolPresenter.view!!.onCancelacionVtolExceptionResult(e.message)
            }
        }
    }

    override fun executeCancelacionVtolRequest(response: EMVResponse?,
                                               setYear: String?,
                                               setMonth: String?,
                                               setDay: String?,
                                               setTicket: String?) {
        cancelacionVtolRequest(response,setYear,setMonth,setDay,setTicket)
    }


}
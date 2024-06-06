package com.Model

import android.util.Log
import com.Constants.ConstantsPreferences
import com.DataModel.DescuentosAplicados
import com.Interfaces.DescuentosAplicadosMVP
import com.Presenter.DescuentosAplicadosPresenter
import com.Presenter.DescuentosAplicadosPresenter.Companion.filesWeakReference
import com.Presenter.DescuentosAplicadosPresenter.Companion.preferenceHelper
import com.Presenter.DescuentosAplicadosPresenter.Companion.preferenceHelperLogs
import com.Presenter.DescuentosAplicadosPresenter.Companion.view
import com.View.Fragments.ContentFragment
import com.webservicestasks.ReadJSONFeedTask
import com.webservicestasks.ToksWebServiceExceptionRate
import com.webservicestasks.ToksWebServicesConnection
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.ksoap2.serialization.SoapObject

class DescuentosAplicadosModel:DescuentosAplicadosMVP.Model {
    private object WebServiceTaskCaller {
        fun CallWSConsumptionTask(request: SoapObject, method: String) {
            val service: ReadJSONFeedTask = object : ReadJSONFeedTask() {
                override fun onResponseReceived(jsonResult: String) {
                    if (ToksWebServiceExceptionRate.rateError(jsonResult) != "") {
                        val exceptionRated = ToksWebServiceExceptionRate.rateError(jsonResult)
                        view!!.onExceptionDescuentosAplicadosResult(exceptionRated)
                    } else {
                        descuentosAplicadosRequestResult(jsonResult)
                    }
                }
            }
            service.execute(request, method, preferenceHelper)
        }
    }

    companion object{
        private fun descuentosAplicadosRequest() {
            val request = SoapObject(
                ToksWebServicesConnection.NAMESPACE,
                ToksWebServicesConnection.CONSULTAR_DESCUENTOS_APLICADOS
            )
            request.addProperty("Llave", ToksWebServicesConnection.KEY)
            request.addProperty("Cadena", ToksWebServicesConnection.STRING)
            request.addProperty(
                "Unidad",
                preferenceHelper!!.getString(ConstantsPreferences.PREF_UNIDAD, null)
            )
            request.addProperty("Folio", preferenceHelper!!.getString(ConstantsPreferences.PREF_FOLIO, null))
            Log.d(DescuentosAplicadosPresenter.TAG, "descuentosAplicadosRequest: $request")
            filesWeakReference!!.get()!!.registerLogs(
                "Inicia ConsultarDescuentosAplicados",
                request.toString(),
                preferenceHelperLogs!!,
                preferenceHelper!!
            )
            WebServiceTaskCaller.CallWSConsumptionTask(
                request,
                ToksWebServicesConnection.CONSULTAR_DESCUENTOS_APLICADOS
            )
        }

        private fun descuentosAplicadosRequestResult(jsonResult: String) {
            try {
                filesWeakReference!!.get()!!.registerLogs(
                    "Termina ConsultarDescuentosAplicados",
                    jsonResult,
                    preferenceHelperLogs!!,
                    preferenceHelper!!
                )
                val jsonArray = JSONArray(jsonResult)
                val codigo = jsonArray.getJSONObject(0).getString("CODIGO").toUpperCase()
                val descuentosAplicadosArrayList = ArrayList<DescuentosAplicados?>()
                if (codigo == "OK") {
                    var jsonObject: JSONObject
                    for (i in 0 until jsonArray.length()) {
                        jsonObject = jsonArray.getJSONObject(i)
                        val FECHA = jsonObject.getString("FECHA")
                        val ID_LOCAL = jsonObject.getString("ID_LOCAL")
                        val ID_TERM = jsonObject.getString("ID_TERM")
                        val ID_COMA = jsonObject.getString("ID_COMA")
                        val TIPO_DESC = jsonObject.getString("TIPO_DESC")
                        val ID_DESC = jsonObject.getString("ID_DESC")
                        val PORCENTAJE = jsonObject.getString("PORCENTAJE")
                        val DESC_CON = jsonObject.getString("DESC_CON")
                        val DESC_EMP = jsonObject.getString("DESC_EMP")
                        val DESC_IDEMP = jsonObject.getString("DESC_IDEMP")
                        val DESC_REF = jsonObject.getString("DESC_REF")
                        val ID_POS = jsonObject.getString("ID_POS")
                        val M_DESC = jsonObject.getString("M_DESC")
                        val M_TOTAL = jsonObject.getString("M_TOTAL")
                        val CONTADOR = jsonObject.getString("CONTADOR")
                        if(TIPO_DESC=="2" )
                            ContentFragment.haveGRGDescuento=true
                        descuentosAplicadosArrayList.add(
                            DescuentosAplicados(
                                ID_TERM,
                                PORCENTAJE,
                                DESC_EMP,
                                FECHA,
                                DESC_IDEMP,
                                DESC_CON,
                                ID_LOCAL,
                                CONTADOR,
                                ID_POS,
                                ID_COMA,
                                M_TOTAL,
                                DESC_REF,
                                TIPO_DESC,
                                M_DESC,
                                ID_DESC
                            )
                        )
                    }
                    view!!.onSuccessDescuentosAplicadosResult(descuentosAplicadosArrayList)
                } else {
                    view!!.onFailDescuentosAplicadosResult(codigo)
                }
            } catch (e: JSONException) {
                e.printStackTrace()
                filesWeakReference!!.get()!!.createFileException(
                    "Controller/DescuentosAplicadosRequest/descuentosAplicadosRequestResult " + jsonResult +
                            " " + Log.getStackTraceString(e), preferenceHelper!!
                )
                view!!.onExceptionDescuentosAplicadosResult(e.message)
            }
        }
    }

    override fun executeDescuentosAplicadosRequest() {
        descuentosAplicadosRequest()
    }
}
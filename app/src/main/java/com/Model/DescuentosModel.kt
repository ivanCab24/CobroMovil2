package com.Model

import android.util.Log
import com.Constants.ConstantsPreferences
import com.DataModel.Descuentos
import com.DataModel.DescuentosAplicados
import com.Interfaces.DescuentosMVP
import com.Presenter.DescuentosAplicadosPresenter
import com.Presenter.DescuentosPresenter
import com.Presenter.DescuentosPresenter.Companion.filesWeakReference
import com.Presenter.DescuentosPresenter.Companion.preferencesHelper
import com.Presenter.DescuentosPresenter.Companion.view
import com.Utilities.Utilities
import com.Utilities.Utils
import com.View.Fragments.ContentFragment
import com.webservicestasks.ReadJSONFeedTask
import com.webservicestasks.ToksWebServiceExceptionRate
import com.webservicestasks.ToksWebServicesConnection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.ksoap2.serialization.SoapObject
import kotlin.coroutines.CoroutineContext

class DescuentosModel : DescuentosMVP.Model, CoroutineScope {
    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    object WebServiceTaskCaller {
        fun CallWSConsumptionTask(request: SoapObject, method: String) {
            val service: ReadJSONFeedTask = object : ReadJSONFeedTask() {
                override fun onResponseReceived(jsonResult: String) {
                    if (ToksWebServiceExceptionRate.rateError(jsonResult) != "") {
                        val exceptionRated = ToksWebServiceExceptionRate.rateError(jsonResult)
                        view!!.onExceptionResult(exceptionRated)
                    } else {
                        if(method==CONSULTAR_DESCUENTOS_APLICADOS)
                            descuentosAplicadosRequestResult(jsonResult)
                        else
                            descuentosRequestResult(jsonResult)
                    }
                }
            }
            service.execute(request, method, preferencesHelper)
        }
    }

    companion object {
        private fun descuentosRequest() {
            val request = SoapObject(
                ToksWebServicesConnection.NAMESPACE,
                ToksWebServicesConnection.CAT_DESCUENTOS
            )
            request.addProperty("Llave", ToksWebServicesConnection.KEY)
            request.addProperty("Cadena", ToksWebServicesConnection.STRING)
            Log.d(DescuentosPresenter.TAG, "descuentosRequest: $request")
            WebServiceTaskCaller.CallWSConsumptionTask(
                request,
                ToksWebServicesConnection.CAT_DESCUENTOS
            )
        }

        private fun descuentosRequestResult(jsonResult: String) {
            try {
                preferencesHelper!!.putString(
                    ConstantsPreferences.PREF_JSON_DESCUENTOS,
                    jsonResult
                )
                val jsonArray = JSONArray(jsonResult)
                val codigo = jsonArray.getJSONObject(0).getString("CODIGO").toUpperCase()
                val descuentosArrayList = ArrayList<Descuentos?>()
                val descuentosArrayList2 = ArrayList<Descuentos?>()
                if (codigo == "OK") {
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val tipo = jsonObject.getInt("TIPO")
                        val idDesc = jsonObject.getInt("ID_DESC")
                        val descuento = jsonObject.getString("DESCUENTO")
                        val descripcion = jsonObject.getString("DESCRIPCION")
                        val porcentaje = jsonObject.getDouble("PORCENTAJE")
                        val textReferencia = jsonObject.getString("TEXTREFERENCIA")
                        val favorito = jsonObject.getInt("FAVORITO")
                        if(tipo==5&&idDesc==997&&descuento=="PUNTOS A COMER")
                            ContentFragment.descuentoPorcentaje =
                            Descuentos(
                                5,
                                997,
                                "PUNTOS A COMER",
                                "descripcion",
                                20.0,
                                "textReferencia",1

                            )
                        else{
                            if(!Utils.catalogDescuentosCorporativos().containsKey(idDesc) && tipo!=4)
                            descuentosArrayList.add(Descuentos(
                                tipo,
                                idDesc,
                                descuento,
                                descripcion,
                                porcentaje,
                                textReferencia,
                                favorito
                            ))

                        }
                        descuentosArrayList2.add(Descuentos(
                            tipo,
                            idDesc,
                            descuento,
                            descripcion,
                            porcentaje,
                            textReferencia,
                            favorito
                        ))
                    }
                    ContentFragment.catalogoDescuentosSelect = descuentosArrayList2
                    view!!.onSuccess(descuentosArrayList)
                } else {
                    view!!.onFailResult(codigo)
                }
            } catch (e: JSONException) {
                e.printStackTrace()
                filesWeakReference!!.get()!!.createFileException(
                    "Controller/DescuentosRequest/descuentosRequestResult " + jsonResult +
                            " " + Log.getStackTraceString(e), preferencesHelper!!
                )
                view!!.onExceptionResult(e.message)
            }
        }

        private fun descuentosAplicadosRequest() {
            val request = SoapObject(
                ToksWebServicesConnection.NAMESPACE,
                ToksWebServicesConnection.CONSULTAR_DESCUENTOS_APLICADOS
            )
            request.addProperty("Llave", ToksWebServicesConnection.KEY)
            request.addProperty("Cadena", ToksWebServicesConnection.STRING)
            request.addProperty(
                "Unidad",
                Utilities.preferenceHelper!!.getString(ConstantsPreferences.PREF_UNIDAD, null)
            )
            request.addProperty("Folio", Utilities.preferenceHelper!!.getString(ConstantsPreferences.PREF_FOLIO, null))
            Log.d(DescuentosAplicadosPresenter.TAG, "descuentosAplicadosRequest: $request")

            WebServiceTaskCaller.CallWSConsumptionTask(
                request,
                ToksWebServicesConnection.CONSULTAR_DESCUENTOS_APLICADOS
            )
        }

        private fun descuentosAplicadosRequestResult(jsonResult: String) {
            try {

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
                        if(TIPO_DESC=="2")
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

                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
    }

    override fun executeFormasPagoRequest() {
        descuentosRequest()
    }

    override fun executeDescuentosAplicadosRequest() {
        descuentosAplicadosRequest()
    }
}
package com.Model

import android.util.Log
import com.Constants.ConstantsPreferences
import com.Interfaces.DialogVisualizadorMVP
import com.Presenter.DialogVisulizadorPresenter.Companion.preferencesHelper
import com.Presenter.DialogVisulizadorPresenter.Companion.view
import com.Utilities.Utilities
import com.Utilities.Utilities.getJulianDate
import com.Utilities.Utilities.preferenceHelper
import com.View.Fragments.ContentFragment
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

class DialogVisualozadorModel:DialogVisualizadorMVP.Model, CoroutineScope {
    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO
    override fun getMarca(): String {
        return preferenceHelper!!.getString(ConstantsPreferences.PREF_MARCA_SELECCIONADA, null)
    }
    object WebServiceTaskCaller {
        fun CallWSConsumptionTask(request: SoapObject, method: String) {
            val service: ReadJSONFeedTask = object : ReadJSONFeedTask() {
                override fun onResponseReceived(jsonResult: String) {
                    if (ToksWebServiceExceptionRate.rateError(jsonResult) != "") {
                        val exceptionRated = ToksWebServiceExceptionRate.rateError(jsonResult)
                        ContentFragment!!.contentFragment!!.onExceptionVisualizadorResult(exceptionRated)
                    } else {
                        catalogoPayclubRequestResult(jsonResult)
                    }
                }
            }
            service.execute(request, method, preferencesHelper)
        }
    }

    companion object {
        private fun catalogoPayClubRequest() {
            val request = SoapObject(
                ToksWebServicesConnection.NAMESPACE,
                ToksWebServicesConnection.CATALOGO_PAYCLUB
            )
            request.addProperty("Llave", ToksWebServicesConnection.KEY)
            request.addProperty("Cadena", ToksWebServicesConnection.STRING)
            request.addProperty(
                "id_local",
                Utilities.preferenceHelper!!.getString(ConstantsPreferences.PREF_UNIDAD, null)
            )
            request.addProperty(
                "Fecha",
                getJulianDate()
            )


            WebServiceTaskCaller.CallWSConsumptionTask(
                request,
                ToksWebServicesConnection.CATALOGO_PAYCLUB
            )
        }

        private fun catalogoPayclubRequestResult(jsonResult: String) {
            try {
                Log.i("sdsds",jsonResult)
                val cuentas = JSONArray(jsonResult)
                //val cuentas = jsonArray.getJSONArray(0)
                var cuentasList:ArrayList<ArrayList<String>> = ArrayList()

                var header = listOf<String>("MESA","FOLIO","INFORMACION" ,"ACC","TOTAL")
                for(i in 0 until cuentas.length()){
                    val cuenta = cuentas.getJSONObject(i)
                    var cuentaS:ArrayList<String> = ArrayList()
                    for(j in 0..4){
                        cuentaS.add(cuenta.getString(header[j]).trim { it <= ' ' })
                        Log.i("mmmmmm",cuenta.getString(header[j]).trim { it <= ' ' }+"df")
                    }
                    cuentasList.add(cuentaS)
                }
                ContentFragment.contentFragment!!.onSuccessVisualizadorResult(cuentasList)

            } catch (e: JSONException) {
                if(jsonResult.contains("No hay datos"))
                    view!!.onFailVisualizadorResult("No hay datos")
                else
                    view!!.onExceptionVisualizadorResult(e.message!!)
                e.printStackTrace()

            }
        }
    }

    override fun executeCatalogoPayRequest() {
        preferenceHelper = Utilities.preferenceHelper
        catalogoPayClubRequest()
    }

}
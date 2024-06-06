package com.Model

import android.util.Log
import com.Constants.ConstantsPreferences
import com.Interfaces.SeleccionImpresoraMVP
import com.Presenter.ImpresoraPresenter
import com.Presenter.ImprimeTicketPresenter
import com.Utilities.Curl
import com.Utilities.PreferenceHelper
import com.Utilities.Utilities
import com.View.Fragments.ContentFragment
import com.webservicestasks.ReadJSONFeedTask
import com.webservicestasks.ToksWebServiceExceptionRate
import com.webservicestasks.ToksWebServicesConnection
import dagger.Lazy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.json.JSONArray
import org.ksoap2.serialization.SoapObject
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class ImpresoraModel @Inject constructor(private var presenter: Lazy<SeleccionImpresoraMVP.Presenter>, private var preferenceHelper: PreferenceHelper) :  SeleccionImpresoraMVP.Model { //obtener las impresora y para mandar a imprimir


    private val job = Job()
    val coroutineContext: CoroutineContext //  override
        get() = job + Dispatchers.IO


    object WebServiceTaskCaller {
        fun CallWSConsumptionTask(request: SoapObject, method: String) {
            val service: ReadJSONFeedTask = object : ReadJSONFeedTask() {
                override fun onResponseReceived(jsonResult: String) {
                    if (ToksWebServiceExceptionRate.rateError(jsonResult) != "") {
                        val exceptionRated = ToksWebServiceExceptionRate.rateError(jsonResult)
                        ImprimeTicketPresenter.view!!.onExceptionImprimeTicketResult(exceptionRated)
                    } else {

                    }
                }
            }
            service.execute(request, method, ImprimeTicketPresenter.preferenceHelper)
        }
    }

        override fun getPrinters(){
            var params = HashMap<String, String>()
            params["Llave"] = "e936c17f7b54f02d32@@5463e0c3f749080b9"
            params["Cadena"] = ToksWebServicesConnection.STRING
            var reponse = Curl.sendPostRequest(params, "Cat_Impresoras", preferenceHelper, 7)
            val jsonArray = JSONArray(reponse)
            presenter!!.get().getV().onSuccesGetPrint(jsonArray)
        }

    override fun imprimePrinte(id_printer: String?, texto: String?, bandera:Int) {
        var params= HashMap<String,String>()
       var terminal = preferenceHelper.getString(ConstantsPreferences.PREF_NUMERO_TERMINAL, null)
        var folio = ""
        if(bandera==1){//TICKET
            folio =ContentFragment?.cuenta?.folio
        }else if(bandera==2){ //HISTORICO
            folio =ContentFragment.folioHistorico
        }else if(bandera==0){ //ERROR
            folio = ""
        }
        params["Llave"] = "e936c17f7b54f02d32@@5463e0c3f749080b9"
        params["Cadena"] = ToksWebServicesConnection.STRING
        params["id_printer"] = id_printer.toString()
        params["fecha"] = Utilities.getJulianDate()
        params["id_term"] = preferenceHelper.getString(ConstantsPreferences.PREF_NUMERO_TERMINAL, null)
        params["contenido"] = texto.toString()
        params["barcode"] = folio
        var reponse = Curl.sendPostRequest(params,"Envia_impresion", preferenceHelper,7)
        Log.i("***Request", reponse)
        Log.i("***Response", reponse)
        Log.i("****bandera", bandera.toString())
        Log.i("****folio", folio)
        Log.i("****texto", texto.toString())
        Log.i("****id_terminal", terminal)
        Log.i("****response", reponse)
        Log.i("****id_printer", id_printer.toString())
        Log.i("****Contenido", texto.toString())
        ImpresoraPresenter.view!!.onResultPrint("Se realizo correctamente la impresion")
    }

    fun excuteObtenerImpresoras() {
            getPrinters()
        }

}






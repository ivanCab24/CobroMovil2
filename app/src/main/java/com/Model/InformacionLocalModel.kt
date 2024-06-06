package com.Model

import com.Constants.ConstantsPreferences
import com.Interfaces.InformacionLocalMVP
import com.Presenter.InformacionLocalPresenter
import com.webservicestasks.ReadJSONFeedTask
import com.webservicestasks.ToksWebServiceExceptionRate
import com.webservicestasks.ToksWebServicesConnection
import org.ksoap2.serialization.SoapObject

class InformacionLocalModel:InformacionLocalMVP.Model {
    private object WebServiceTaskCaller {
        fun CallWSConsumptionTask(request: SoapObject, method: String) {
            val service: ReadJSONFeedTask = object : ReadJSONFeedTask() {
                override fun onResponseReceived(jsonResult: String) {
                    InformacionLocalPresenter.filesWeakReference!!.get()!!.registerLogs(
                        "Termina CATALOGO_LOCAL",
                        jsonResult,
                        InformacionLocalPresenter.preferenceHelperLogs!!,
                        InformacionLocalPresenter.preferenceHelper!!
                    )
                    if (ToksWebServiceExceptionRate.rateError(jsonResult) != "") {
                        val exceptionResult = ToksWebServiceExceptionRate.rateError(jsonResult)
                    } else {
                        informacionLocalRequestResult(jsonResult)
                    }
                }
            }
            service.execute(request, method, InformacionLocalPresenter.preferenceHelper)
        }
    }
    companion object{
        private fun informacionLocalRequest() {
            val request = SoapObject(
                ToksWebServicesConnection.NAMESPACE,
                ToksWebServicesConnection.CATALOGO_LOCAL
            )
            request.addProperty("Llave", ToksWebServicesConnection.KEY)
            request.addProperty("Cadena", ToksWebServicesConnection.STRING)
            request.addProperty(
                "id_local",
                InformacionLocalPresenter.preferenceHelper!!.getString(ConstantsPreferences.PREF_UNIDAD, null)
            )
            InformacionLocalPresenter.filesWeakReference!!.get()!!.registerLogs(
                "Inicia CATALOGO_LOCAL",
                request.toString(),
                InformacionLocalPresenter.preferenceHelperLogs!!,
                InformacionLocalPresenter.preferenceHelper!!
            )
            WebServiceTaskCaller.CallWSConsumptionTask(
                request,
                ToksWebServicesConnection.CATALOGO_LOCAL
            )
        }

        private fun informacionLocalRequestResult(jsonResult: String) {
            if (!jsonResult.contains("CODIGO")) {
                InformacionLocalPresenter.preferenceHelper!!.putString(ConstantsPreferences.PREF_INFO_LOCAL, jsonResult)
            }
        }
    }

    override fun executeInformacionLocalRequest() {
        informacionLocalRequest()
    }
}
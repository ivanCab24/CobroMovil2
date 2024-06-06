/*
 * *
 *  * Created by Gerardo Ruiz on 11/26/20 10:54 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 11/26/20 10:54 PM
 *
 */
package com.Controller

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.util.Log
import com.Constants.ConstantsPreferences
import com.Interfaces.ConsultaPagoMercadoPagoMVP
import com.Presenter.ConsultaPagoMercadoPagoPresenter
import com.Utilities.Files
import com.Utilities.PreferenceHelper
import com.Utilities.PreferenceHelperLogs
import com.webservicestasks.ReadJSONFeedTask
import com.webservicestasks.ToksWebServiceExceptionRate
import com.webservicestasks.ToksWebServicesConnection
import org.json.JSONArray
import org.json.JSONException
import org.ksoap2.serialization.SoapObject
import java.lang.ref.WeakReference

class ConsultaPagoMPModel : ToksWebServicesConnection,
    ConsultaPagoMercadoPagoMVP.Model {

    override fun executeConsultaPagoRequest() {
        consultaPagoRequest()
    }

    object WebServiceTaskCaller {
        fun CallWSConsumptionTask(request: SoapObject, method: String) {
            val service: ReadJSONFeedTask = object : ReadJSONFeedTask() {
                @SuppressLint("StaticFieldLeak")
                override fun onResponseReceived(jsonResult: String) {
                    if (ToksWebServiceExceptionRate.rateError(jsonResult) != "") {
                        val exceptionResult = ToksWebServiceExceptionRate.rateError(jsonResult)
                        view!!.onExceptionConsultaPagoMercadoPagoResult(
                            exceptionResult
                        )
                    } else {
                        consultaPagoMercadoPagoRequestResult(
                            jsonResult
                        )
                    }
                }
            }
            service.execute(
                request,
                method,
                ConsultaPagoMercadoPagoPresenter.preferenceHelper
            )
        }
    }

    companion object {
        private const val TAG = "ConsultaPagoMercadoPago"
        private var view: ConsultaPagoMercadoPagoMVP.View? = ConsultaPagoMercadoPagoPresenter.view
        private var getSharedPreferences: SharedPreferences? = null
        private var preferenceHelper: PreferenceHelper? =
            ConsultaPagoMercadoPagoPresenter.preferenceHelper
        private var preferenceHelperLogs: PreferenceHelperLogs? =
            ConsultaPagoMercadoPagoPresenter.preferenceHelperLogs
        private var filesWeakReference: WeakReference<Files?>? =
            ConsultaPagoMercadoPagoPresenter.filesWeakReference

        private fun consultaPagoRequest() {
            val request = SoapObject(
                ToksWebServicesConnection.NAMESPACE,
                ToksWebServicesConnection.CONSULTA_PAGO_MP
            )
            request.addProperty("Llave", ToksWebServicesConnection.KEY)
            request.addProperty(
                "folio",
                ConsultaPagoMercadoPagoPresenter.preferenceHelper!!.getString(
                        ConstantsPreferences.PREF_FOLIO,
                        null
                )
            )
            Log.i(
                TAG,
                "consultaPagoRequest: $request"
            )
            ConsultaPagoMercadoPagoPresenter.filesWeakReference!!.get()!!.registerLogs(
                "Inicia BuscaPagoMP",
                request.toString(),
                ConsultaPagoMercadoPagoPresenter.preferenceHelperLogs!!,
                ConsultaPagoMercadoPagoPresenter.preferenceHelper!!
            )
            WebServiceTaskCaller.CallWSConsumptionTask(
                request,
                ToksWebServicesConnection.CONSULTA_PAGO_MP
            )
        }

        private fun consultaPagoMercadoPagoRequestResult(jsonResult: String) {
            try {
                val jsonArrayMain = JSONArray("[$jsonResult]")
                val jsonObjectMain = jsonArrayMain.getJSONObject(0)
                val result = jsonObjectMain.getString("results")
                val jsonArrayResult = JSONArray(result)
                if (jsonArrayResult.length() != 0) {
                    val jsonObjectResult = jsonArrayResult.getJSONObject(0)
                    val autorizacion = jsonObjectResult.getString("authorization_code")
                    ConsultaPagoMercadoPagoPresenter.view!!.onSuccessConsultaPagoMercadoPagoResult(
                        autorizacion
                    )
                } else {
                    ConsultaPagoMercadoPagoPresenter.view!!.onFailConsultaPagoMercadoPagoResult(
                        "Aun no se encuentra"
                    )
                }
            } catch (exception: JSONException) {
                exception.printStackTrace()
                ConsultaPagoMercadoPagoPresenter.filesWeakReference!!.get()!!
                    .createFileException(
                        "Controller/ConsultaPagoMercadoPago " + Log.getStackTraceString(exception),
                        ConsultaPagoMercadoPagoPresenter.preferenceHelper!!
                    )
                ConsultaPagoMercadoPagoPresenter.view!!.onExceptionConsultaPagoMercadoPagoResult(
                    exception.message
                )
            }
        }
    }
}
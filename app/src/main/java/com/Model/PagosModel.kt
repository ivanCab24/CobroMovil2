package com.Model

import android.util.Log
import android.view.View
import com.Constants.ConstantsPreferences
import com.DataModel.Pagos
import com.Interfaces.PagosMVP
import com.Presenter.PagosPresenter
import com.Presenter.PagosPresenter.Companion.TAG
import com.Presenter.PagosPresenter.Companion.filesWeakReference
import com.Presenter.PagosPresenter.Companion.fragmentContentBindingWeakReference
import com.Presenter.PagosPresenter.Companion.pagosArrayList
import com.Presenter.PagosPresenter.Companion.preferenceHelperLogs
import com.Presenter.PagosPresenter.Companion.preferencesHelper
import com.Presenter.PagosPresenter.Companion.view
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

class PagosModel : PagosMVP.Model, CoroutineScope {
    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    object WebServiceTaskCaller {
        fun CallWSConsumptionTask(request: SoapObject, method: String) {
            val service: ReadJSONFeedTask = object : ReadJSONFeedTask() {
                override fun onResponseReceived(jsonResult: String) {
                    if (ToksWebServiceExceptionRate.rateError(jsonResult) != "") {
                        val exceptionRated = ToksWebServiceExceptionRate.rateError(jsonResult)
                        view!!.onExceptionPagosResult(exceptionRated)
                    } else {
                        pagosRequestResult(jsonResult)
                    }
                }
            }
            service.execute(request, method, preferencesHelper)
        }
    }

    companion object {
        private fun pagosRequest() {
            val request = SoapObject(
                ToksWebServicesConnection.NAMESPACE,
                ToksWebServicesConnection.PIDE_PAGOS
            )
            request.addProperty("Llave", ToksWebServicesConnection.KEY)
            request.addProperty("Cadena", ToksWebServicesConnection.STRING)
            request.addProperty(
                "Folio",
                preferencesHelper!!.getString(ConstantsPreferences.PREF_FOLIO, null)
            )
            request.addProperty(
                "Unidad",
                preferencesHelper!!.getString(ConstantsPreferences.PREF_UNIDAD, null)
            )
            Log.d(TAG, "pagosRequest: $request")
            PagosPresenter.filesWeakReference!!.get()!!.registerLogs(
                "Inicia PidePagos",
                request.toString(),
                preferenceHelperLogs!!,
                preferencesHelper!!
            )
            WebServiceTaskCaller.CallWSConsumptionTask(
                request,
                ToksWebServicesConnection.PIDE_PAGOS
            )
        }

        private fun pagosRequestResult(jsonResult: String) {
            try {
                filesWeakReference!!.get()!!.registerLogs(
                    "Termina PidePagos",
                    jsonResult,
                    preferenceHelperLogs!!,
                    preferencesHelper!!
                )
                pagosArrayList.clear()
                val jsonArray = JSONArray(jsonResult)
                val codigo = jsonArray.getJSONObject(0).getString("CODIGO").toUpperCase()
                if (codigo == "OK") {
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val fecha = jsonObject.getInt("FECHA")
                        val idlocal = jsonObject.getInt("ID_LOCAL")
                        val idTerm = jsonObject.getInt("ID_TERM")
                        val idComa = jsonObject.getInt("ID_COMA")
                        val idPos = jsonObject.getInt("ID_POS")
                        val idFpgo = jsonObject.getInt("ID_FPGO")
                        val formaPago = jsonObject.getString("FORMA_PAGO")
                        val abonoPropina = jsonObject.getDouble("ABONO_PROPINA")
                        val abonoMn = jsonObject.getDouble("ABONO_MN")
                        val contador = jsonObject.getInt("CONTADOR")
                        val cancelado = jsonObject.getInt("CANCELADO") == 1
                        val ticket = jsonObject.getString("TICKET")
                        val tc_autorizacion = jsonObject.getString("TC_AUTORIZACION")
                        val referencia = jsonObject.getString("REFERENCIA")
                        pagosArrayList.add(
                            Pagos(
                                fecha,
                                idlocal,
                                idTerm,
                                idComa,
                                idPos,
                                idFpgo,
                                formaPago,
                                abonoPropina,
                                abonoMn,
                                contador,
                                cancelado,
                                ticket,
                                tc_autorizacion,
                                referencia
                            )
                        )
                        updateUI()
                    }
                } else {
                    view!!.onFailPagosResult(codigo)
                }
            } catch (e: JSONException) {
                e.printStackTrace()
                filesWeakReference!!.get()!!.createFileException(
                    "Controller/PagosRequest/pagosRequestResult " + jsonResult +
                            " " + Log.getStackTraceString(e), preferencesHelper!!
                )
                view!!.onExceptionPagosResult(e.message)
            }
        }

        private fun updateUI() {
            var ban = true
            fragmentContentBindingWeakReference!!.get()!!.recyclerViewDiscounts.visibility =
                View.VISIBLE
            for (item in pagosArrayList) {
                if (item!!.isCancelado) {
                    ban = false
                } else {
                    ban = true
                    if (fragmentContentBindingWeakReference!!.get()!!.recyclerViewDiscounts.visibility == View.INVISIBLE) PagosPresenter.fragmentContentBindingWeakReference!!.get()!!.recyclerViewDiscounts.visibility =
                        View.VISIBLE
                    break
                }
            }
            if (ban) fragmentContentBindingWeakReference!!.get()!!.buttonNoTip.visibility =
                View.INVISIBLE else view!!.onSuccessPagosResult(
                pagosArrayList
            )
            view!!.onSuccessPagosResult(pagosArrayList)
        }
    }

    override fun executeFormasPagoRequest() {
        pagosRequest()
    }
}
package com.Model

import android.annotation.SuppressLint
import android.util.Log
import com.Constants.ConstantsAComer
import com.Constants.ConstantsPreferences
import com.DataModel.ACCException
import com.DataModel.BusquedaTransaccionRSSN
import com.Interfaces.RedencionDePuntosMVP
import com.Presenter.RedencionDePuntosPresenter.Companion.filesWeakReference
import com.Presenter.RedencionDePuntosPresenter.Companion.preferenceHelper
import com.Presenter.RedencionDePuntosPresenter.Companion.preferenceHelperLogs
import com.Presenter.RedencionDePuntosPresenter.Companion.view
import com.Utilities.Utils
import com.View.Fragments.ContentFragment
import com.squareup.moshi.Moshi
import com.webservicestasks.ReadJSONFeedTask
import com.webservicestasks.ToksWebServiceErrors
import com.webservicestasks.ToksWebServiceExceptionRate
import com.webservicestasks.ToksWebServicesConnection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.json.JSONArray
import org.json.JSONException
import org.ksoap2.serialization.SoapObject
import kotlin.coroutines.CoroutineContext

class RedencionPuntosModel : RedencionDePuntosMVP.Model, CoroutineScope {
    private val job = Job()

    private object WebServiceTaskCaller {
        fun CallWSConsumptionTask(request: SoapObject, method: String) {
            val service: ReadJSONFeedTask = object : ReadJSONFeedTask() {
                @SuppressLint("StaticFieldLeak")
                override fun onResponseReceived(jsonResult: String) {
                    val jsonResult2 = "[$jsonResult]"
                    if (ToksWebServiceExceptionRate.rateError(jsonResult) != "") {
                        if (jsonResult.contains(ToksWebServiceErrors.TIMEOUT) || jsonResult.contains(
                                ToksWebServiceErrors.TIMEOUT_READ_JSON
                            )
                        ) {
                            Log.i(TAG, "onResponseReceived: Validacion correcta ")
                            if (method == REDENCION_PUNTOS) buscarTransaccionRSSNRequest() else showException(
                                jsonResult
                            )
                        } else {
                            showException(jsonResult)
                        }
                    } else if (method == REDENCION_PUNTOS) {
                        redencionDePuntosRequestResult(jsonResult2)
                    } else if (method == BUSCAR_TRANSACCION_RSSN) {
                        val membresia = ContentFragment.miembro!!.response.membresia
                        var mensaje = ""
                        if (membresia != null) {
                            val puntosActuales =
                                membresia.puntosNoCalificados - mPuntosARedimir
                            membresia.puntosNoCalificados = puntosActuales
                            mensaje =
                                membresia.nombre + " tiene " + membresia.puntosNoCalificados + " puntos"
                        }
                        view!!.onSuccessRedencionDePuntosResult(
                            "Redención de puntos correcta.",
                            mensaje
                        )
                        //buscarTransaccionRSSNRequestResult(jsonResult)
                    }
                }
            }
            service.execute(request, method, preferenceHelper)
        }
    }

    companion object {
        private const val TAG = "RedencionDePuntosReques"
        private var mPuntosARedimir = 0
        private var mMembresia = ""
        private val moshi = Moshi.Builder().build()

        private fun validaInformacion(puntosARedimir: String?): Boolean {
            val membresia = ContentFragment.miembro!!.response.membresia
            return if (puntosARedimir!!.isEmpty()) {
                view!!.onWarningRedencionDePuntosResult("Ingrese el numero de puntos a redimir")
                false
            } else if (membresia != null) {
                if (puntosARedimir.toInt() > membresia.puntosNoCalificados) {
                    view!!.onWarningRedencionDePuntosResult("Monto excedido")
                    false
                }  else {
                    true
                }
            } else {
                view!!.onWarningRedencionDePuntosResult("Error al generar membresia.\nVuelva a consultarla")
                false
            }
        }

        private fun redencionDePuntosRequest(puntosARedimir: String?, nip: String?) {
            val membresia = ContentFragment.miembro!!.response.membresia
            if (membresia != null) {
                mMembresia = membresia.numeroMembresia
                mPuntosARedimir = puntosARedimir!!.toInt()
                val marca =
                    preferenceHelper!!.getString(ConstantsPreferences.PREF_MARCA_SELECCIONADA, null)
                val request = SoapObject(
                    ToksWebServicesConnection.NAMESPACE,
                    ToksWebServicesConnection.REDENCION_PUNTOS
                )
                request.addProperty("Llave", ToksWebServicesConnection.KEY)
                request.addProperty("nombre", membresia.nombre)
                request.addProperty("correo", membresia.email)
                request.addProperty("membresia", membresia.numeroMembresia)
                request.addProperty(
                    "ticket",
                    preferenceHelper!!.getString(ConstantsPreferences.PREF_FOLIO, null)
                )
                request.addProperty("puntos", puntosARedimir)
                request.addProperty("marca", Utils.evaluarMarcaSeleccionada(marca))
                request.addProperty("Nip", nip)
                request.addProperty(
                    "numeroTicketBase",
                    preferenceHelper!!.getString(ConstantsPreferences.PREF_FOLIO, null)
                )
                Log.d(TAG, "redencionDePuntosRequest: $request")
                filesWeakReference!!.get()!!.registerLogs(
                    "Inicia RedencionPuntos",
                    request.toString(),
                    preferenceHelperLogs!!,
                    preferenceHelper!!
                )
                WebServiceTaskCaller.CallWSConsumptionTask(
                    request,
                    ToksWebServicesConnection.REDENCION_PUNTOS
                )
            } else {
                view!!.onExceptionRedencionDePuntosResult("Error al generar membresia.\nVuelva a consultarla")
            }
        }

        private fun redencionDePuntosRequestResult(jsonResult2: String) {
            try {
                filesWeakReference!!.get()!!.registerLogs(
                    "Termina RedencionPuntos",
                    jsonResult2,
                    preferenceHelperLogs!!,
                    preferenceHelper!!
                )
                if (jsonResult2.contains("code")) {
                    val jsonArray = JSONArray(jsonResult2)
                    val code = jsonArray.getJSONObject(0).getString("code")
                    val exceptionMessage = jsonArray.getJSONObject(0).getString("exceptionMessage")
                    if (code == "200"||code == "0") {
                        if (exceptionMessage == "") {
                            val response = jsonArray.getJSONObject(0).getString("response")
                            val jsonArrayRes = JSONArray("[$response]")
                            val estatus = jsonArrayRes.getJSONObject(0).getString("estatus")
                            val subEstatus = jsonArrayRes.getJSONObject(0).getString("subEstatus")
                            if (estatus == ConstantsAComer.ESTATUS_SUCCESS) {
                                val membresia = ContentFragment.miembro!!.response.membresia
                                var mensaje = ""
                                if (membresia != null) {
                                    val puntosActuales = ContentFragment.miembro!!.response.membresia!!.puntosNoCalificados - mPuntosARedimir
                                    ContentFragment.miembro!!.response.membresia!!.puntosNoCalificados = puntosActuales
                                    mensaje = membresia.nombre + " tiene " + membresia.puntosNoCalificados + " puntos"
                                }
                                view!!.onSuccessRedencionDePuntosResult(
                                    "Redención de puntos correcta.",
                                    mensaje
                                )
                            } else if (estatus == ConstantsAComer.ESTATUS_ERROR && subEstatus == ConstantsAComer.SUB_ESTATUS_ERROR_PUNTOS_INSUFICIENTES) {
                                view!!.onFailRedencionDePuntosResult("Puntos insuficientes para realizar la redención solicitada.")
                            } else {
                                view!!.onFailRedencionDePuntosResult(
                                    "Erro al redimir puntos $estatus\n$subEstatus"
                                )
                            }
                        } else {
                            view!!.onFailRedencionDePuntosResult(exceptionMessage)
                        }
                    } else {
                        buscarTransaccionRSSNRequest()
                    }
                } else {
                    buscarTransaccionRSSNRequest()
                }
            } catch (e: JSONException) {
                e.printStackTrace()
                filesWeakReference!!.get()!!.createFileException(
                    "Controller/RedencionDePuntosRequest/redencionDePuntosRequestResult " + jsonResult2 +
                            " " + Log.getStackTraceString(e), preferenceHelper!!
                )
                view!!.onExceptionRedencionDePuntosResult(e.message)
            }
        }

        private fun buscarTransaccionRSSNRequest() {
            val ticket = preferenceHelper!!.getString(ConstantsPreferences.PREF_FOLIO, null)
            val request = SoapObject(
                ToksWebServicesConnection.NAMESPACE,
                ToksWebServicesConnection.BUSCAR_TRANSACCION_RSSN
            )
            request.addProperty("Llave", ToksWebServicesConnection.KEY)
            request.addProperty("ticket", ticket)
            filesWeakReference!!.get()!!.registerLogs(
                "Inicia Busqueda RSSN",
                request.toString(),
                preferenceHelperLogs!!,
                preferenceHelper!!
            )
            WebServiceTaskCaller.CallWSConsumptionTask(
                request,
                ToksWebServicesConnection.BUSCAR_TRANSACCION_RSSN
            )
        }

        private fun buscarTransaccionRSSNRequestResult(jsonResult: String) {
            filesWeakReference!!.get()!!.registerLogs(
                "Termina Busqueda RSSN",
                jsonResult,
                preferenceHelperLogs!!,
                preferenceHelper!!
            )
            if (jsonResult.contains("code")) {
                val jsonAdapter = moshi.adapter(
                    BusquedaTransaccionRSSN::class.java
                )
                try {
                    val busquedaTransaccionRSSN = jsonAdapter.fromJson(jsonResult)
                    if (busquedaTransaccionRSSN != null) {
                        val membresia = ContentFragment.miembro!!.response.membresia
                        if (ContentFragment.miembro!!.response.membresia != null) {
                            var response: BusquedaTransaccionRSSN.Response? = null
                            for (i in busquedaTransaccionRSSN.response.indices) {
                                val (memberNumber, _, _, puntos, _, _, status, substatus) = busquedaTransaccionRSSN.response[i]
                                if (memberNumber == mMembresia && puntos == mPuntosARedimir.toString()) {
                                    if (status == ConstantsAComer.ESTATUS_SUCCESS && substatus == ConstantsAComer.SUB_ESTATUS_SUCCESS) {
                                        response = busquedaTransaccionRSSN.response[i]
                                    }
                                }
                            }
                            if (true){//response != null) {
                                var mensaje = ""
                                if (membresia != null) {
                                    val puntosActuales =
                                        membresia.puntosNoCalificados - mPuntosARedimir
                                    membresia.puntosNoCalificados = puntosActuales
                                    mensaje =
                                        membresia.nombre + " tiene " + membresia.puntosNoCalificados + " puntos"
                                }
                                view!!.onSuccessRedencionDePuntosResult(
                                    "Redención de puntos correcta.",
                                    mensaje
                                )
                            } else {
                                view!!.onFailRedencionDePuntosResult("No se encontro ninguna transaccion RSSN que coincida. \nLlamar a soporte")
                            }
                        } else {
                            view!!.onFailRedencionDePuntosResult("No se pudo realizar la busqueda RSSN. \n Llamar a soporte.")
                        }
                    } else {
                        view!!.onFailRedencionDePuntosResult("No se pudo realizar la busqueda RSSN. \n Llamar a soporte.")
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    filesWeakReference!!.get()!!.createFileException(
                        "Controller/RedencionDePuntosRequest/buscarTransaccionRSSNRequestResult " + "Json: " + jsonResult
                                + " " + Log.getStackTraceString(e), preferenceHelper!!
                    )
                    view!!.onExceptionRedencionDePuntosResult(e.message)
                }
            } else {
                try {
                    val jsonAdapter = moshi.adapter(
                        ACCException::class.java
                    )
                    val accException = jsonAdapter.fromJson(jsonResult)
                    if (accException != null) view!!.onFailRedencionDePuntosResult(accException.ExceptionMessage) else view!!.onFailRedencionDePuntosResult(
                        "Error al generar respues RSSN. \n Llamar a soporte."
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                    filesWeakReference!!.get()!!.createFileException(
                        "Controller/RedencionDePuntosRequest/buscarTransaccionRSSNRequestResult " + "Json: " + jsonResult
                                + " " + Log.getStackTraceString(e), preferenceHelper!!
                    )
                    view!!.onExceptionRedencionDePuntosResult(e.message)
                }
            }
        }

        private fun showException(jsonResult: String) {
            val exceptionResult = ToksWebServiceExceptionRate.rateError(jsonResult)
            view!!.onExceptionRedencionDePuntosResult(exceptionResult)
        }
    }

    override fun executeRedencionPRequest(puntosARedimir: String?, nip: String?) {
        redencionDePuntosRequest(puntosARedimir, nip)
    }

    override fun executeValidaInformacion(puntosARedimir: String?): Boolean {
        return validaInformacion(puntosARedimir)
    }

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO
}
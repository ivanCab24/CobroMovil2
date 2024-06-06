package com.Model

import android.util.Log
import com.Constants.ConstantsAComer
import com.Constants.ConstantsPreferences
import com.Interfaces.RedencionCuponMVP
import com.Presenter.RedencionCuponPresenter.Companion.TAG
import com.Presenter.RedencionCuponPresenter.Companion.filesWeakReference
import com.Presenter.RedencionCuponPresenter.Companion.formatter
import com.Presenter.RedencionCuponPresenter.Companion.preferenceHelper
import com.Presenter.RedencionCuponPresenter.Companion.preferenceHelperLogs
import com.Presenter.RedencionCuponPresenter.Companion.view
import com.Utilities.Utils
import com.View.Fragments.ContentFragment
import com.webservicestasks.ReadJSONFeedTask
import com.webservicestasks.ToksWebServiceExceptionRate
import com.webservicestasks.ToksWebServicesConnection
import org.json.JSONException
import org.json.JSONObject
import org.ksoap2.serialization.SoapObject

class RedencionCuponModel: RedencionCuponMVP.Model {
    object WebServiceTaskCaller {
        fun CallWSConsumptionTask(request: SoapObject, method: String) {
            val service: ReadJSONFeedTask = object : ReadJSONFeedTask() {
                override fun onResponseReceived(jsonResult: String) {
                    Log.i(TAG, "onResponseReceived: $jsonResult")
                    if (ToksWebServiceExceptionRate.rateError(jsonResult) != "") {
                        val exceptionResult = ToksWebServiceExceptionRate.rateError(jsonResult)
                        view!!.onExceptionRedencionCuponResult(exceptionResult)
                    } else {
                        if (method == REDIME_CUPON) {
                            redimeCuponRequestResult(jsonResult)
                        }
                        if (method == APLICA_DESCUENTO) {
                            filesWeakReference!!.get()!!.registerLogs(
                                "Termina APLICA_DESCUENTO CUPON",
                                jsonResult,
                                preferenceHelperLogs!!,
                                preferenceHelper!!
                            )
                            view!!.onSuccessRedencionCuponDescuentoResult()
                        }
                    }
                }

            }
            service.execute(request, method, preferenceHelper)
        }
    }

    companion object{
        private fun redimeCuponRequest(nip: String?) {
            val marca = Utils.evaluarMarcaSeleccionada(
                preferenceHelper!!.getString(ConstantsPreferences.PREF_MARCA_SELECCIONADA, null)
            )
            val membresia = ContentFragment.miembro!!.response.membresia
            if (membresia != null) {
                val request = SoapObject(
                    ToksWebServicesConnection.NAMESPACE,
                    ToksWebServicesConnection.REDIME_CUPON
                )
                request.addProperty("Llave", ToksWebServicesConnection.KEY)
                request.addProperty("nombre", membresia.nombre)
                request.addProperty("membresia", membresia.numeroMembresia)
                request.addProperty("correo", membresia.numeroMembresia)
                request.addProperty(
                    "ticket",
                    preferenceHelper!!.getString(ConstantsPreferences.PREF_FOLIO, null)
                )
                request.addProperty("monto", formatter.format(ContentFragment.cuenta.saldo * .10))
                request.addProperty("puntos", "0")
                request.addProperty("producto", ContentFragment.cuponMiembro!!.codigoCupon)
                request.addProperty("cupon", ContentFragment.cuponMiembro!!.numeroCupon)
                request.addProperty("marca", marca)
                request.addProperty("Nip", nip)
                request.addProperty(
                    "numeroTicketBase",
                    preferenceHelper!!.getString(ConstantsPreferences.PREF_FOLIO, null)
                )
                Log.i(TAG, "WSTaskRedimeCupon: $request")
                filesWeakReference!!.get()!!.registerLogs(
                    "Inicia REDIME_CUPON",
                    request.toString(),
                    preferenceHelperLogs!!,
                    preferenceHelper!!
                )
                WebServiceTaskCaller.CallWSConsumptionTask(
                    request,
                    ToksWebServicesConnection.REDIME_CUPON
                )
            } else {
                view!!.onExceptionRedencionCuponResult("Error al generar membresia.\nVuelva a consultarla")
            }
        }

        private fun redimeCuponRequestResult(jsonResult: String) {
            try {
                if (jsonResult.contains("code")) {
                    val jsonObjectMain = JSONObject(jsonResult)
                    val exceptionMessage = jsonObjectMain.getString("exceptionMessage")
                    if (exceptionMessage.isEmpty()) {
                        val response = jsonObjectMain.getString("response")
                        val jsonObject = JSONObject(response)
                        val estatus = jsonObject.getString("estatus")
                        val subEstatus = jsonObject.getString("subEstatus")
                        if (estatus == ConstantsAComer.ESTATUS_SUCCESS && subEstatus == ConstantsAComer.SUB_ESTATUS_SUCCESS) {
                            ContentFragment.cuponMiembro = null
                            view!!.onSuccessRedencionCuponResult("Redención de cupón correcta.")
                            aplicaDescuentoRequest()
                        } else {
                            view!!.onFailRedencionCuponResult("Fallo la redención de cupón.\nEstatus $estatus SubEstatus $subEstatus")
                        }
                    } else {
                        view!!.onFailRedencionCuponResult("Fallo la redención de cupón.\n$exceptionMessage")
                    }
                } else {
                    val jsonObject = JSONObject(jsonResult)
                    val code = jsonObject.getString("Code")
                    if (code != "0") {
                        val message = jsonObject.getString("ExceptionMessage")
                        view!!.onFailRedencionCuponResult(message)
                    }
                }
            } catch (e: JSONException) {
                e.printStackTrace()
                filesWeakReference!!.get()!!.createFileException(
                    "Controller/RedencionCuponReuquest/RedencionCupon " + "Json: " + jsonResult + e.message,
                    preferenceHelper!!
                )
                //chequeo de cupones
                System.out.println("resultado json cupon" + jsonResult)
                view!!.onExceptionRedencionCuponResult("Error al leer la respuesta del servidor.")
            }
        }

        private fun aplicaDescuentoRequest() {
            val request = SoapObject(
                ToksWebServicesConnection.NAMESPACE,
                ToksWebServicesConnection.APLICA_DESCUENTO
            )
            request.addProperty("Llave", ToksWebServicesConnection.KEY)
            request.addProperty("Cadena", ToksWebServicesConnection.STRING)
            request.addProperty(
                "Folio",
                preferenceHelper!!.getString(ConstantsPreferences.PREF_FOLIO, null)
            )
            request.addProperty(
                "Unidad", preferenceHelper!!.getString(ConstantsPreferences.PREF_UNIDAD, null)
            )
            request.addProperty("Tipo", ContentFragment.descuentoCuponAComer!!.tipo)
            request.addProperty("Id_Desc", ContentFragment.descuentoCuponAComer!!.idDesc)
            request.addProperty("Descripcion", ContentFragment.descuentoCuponAComer!!.descripcion)
            request.addProperty(
                "Porcentaje",
                ContentFragment.descuentoCuponAComer!!.porcentaje.toString()
            )
            request.addProperty(
                "Referencia",
                ContentFragment.miembro!!.response.membresia!!.numeroMembresia
            )
            request.addProperty(
                "Usuario",
                preferenceHelper!!.getString(ConstantsPreferences.PREF_ESTAFETA, null)
            )
            request.addProperty(
                "Empleado",
                preferenceHelper!!.getString(ConstantsPreferences.PREF_NOMBRE_EMPLEADO, null)
            )
            Log.i(TAG, "WSTaskAplicaDescuento: $request")
            filesWeakReference!!.get()!!.registerLogs(
                "Inicia APLICA_DESCUENTO Cupon",
                request.toString(),
                preferenceHelperLogs!!,
                preferenceHelper!!
            )
            WebServiceTaskCaller.CallWSConsumptionTask(
                request,
                ToksWebServicesConnection.APLICA_DESCUENTO
            )
        }
    }

    override fun executeRedimeCuponRequest(nip: String?) {
        redimeCuponRequest(nip)
    }
}

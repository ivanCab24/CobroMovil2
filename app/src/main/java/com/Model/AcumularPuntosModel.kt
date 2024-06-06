package com.Model

import android.util.Log
import com.Constants.ConstantsAComer
import com.Constants.ConstantsPreferences
import com.Controller.BD.Entity.TicketNoInsertados
import com.DataModel.ACCException
import com.DataModel.AcumulacionPuntosACC
import com.DataModel.MiembroAuxiliar
import com.Interfaces.AcumularPuntosMVP
import com.Presenter.AcumularPuntosPresenter
import com.Presenter.AcumularPuntosPresenter.Companion.preferenceHelper
import com.Utilities.Utils
import com.View.Fragments.ContentFragment
import com.webservicestasks.ReadJSONFeedTask
import com.webservicestasks.ToksWebServiceErrors
import com.webservicestasks.ToksWebServiceExceptionRate
import com.webservicestasks.ToksWebServicesConnection
import org.json.JSONArray
import org.json.JSONException
import org.ksoap2.serialization.SoapObject

class AcumularPuntosModel:AcumularPuntosMVP.Model {
    private object WebServiceTaskCaller {
        fun CallWSConsumptionTask(request: SoapObject, method: String) {
            val service: ReadJSONFeedTask = object : ReadJSONFeedTask() {
                override fun onResponseReceived(jsonResult: String) {
                    if (ToksWebServiceExceptionRate.rateError(jsonResult) == "") {
                        if ((!jsonResult.contains(ToksWebServiceErrors.TIMEOUT) || jsonResult.contains(
                                ToksWebServiceErrors.TIMEOUT_READ_JSON)
                            )
                        ) {
                            if (method == BUSCAR_TRANSACCION_ASSN) {
                                buscarAcumulacionRequest()
                            } else if (method == ACUMULA_PUNTOS) {
                                acumulaPuntosRequestResult(jsonResult)
                            } else if (method == BUSCAR_TRANSACCION_ASSN) {
                                buscarAcumulacionRequestResult(jsonResult)
                            } else {
                                showException(jsonResult)
                            }
                        }else {
                            showException(jsonResult)
                        }
                    }else {
                        showException(jsonResult)
                    }
                }
            }
            service.execute(request, method, AcumularPuntosPresenter.preferenceHelper)
        }
    }

    companion object{
        private fun acumularPuntosRequest() {
            if (ContentFragment.miembro != null) {
                val membresia = ContentFragment.miembro!!.response.membresia
                val marca =
                    preferenceHelper!!.getString(ConstantsPreferences.PREF_MARCA_SELECCIONADA, null)
                val marcaAux = Utils.evaluarMarcaSeleccionada(marca)
                val ticket = AcumularPuntosPresenter.preferenceHelper!!.getString(
                        ConstantsPreferences.PREF_FOLIO,
                        null)
                val monto =ContentFragment.cuenta.total.toInt()
                val fechaConsumo = Utils.dateAndTimeAComer()
                val request = SoapObject(
                    ToksWebServicesConnection.NAMESPACE,
                    ToksWebServicesConnection.ACUMULA_PUNTOS
                )
                request.addProperty("Llave", ToksWebServicesConnection.KEY)
                request.addProperty("nombre", membresia!!.nombre)
                request.addProperty("correo", membresia.email)
                request.addProperty("membresia", membresia.numeroMembresia)
                request.addProperty("ticket", ticket)
                request.addProperty("monto", monto)
                request.addProperty("marca", marcaAux)
                request.addProperty("numeroTicketBase", ticket)
                Log.d(AcumularPuntosPresenter.TAG, "WSTaskAcumulaPuntos: $request")
                AcumularPuntosPresenter.miembroAuxiliar = MiembroAuxiliar(
                    ticket, monto, "", "", "", marcaAux, fechaConsumo
                )
                AcumularPuntosPresenter.filesWeakReference!!.get()!!.registerLogs(
                    "Inicia AcumulaPuntos",
                    request.toString(),
                    AcumularPuntosPresenter.preferenceHelperLogs!!,
                    AcumularPuntosPresenter.preferenceHelper!!
                )
                WebServiceTaskCaller.CallWSConsumptionTask(
                    request,
                    ToksWebServicesConnection.ACUMULA_PUNTOS
                )
            } else {
                AcumularPuntosPresenter.view!!.onFailAcumularPuntosResult("No fue posible realizar la acumulación \n Llamar a sistemas.")
            }
        }

        private fun acumulaPuntosRequestResult(jsonResult: String) {
            try {
                AcumularPuntosPresenter.filesWeakReference!!.get()!!.registerLogs(
                    "Termina AcumulaPuntos",
                    jsonResult,
                    AcumularPuntosPresenter.preferenceHelperLogs!!,
                    AcumularPuntosPresenter.preferenceHelper!!
                )
                val jsonArray = JSONArray("[$jsonResult]")
                if (jsonResult.contains("code")) {
                    val code = jsonArray.getJSONObject(0).getString("code")
                    if (code == "200"||code == "0") {
                        val response = jsonArray.getJSONObject(0).getString("response")
                        val jsonArrayRes = JSONArray("[$response]")
                        val estatus = jsonArrayRes.getJSONObject(0).getString("estatus")
                        val subEstatus = jsonArrayRes.getJSONObject(0).getString("subEstatus")
                        if (estatus == ConstantsAComer.ESTATUS_SUCCESS && subEstatus == ConstantsAComer.SUB_ESTATUS_SUCCESS) {
                            val res = jsonArrayRes.getJSONObject(0).getString("res")
                            val jsonArrayInfo = JSONArray("[$res]")
                            val nombreMiembro =
                                ContentFragment.miembro!!.response.membresia!!.nombre
                            val puntosAcumulados =
                                jsonArrayInfo.getJSONObject(0).getString("puntos")
                            val puntosActuales =
                                jsonArrayInfo.getJSONObject(0).getString("puntosNoCalificados")
                            val puntosSiguienteNivel =
                                jsonArrayInfo.getJSONObject(0).getString("puntosSiguienteNivel")
                            val nivelActual =
                                jsonArrayInfo.getJSONObject(0).getString("nilvelActual")
                            ContentFragment.isComprobantePuntosPrintable = true
                            ContentFragment.comprobantePuntosOffline.clear()
                            ContentFragment.comprobantePuntosOffline =
                                Utils.generaOfflineTic(
                                    nombreMiembro,
                                    puntosActuales
                                )
                            ContentFragment.comprobantePuntos.clear()
                            ContentFragment.comprobantePuntos =
                                Utils.generaComprobanteAcumulacionAcomer(
                                    nombreMiembro,
                                    puntosAcumulados,
                                    puntosActuales,
                                    puntosSiguienteNivel,
                                    nivelActual
                                )
                            AcumularPuntosPresenter.view!!.onSuccessAcumularPuntosResult(puntosAcumulados)
                        } else if (estatus == ConstantsAComer.ESTATUS_SUCCESS && subEstatus == ConstantsAComer.SUB_ESTATUS_ERROR_TOPE_ACUMULAR_PUNTOS) {
                            AcumularPuntosPresenter.view!!.onFailAcumularPuntosResult("El miembro ha alcanzado el limite de acumulación.")
                        } else {
                            AcumularPuntosPresenter.view!!.onFailAcumularPuntosResult("No se pudo realizar la acumulación de puntos. \n Llamar a soporte.")
                        }
                    } else {
                        AcumularPuntosPresenter.view!!.onFailAcumularPuntosResult("No se pudo realizar la acumulación de puntos. \n Llamar a soporte.")
                    }
                } else {
                    buscarAcumulacionRequest()
                }
            } catch (e: JSONException) {
                e.printStackTrace()
                AcumularPuntosPresenter.filesWeakReference!!.get()!!.createFileException(
                    "Controller/AcumularPuntosRequest/acumulaPuntosRequestResult " + "Json: " + jsonResult
                            + " " + Log.getStackTraceString(e), AcumularPuntosPresenter.preferenceHelper!!
                )
                insertarTicketToBD()
                AcumularPuntosPresenter.view!!.onExceptionAcumularPuntosResult(e.message)
            }
        }

        private fun buscarAcumulacionRequest() {
            val ticket = AcumularPuntosPresenter.preferenceHelper!!.getString(ConstantsPreferences.PREF_FOLIO, null)
            val request = SoapObject(
                ToksWebServicesConnection.NAMESPACE,
                ToksWebServicesConnection.BUSCAR_TRANSACCION_ASSN
            )
            request.addProperty("Llave", ToksWebServicesConnection.KEY)
            request.addProperty("ticket", ticket)
            AcumularPuntosPresenter.filesWeakReference!!.get()!!.registerLogs(
                "Inicia Busqueda ASSN",
                request.toString(),
                AcumularPuntosPresenter.preferenceHelperLogs!!,
                AcumularPuntosPresenter.preferenceHelper!!
            )
            WebServiceTaskCaller.CallWSConsumptionTask(
                request,
                ToksWebServicesConnection.BUSCAR_TRANSACCION_ASSN
            )
        }

        private fun buscarAcumulacionRequestResult(jsonResult: String) {
            AcumularPuntosPresenter.filesWeakReference!!.get()!!.registerLogs(
                "Termina Busqueda ASSN",
                jsonResult,
                AcumularPuntosPresenter.preferenceHelperLogs!!,
                AcumularPuntosPresenter.preferenceHelper!!
            )
            if (jsonResult.contains("code")) {
                val jsonAdapter = AcumularPuntosPresenter.moshi.adapter(
                    AcumulacionPuntosACC::class.java
                )
                try {
                    val acumulacionPuntosACC = jsonAdapter.fromJson(jsonResult)
                    if (acumulacionPuntosACC != null) {
                        if (ContentFragment.miembro != null) {
                            if (acumulacionPuntosACC.response.estatus == ConstantsAComer.ESTATUS_SUCCESS && acumulacionPuntosACC.response.subEstatus == ConstantsAComer.SUB_ESTATUS_SUCCESS) {
                                val (_, _, puntos, _, _, _, puntosNoCalificados, _, nilvelActual, _, puntosSiguienteNivel) = acumulacionPuntosACC.response.res
                                ContentFragment.isComprobantePuntosPrintable = true
                                ContentFragment.comprobantePuntosOffline.clear()
                                ContentFragment.comprobantePuntosOffline =
                                    Utils.generaOfflineTic(
                                        ContentFragment.miembro!!.response.membresia!!.nombre,
                                        puntosNoCalificados.toString()
                                    )
                                ContentFragment.comprobantePuntos.clear()
                                ContentFragment.comprobantePuntos =
                                    Utils.generaComprobanteAcumulacionAcomer(
                                        ContentFragment.miembro!!.response.membresia!!.nombre,
                                        puntos.toString(),
                                        puntosNoCalificados.toString(),
                                        puntosSiguienteNivel.toString(),
                                        nilvelActual
                                    )
                                AcumularPuntosPresenter.view!!.onSuccessAcumularPuntosResult(
                                    puntos.toString()
                                )
                            } else if (acumulacionPuntosACC.response.estatus == ConstantsAComer.ESTATUS_SUCCESS && acumulacionPuntosACC.response.subEstatus == ConstantsAComer.SUB_ESTATUS_ERROR_TOPE_ACUMULAR_PUNTOS) {
                                AcumularPuntosPresenter.view!!.onFailAcumularPuntosResult("El miembro ha alcanzado el limite de acumulación.")
                            } else {
                                AcumularPuntosPresenter.view!!.onFailAcumularPuntosResult("No se pudo realizar la acumulación de puntos. \n Llamar a soporte.")
                            }
                        } else {
                            AcumularPuntosPresenter.view!!.onFailAcumularPuntosResult("No se pudo realizar la acumulación de puntos. \n Llamar a soporte.")
                        }
                    } else {
                        AcumularPuntosPresenter.view!!.onExceptionAcumularPuntosResult("Error al generar respuesta.")
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    AcumularPuntosPresenter.filesWeakReference!!.get()!!.createFileException(
                        "Controller/AcumularPuntosRequest/buscarAcumulacionRequestResult " + "Json: " + jsonResult
                                + " " + Log.getStackTraceString(e), AcumularPuntosPresenter.preferenceHelper!!
                    )
                    AcumularPuntosPresenter.view!!.onExceptionAcumularPuntosResult(e.message)
                }
            } else {
                val jsonAdapter = AcumularPuntosPresenter.moshi.adapter(
                    ACCException::class.java
                )
                try {
                    val accException = jsonAdapter.fromJson(jsonResult)
                    if (accException != null) {
                        AcumularPuntosPresenter.view!!.onFailAcumularPuntosResult(accException.ExceptionMessage)
                    } else {
                        AcumularPuntosPresenter.view!!.onFailAcumularPuntosResult("No se pudo realizar la acumulación de puntos. \n Llamar a soporte.")
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    AcumularPuntosPresenter.filesWeakReference!!.get()!!.createFileException(
                        "Controller/AcumularPuntosRequest/buscarAcumulacionRequestResult " + "Json: " + jsonResult
                                + " " + Log.getStackTraceString(e), AcumularPuntosPresenter.preferenceHelper!!
                    )
                    AcumularPuntosPresenter.view!!.onExceptionAcumularPuntosResult(e.message)
                }
            }
        }

        private fun showException(jsonResult: String) {
            val exceptionResult = ToksWebServiceExceptionRate.rateError(jsonResult)
            insertarTicketToBD()
            AcumularPuntosPresenter.view!!.onExceptionAcumularPuntosResult(exceptionResult)
        }

        private fun insertarTicketToBD() {
            AcumularPuntosPresenter.ticketsNoRegistradosDAOWeakReference!!.get()!!.insertarTicketNoRegistrado(
                TicketNoInsertados(
                    AcumularPuntosPresenter.miembroAuxiliar!!.ticket,
                    AcumularPuntosPresenter.miembroAuxiliar!!.monto,
                    AcumularPuntosPresenter.miembroAuxiliar!!.membresia,
                    AcumularPuntosPresenter.miembroAuxiliar!!.marca,
                    AcumularPuntosPresenter.miembroAuxiliar!!.fechaConsumo
                )
            )
        }
    }

    override fun executeAcumulaPuntosRequest() {
        acumularPuntosRequest()
    }

}
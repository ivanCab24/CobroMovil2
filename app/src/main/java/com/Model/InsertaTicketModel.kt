package com.Model

import android.util.Log
import com.Constants.ConstantsAComer
import com.Constants.ConstantsPreferences
import com.Controller.BD.Entity.TicketNoInsertados
import com.DataModel.MiembroAuxiliar
import com.Interfaces.InsertaTicketMVP
import com.Presenter.InsertaTicketPresenter
import com.Presenter.InsertaTicketPresenter.Companion.TAG
import com.Presenter.InsertaTicketPresenter.Companion.filesWeakReference
import com.Presenter.InsertaTicketPresenter.Companion.miembroAuxiliar
import com.Presenter.InsertaTicketPresenter.Companion.preferenceHelper
import com.Presenter.InsertaTicketPresenter.Companion.preferenceHelperLogs
import com.Presenter.InsertaTicketPresenter.Companion.ticketsNoRegistradosDAOWeakReference
import com.Presenter.InsertaTicketPresenter.Companion.view
import com.Utilities.Utils
import com.View.Activities.ActividadPrincipal
import com.View.Fragments.ContentFragment
import com.webservicestasks.ReadJSONFeedTask
import com.webservicestasks.ToksWebServiceExceptionRate
import com.webservicestasks.ToksWebServicesConnection
import org.json.JSONArray
import org.json.JSONException
import org.ksoap2.serialization.SoapObject

class InsertaTicketModel:InsertaTicketMVP.Model {
    private object WebServiceTaskCaller {
        fun CallWSConsumptionTask(request: SoapObject, method: String) {
            val service: ReadJSONFeedTask = object : ReadJSONFeedTask() {
                override fun onResponseReceived(jsonResult: String) {
                    if (ToksWebServiceExceptionRate.rateError(jsonResult) != "") {
                        insertTicketToBD()
                        val exceptionResult = ToksWebServiceExceptionRate.rateError(jsonResult)
                        view!!.onExceptionInsertaTicketResult(exceptionResult)
                    } else {
                        insertaTicketRequestResult(jsonResult)
                    }
                }
            }
            service.execute(request, method, InsertaTicketPresenter.preferenceHelper)
        }
    }

    companion object{
        private fun insertaTicketRequest() {
            val marca = preferenceHelper!!.getString(ConstantsPreferences.PREF_MARCA_SELECCIONADA, null)
            val marcaAux = Utils.evaluarMarcaSeleccionada(marca)
            val request = SoapObject(
                ToksWebServicesConnection.NAMESPACE,
                ToksWebServicesConnection.INSERTA_TICKET
            )
            request.addProperty("Llave", ToksWebServicesConnection.KEY)
            request.addProperty(
                "ticket",
                preferenceHelper!!.getString(ConstantsPreferences.PREF_FOLIO_CUENTA_CERRADA, null)
            )
            request.addProperty(
                "monto",
                if (ActividadPrincipal.isDescuentoGRG) 0.0.toInt() else ContentFragment.cuenta.total
                    .toInt()
            )
            request.addProperty("membresia", "")
            request.addProperty("marca", marcaAux)
            request.addProperty("FechaConsumo", Utils.dateAndTimeAComer())
            miembroAuxiliar = MiembroAuxiliar(
                preferenceHelper!!.getString(ConstantsPreferences.PREF_FOLIO_CUENTA_CERRADA, null),
                if (ActividadPrincipal.isDescuentoGRG) 0.0.toInt() else ContentFragment.cuenta.total
                    .toInt(),
                "", "", "", marcaAux, Utils.dateAndTimeAComer()
            )
            Log.d(TAG, "insertaTicketRequest: $request")
                filesWeakReference!!.get()!!.registerLogs(
                "Inicia InsertaTicket",
                request.toString(),
                preferenceHelperLogs!!,
                preferenceHelper!!
            )
            WebServiceTaskCaller.CallWSConsumptionTask(
                request,
                ToksWebServicesConnection.INSERTA_TICKET
            )
        }

        private fun insertaTicketRequestResult(jsonResult: String) {
            Log.i(TAG, "insertaTicketRequestResult: $jsonResult")
            val json = "[$jsonResult]"
            try {
                filesWeakReference!!.get()!!.registerLogs(
                    "Termina InsertaTicket",
                    jsonResult,
                    preferenceHelperLogs!!,
                    preferenceHelper!!
                )
                val jsonArray = JSONArray(json)
                val code = jsonArray.getJSONObject(0).getString("code")
                val response = jsonArray.getJSONObject(0).getString("response")
                val jsonArrayRes = JSONArray("[$response]")
                val mensaje = jsonArrayRes.getJSONObject(0).getString("mensaje")
                if (code == "200"||code == "0") {
                    view!!.onSuccessInsertaTicketResult(mensaje)
                } else if (code == "400") {
                    if (mensaje.contains(ConstantsAComer.MENSAJE_INSERTAR_TICKET_EXISTENTE)) {
                        view!!.onFailInsertaTicketResult("Este ticket ya ha sido insertado.")
                    } else if (mensaje.contains(ConstantsAComer.MENSAJE_INSERTAR_TICKET_DATOS_INCORRECTOS)) {
                        view!!.onFailInsertaTicketResult("Datos incorrecto al insertar ticket. \n Llamar a sistemas.")
                    } else {
                        insertTicketToBD()
                        view!!.onFailInsertaTicketResult("Ticket no insertado")
                    }
                }
            } catch (e: JSONException) {
                e.printStackTrace()
                filesWeakReference!!.get()!!.createFileException(
                    "Controller/InsertaTicketRequest/insertaTicketRequestResult " + jsonResult +
                            " " + Log.getStackTraceString(e), preferenceHelper!!
                )
                insertTicketToBD()
                view!!.onExceptionInsertaTicketResult(e.message)
            }
            System.out.println("De la clase Inserta Ticket->" + jsonResult)
        }

        private fun insertTicketToBD() {
            ticketsNoRegistradosDAOWeakReference!!.get()!!.insertarTicketNoRegistrado(
                TicketNoInsertados(
                    miembroAuxiliar!!.ticket,
                    miembroAuxiliar!!.monto,
                    miembroAuxiliar!!.membresia,
                    miembroAuxiliar!!.marca,
                    miembroAuxiliar!!.fechaConsumo
                )
            )
        }
    }

    override fun executeInsertaTicketRequet() {
        insertaTicketRequest()
    }

}
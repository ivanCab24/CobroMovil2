package com.Model

import android.util.Log
import com.Constants.ConstantsAComer
import com.Constants.ConstantsPreferences
import com.DataModel.Miembro
import com.DataModel.Producto
import com.Interfaces.CuponesDescuentosMVP
import com.Presenter.ConsultaAfiliadoPresenter.modelo.filesWeakReference
import com.Presenter.ConsultaAfiliadoPresenter.modelo.preferenceHelper
import com.Presenter.ConsultaAfiliadoPresenter.modelo.preferenceHelperLogs
import com.Presenter.DialogVisulizadorPresenter
import com.Utilities.Utilities
import com.View.Fragments.ContentFragment
import com.View.UserInteraction
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

class CuponesDescuentosModel:CuponesDescuentosMVP.Model, CoroutineScope {
    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    override fun executeSendCupones(cupon:Miembro.Response.Cupones,producto:Producto) {
        sendCuponRequest(cupon,producto)
    }

    object WebServiceTaskCaller {
        fun CallWSConsumptionTask(request: SoapObject, method: String) {
            val service: ReadJSONFeedTask = object : ReadJSONFeedTask() {
                override fun onResponseReceived(jsonResult: String) {
                    Log.i("s",jsonResult)
                    if (ToksWebServiceExceptionRate.rateError(jsonResult) != "") {
                        val exceptionRated = ToksWebServiceExceptionRate.rateError(jsonResult)

                        //ContentFragment!!.contentFragment!!.onExceptionVisualizadorResult(exceptionRated)
                    } else {
                        sendCuponRequestResult(jsonResult)
                    }
                }
            }
            service.execute(request, method, Utilities.preferenceHelper)
        }
    }

    companion object {
        private fun sendCuponRequest(cupon:Miembro.Response.Cupones,producto:Producto) {
            val request = SoapObject(
                ToksWebServicesConnection.NAMESPACE,
                ToksWebServicesConnection.DESCUENTOS
            )
            request.addProperty("Fecha",Utilities.getJulianDate())
            request.addProperty("Id_Local",Utilities.preferenceHelper.getString(ConstantsPreferences.PREF_UNIDAD, null))
            request.addProperty("Id_Term",ContentFragment.cuenta.detalle.id_term)
            request.addProperty("Id_Coma",ContentFragment.cuenta.detalle.id_coma)
            request.addProperty("Id_Pos",ContentFragment.contentFragment!!.pos)
            request.addProperty("Id_Ord",producto.id_ord)
            request.addProperty("Tipo_desc","5")
            request.addProperty("Id_Desc","996")
            request.addProperty("Porcentaje",
                if(cupon.restricciones.descuentoPorcentaje>0)
                    if(cupon.restricciones.subTipoCupon.equals("LOY_SDP_DESC_PRODUCTO"))
                        (cupon.restricciones.descuentoPorcentaje*cupon.restricciones.cantidadProductos/producto.porciones).toString()
                    else cupon.restricciones.descuentoPorcentaje.toString()
                else
                        (cupon.descuentoM*100/(producto.porciones*producto.precio_unit)).toString())
            request.addProperty("usuario", Utilities.preferenceHelper.getString(ConstantsPreferences.PREF_ESTAFETA, null))
            request.addProperty("empleado",Utilities.preferenceHelper.getString(ConstantsPreferences.PREF_NOMBRE_EMPLEADO, null))
            request.addProperty("Referencia","${ContentFragment.miembro!!.response.membresia!!.numeroMembresia}-${cupon.numeroCupon}")
            request.addProperty("Descripcion",cupon.descripcion)
            request.addProperty("Recalculo",0)
            request.addProperty("Numdescuento",0)
            request.addProperty("ID_SEC",producto.id_sec)
            request.addProperty("RNip",ContentFragment.nipA)
            Log.d("CUPONES Y DESCUENTOS",request.toString())

            WebServiceTaskCaller.CallWSConsumptionTask(request, ToksWebServicesConnection.DESCUENTOS)
            Log.i("kkkkkk",request.toString())

        }

        private fun sendCuponRequestResult(jsonResult: String) {
            try {
                filesWeakReference!!.get()!!.registerLogs(
                    "CUPONES Y DESCCUENTOS",
                    jsonResult,
                    preferenceHelperLogs!!,
                    preferenceHelper!!
                )
                try{
                    val jsonArray = JSONArray("[$jsonResult]")
                    if (jsonResult.contains("code")) {
                        val code = jsonArray.getJSONObject(0).getString("code")
                        if (code == "200"||code == "0") {
                            val response = jsonArray.getJSONObject(0).getString("response")
                            val jsonArrayRes = JSONArray("[$response]")
                            val estatus = jsonArrayRes.getJSONObject(0).getString("estatus")
                            val subEstatus = jsonArrayRes.getJSONObject(0).getString("subEstatus")
                            if (estatus == ConstantsAComer.ESTATUS_SUCCESS && subEstatus == ConstantsAComer.SUB_ESTATUS_SUCCESS) {
                                UserInteraction.snackySuccess(ContentFragment.activity,null,"Cupón aplicado")
                            }else {
                                UserInteraction.snackyFail(ContentFragment.activity,null,jsonResult)
                            }
                        } else {
                            UserInteraction.snackyFail(ContentFragment.activity,null,jsonResult)
                        }
                    } else {
                        UserInteraction.snackyFail(ContentFragment.activity,null,jsonResult)
                    }
                }catch (e:Exception){
                    UserInteraction.snackyFail(ContentFragment.activity,null,jsonResult)
                }

            } catch (e: JSONException) {
                DialogVisulizadorPresenter.view!!.onExceptionVisualizadorResult(jsonResult)
                e.printStackTrace()
            }
        }
    }

}
package com.Model

import android.annotation.SuppressLint
import android.util.Log
import com.Constants.ConstantsPreferences
import com.DataModel.CuponEngachementJsonAdapter
import com.DataModel.CuponGRG
import com.DataModel.Miembro
import com.DataModel.MiembroException
import com.DataModel.Producto
import com.Interfaces.ConsultaAfiliadoMVP
import com.Presenter.AcumularPuntosPresenter.Companion.moshi
import com.Presenter.ConsultaAfiliadoPresenter
import com.Utilities.Utilities
import com.View.Fragments.ContentFragment
import com.View.UserInteraction
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.Moshi
import com.webservicestasks.ReadJSONFeedTask
import com.webservicestasks.ToksWebServiceExceptionRate
import com.webservicestasks.ToksWebServicesConnection
import org.json.JSONArray
import org.ksoap2.serialization.SoapObject
import java.io.IOException

class ConsultaAfiliadoModel:ConsultaAfiliadoMVP.Model {
    private object WebServiceTaskCaller {
        fun CallWSConsumptionTask(request: SoapObject, method: String) {
            val service: ReadJSONFeedTask = @SuppressLint("StaticFieldLeak")
            object : ReadJSONFeedTask() {
                override fun onResponseReceived(jsonResult: String) {
                    val jsonResult2 = "[$jsonResult]"
                    if (ToksWebServiceExceptionRate.rateError(jsonResult2) != "") {
                        val exceptionRated = ToksWebServiceExceptionRate.rateError(jsonResult2)
                        ConsultaAfiliadoPresenter.view!!.onExceptionConsultaAfiliado(exceptionRated)
                    } else {
                        if(method==ToksWebServicesConnection.CONSULTA_AFILIADO)
                            consultaAfiliadoRequestResult(jsonResult)
                        else if(method==ToksWebServicesConnection.VALIDA_CUPON_GRG){
                            cuponGRGResult(jsonResult)
                        }
                        else if(method==ToksWebServicesConnection.VALIDA_CUPON_ENGACHEMENT){
                            cuponEngachementResult(jsonResult)
                        } else if (method == APLICA_DESCUENTO) {
                            Log.i("TAG", "JsonResult: $jsonResult")
                        }
                        else{

                            Log.i("TAG", "method: $method")
                            val jsonArray = JSONArray(jsonResult2)
                            val code = jsonArray.getJSONObject(0).getString("code")
                            val exceptionMessage = jsonArray.getJSONObject(0).getString("exceptionMessage")
                            if(!exceptionMessage.isEmpty()){

                            UserInteraction.snackyException(
                                ContentFragment.activity,
                                null,
                                exceptionMessage
                            )
                            }
                            else{
                                UserInteraction.getDialogWaiting.dismiss()
                                UserInteraction.snackySuccess(ContentFragment.activity,null,"Cupón aplicado")
                                ContentFragment.contentFragment!!.presenter!!.buscarMesa()
                            }

                        }
                    }
                }
            }
            service.execute(request, method, ConsultaAfiliadoPresenter.preferenceHelper)
        }
    }

    companion object{
        private var producto: Producto? = null
        fun generaDescuento(cupon: CuponGRG){
            if(cupon.subTipoCupon=="LOY_SDP_PRECIO_PREFERENCIAL"){
                val productoDes = ContentFragment.cuenta.detalle.getMinPrice(cupon.plusParticipantes.toWords())
                cupon.descuentoM = productoDes!!.precio_unit-cupon.precioPreferencial
                producto=productoDes
                //cupon.producto = productoDes
            }
            if(cupon.subTipoCupon=="LOY_SDP_2_X_1"){
                val productoDes = ContentFragment.cuenta.detalle.getMinPrice(cupon.plusParticipantes.toWords())
                cupon.descuentoM = productoDes!!.precio_unit
                producto=productoDes
                //cupon.producto = productoDes
            }
            if(cupon.subTipoCupon=="LOY_SDP_DESC_PRODUCTO"){
                val productoDes = ContentFragment.cuenta.detalle.getMinPriceCANT(cupon.plusParticipantes.toWords(),cupon.cantidadProductos)
                if(cupon.descuentoPorcentaje>0)
                    cupon.descuentoM =  productoDes!!.precio_unit*cupon.cantidadProductos * (cupon.descuentoPorcentaje/100)
                if(cupon.precioPreferencial>0)
                    cupon.descuentoM =  productoDes!!.precio_unit - cupon.precioPreferencial
                //cupon.producto = productoDes!!
                producto=productoDes!!
            }
            if(cupon.subTipoCupon=="LOY_SDP_DESC_CUENTA"){
                cupon.descuentoM = ContentFragment.cuenta.saldo2* ((cupon.descuentoPorcentaje/100))
                producto= Producto("0","dummy",0.0,0,0.0,0,0,"","","")
            }
            if(cupon.subTipoCupon=="LOY_SDP_3_X_2"){
                val productoDes = ContentFragment.cuenta.detalle.getMinPrice(cupon.plusParticipantes.toWords())
                cupon.descuentoM = productoDes!!.precio_unit
                //cupon.producto = productoDes
                producto=productoDes
            }
            cupon.descuentoM=cupon.descuentoM.round(2)
        }

        fun genMontoDesc(cupon: CuponGRG){
            cupon.descuentoM = 0.0
            try{
                if(cupon.subTipoCupon=="LOY_SDP_PRECIO_PREFERENCIAL"){
                    val productoDes = ContentFragment.cuenta.detalle.getMinPrice(cupon.plusParticipantes.toWords())
                    cupon.descuentoM = productoDes!!.precio_unit-cupon.precioPreferencial
                }
                if(cupon.subTipoCupon=="LOY_SDP_2_X_1"){
                    val productoDes = ContentFragment.cuenta.detalle.getMinPrice(cupon.plusParticipantes.toWords())
                    cupon.descuentoM = productoDes!!.precio_unit
                }
                if(cupon.subTipoCupon=="LOY_SDP_DESC_PRODUCTO"){
                    val productoDes = ContentFragment.cuenta.detalle.getMinPrice(cupon.plusParticipantes.toWords())
                    if(cupon.descuentoPorcentaje>0)
                        cupon.descuentoM =  productoDes!!.precio_unit * cupon.cantidadProductos * (cupon.descuentoPorcentaje/100)
                    if(cupon.precioPreferencial>0)
                        cupon.descuentoM =  productoDes!!.precio_unit - cupon.precioPreferencial
                }
                if(cupon.subTipoCupon=="LOY_SDP_DESC_CUENTA"){
                    cupon.descuentoM = ContentFragment.cuenta.saldo2* (cupon.descuentoPorcentaje/100)
                }
                if(cupon.subTipoCupon=="LOY_SDP_3_X_2"){
                    val productoDes = ContentFragment.cuenta.detalle.getMinPrice(cupon.plusParticipantes.toWords())
                    cupon.descuentoM = productoDes!!.precio_unit
                }
                cupon.descuentoM=cupon.descuentoM.round(2)
            }catch(e:Exception){
                cupon.descuentoM = 0.0
            }
        }

        fun genMontoDescEngachement(cupon: CuponEngachementJsonAdapter){
            cupon.cupon.DESCUENTOM = 0.0
            try{
                    cupon.cupon.DESCUENTOM = cupon.cupon.MONTO.toDouble()

            }catch(e:Exception){
                cupon.cupon.DESCUENTOM = 0.0
            }
        }

        fun Double.round(decimals: Int = 2): Double = "%.${decimals}f".format(this).toDouble()

        fun validaCupon(cupon: CuponGRG):Boolean{
            if(cupon.compraMinima> ContentFragment.cuenta.saldo2){
                UserInteraction.snackyWarning(ContentFragment.activity,null,"El cupon seleccionado no cumple con la compra minima")
                return false
            }
            if(cupon.subTipoCupon=="LOY_SDP_PRECIO_PREFERENCIAL"&&!ContentFragment.cuenta.detalle.containPLU(cupon.plusParticipantes.toWords())){
                UserInteraction.snackyWarning(ContentFragment.activity,null,"La cuenta no tiene el producto del cupón")
                return false
            }
            if(cupon.subTipoCupon=="LOY_SDP_2_X_1"&& ContentFragment.cuenta.detalle.getTotOfPLU(cupon.plusParticipantes.toWords())<2){
                UserInteraction.snackyWarning(ContentFragment.activity,null,"La cuenta no contiene los productos para aplicar el cupón")
                return false
            }
            if(cupon.subTipoCupon=="LOY_SDP_DESC_PRODUCTO"&&!ContentFragment.cuenta.detalle.containPLUMIN(cupon.plusParticipantes.toWords(),cupon.cantidadProductos)){
                UserInteraction.snackyWarning(ContentFragment.activity,null,"La cuenta no tiene el producto del cupón o los productos se encuentran separados")
                return false
            }
            if(cupon.subTipoCupon=="LOY_SDP_DESC_CUENTA"){

            }
            if(cupon.subTipoCupon=="LOY_SDP_3_X_2"&& ContentFragment.cuenta.detalle.getTotOfPLU(cupon.plusParticipantes.toWords())<3){
                UserInteraction.snackyWarning(ContentFragment.activity,null,"La cuenta no contiene los productos para aplicar el cupón")
                return false
            }
            return true
        }

        fun validaCuponEngachement(cupon: CuponEngachementJsonAdapter):Boolean{
            if(cupon.cupon.IMPORTE_MIN> ContentFragment.cuenta.saldo2){
                UserInteraction.snackyWarning(ContentFragment.activity,null,"El cupon seleccionado no cumple con la compra minima")
                return false
            }
            return true
        }

        inline fun String.toWords(): ArrayList<String> {
            val words = ArrayList<String>()
            for (w in this.trim(' ').split("/")) {
                if (w.isNotEmpty()) {
                    words.add(w)
                }
            }
            return words
        }

        private fun consultaAfiliadoRequest(numeroMiembro: String?) {
            val request = SoapObject(
                ToksWebServicesConnection.NAMESPACE,
                ToksWebServicesConnection.CONSULTA_AFILIADO
            )
            request.addProperty("Llave", ToksWebServicesConnection.KEY)
            request.addProperty("Folio", numeroMiembro)
            request.addProperty("Ticket", ContentFragment.cuenta.folio)
            Log.d(ConsultaAfiliadoPresenter.TAG, "consultaAfiliadoRequest: $request")
            ConsultaAfiliadoPresenter.filesWeakReference!!.get()!!.registerLogs(
                "Inicia ConsultaAfiliado",
                request.toString(),
                ConsultaAfiliadoPresenter.preferenceHelperLogs!!,
                ConsultaAfiliadoPresenter.preferenceHelper!!
            )
            WebServiceTaskCaller.CallWSConsumptionTask(
                request,
                ToksWebServicesConnection.CONSULTA_AFILIADO
            )
        }

        private fun verificaCuponGRGRequest(cupon: String) {

            val request = SoapObject(
                ToksWebServicesConnection.NAMESPACE,
                ToksWebServicesConnection.VALIDA_CUPON_GRG
            )
            request.addProperty("Llave", ToksWebServicesConnection.KEY)
            request.addProperty("Cadena", ToksWebServicesConnection.STRING)
            request.addProperty("Cupon", cupon)
            Log.d(ConsultaAfiliadoPresenter.TAG, "redime cupon GRG: $request")
            ConsultaAfiliadoPresenter.filesWeakReference!!.get()!!.registerLogs(
                "redime cupon GRG",
                request.toString(),
                ConsultaAfiliadoPresenter.preferenceHelperLogs!!,
                ConsultaAfiliadoPresenter.preferenceHelper!!
            )
            WebServiceTaskCaller.CallWSConsumptionTask(
                request,
                ToksWebServicesConnection.VALIDA_CUPON_GRG
            )
        }

        private fun verificaCuponEngachemnet(cupon: String) { //varifica si el cupon es valido

            val request = SoapObject(
                ToksWebServicesConnection.NAMESPACE,
                ToksWebServicesConnection.VALIDA_CUPON_ENGACHEMENT
            )
            request.addProperty("Llave", ToksWebServicesConnection.KEY)
            request.addProperty("Folio", cupon)
            Log.d(ConsultaAfiliadoPresenter.TAG, "redime cupon GRG: $request")
            ConsultaAfiliadoPresenter.filesWeakReference!!.get()!!.registerLogs(
                "redime cupon GRG",
                request.toString(),
                ConsultaAfiliadoPresenter.preferenceHelperLogs!!,
                ConsultaAfiliadoPresenter.preferenceHelper!!
            )
            WebServiceTaskCaller.CallWSConsumptionTask(
                request,
                ToksWebServicesConnection.VALIDA_CUPON_ENGACHEMENT
            )
        }

        private fun redimeCuponRequest(numeroCupon: String?, producto: Producto) {
             val request = SoapObject(
                ToksWebServicesConnection.NAMESPACE,
                ToksWebServicesConnection.DESCUENTOS
            )
            request.addProperty("Fecha", Utilities.getJulianDate())
            request.addProperty("Id_Local",
                Utilities.preferenceHelper.getString(ConstantsPreferences.PREF_UNIDAD, null))
            request.addProperty("Id_Term", ContentFragment.cuenta.detalle.id_term)
            request.addProperty("Id_Coma", ContentFragment.cuenta.detalle.id_coma)
            request.addProperty("Id_Pos", ContentFragment.cuenta.detalle.id_pos)
            request.addProperty("Id_Ord",producto.id_ord)
            request.addProperty("Tipo_desc","8")
            request.addProperty("Id_Desc","666")
            request.addProperty("Porcentaje","100")
            request.addProperty("usuario", Utilities.preferenceHelper.getString(ConstantsPreferences.PREF_ESTAFETA, null))
            request.addProperty("empleado",
                Utilities.preferenceHelper.getString(ConstantsPreferences.PREF_NOMBRE_EMPLEADO, null))
            request.addProperty("Referencia",numeroCupon)
            request.addProperty("Descripcion","Cupon encuesta")
            request.addProperty("Recalculo",0)
            request.addProperty("Numdescuento",0)
            request.addProperty("ID_SEC",producto.id_sec)
            request.addProperty("RNip", ContentFragment.nipA)
            Log.d("kkkkk", request.toString())
            WebServiceTaskCaller.CallWSConsumptionTask(
                request,
                ToksWebServicesConnection.DESCUENTOS
            )
        }

        private fun redimeCuponRequest20(numeroCupon: String?) {
            val request = SoapObject(
                ToksWebServicesConnection.NAMESPACE,
                ToksWebServicesConnection.APLICA_DESCUENTO
            )
            request.addProperty("Llave", ToksWebServicesConnection.KEY)
            request.addProperty("Cadena", ToksWebServicesConnection.STRING)
            request.addProperty("Folio", Utilities.preferenceHelper!!.getString(ConstantsPreferences.PREF_FOLIO, null))
            request.addProperty("Unidad", Utilities.preferenceHelper!!.getString(ConstantsPreferences.PREF_UNIDAD, null))
            request.addProperty("Tipo", "5")
            request.addProperty("Id_Desc", "658")
            request.addProperty("Descripcion", "Cupon Encuestas 20")
            request.addProperty("Porcentaje", "10")
            request.addProperty("Referencia", numeroCupon)
            request.addProperty("Usuario", Utilities.preferenceHelper!!.getString(ConstantsPreferences.PREF_ESTAFETA, null))
            request.addProperty("Empleado",Utilities.preferenceHelper!!.getString(ConstantsPreferences.PREF_NOMBRE_EMPLEADO, null))
            Log.d("CUPONES Y DESCUENTOS",request.toString())
            WebServiceTaskCaller.CallWSConsumptionTask(
                request,
                ToksWebServicesConnection.APLICA_DESCUENTO
            )
            Log.i("kkkkkk",request.toString())
        }

        private fun sendCuponCuentaGRGRequest(cupon:CuponGRG,producto:Producto) {
            val request = SoapObject(
                ToksWebServicesConnection.NAMESPACE,
                ToksWebServicesConnection.APLICA_DESCUENTO
            )
            request.addProperty("Llave", ToksWebServicesConnection.KEY)
            request.addProperty("Cadena", ToksWebServicesConnection.STRING)
            request.addProperty("Folio", Utilities.preferenceHelper!!.getString(ConstantsPreferences.PREF_FOLIO, null))
            request.addProperty("Unidad", Utilities.preferenceHelper!!.getString(ConstantsPreferences.PREF_UNIDAD, null))
            request.addProperty("Tipo", cupon.tipo_desc)
            request.addProperty("Id_Desc", cupon.id_desc)
            request.addProperty("Descripcion", "Cupon GRG")
            request.addProperty("Porcentaje", cupon.descuentoPorcentaje.toInt())
            request.addProperty("Referencia", "${cupon.numeroCupon}+${cupon.code_id}")
            request.addProperty("Usuario", Utilities.preferenceHelper!!.getString(ConstantsPreferences.PREF_ESTAFETA, null))
            request.addProperty("Empleado",Utilities.preferenceHelper!!.getString(ConstantsPreferences.PREF_NOMBRE_EMPLEADO, null))
            Log.d("CUPONES Y DESCUENTOS",request.toString())
            WebServiceTaskCaller.CallWSConsumptionTask(
                request,
                ToksWebServicesConnection.APLICA_DESCUENTO
            )
            Log.i("kkkkkk",request.toString())

        }

        private fun sendCuponCuentaEngachementRequest(cupon:CuponEngachementJsonAdapter) {
            val request = SoapObject(
                    ToksWebServicesConnection.NAMESPACE,
                    ToksWebServicesConnection.APLICA_DESCUENTO
            )
            request.addProperty("Llave", ToksWebServicesConnection.KEY)
            request.addProperty("Cadena", ToksWebServicesConnection.STRING)
            request.addProperty("Folio", Utilities.preferenceHelper!!.getString(ConstantsPreferences.PREF_FOLIO, null))
            request.addProperty("Unidad", Utilities.preferenceHelper!!.getString(ConstantsPreferences.PREF_UNIDAD, null))
            request.addProperty("Tipo", cupon.cupon.TIPO)
            request.addProperty("Id_Desc", cupon.cupon.ID_DESC)
            request.addProperty("Descripcion", "CuponEngachement")
            request.addProperty("Porcentaje", (cupon.cupon.DESCUENTOM * 100/ContentFragment.cuenta.saldo2).toInt())
            request.addProperty("Referencia", "${cupon.cupon.numeroCupon}")
            request.addProperty("Usuario", Utilities.preferenceHelper!!.getString(ConstantsPreferences.PREF_ESTAFETA, null))
            request.addProperty("Empleado",Utilities.preferenceHelper!!.getString(ConstantsPreferences.PREF_NOMBRE_EMPLEADO, null))
            Log.d("CUPONES Y DESCUENTOS",request.toString())
            WebServiceTaskCaller.CallWSConsumptionTask(
                    request,
                    ToksWebServicesConnection.APLICA_DESCUENTO
            )
            Log.i("kkkkkk",request.toString())

        }
        private fun sendCuponLicuadora(cupon:CuponGRG){ //envia informacion para el cobro del cupon
            val request = SoapObject(
                ToksWebServicesConnection.NAMESPACE,
                ToksWebServicesConnection.APLICA_DESCUENTO
            )
            request.addProperty("Llave", ToksWebServicesConnection.KEY)
            request.addProperty("Cadena", ToksWebServicesConnection.STRING)
            request.addProperty("Folio", Utilities.preferenceHelper!!.getString(ConstantsPreferences.PREF_FOLIO, null))
            request.addProperty("Unidad", Utilities.preferenceHelper!!.getString(ConstantsPreferences.PREF_UNIDAD, null))
            request.addProperty("Tipo", cupon.tipo_desc)
            request.addProperty("Id_Desc", cupon.id_desc)
            request.addProperty("Descripcion", "Cupon GRG")
            request.addProperty("Porcentaje", cupon.descuentoPorcentaje.toInt())
            request.addProperty("Referencia", "${cupon.numeroCupon}+${cupon.code_id}")
            request.addProperty("Usuario", Utilities.preferenceHelper!!.getString(ConstantsPreferences.PREF_ESTAFETA, null))
            request.addProperty("Empleado",Utilities.preferenceHelper!!.getString(ConstantsPreferences.PREF_NOMBRE_EMPLEADO, null))
            Log.d("CUPONES Y DESCUENTOS",request.toString())
            WebServiceTaskCaller.CallWSConsumptionTask(
                request,
                ToksWebServicesConnection.APLICA_DESCUENTO
            )
            Log.i("kkkkkk",request.toString())



        }

        private fun redimeCuponGRGRequest(cupon: CuponGRG, producto: Producto) {
            val request = SoapObject(
                ToksWebServicesConnection.NAMESPACE,
                ToksWebServicesConnection.DESCUENTOS
            )
            request.addProperty("Fecha", Utilities.getJulianDate())
            request.addProperty("Id_Local",
                Utilities.preferenceHelper.getString(ConstantsPreferences.PREF_UNIDAD, null))
            request.addProperty("Id_Term", ContentFragment.cuenta.detalle.id_term)
            request.addProperty("Id_Coma", ContentFragment.cuenta.detalle.id_coma)
            request.addProperty("Id_Pos", ContentFragment.cuenta.detalle.id_pos)
            request.addProperty("Id_Ord",producto.id_ord)
            request.addProperty("Tipo_desc",cupon.tipo_desc)
            request.addProperty("Id_Desc",cupon.id_desc)
            request.addProperty("Porcentaje",if(cupon.descuentoPorcentaje>0)(cupon.descuentoPorcentaje/producto.porciones).toString() else (cupon.descuentoM*100/producto.m_total).toString())
            request.addProperty("usuario", Utilities.preferenceHelper.getString(ConstantsPreferences.PREF_ESTAFETA, null))
            request.addProperty("empleado",
                Utilities.preferenceHelper.getString(ConstantsPreferences.PREF_NOMBRE_EMPLEADO, null))
            request.addProperty("Referencia","${cupon.numeroCupon}+${cupon.code_id}")
            request.addProperty("Descripcion","Cupon GRG")
            request.addProperty("Recalculo",0)
            request.addProperty("Numdescuento",0)
            request.addProperty("ID_SEC",producto.id_sec)
            request.addProperty("RNip", "6666")
            Log.d("kkkkk", request.toString())
            WebServiceTaskCaller.CallWSConsumptionTask(
                request,
                ToksWebServicesConnection.DESCUENTOS
            )
        }

        private fun consultaAfiliadoRequestResult(jsonResult2: String) {
            try {

                Log.d(ConsultaAfiliadoPresenter.TAG, "consultaAfiliadoRequest: $jsonResult2")
                ConsultaAfiliadoPresenter.filesWeakReference!!.get()!!.registerLogs(
                    "Termina ConsultaAfiliado",
                    jsonResult2,
                    ConsultaAfiliadoPresenter.preferenceHelperLogs!!,
                    ConsultaAfiliadoPresenter.preferenceHelper!!
                )
                val moshi = Moshi.Builder().build()
                val adapter = moshi.adapter(Miembro::class.java)
                val miembros = adapter.fromJson(jsonResult2)
                    if (miembros != null) {
                        if (miembros.code == 0||miembros.code == 200 ) ConsultaAfiliadoPresenter.view!!.onSuccessConsultaAfiliado(miembros)
                        else ConsultaAfiliadoPresenter.view!!.onFailConsultaAfiliado("Afiliado no encontrado.")
                    } else {
                        ConsultaAfiliadoPresenter.view!!.onFailConsultaAfiliado("Vuelva a consultar la membresia.")
                    }


            } catch (exception: JsonDataException) {
                try {
                    exception.printStackTrace()
                    val moshi = Moshi.Builder().build()
                    val adapter = moshi.adapter(
                        MiembroException::class.java
                    )
                    val miembroException = adapter.fromJson(jsonResult2)
                    if (miembroException != null) {
                        if (!miembroException.success) ConsultaAfiliadoPresenter.view!!.onExceptionConsultaAfiliado(
                            miembroException.exceptionMessage
                        )
                        else ConsultaAfiliadoPresenter.view!!.onExceptionConsultaAfiliado("Error inesperado.\nVuelva a consultar la membresia")
                    } else {
                        ConsultaAfiliadoPresenter.view!!.onExceptionConsultaAfiliado("Error al generar la membresia.\nVuelva a consultarla")
                    }
                } catch (jsonDataException: JsonDataException) {
                    jsonDataException.printStackTrace()
                    FirebaseCrashlytics.getInstance().recordException(jsonDataException)
                    ConsultaAfiliadoPresenter.filesWeakReference!!.get()!!.createFileException(
                        "Controller/ConsultaAfiliadoRequest/consultaAfiliadoRequestResult " + jsonResult2 +
                                " " + Log.getStackTraceString(jsonDataException), ConsultaAfiliadoPresenter.preferenceHelper!!
                    )
                    ConsultaAfiliadoPresenter.view!!.onExceptionConsultaAfiliado(jsonDataException.message)
                } catch (jsonDataException: IOException) {
                    jsonDataException.printStackTrace()
                    FirebaseCrashlytics.getInstance().recordException(jsonDataException)
                    ConsultaAfiliadoPresenter.filesWeakReference!!.get()!!.createFileException(
                        "Controller/ConsultaAfiliadoRequest/consultaAfiliadoRequestResult " + jsonResult2 +
                                " " + Log.getStackTraceString(jsonDataException), ConsultaAfiliadoPresenter.preferenceHelper!!
                    )
                    ConsultaAfiliadoPresenter.view!!.onExceptionConsultaAfiliado(jsonDataException.message)
                }
            } catch (e: IOException) {
                e.printStackTrace()
                FirebaseCrashlytics.getInstance().recordException(e)
                ConsultaAfiliadoPresenter.filesWeakReference!!.get()!!.createFileException(
                    "Controller/ConsultaAfiliadoRequest/consultaAfiliadoRequestResult " + jsonResult2 +
                            " " + Log.getStackTraceString(e), ConsultaAfiliadoPresenter.preferenceHelper!!
                )
                ConsultaAfiliadoPresenter.view!!.onExceptionConsultaAfiliado(e.message)
            }
        }
        private fun cuponEngachementResult(jsonResult2: String) {
            try {
                Log.i("Consultando el QR", jsonResult2)
                val jsonArray = JSONArray("[$jsonResult2]")
                val codigo = jsonArray.getJSONObject(0).getString("message")
                if(codigo.equals("Cupón disponible")){
                    val cuponJson  = jsonArray.getJSONObject(0).getJSONObject("cupon")
                    Log.i("CUPON_ENGACHEMENT", cuponJson.toString());
                    val jsonAdapter: JsonAdapter<CuponEngachementJsonAdapter> = moshi.adapter(CuponEngachementJsonAdapter::class.java)
                    val cuponResponse: CuponEngachementJsonAdapter? = jsonAdapter.fromJson(jsonResult2)
                    Log.d("mmmmm",cuponResponse.toString())
                    if(validaCuponEngachement(cuponResponse!!)){
                        UserInteraction.getDialogBuscarAfiliado.dismiss()
                        genMontoDescEngachement(cuponResponse)
                        // generaDescuentoE(cuponResponse)
                        UserInteraction.showWaitingDialog(ContentFragment.contentFragment!!.fragmentManager2!!,"Aplicando descuento")

                        sendCuponCuentaEngachementRequest(cuponResponse)

                    }
                }else{
                    UserInteraction.getDialogBuscarAfiliado.dismiss()
                    UserInteraction.snackyException(ContentFragment.activity,null,jsonArray.getJSONObject(0).getString("mensaje") )
                }


            } catch (exception: Exception) {
                exception.printStackTrace()
                UserInteraction.snackyWarning(ContentFragment.activity,null,exception.message)
            }
        }
        private fun cuponGRGResult(jsonResult2: String) {
            try {
                Log.i("Consultando el QR", jsonResult2)
                val jsonArray = JSONArray(jsonResult2)
                val codigo = jsonArray.getJSONObject(0).getString("code")
                if(codigo.equals("200")){
                    val jsonAdapter: JsonAdapter<CuponGRG> = moshi.adapter(CuponGRG::class.java)
                    val cuponResponse: CuponGRG? = jsonAdapter.fromJson(jsonResult2.replace("[","").replace("]",""))
                    Log.d("mmmmm",cuponResponse.toString())
                    var marca2 = ""
                    if(validaCupon(cuponResponse!!)){
                        UserInteraction.getDialogBuscarAfiliado.dismiss()
                        genMontoDesc(cuponResponse)
                        generaDescuento(cuponResponse)
                        UserInteraction.showWaitingDialog(ContentFragment.contentFragment!!.fragmentManager2!!,"Aplicando descuento")
                        if(cuponResponse.subTipoCupon.equals("LOY_SDP_DESC_CUENTA"))
                            sendCuponCuentaGRGRequest(cuponResponse, producto!!)
                        else
                            redimeCuponGRGRequest(cuponResponse,producto!!)
                    }
                }else{
                    UserInteraction.getDialogBuscarAfiliado.dismiss()
                    UserInteraction.snackyException(ContentFragment.activity,null,jsonArray.getJSONObject(0).getString("mensaje") )
                }


            } catch (exception: JsonDataException) {
                UserInteraction.snackyWarning(ContentFragment.activity,null,exception.message)
            }
        }
    }


    override fun executeConsultaAfiliadoRequest(numeroMiembro: String?) {
        consultaAfiliadoRequest(numeroMiembro)
    }

    override fun executeRedimeCuponRequest(numeroCupon: String?, producto: Producto) {
        redimeCuponRequest(numeroCupon, producto)
    }

    override fun executeRedimeCuponRequest20(numeroCupon: String?) {
        redimeCuponRequest20(numeroCupon)
    }

    fun executeRedimeCuponGRGRequest(numeroCupon: String?, producto: Producto) {
        redimeCuponRequest(numeroCupon, producto)
    }

    override fun executeVerificaCuponGRGRequest(cupon: String) {

        verificaCuponGRGRequest(cupon)
    }

    override fun executeVerificaCuponEngachement(cupon: String) {
        verificaCuponEngachemnet(cupon)
    }


}
package com.Presenter

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.util.Log
import android.view.View
import androidx.fragment.app.FragmentManager
import com.Constants.ConstantsAComer
import com.Constants.ConstantsDescuentos
import com.Constants.ConstantsPreferences
import com.DEBUG
import com.DataModel.Cuenta
import com.DataModel.Producto
import com.DataModel.Productos
import com.Interfaces.CajaMVP
import com.Interfaces.ContentFragmentMVP
import com.Interfaces.DialogRedencionCuponListener
import com.Interfaces.PinpadTaskMVP
import com.Utilities.Curl
import com.Utilities.Files
import com.Utilities.PreferenceHelper
import com.Utilities.PreferenceHelperLogs
import com.Utilities.Utilities
import com.Utilities.Utils
import com.Utilities.Utils.round
import com.View.Dialogs.CustomDialog
import com.View.Fragments.ContentFragment
import com.View.Fragments.ContentFragment.Companion.activity
import com.View.UserInteraction
import com.innovacion.eks.beerws.R
import com.innovacion.eks.beerws.databinding.FragmentContentBinding
import com.webservicestasks.ReadJSONFeedTask
import com.webservicestasks.ToksWebServiceExceptionRate
import com.webservicestasks.ToksWebServicesConnection
import com.webservicestasks.ToksWebServicesConnection.STRING
import org.json.JSONArray
import org.json.JSONException
import org.ksoap2.serialization.SoapObject
import java.lang.ref.WeakReference
import java.text.DecimalFormat
import javax.inject.Inject

/**
 * Type: Class.
 * Access: Public.
 * Name: CuentaRequest.
 *
 * @Description.
 * @EndDescription.
 */
class ContentFragmentPresenter @Inject constructor(private var model: ContentFragmentMVP.Model) :
    ToksWebServicesConnection, ContentFragmentMVP.Presenter {

    var tipAmount = 0.0
    var banderaCobroRefound = 0
    private var fragmentManager: FragmentManager? = null
    lateinit var pinpadTaskPresenter: PinpadTaskMVP.Presenter
    private lateinit var cajaPresenter: CajaMVP.Presenter
    private lateinit var binding: FragmentContentBinding


    private val TAG = "CFPresenter"

    private object WebServiceTaskCaller {
        fun CallWSConsumptionTask(request: SoapObject, method: String) {
            val service: ReadJSONFeedTask = @SuppressLint("StaticFieldLeak")
            object : ReadJSONFeedTask() {
                override fun onResponseReceived(jsonResult: String) {
                    if (ToksWebServiceExceptionRate.rateError(jsonResult) != "") {
                        val exceptionRated = ToksWebServiceExceptionRate.rateError(jsonResult)
                        view!!.onExceptionCuentaResult(exceptionRated)
                    } else {
                        cuentaRequestResult(jsonResult)
                    }
                }
            }
            service.execute(request, method, preferenceHelper)
        }
    }


    override fun setView(getView: ContentFragmentMVP.View?) {
        view = getView
    }

    override fun have_pagos():Boolean {
        var params= HashMap<String,String>()
        params["Llave"] = "e936c17f7b54f02d32@@5463e0c3f749080b9"
        params["Cadena"] = STRING
        params["Folio"] = ContentFragment.cuenta.folio
        params["Unidad"] = preferenceHelper!!.getString(ConstantsPreferences.PREF_UNIDAD, null)
        var reponse = Curl.sendPostRequest(params,"Pide_Pagos", preferenceHelper!!,1)
        Log.i("Busca_pagos", reponse.toString())
        return reponse.contains("OK")
    }

    override fun requiereAuth(): Boolean {
        var params= HashMap<String,String>()
        params["Llave"] = "e936c17f7b54f02d32@@5463e0c3f749080b9"
        params["Cadena"] = STRING
        params["estafeta"] = Utilities.preferenceHelper.getString(ConstantsPreferences.PREF_ESTAFETA, null)
        var reponse = Curl.sendPostRequest(params,"Valida_monto_descuento", preferenceHelper!!,1)
        Log.i("response auth", reponse.toString())
        val jsonArray = JSONArray(reponse)
        val REQUIEREAUTORIZA = jsonArray.getJSONObject(0).getInt("REQUIEREAUTORIZA")

        Log.i("Revisa_saldo", reponse)
        if( REQUIEREAUTORIZA==0 )
            return false
        return true
    }

    override fun liberarCuenta() {
        var params= HashMap<String,String>()
        params["Llave"] = "e936c17f7b54f02d32@@5463e0c3f749080b9"
        params["Cadena"] = STRING
        params["folio"] = ContentFragment.cuenta.folio
        params["id_local"] = preferenceHelper!!.getString(ConstantsPreferences.PREF_UNIDAD, null)
        var reponse = Curl.sendPostRequest(params,"Libera_cuenta_acc", preferenceHelper!!,1)
        if(reponse.contains("OK")){
            UserInteraction.snackySuccess(activity,null,"Cuenta liberada correctamente.")
            UserInteraction.getDialogWaiting.dismiss()
            buscarMesa()
        }
    }

    override fun revisa_saldo(): Boolean {
        var params= HashMap<String,String>()
        params["Llave"] = "e936c17f7b54f02d32@@5463e0c3f749080b9"
        params["Cadena"] = STRING
        params["folio"] = ContentFragment.cuenta.folio
        params["Unidad"] = preferenceHelper!!.getString(ConstantsPreferences.PREF_UNIDAD, null)
        var reponse = Curl.sendPostRequest(params,"Revisa_saldo", preferenceHelper!!,1)
        val jsonArray = JSONArray(reponse)
        val cerrada = jsonArray.getJSONObject(0).getString("CERRADA").toUpperCase()
        val saldo = jsonArray.getJSONObject(0).getString("SALDO").toUpperCase()
        Log.i("Revisa_saldo", reponse)
        if( saldo.toDouble()>ContentFragment.cuenta.saldo )
            return false
        return true
    }

    override fun getMarca() {
        view?.getCurrentMarca(model.getMarca())
    }

    override fun payButtonAction() {
        var isCuponAniversarion = false
        var importeParaDescuento = 0.0
        if (ContentFragment.descuentoCuponAComer != null && ContentFragment.copiaMiembro != null && false) {
            Log.i(TAG, "payButtonAction: " + ContentFragment.copiaMiembro!!.response.cupones.size)
            if (ContentFragment.copiaMiembro!!.response.cupones.size <= 0) {
                UserInteraction.snackyWarning(
                    activity,
                    null,
                    ContentFragment.miembro!!.response.membresia!!.nombre + " no tiene cupones disponibles."
                )
                executePayAction()
            } else {
                for (i in ContentFragment.copiaMiembro!!.response.cupones.indices) {
                    val marca = Utils.evaluarMarcaSeleccionada(
                        preferenceHelper!!.getString(ConstantsPreferences.PREF_MARCA_SELECCIONADA, null)
                    )
                    if (ContentFragment.copiaMiembro!!.response.cupones[i].codigoCupon == ConstantsDescuentos.DESCUENTO_ANIVERSARIO_FAROLITO && marca == ConstantsAComer.MARCA_FAROLITO) {
                        ContentFragment.cuponMiembro =
                            ContentFragment.copiaMiembro!!.response.cupones[i]
                        importeParaDescuento =
                            ContentFragment.cuponMiembro!!.restricciones.compraMinima.toDouble()
                        isCuponAniversarion =
                            ContentFragment.cuponMiembro!!.eventoBeneficio == ConstantsAComer.EVENTO_BENEFICIO_ANIVERSARIO
                    } else if (ContentFragment.copiaMiembro!!.response.cupones[i].codigoCupon == ConstantsDescuentos.DESCUENTO_ANIVERSARIO_BFF && marca == ConstantsAComer.MARCA_BEER) {
                        ContentFragment.cuponMiembro =
                            ContentFragment.copiaMiembro!!.response.cupones[i]
                        importeParaDescuento =
                            ContentFragment.cuponMiembro!!.restricciones.compraMinima.toDouble()
                        isCuponAniversarion =
                            ContentFragment.cuponMiembro!!.eventoBeneficio == ConstantsAComer.EVENTO_BENEFICIO_ANIVERSARIO
                    } else if (ContentFragment.copiaMiembro!!.response.cupones[i].codigoCupon == ConstantsDescuentos.DESCUENTO_ANIVERSARIO_TOKS && marca == ConstantsAComer.MARCA_TOKS) {
                        ContentFragment.cuponMiembro =
                            ContentFragment.copiaMiembro!!.response.cupones[i]
                        importeParaDescuento =
                            ContentFragment.cuponMiembro!!.restricciones.compraMinima.toDouble()
                        isCuponAniversarion =
                            ContentFragment.cuponMiembro!!.eventoBeneficio == ConstantsAComer.EVENTO_BENEFICIO_ANIVERSARIO
                    }
                }
                if (isCuponAniversarion && ContentFragment.cuenta.importe >= importeParaDescuento) {
                    val mensaje =
                        """${ContentFragment.copiaMiembro!!.response.membresia!!.nombre} tiene un ${ContentFragment.cuponMiembro!!.descripcion.toLowerCase()}.¿Desea aplicarlo?"""

                    var dialogRedencionCuponListener: DialogRedencionCuponListener = object :
                        DialogRedencionCuponListener {
                        override fun onCancelSelected() {
                            executePayAction()
                        }

                    }
                    UserInteraction.showDialogRedencionCupon(
                        fragmentManager,
                        dialogRedencionCuponListener,
                        "Cupón de aniversario",
                        mensaje
                    )
                } else {
                    executePayAction()
                }
            }
        } else {
            executePayAction()
        }
    }

    override fun executePayAction() {
        //sendACK2();
        if (preferenceHelper!!.getString(ConstantsPreferences.PREF_PINPAD, null).isNotEmpty())  {//knne  // agregar un ! antes del preferenceHelper y el isNotEmpty() == true
            ContentFragment.banderaCobroRefound = 0
            if (!DEBUG){ //knne !DEBUG ==false
                System.out.println("---->al dar click en pagar<-----")
                UserInteraction.showWaitingDialog(fragmentManager, "Esperando la pinpad...")
                this.pinpadTaskPresenter.executeTaskConnection()
            } else {
                UserInteraction.showWaitingDialog(
                    fragmentManager,
                    "Obteniendo la información \n de la caja"
                )
                this.cajaPresenter.executeCaja()
            }
            // TODO: 18/03/2020 Descomentar la linea de arriba
        } else {
            UserInteraction.showCustomDialog(
                fragmentManager,
                "Configure los parametros de la pinpad/terminal111",
                object : CustomDialog.DialogButtonClickListener {
                    override fun onPositiveButton() {
                        UserInteraction.getCustomDialog.dismiss()
                    }
                })
        }
    }

    override fun setFM(fragmentManager: FragmentManager) {
        this.fragmentManager = fragmentManager
    }

    override fun setPinPadPresenter(pinpadTaskPresenter: PinpadTaskMVP.Presenter) {
        this.pinpadTaskPresenter = pinpadTaskPresenter
    }

    override fun setCajaPresenter(cajaPresenter: CajaMVP.Presenter) {
        this.cajaPresenter = cajaPresenter
    }

    override fun buscarMesa() {
        ContentFragment.contentFragment!!.copiaprinted = false
        ContentFragment.contentFragment!!.conteoImpresiones = 0
        tipControls()
        ContentFragment.imm!!.hideSoftInputFromWindow(binding.editTextMesa.windowToken, 0)
        val mesa = binding.editTextMesa.text.toString()
        preferenceHelper!!.putString(ConstantsPreferences.PREF_MESA, mesa)
        val mesaGuardada = preferenceHelper!!.getString(ConstantsPreferences.PREF_MESA, null)
        val comensal = binding.numberPickerDiner.value.toString()
        val juntaSeparada = if (binding.checkBoxSeparateAccount.isChecked) "S" else "J"
        ContentFragment.contentFragment!!.pos = if(juntaSeparada=="J")"0" else comensal

        if (mesa.isEmpty()) {
            UserInteraction.snackyWarning(activity, null, "Ingrese un numero de mesa")
        } else {
            UserInteraction.showWaitingDialog(
                fragmentManager,
                "Obteniendo la información de la cuenta"
            )
            cuentaRequestExecute(mesa, comensal, juntaSeparada)
        }
    }

    override fun tipControls() {
        if (ContentFragment.tipAmount > 0 || ContentFragment.propinaPorcentaje > 0) {
            ContentFragment.tipAmount = 0.0
            ContentFragment.propinaPorcentaje = 0.0
            ContentFragment.isTip = false
            binding.customTipEditText.setText(ContentFragment.activity!!.getString(R.string.empty))
            binding.textViewMontoPropina.text =
                ContentFragment.activity!!.getString(R.string.zero)
            binding.buttonNoTip.visibility = View.INVISIBLE
        }
    }


    override fun setBinding(fragmentContentBinding: FragmentContentBinding?) {
        fragmentContentBindingWeakReference = WeakReference(fragmentContentBinding)
        this.binding = fragmentContentBinding!!
    }

    override fun preferences(
        sharedPreferences: SharedPreferences?,
        preferenceHelper: PreferenceHelper?
    ) {
        getSharedPreferences = sharedPreferences!!
        Companion.preferenceHelper = preferenceHelper
    }

    override fun setLogsInfo(preferenceHelperLogs: PreferenceHelperLogs?, files: Files?) {
        Companion.preferenceHelperLogs = preferenceHelperLogs!!
        filesWeakReference = WeakReference(files)
    }

    override fun setTip(tipAmount: Double) {
        tip = tipAmount
    }

    override fun cuentaRequestExecute(mesa: String?, comensal: String?, juntaSeparada: String?) {
        cuentaRequest((if (mesa!!.length == 1) "0$mesa" else mesa), comensal, juntaSeparada)
    }

    companion object {
        private val TAG = "CFPresenter"
        private var view: ContentFragmentMVP.View? = null
        private var getSharedPreferences: SharedPreferences? = null

        @JvmStatic
        private var preferenceHelper: PreferenceHelper? = null

        @JvmStatic
        private var preferenceHelperLogs: PreferenceHelperLogs? = null
        private var fragmentContentBindingWeakReference: WeakReference<FragmentContentBinding?>? =
            null
        private var filesWeakReference: WeakReference<Files?>? = null
        private var tip = 0.0

        private fun cuentaRequest(mesa: String, comensal: String?, juntaSeparada: String?) {
            val request = SoapObject(
                ToksWebServicesConnection.NAMESPACE,
                ToksWebServicesConnection.PIDE_CUENTA
            )
            request.addProperty("Llave", ToksWebServicesConnection.KEY)
            request.addProperty("Cadena", ToksWebServicesConnection.STRING)
            request.addProperty(
                "Unidad",
                preferenceHelper!!.getString(ConstantsPreferences.PREF_UNIDAD, null)
            )
            request.addProperty(
                "Mesa",
                if (preferenceHelper!!.getString(ConstantsPreferences.PREF_MESA, null).isEmpty())
                    if (mesa.length == 1) "0$mesa" else mesa
                else if (preferenceHelper!!.getString(ConstantsPreferences.PREF_MESA, null).length == 1) "0" + preferenceHelper!!.getString(ConstantsPreferences.PREF_MESA, null)
                else preferenceHelper!!.getString(ConstantsPreferences.PREF_MESA, null)
            )
            request.addProperty("Comensal", comensal)
            request.addProperty("JuntaSeparada", juntaSeparada)
            Log.d(TAG, "cuentaRequest: $request")
            filesWeakReference!!.get()!!.registerLogs(
                "Inicia Pide Cuenta",
                request.toString(),
                preferenceHelperLogs!!,
                preferenceHelper!!
            )

            WebServiceTaskCaller.CallWSConsumptionTask(
                request,
                ToksWebServicesConnection.PIDE_CUENTA
            )
        }

        private fun cuentaRequestResult(jsonResult: String) {
            //ContentFragment.miembro=null
            try {
                filesWeakReference!!.get()!!.registerLogs(
                    "Termina Pide Cuenta",
                    jsonResult,
                    preferenceHelperLogs!!,
                    preferenceHelper!!
                )
                val jsonArray = JSONArray(jsonResult)
                val jsonObject = jsonArray.getJSONObject(0)
                val codigo = jsonObject.getString("CODIGO").toUpperCase()
                if (codigo == "OK") {
                    val fecha = jsonObject.getLong("FECHA")
                    val idLocal = jsonObject.getLong("ID_LOCAL")
                    val idComa = jsonObject.getLong("ID_COMA")
                    val idPos = jsonObject.getLong("ID_POS")
                    val saldo = jsonObject.getDouble("SALDO")
                    val total = jsonObject.getDouble("M_TOTAL")
                    val importe = jsonObject.getDouble("M_IMPORTE")
                    val desc = jsonObject.getDouble("M_DESC")
                    val descTipo = jsonObject.getLong("DESC_TIPO")
                    val descID = jsonObject.getLong("DESC_ID")
                    val referencia = jsonObject.getLong("REFERENCIA")
                    val cerrada = jsonObject.getInt("CERRADA")
                    val puedeCerrar = jsonObject.getLong("PUEDECERRAR")
                    val facturado = jsonObject.getInt("FACTURADA")
                    val imprime = jsonObject.getInt("IMPRIME")
                    val folio = jsonObject.getString("FOLIOCTA")
                    val numcomensales = jsonObject.getString("NUMCOMENSALES")
                    var ACC = jsonObject.getString("ACC")
                    if(ACC=="null"||ACC==null)
                        ACC = "1"

                    val jsonDetalle = jsonObject.getJSONArray("Detalle")
                    val jsonD = jsonDetalle.getJSONObject(0)
                    var i = 0
                    var productosCuenta = ArrayList<Producto>()
                    var productos = HashMap<String,Productos>()
                    while(i<jsonDetalle.length()){
                        val jsonD = jsonDetalle.getJSONObject(i)
                        val id_prod = jsonD.getString("ID_PRO")
                        val producto = jsonD.getString("PRODUCTO")
                        val precio_unit = jsonD.getDouble("PRECIO_UNIT")
                        val porciones = jsonD.getInt("PORCIONES")
                        val m_total = jsonD.getDouble("M_TOTAL")
                        val desc_id = jsonD.getInt("DESC_ID")
                        val desc_tipo = jsonD.getInt("DESC_TIPO")
                        val desc_con = jsonD.getString("DESC_CON")
                        val id_ord = jsonD.getString("ID_ORD")
                        val id_sec = jsonD.getString("ID_SEC")
                        val productoA = Producto(id_prod,producto,precio_unit,porciones, m_total, desc_id,desc_tipo, desc_con,id_ord,id_sec)
                        productosCuenta.add(productoA)
                        if(!productos.containsKey(id_prod)){
                            val productosArray = ArrayList<Producto>()
                            productosArray.add(productoA)
                            val productosM = Productos(productosArray)
                            productos[id_prod]=productosM
                        }else{
                            productos[id_prod]!!.productos.add(productoA)
                        }
                        i++
                    }
                    val detalle = Cuenta.Detalle(jsonD.getInt("ID_LOCAL"),jsonD.getInt("ID_TERM"),jsonD.getInt("ID_COMA"),jsonD.getInt("ID_POS"),productos)
                    val cuenta = Cuenta(
                        fecha,
                        idLocal,
                        idComa,
                        idPos,
                        saldo,
                        total,
                        importe,
                        desc,
                        descTipo,
                        descID,
                        referencia,
                        cerrada,
                        puedeCerrar,
                        facturado,
                        imprime,
                        folio,
                        numcomensales,
                        ACC.toInt(),
                        productosCuenta,
                        detalle,
                        saldo
                        )
                    updateUI(cuenta)

                } else {
                    view!!.onFailCuentaResult(codigo)
                }
            } catch (e: JSONException) {
                e.printStackTrace()
                filesWeakReference!!.get()!!.createFileException(
                    "Controller/CuentaRequest/cuentaRequestResult " + jsonResult +
                            " " + Log.getStackTraceString(e), preferenceHelper!!
                )
                view!!.onExceptionCuentaResult("Error al generar la cuenta")
            }
        }

        private fun updateUI(cuenta: Cuenta) {
            val formatter = DecimalFormat("#0.00")
            var str: String? = "Folio:" + " " + cuenta.folio
            fragmentContentBindingWeakReference!!.get()!!.textViewFolio.text = str
            str = if (cuenta.importe != 0.0) formatter.format(cuenta.importe) else "0.00"
            fragmentContentBindingWeakReference!!.get()!!.textViewImporte.text = str
            str = if (cuenta.desc != 0.0) formatter.format(cuenta.desc) else "0.00"
            fragmentContentBindingWeakReference!!.get()!!.textViewMontoDescuento.text = str
            str = if (cuenta.total != 0.0) formatter.format(cuenta.total) else "0.00"
            fragmentContentBindingWeakReference!!.get()!!.textViewMontoTotal.text =
                str
            var text: String
            if (cuenta.importe == 0.0) {  //boton por cobrar
                fragmentContentBindingWeakReference!!.get()!!.buttonCobrar.text = "Por Cobrar $0.00"
                fragmentContentBindingWeakReference!!.get()!!.buttonNoDiscount.visibility =
                    View.INVISIBLE
            } else {
                text = formatter.format(round(cuenta.saldo + tip, 2))
                fragmentContentBindingWeakReference!!.get()!!.buttonCobrar.text =
                    if (cuenta.saldo == 0.0) "Cerrar Cuenta" else "Por Cobrar $$text"
                fragmentContentBindingWeakReference!!.get()!!.buttonNoDiscount.visibility =
                    if (cuenta.desc > 0.0) View.VISIBLE else View.INVISIBLE
            }
            view!!.onSuccessCuenta(cuenta)
        }
    }
}
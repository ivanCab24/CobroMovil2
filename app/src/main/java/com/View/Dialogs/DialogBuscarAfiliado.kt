/*
 * *
 *  * Created by Gerardo Ruiz on 11/13/20 11:02 AM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 11/13/20 11:02 AM
 *
 */
package com.View.Dialogs

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.Constants.ConstantsAComer.MEMBRESIA_SUBTIPO_CORPORATIVA
import com.Constants.ConstantsAComer.MEMBRESIA_SUBTIPO_INAPAM
import com.Constants.ConstantsMarcas
import com.Constants.ConstantsPreferences
import com.DI.BaseApplication
import com.DataModel.Miembro
import com.DataModel.Miembro.Response.Membresia
import com.DataModel.Producto
import com.Interfaces.ConsultaAfiliadoMVP
import com.Interfaces.EscaneoAfiliadoResult
import com.Utilities.Files
import com.Utilities.PreferenceHelper
import com.Utilities.PreferenceHelperLogs
import com.Utilities.Utils
import com.Utilities.Utils.catalogDescuentosCorporativos
import com.Utilities.Utils.hideSoftKeyboard
import com.View.Activities.ActividadPrincipal
import com.View.Fragments.ContentFragment
import com.View.Fragments.ContentFragment.Companion.isConsultaMiembro
import com.View.UserInteraction
import com.github.ybq.android.spinkit.style.DoubleBounce
import com.google.zxing.integration.android.IntentIntegrator
import com.innovacion.eks.beerws.R
import com.innovacion.eks.beerws.databinding.CustomDialogAcomerLayoutBinding
import com.orhanobut.hawk.Hawk
import javax.inject.Inject
import javax.inject.Named

/**
 * Type: Class.
 * Access: Public.
 * Name: DialogBuscarAfiliado.
 *
 * @Description.
 * @EndDescription.
 */
class DialogBuscarAfiliado : DialogFragment(), ConsultaAfiliadoMVP.View,
    EscaneoAfiliadoResult {
    private var customDialogAcomerLayoutBinding: CustomDialogAcomerLayoutBinding? = null
    private var fragmentManager2: FragmentManager? = null

    /**
     * The Shared preferences.
     */
    @JvmField
    @Named("Preferencias")
    @Inject
    var sharedPreferences: SharedPreferences? = null

    /**
     * The Preference helper.
     */
    @JvmField
    @Inject
    var preferenceHelper: PreferenceHelper? = null

    /**
     * The Preference helper logs.
     */
    @JvmField
    @Inject
    var preferenceHelperLogs: PreferenceHelperLogs? = null

    /**
     * The Files.
     */
    @JvmField
    @Inject
    var files: Files? = null

    /**
     * The Consulta afiliado presenter.
     */
    @JvmField
    @Inject
    var presenter: ConsultaAfiliadoMVP.Presenter? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        customDialogAcomerLayoutBinding =
            CustomDialogAcomerLayoutBinding.inflate(inflater, container, false)
        dialog!!.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        return customDialogAcomerLayoutBinding!!.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context.applicationContext as BaseApplication).plusAcomerClubSubComponent().inject(this)
        doubleBounce = DoubleBounce()
        presenter!!.setPreferences(sharedPreferences, preferenceHelper)
        presenter!!.setLogsInfo(preferenceHelperLogs, files)
        val marca = presenter!!.getMarca()
        getCurrentMarca(marca)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentManager2 = fragmentManager
        initDI()
        customDialogAcomerLayoutBinding!!.progressBarAfiliado.indeterminateDrawable = doubleBounce
        customDialogAcomerLayoutBinding!!.btnBuscarMiembro.isEnabled = true
        customDialogAcomerLayoutBinding!!.imageButtonAbrirEscaner.setOnClickListener {
            ActividadPrincipal.escaneoAfiliadoResult = this
            val intentIntegrator = IntentIntegrator(activity)
            intentIntegrator.initiateScan()
        }
        customDialogAcomerLayoutBinding!!.btnBuscarMiembro.setOnClickListener {
            if (customDialogAcomerLayoutBinding!!.editTextNumeroMiembro.text.toString().isEmpty()) {
                UserInteraction.snackyWarning(
                    null,
                    getView(),
                    "Proporcione la información solicitada"
                )
            } else {
                var isNumber = try {
                    updateUI(true)
                    customDialogAcomerLayoutBinding!!.editTextNumeroMiembro.text.toString().toLong()
                    true
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                    false
                }
                if(isNumber){
                    isConsultaMiembro = true
                    presenter!!.executeConsultaAfiliado(customDialogAcomerLayoutBinding!!.editTextNumeroMiembro.text.toString())
                }else
                    presenter!!.executeVerificaCuponGRG(customDialogAcomerLayoutBinding!!.editTextNumeroMiembro.text.toString())

            }
        }
        customDialogAcomerLayoutBinding!!.buttonCancelar.setOnClickListener {
            ActividadPrincipal.banderaDialogMostrar = ""
            hideSoftKeyboard(customDialogAcomerLayoutBinding!!.editTextNumeroMiembro, activity)
            dismiss()
        }
    }

    private fun initDI() {
        presenter!!.setView(this)
        presenter!!.setPreferences(sharedPreferences, preferenceHelper)
        presenter!!.setLogsInfo(preferenceHelperLogs, files)
    }

    override fun onResume() {
        super.onResume()

        //getDialog().getWindow().setLayout(500, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog!!.window!!.attributes.windowAnimations = R.style.DialogTheme
        dialog!!.setCancelable(false)
    }

    private fun updateUI(isUpdated: Boolean) {
        customDialogAcomerLayoutBinding!!.btnBuscarMiembro.isEnabled = !isUpdated
        customDialogAcomerLayoutBinding!!.progressBarAfiliado.visibility =
            if (isUpdated) View.VISIBLE else View.INVISIBLE
    }

    //================================================================================
    // Inicia Presenter ConsultaAfiliado
    //================================================================================
    override fun onExceptionConsultaAfiliado(onExceptionResult: String?) {
        updateUI(false)
        UserInteraction.snackyException(ContentFragment.activity, null, onExceptionResult)
        try{
            UserInteraction.getDialogWaiting.dismiss()
        }catch (e:Exception){

        }
    }

    override fun onFailConsultaAfiliado(onFailResult: String?) {
        updateUI(false)
        UserInteraction.snackyFail(activity, view, onFailResult)
    }

    override fun onSuccessConsultaAfiliado(miembro: Miembro?) {
        preferenceHelper!!.putString(ConstantsPreferences.PREF_BANDERA_AFILIADO, "1")
        ContentFragment.copiaMiembro = miembro
        ContentFragment.nipA = "0000"
        val membresia = miembro!!.response.membresia
        updateUI(false)
        if (false){//&& membresia.subTipoMembresia!= MEMBRESIA_SUBTIPO_CORPORATIVA) {
            UserInteraction.showNipAComerDialog(fragmentManager2, miembro.response.membresia!!.numeroMembresia,
                object : DialogInputNipAComer.DialogButtonClickListener {
                    override fun onPositiveButton(nip: String?) {
                        run {
                            customDialogAcomerLayoutBinding!!.progressBarAfiliado.visibility = View.VISIBLE
                            customDialogAcomerLayoutBinding!!.btnBuscarMiembro.isEnabled = false
                            aceptaMiembro(membresia, miembro)
                            if (nip != null) {
                                ContentFragment.nipA=nip
                            }
                            ContentFragment.miembroCuentaA!![ContentFragment.cuenta.folio]=miembro
                            Hawk.put("membresias",ContentFragment.miembroCuentaA)
                        }
                    }

                }
            )
        } else {
            aceptaMiembro(membresia, miembro)
            ContentFragment.miembroCuentaA!![ContentFragment.cuenta.folio]=miembro
            Hawk.put("membresias",ContentFragment.miembroCuentaA)
            if(membresia!!.subTipoMembresia==MEMBRESIA_SUBTIPO_CORPORATIVA)
                ContentFragment.nipA="0000"
            if(membresia.subTipoMembresia== MEMBRESIA_SUBTIPO_INAPAM)
                ContentFragment.nipA="0000"
        }
        dismiss()
    }

    override fun getCurrentMarca(value: String) {
        when (value) {
            ConstantsMarcas.MARCA_BEER_FACTORY -> doubleBounce?.color =
                ContextCompat.getColor(context!!, R.color.doradoBeer)
            ConstantsMarcas.MARCA_EL_FAROLITO, ConstantsMarcas.MARCA_TOKS -> doubleBounce?.color =
                ContextCompat.getColor(context!!, R.color.white)
        }
    }

    @SuppressLint("SuspiciousIndentation")
    private fun procesoPuntos(miembro: Miembro?) {
        ContentFragment.miembro = miembro
        if (ActividadPrincipal.banderaDialogMostrar != "1") {
            UserInteraction.showRedencionDePuntosDialog(fragmentManager)
        } else {
            ContentFragment.imm!!.hideSoftInputFromWindow(
                customDialogAcomerLayoutBinding!!.editTextNumeroMiembro.windowToken,
                0
            )
            val membresia = miembro!!.response.membresia
            if (membresia == null) UserInteraction.snackyException(
                activity,
                null,
                "Error al generar membresia.\nVuelva a consultarla"
            )
            else {
                UserInteraction.snackySuccess(activity, null, "Bienvenido " + membresia.nombre)
                if(ContentFragment.banderaCuentabuscada){
                    val marcaSelec = preferenceHelper!!.getString(ConstantsPreferences.PREF_MARCA_SELECCIONADA, null)
                    val cupones = presenter!!.generaCupones(miembro.response.cupones, marcaSelec)
                    val descuentos =presenter!!.generaDescuentos(miembro.response.cupones, marcaSelec)
                    val descuentosAplicados = ArrayList<Miembro.Response.Cupones>()
                    if(membresia!!.subTipoMembresia==MEMBRESIA_SUBTIPO_CORPORATIVA){
                        var idDesc = Utils.catalogDescuentosCorporativosINV()[membresia.empleadorMembresiaCorp!!.toUpperCase()]
                        var descuentos_obj = ContentFragment.catalogoDescuentosSelect!!.filter { it!!.idDesc==idDesc && it.tipo!=4 }
                        for(descuento_obj in descuentos_obj){
                            if(descuento_obj!!.tipo!=4) {
                                val dessss =(
                                    Miembro.Response.Cupones(
                                        "corporativo",
                                        descuento_obj!!.descuento,
                                        descuento_obj.descripcion,
                                        0,
                                        marcaSelec,
                                        descuento_obj.idDesc.toString(),
                                        descuento_obj.tipo.toString(),
                                        "dummy",
                                        "dummy",
                                        Miembro.Response.Cupones.Restricciones(
                                            "LOY_CANAL_TODOS",
                                            "LOY_TDP_DESC_PORCENTAJE",
                                            "LOY_SDP_DESC_CUENTA",
                                            descuento_obj.porcentaje,
                                            0,
                                            0,
                                            0,
                                            0,
                                            false,
                                            true,
                                            true,
                                            "PFADIC23/4"
                                        ),
                                        "Minimo de comprra: 0",
                                        0.0
                                    )
                                )
                                descuentos.add(dessss)
                                //descuentosAplicados.add(dessss)
                            }
                        }
                    }
                    if(membresia.subTipoMembresia== MEMBRESIA_SUBTIPO_INAPAM) {
                        var descuentos_obj = ContentFragment.catalogoDescuentosSelect!!.filter { it!!.idDesc==50 && it.tipo==4 }
                        for(descuento_obj in descuentos_obj){
                            if(descuento_obj!!.tipo==4) {
                                val dessss=(
                                    Miembro.Response.Cupones(
                                        "inapam",
                                        descuento_obj!!.descuento,
                                        descuento_obj.descuento,
                                        0,
                                        marcaSelec,
                                        descuento_obj.idDesc.toString(),
                                        descuento_obj.tipo.toString(),
                                        "dummy",
                                        "dummy",
                                        Miembro.Response.Cupones.Restricciones(
                                            "LOY_CANAL_TODOS",
                                            "LOY_TDP_DESC_PORCENTAJE",
                                            "LOY_SDP_DESC_CUENTA",
                                            descuento_obj.porcentaje,
                                            0,
                                            0,
                                            0,
                                            0,
                                            false,
                                            true,
                                            true,
                                            "PFADIC23/4"
                                        ),
                                        "Minimo de comprra: 0",
                                        0.0
                                    )
                                )
                                descuentos.add(dessss)
                                //descuentosAplicados.add(dessss)
                            }
                        }
                    }

                    if(cupones.size > 0 || descuentos.size>0){
                        UserInteraction.showDialogDescuentos(fragmentManager2,cupones,descuentos,descuentosAplicados)
                    }

                    else {
                        UserInteraction.snackyException(
                            activity,
                            null,
                            "No hay cupones disponibles"
                        )
                        UserInteraction.showRedencionDePuntosDialog(fragmentManager2)
                        UserInteraction.snackyWarning(activity,null,miembro.exceptionMessage)
                    }


                }else{
                    UserInteraction.snackyFail(activity,null,"Para aplicar un descuento, es necesario buscar una mesa")
                }
            }
            dismiss()

        }
    }

    private fun aceptaMiembro(membresia: Membresia?, miembro: Miembro?) {
        if (ContentFragment.getDescuento != null) {
            if (catalogDescuentosCorporativos().containsKey(ContentFragment.getDescuento!!.idDesc)) {
                if (membresia != null) {
                    if (membresia.subTipoMembresia.equals(
                            MEMBRESIA_SUBTIPO_CORPORATIVA,
                            ignoreCase = true
                        )
                    ) {
                        ContentFragment.isComprobanteDescuentoGRGPrintable = true
                        ContentFragment.miembroGRG = miembro
                        UserInteraction.showInputDialog(fragmentManager, "5", "Estafeta")
                        dismiss()
                        //redencionCuponPresenter.executeRedimeCuponRequest(nip);
                    } else {
                        hideSoftKeyboard(
                            customDialogAcomerLayoutBinding!!.editTextNumeroMiembro,
                            activity
                        )
                        UserInteraction.snackyFail(
                            activity, null, "El descuento seleccionado no es aplicable para " +
                                    membresia.nombre
                        )
                    }
                } else {
                    procesoPuntos(miembro)
                }
            } else {
                procesoPuntos(miembro)
            }
        } else {
            procesoPuntos(miembro)
        }
        dismiss()
    }

    override fun onExceptionResultAfiliado(onException: String?) {
        UserInteraction.snackyException(activity, view, onException)
    }

    override fun onFailResultAfiliado(onFail: String?) {
        UserInteraction.snackyFail(activity, view, onFail)
    }

    //================================================================================
    // Termina Presenter ConsultaAfiliado
    //================================================================================
    override fun onResultAfiliado(numeroMiembro: String?) {
        customDialogAcomerLayoutBinding!!.btnBuscarMiembro.isEnabled = true
        customDialogAcomerLayoutBinding!!.editTextNumeroMiembro.setText(numeroMiembro)
    }

    override fun onResultCuponGRG(numeroCupon: String?) {

        presenter!!.executeVerificaCuponGRG(numeroCupon!!)
    }

    override fun onResultCuponEngachement(numeroCupon: String?) {
        presenter!!.executeVerificaCuponEngachement(numeroCupon!!)
    }

    override fun onResultCuponEncuestas(numeroCupon: String?, producto: Producto) {
        try{
            UserInteraction.showWaitingDialog(fragmentManager2,"Aplicando cupón")
            UserInteraction.getDialogBuscarAfiliado.dismiss()
        }catch (e:Exception){
            e.printStackTrace()
            Log.d("Cupon", e.cause.toString())
        }

        presenter!!.executeRedimeCupon(numeroCupon, producto)
    }

    override fun onResultCuponEncuestas20(numeroCupon: String?) {
        try{
            UserInteraction.showWaitingDialog(fragmentManager2,"Aplicando cupón")
            UserInteraction.getDialogBuscarAfiliado.dismiss()
        }catch (e:Exception){
            e.printStackTrace()
            Log.d("Cupon", e.cause.toString())
        }

        presenter!!.executeRedimeCupon2(numeroCupon)
    }

    //cuando se escanea el QR y no se encuentra el producto
    override fun onFailCupon() {
        UserInteraction.getDialogWaiting.dismiss()
        UserInteraction.snackyFail(activity,null,"No cuentas con el producto en la cuenta")
    }

    companion object {
        private const val TAG = "DialogAComer"
        private var doubleBounce: DoubleBounce? = null

        /**
         * Type: Method.
         * Parent: DialogBuscarAfiliado.
         * Name: newInstance.
         *
         * @return DialogBuscarAfiliado
         * @Description.
         * @EndDescription.
         */
        @JvmStatic
        fun newInstance(): DialogBuscarAfiliado {
            val args = Bundle()
            val fragment = DialogBuscarAfiliado()
            fragment.arguments = args

            return fragment
        }
    }
}
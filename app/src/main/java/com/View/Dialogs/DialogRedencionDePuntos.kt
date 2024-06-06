/*
 * *
 *  * Created by Gerardo Ruiz on 11/11/20 5:09 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 11/11/20 5:09 PM
 *
 */
package com.View.Dialogs

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.Constants.ConstantsAComer
import com.Constants.ConstantsMarcas
import com.DI.BaseApplication
import com.Interfaces.AplicaDescuentoPuntosMVP
import com.Interfaces.RedencionDePuntosMVP
import com.Utilities.Files
import com.Utilities.PreferenceHelper
import com.Utilities.PreferenceHelperLogs
import com.Utilities.Utils.hideSoftKeyboard
import com.View.Fragments.ContentFragment
import com.View.UserInteraction
import com.github.ybq.android.spinkit.style.DoubleBounce
import com.innovacion.eks.beerws.R
import com.innovacion.eks.beerws.databinding.CustomDialogRendencionPuntosBinding
import com.orhanobut.hawk.Hawk
import javax.inject.Inject
import javax.inject.Named

/**
 * Type: Class.
 * Access: Public.
 * Name: DialogRedencionDePuntos.
 *
 * @Description.
 * @EndDescription.
 */
class DialogRedencionDePuntos : DialogFragment(), RedencionDePuntosMVP.View,
    AplicaDescuentoPuntosMVP.View {
    private var customDialogRendencionPuntosBinding: CustomDialogRendencionPuntosBinding? = null
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
     * The Redencion de puntos presenter.
     */
    @JvmField
    @Inject
    var presenter: RedencionDePuntosMVP.Presenter? = null

    /**
     * The Aplica descuento puntos presenter.
     */
    @JvmField
    @Inject
    var aplicaDescuentoPuntosPresenter: AplicaDescuentoPuntosMVP.Presenter? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        customDialogRendencionPuntosBinding =
            CustomDialogRendencionPuntosBinding.inflate(inflater, container, false)
        return customDialogRendencionPuntosBinding!!.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context.applicationContext as BaseApplication).plusAcomerClubSubComponent().inject(this)
        doubleBounce = DoubleBounce()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentManager2 = fragmentManager
        initDI()
        presenter!!.getMarca()
        customDialogRendencionPuntosBinding!!.progressBarRedimirPuntos.indeterminateDrawable =
            doubleBounce
        customDialogRendencionPuntosBinding!!.progressBarRedimirPuntos.visibility = View.INVISIBLE
        if (ContentFragment.miembro != null) {
            val membresia = ContentFragment.miembro!!.response.membresia
            if (membresia != null) {
                customDialogRendencionPuntosBinding!!.editTextPuntos.filters =
                    arrayOf<InputFilter>(LengthFilter(membresia.puntosNoCalificados.toString().length))
                customDialogRendencionPuntosBinding!!.dialogMessage.text =
                    membresia.nombre + " tiene " + membresia.puntosNoCalificados + " puntos"
                customDialogRendencionPuntosBinding!!.btnRedimirPuntos.setOnClickListener {
                    if (customDialogRendencionPuntosBinding!!.editTextPuntos.text.toString().isNotEmpty()
                    ) {
                        if( customDialogRendencionPuntosBinding!!.editTextPuntos.text.toString().toDouble()<=ContentFragment.cuenta.saldo || customDialogRendencionPuntosBinding!!.editTextPuntos.text.toString().toDouble()-ContentFragment.cuenta.saldo<=.99){

                                if(ContentFragment.nipA=="0000"){
                                    UserInteraction.showNipAComerDialog(fragmentManager, ContentFragment.miembro!!.response.membresia!!.numeroMembresia,
                                        object : DialogInputNipAComer.DialogButtonClickListener {
                                            override fun onPositiveButton(nip: String?) {
                                                run {
                                                    updateUI(true)
                                                    hideSoftKeyboard(getView(), activity)
                                                    ContentFragment.nipA=nip!!
                                                    presenter!!.executeRedencionDePuntos(
                                                        customDialogRendencionPuntosBinding!!.editTextPuntos.text.toString(),
                                                        nip
                                                    )
                                                }
                                            }

                                        }
                                    )
                                }else{
                                    updateUI(true)
                                    hideSoftKeyboard(getView(), activity)
                                    presenter!!.executeRedencionDePuntos(
                                        customDialogRendencionPuntosBinding!!.editTextPuntos.text.toString(),
                                        ContentFragment.nipA
                                    )




                            }
                        }else{
                            UserInteraction.snackyWarning(
                                null,
                                getView(),
                                "El numero ingresado es mayor al de la cuenta"
                            )
                        }

                    } else {
                        UserInteraction.snackyWarning(
                            null,
                            getView(),
                            "Ingrese numero de puntos a redimir"
                        )
                    }
                }
                customDialogRendencionPuntosBinding!!.buttonCancelar.setOnClickListener {
                    hideSoftKeyboard(getView(), activity)
                    dismiss()
                }
            } else {
                dismiss()
                UserInteraction.snackyFail(
                    activity,
                    null,
                    "Error al generar membresia\nVuelva a consultarla"
                )
            }
        } else {
            dismiss()
            UserInteraction.snackyFail(activity, null, "Vuelva a buscar al miembro")
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog!!.window!!.attributes.windowAnimations = R.style.DialogTheme
        dialog!!.setCancelable(false)
    }

    private fun initDI() {
        presenter!!.setView(this)
        presenter!!.setPreferences(sharedPreferences, preferenceHelper)
        presenter!!.setLogsInfo(preferenceHelperLogs, files)
        aplicaDescuentoPuntosPresenter!!.setView(this)
        aplicaDescuentoPuntosPresenter!!.setPreferences(sharedPreferences, preferenceHelper)
        aplicaDescuentoPuntosPresenter!!.setLogsInfo(preferenceHelperLogs, files)
    }

    private fun updateUI(isUpdated: Boolean) {
        customDialogRendencionPuntosBinding!!.progressBarRedimirPuntos.visibility =
            if (isUpdated) View.VISIBLE else View.INVISIBLE
        customDialogRendencionPuntosBinding!!.btnRedimirPuntos.isEnabled = !isUpdated
    }

    private fun ocultarDialog() {
        if (UserInteraction.getDialogWaiting != null && UserInteraction.getDialogWaiting.dialog != null) {
            UserInteraction.getDialogWaiting.dialog!!.dismiss()
        }
    }

    //================================================================================
    // Inicia Presenter RedencionDePuntos
    //================================================================================
    override fun onExceptionRedencionDePuntosResult(onExceptionResult: String?) {
        updateUI(false)
        UserInteraction.snackyException(null, view, onExceptionResult)
    }

    override fun onWarningRedencionDePuntosResult(onWarningResult: String?) {
        updateUI(false)
        UserInteraction.snackyWarning(null, view, onWarningResult)
    }

    override fun onFailRedencionDePuntosResult(onFailResult: String?) {
        updateUI(false)
        UserInteraction.snackyFail(null, view, onFailResult)
    }

    override fun onSuccessRedencionDePuntosResult(onSuccessResult: String?, mensaje: String?) {
        updateUI(false)
        customDialogRendencionPuntosBinding!!.dialogMessage.text = mensaje
        UserInteraction.snackySuccess(null, view, onSuccessResult)
        UserInteraction.showWaitingDialog(fragmentManager, "Aplicando Puntos")
        aplicaDescuentoPuntosPresenter!!.executeAplicaDescuento(customDialogRendencionPuntosBinding!!.editTextPuntos.text.toString())
    }

    override fun getCurrentMarca(value: String) {
        when (value) {
            ConstantsMarcas.MARCA_BEER_FACTORY -> doubleBounce?.color =
                ContextCompat.getColor(context!!, R.color.doradoBeer)
            ConstantsMarcas.MARCA_EL_FAROLITO, ConstantsMarcas.MARCA_TOKS -> doubleBounce?.color =
                ContextCompat.getColor(context!!, R.color.white)
        }
    }

    //================================================================================
    // Termina Presenter RedencionDePuntos
    //================================================================================
    //================================================================================
    // Inicia Presenter AplicaDescuento
    //================================================================================
    override fun onExceptionAplicaDescuentoResult(onExceptionResult: String?) {
        ocultarDialog()
        UserInteraction.snackyException(activity, null, onExceptionResult)
    }

    override fun onFailAplicaDescuentoResult(onFailResult: String?) {
        ocultarDialog()
        UserInteraction.snackyFail(activity, null, onFailResult)
    }

    override fun onSuccessAplicaDescuentoResult(onSuccessResult: String?) {
        ocultarDialog()
        ContentFragment.contentFragment!!.cuenta
        dismiss()
    } //================================================================================

    // Termina Presenter AplicaDescuento
    //================================================================================
    companion object {
        private const val TAG = "DialogRedencionDePuntos"
        private var doubleBounce: DoubleBounce? = null

        /**
         * Type: Method.
         * Parent: DialogRedencionDePuntos.
         * Name: newInstance.
         *
         * @return dialog redencion de puntos
         * @Description.
         * @EndDescription. dialog redencion de puntos.
         */
        @JvmStatic
        fun newInstance(): DialogRedencionDePuntos {
            val args = Bundle()
            val fragment = DialogRedencionDePuntos()
            fragment.arguments = args
            return fragment
        }
    }
}
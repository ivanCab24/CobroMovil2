/*
 * *
 *  * Created by Gerardo Ruiz on 11/26/20 4:37 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 11/26/20 4:37 PM
 *
 */
package com.View.Dialogs

import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.Constants.ConstantsMarcas
import com.Constants.ConstantsPreferences
import com.DI.BaseApplication
import com.Interfaces.ConsultaPagoMercadoPagoMVP
import com.Utilities.Files
import com.Utilities.PreferenceHelper
import com.Utilities.PreferenceHelperLogs
import com.View.Fragments.ContentFragment
import com.View.UserInteraction
import com.github.ybq.android.spinkit.style.DoubleBounce
import com.innovacion.eks.beerws.R
import com.innovacion.eks.beerws.databinding.CustomDialogQrMpBinding
import com.squareup.picasso.Picasso
import javax.inject.Inject
import javax.inject.Named

/**
 * Type: Class.
 * Access: Public.
 * Name: DialogQRMercadoPago.
 *
 * @Description.
 * @EndDescription.
 */
class DialogQRMercadoPago : DialogFragment(), ConsultaPagoMercadoPagoMVP.View {
    /**
     * The Custom dialog qr mp binding.
     */
    var customDialogQrMpBinding: CustomDialogQrMpBinding? = null
    private var clientID: String? = ""
    private var id: String? = ""
    private var handlerConsultaPago: Handler? = null
    private var handlerMostrarBoton: Handler? = null
    private var runnableConsultaPago: Runnable? = null
    private var doubleBounce: DoubleBounce? = null

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
     * The Consulta pago mercado pago presenter.
     */
    @JvmField
    @Inject
    var consultaPagoMercadoPagoPresenter: ConsultaPagoMercadoPagoMVP.Presenter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            clientID = arguments!!.getString(ARG_CLIENT_ID)
            id = arguments!!.getString(ARG_ID)
        } else {
            Toast.makeText(context, "Error instatiating DialogQRMercadoPago", Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        customDialogQrMpBinding = CustomDialogQrMpBinding.inflate(inflater, container, false)
        return customDialogQrMpBinding!!.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context.applicationContext as BaseApplication).plusMercadoPagoSubComponent().inject(this)
        doubleBounce = DoubleBounce()
        val marca = preferenceHelper!!.getString(ConstantsPreferences.PREF_MARCA_SELECCIONADA, null)
        when (marca) {
            ConstantsMarcas.MARCA_BEER_FACTORY -> doubleBounce!!.color =
                context.resources.getColor(R.color.progressbar_color_beer)
            ConstantsMarcas.MARCA_TOKS -> doubleBounce!!.color =
                context.resources.getColor(R.color.progressbar_color_toks)
            ConstantsMarcas.MARCA_EL_FAROLITO -> doubleBounce!!.color =
                context.resources.getColor(R.color.progressbar_color_farolito)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initDI()
        customDialogQrMpBinding!!.progressBarBuscaPedido.indeterminateDrawable = doubleBounce
        val URL =
            "http://" + preferenceHelper!!.getString(ConstantsPreferences.PREF_SERVER, null) + "/ws_pagomovil/images/" +
                    preferenceHelper!!.getString(ConstantsPreferences.PREF_CAJA_MP, null) + ".png"
        Log.i(TAG, "onViewCreated: $URL")
        Picasso.get().load(URL)
            .resize(400, 530)
            .into(customDialogQrMpBinding!!.imageViewQRMercadoPago)
        handlerConsultaPago = Handler()
        runnableConsultaPago = Runnable {
            handlerConsultaPago!!.postDelayed({
                updateUI(true)
                consultaPagoMercadoPagoPresenter!!.doMercadoPagoRequest()
            }, 0)
            handlerConsultaPago!!.postDelayed(runnableConsultaPago!!, 10000)
        }
        handlerConsultaPago!!.postDelayed(runnableConsultaPago!!, 10000)
        handlerMostrarBoton = Handler()
        handlerMostrarBoton!!.postDelayed({
            customDialogQrMpBinding!!.buttonCancelar.visibility = View.VISIBLE
        }, 30000)
        customDialogQrMpBinding!!.buttonCancelar.setOnClickListener {
            ContentFragment.contentFragment!!.binding!!.buttonCobrar.isEnabled = true
            dismiss()
        }
    }

    private fun initDI() {
        consultaPagoMercadoPagoPresenter!!.setView(this)
        consultaPagoMercadoPagoPresenter!!.setPreferences(sharedPreferences, preferenceHelper)
        consultaPagoMercadoPagoPresenter!!.setLogsInfo(preferenceHelperLogs, files)
    }

    private fun startHandler() {
        handlerConsultaPago!!.postDelayed(runnableConsultaPago!!, 10000)
    }

    private fun stopHandler() {
        handlerConsultaPago!!.removeCallbacks(runnableConsultaPago!!)
    }

    private fun updateUI(isExecuting: Boolean) {
        customDialogQrMpBinding!!.progressBarBuscaPedido.visibility =
            if (isExecuting) View.VISIBLE else View.INVISIBLE
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog!!.window!!.attributes.windowAnimations = R.style.DialogTheme
        dialog!!.setCancelable(false)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        stopHandler()
    }

    //================================================================================
    // Inicia PresenterConsultaPagoMercadoPago
    //================================================================================
    override fun onExceptionConsultaPagoMercadoPagoResult(onExceptionResult: String?) {
        updateUI(false)
        stopHandler()
        startHandler()
        UserInteraction.snackyException(null, view, onExceptionResult)
    }

    override fun onFailConsultaPagoMercadoPagoResult(onFailResult: String?) {
        updateUI(false)
        stopHandler()
        startHandler()
        Log.i(TAG, "onFailConsultaPagoMercadoPagoResult: $onFailResult")
    }

    override fun onSuccessConsultaPagoMercadoPagoResult(onSuccessResult: String?) {
        updateUI(false)
        dismiss()
        Log.i(TAG, "onSuccessConsultaPagoMercadoPagoResult: Pago encontrado: $onSuccessResult")
        ContentFragment.contentFragment!!.aplicaPagoMercadoPago(onSuccessResult)
    } //================================================================================

    // Termina PresenterConsultaPagoMercadoPago
    //================================================================================
    companion object {
        private const val TAG = "DialogQRMercadoPago"
        private const val ARG_CLIENT_ID = "clientID"
        private const val ARG_ID = "id"

        /**
         * Type: Method.
         * Parent: DialogQRMercadoPago.
         * Name: newInstance.
         *
         * @param clientID @PsiType:String.
         * @param id       @PsiType:String.
         * @return dialog qr mercado pago
         * @Description.
         * @EndDescription. dialog qr mercado pago.
         */
        @JvmStatic
        fun newInstance(clientID: String?, id: String?): DialogQRMercadoPago {
            val args = Bundle()
            args.putString(ARG_CLIENT_ID, clientID)
            args.putString(ARG_ID, id)
            val fragment = DialogQRMercadoPago()
            fragment.arguments = args
            return fragment
        }
    }
}
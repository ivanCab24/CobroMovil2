/*
 * *
 *  * Created by Gerardo Ruiz on 11/11/20 5:03 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 11/11/20 5:03 PM
 *
 */
package com.View.Dialogs

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.Constants.ConstantsPreferences
import com.DI.BaseApplication
import com.Interfaces.DialogNipMVP
import com.Utilities.PreferenceHelper
import com.Utilities.Utils
import com.Utilities.Utils.getDoubleBounce
import com.Utilities.Utils.hideSoftKeyboard
import com.View.Activities.ActividadPrincipal
import com.View.Fragments.ContentFragment
import com.View.Fragments.ContentFragment.Companion.miembro
import com.View.Fragments.ContentFragment.Companion.offline
import com.View.UserInteraction
import com.github.ybq.android.spinkit.style.DoubleBounce
import com.innovacion.eks.beerws.R
import com.innovacion.eks.beerws.databinding.InputDialogLayoutBinding
import javax.inject.Inject

/**
 * Type: Class.
 * Access: Public.
 * Name: DialogInputNipAComer.
 *
 * @Description.
 * @EndDescription.
 */
class DialogInputNipAComer
/**
 * Type: Method.
 * Parent: DialogInputNipAComer.
 * Name: DialogInputNipAComer.
 *
 * @param membresia @PsiType:String.
 * @Description.
 * @EndDescription.
 */(private val membresia: String) : DialogFragment(), DialogNipMVP.View {
    /**
     * Input dialog layout binding
     */
    private var inputDialogLayoutBinding: InputDialogLayoutBinding? = null
    private var nPresionado  = 0

    /**
     * Dialog button click listener
     */
    private var dialogButtonClickListener: DialogButtonClickListener? = null

    /**
     * Nip
     */
    private var nip: String? = null

    /**
     * Double bounce
     */
    private var doubleBounce: DoubleBounce? = null

    /**
     * Fragment manager2
     */
    private var fragmentManager2: FragmentManager? = null

    /**
     * The Preference helper.
     */
    @JvmField
    @Inject
    var preferenceHelper: PreferenceHelper? = null

    /**
     * The Presenter.
     */
    @JvmField
    @Inject
    var presenter: DialogNipMVP.Presenter? = null

    /**
     * Type: Interface.
     * Parent: DialogInputNipAComer.
     * Name: DialogButtonClickListener.
     */
    interface DialogButtonClickListener {
        /**
         * Type: Method.
         * Parent: DialogButtonClickListener.
         * Name: onPositiveButton.
         *
         * @param nip @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        fun onPositiveButton(nip: String?)
    }

    /**
     * Type: Method.
     * Parent: DialogInputNipAComer.
     * Name: onAttach.
     * @param context Context
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context.applicationContext as BaseApplication).plusAcomerClubSubComponent().inject(this)
        doubleBounce = getDoubleBounce(
            context,
            preferenceHelper!!.getString(ConstantsPreferences.PREF_MARCA_SELECCIONADA, null)
        )
    }

    /**
     * On create view
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        inputDialogLayoutBinding = InputDialogLayoutBinding.inflate(inflater, container, false)
        return inputDialogLayoutBinding!!.root
    }

    /**
     * On view created
     *
     * @param view
     * @param savedInstanceState
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentManager2 = getFragmentManager()
        initDI()
        ContentFragment.imm!!.toggleSoftInput(
            InputMethodManager.SHOW_FORCED,
            InputMethodManager.HIDE_IMPLICIT_ONLY
        )
        inputDialogLayoutBinding!!.progressBarNip.indeterminateDrawable = doubleBounce
        inputDialogLayoutBinding!!.aceptarButton.text = "Validar Nip"

        //inputDialogLayoutBinding.dialogMessage.setText("Ingrese Nip del miembro de A Comer Club");
        inputDialogLayoutBinding!!.dialogMessage.text = "Solicitando Nip"
        inputDialogLayoutBinding!!.dialogTitle.text = "Validación de nip"
        inputDialogLayoutBinding!!.inputEditText.filters = setMaxLenght()
        inputDialogLayoutBinding!!.inputEditText.requestFocus()
        presenter!!.doNipRequest(membresia)
        updateUI(true)
        inputDialogLayoutBinding!!.aceptarButton.setOnClickListener {
            hideSoftKeyboard(getView(), activity)
            if (nip != null) {
                if (nip == inputDialogLayoutBinding!!.inputEditText.text.toString()) {
                    dialogButtonClickListener!!.onPositiveButton(nip)
                    dismiss()
                    if (ActividadPrincipal.banderaDialogMostrar != "1") {
                        UserInteraction.showRedencionDePuntosDialog(fragmentManager)
                    } else {
                        if (ContentFragment.isConsultaMiembro) {
                            ContentFragment.isConsultaMiembro = false
                            UserInteraction.snackySuccess(
                                activity,
                                getView(),
                                "Bienvenido " + miembro!!.response.membresia!!.nombre
                            )

                        }
                    }
                } else if (nip != inputDialogLayoutBinding!!.inputEditText.text.toString()) {
                    UserInteraction.snackyWarning(
                        null,
                        getView(),
                        "Nip Invalido\nVuelva a intentar "
                    )
                } else if (inputDialogLayoutBinding!!.inputEditText.toString() == "") {
                    UserInteraction.snackyWarning(null, getView(), "Ingrese el nip")
                }
            } else {
                UserInteraction.snackyWarning(null, getView(), "Vuelva a identificar al miembro")
            }
        }
        inputDialogLayoutBinding!!.cancelarButton.setOnClickListener {
            ActividadPrincipal.banderaDialogMostrar = ""
            ocultarTeclado()
            dismiss()
            hideSoftKeyboard(getView(), activity)
            ContentFragment.isConsultaMiembro = false
            ContentFragment.nipA="0000"
        }

        inputDialogLayoutBinding!!.buttonToken!!.setOnClickListener {
            if(nPresionado<3){
                presenter!!.btnAction()
                nPresionado++
            }
            if(nPresionado==2){
                inputDialogLayoutBinding!!.buttonToken?.visibility= View.INVISIBLE
            }

            val timer = object: CountDownTimer(30000, 1000) {
                override fun onTick(millisUntilFinished: Long) {

                    var textMensaje  = ""
                    textMensaje = when (nPresionado) {
                        0 -> "Reintentar"
                        1 -> "Reintentar"
                        2 -> "Reintentar"
                        3 -> "Validar por Token"
                        else -> "Validar por Token"
                    }

                    inputDialogLayoutBinding!!.contadorText?.visibility = View.VISIBLE
                    inputDialogLayoutBinding!!.textView23?.visibility = View.VISIBLE
                    inputDialogLayoutBinding!!.buttonToken?.visibility=View.VISIBLE
                    inputDialogLayoutBinding!!.buttonToken?.isEnabled = false
                    inputDialogLayoutBinding!!.buttonToken?.setText(textMensaje)
                    inputDialogLayoutBinding!!.contadorText?.text=((Integer.parseInt(
                        millisUntilFinished.toString()
                    ) / 1000).toString())
                    Log.i("Counter",millisUntilFinished.toString())}
                override fun onFinish() {
                    inputDialogLayoutBinding!!.buttonToken?.isEnabled=true
                }
            }
            timer.start()
            offline = false
            //
        }
    }

    fun acceptByToken() {
        dialogButtonClickListener!!.onPositiveButton(nip)
        dismiss()
        if (ActividadPrincipal.banderaDialogMostrar != "1") {
            UserInteraction.showRedencionDePuntosDialog(fragmentManager)
        } else {
            if (ContentFragment.isConsultaMiembro) {
                ContentFragment.isConsultaMiembro = false
                UserInteraction.snackySuccess(
                    activity,
                    getView(),
                    "Bienvenido " + miembro!!.response.membresia!!.nombre
                )

            }
        }
    }

    /**
     * Ocultar teclado
     *
     */
    private fun ocultarTeclado() {
        if (view != null && activity != null)
            hideSoftKeyboard(view, activity)
    }

    /**
     * On resume
     *
     */
    override fun onResume() {
        super.onResume()
        //getDialog().getWindow().setLayout(500, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    /**
     * On activity created
     *
     * @param savedInstanceState
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog!!.window!!.attributes.windowAnimations = R.style.DialogTheme
        dialog!!.setCancelable(false)
    }

    /**
     * Type: Method.
     * Parent: DialogInputNipAComer.
     * Name: setDialogButtonClickListener.
     *
     * @Description.
     * @EndDescription.
     * @param: listener @PsiType:DialogButtonClickListener.
     */
    fun setDialogButtonClickListener(listener: DialogButtonClickListener?) {
        dialogButtonClickListener = listener
    }

    /**
     * Init d i
     *
     */
    private fun initDI() {
        presenter!!.setView(this)
    }

    /**
     * Update u i
     *
     * @param isRequesting
     */
    private fun updateUI(isRequesting: Boolean) {
        inputDialogLayoutBinding!!.progressBarNip.visibility =
            if (isRequesting) View.VISIBLE else View.GONE
        inputDialogLayoutBinding!!.inputEditText.visibility =
            if (isRequesting) View.GONE else View.VISIBLE
        inputDialogLayoutBinding!!.aceptarButton.visibility =
            if (isRequesting) View.GONE else View.VISIBLE
        inputDialogLayoutBinding!!.cancelarButton.visibility =
            if (isRequesting) View.GONE else View.VISIBLE
    }

    /**
     * Set max lenght
     *
     * @return
     */
    private fun setMaxLenght(): Array<InputFilter?> {
        val inputFilters = arrayOfNulls<InputFilter>(1)
        inputFilters[0] = LengthFilter(4)
        return inputFilters
    }

    /**
     * On exception nip result
     *
     * @param onException
     *///================================================================================
    // Inicia Nip Presenter
    //================================================================================
    override fun onExceptionNipResult(onException: String) {
        ActividadPrincipal.banderaDialogMostrar = ""
        ocultarTeclado()
        dismiss()
        UserInteraction.snackyException(activity, null, onException)
    }

    /**
     * On fail nip result
     *
     * @param onFail
     */
    override fun onFailNipResult(onFail: String) {
        ActividadPrincipal.banderaDialogMostrar = ""
        ocultarTeclado()
        dismiss()
        UserInteraction.snackyFail(activity, null, onFail)
    }

    /**
     * On success nip result
     *
     * @param nip
     */
    override fun onSuccessNipResult(nip: String) {

        updateUI(false)
        this.nip = nip
        ContentFragment.nipA = nip
        inputDialogLayoutBinding!!.dialogMessage.text = "Ingrese Nip"
        if(nip.equals("7777")){
            Utils.hideKeyboard(activity!!)
            offline = true
            dialogButtonClickListener!!.onPositiveButton(nip)
            dismiss()
            if (ActividadPrincipal.banderaDialogMostrar != "1") {
                UserInteraction.showRedencionDePuntosDialog(fragmentManager)
            } else {
                if (ContentFragment.isConsultaMiembro) {
                    ContentFragment.isConsultaMiembro = false
                    UserInteraction.snackyWarning(
                        activity,
                        view,
                        "${miembro!!.response.membresia!!.nombre}\nPeriodo Offline,  en breve estaremos restableciendo los servicios"
                    )
                }
            }
        }else{
            if(true){
                val timer = object: CountDownTimer(15000, 1000) {
                    override fun onTick(millisUntilFinished: Long) {
                        inputDialogLayoutBinding!!.contadorText?.visibility = View.VISIBLE
                        inputDialogLayoutBinding!!.textView23?.visibility = View.VISIBLE
                        inputDialogLayoutBinding!!.buttonToken?.visibility=View.VISIBLE
                        inputDialogLayoutBinding!!.buttonToken?.isEnabled = false
                        inputDialogLayoutBinding!!.buttonToken?.setText("Reintentar")
                        inputDialogLayoutBinding!!.contadorText?.text=((Integer.parseInt(
                            millisUntilFinished.toString()
                        ) / 1000).toString())
                        Log.i("Counter",millisUntilFinished.toString())}
                    override fun onFinish() {
                        inputDialogLayoutBinding!!.buttonToken?.isEnabled=true
                    }
                }
                timer.start()
            }

            offline = false
        }
    }
}
/*
 * *
 *  * Created by Gerardo Ruiz on 11/11/20 5:03 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 11/11/20 5:03 PM
 *
 */
package com.View.Dialogs

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.Utilities.Utils
import com.Verifone.VerifoneTaskManager
import com.View.Fragments.ContentFragment
import com.View.UserInteraction
import com.innovacion.eks.beerws.R
import com.innovacion.eks.beerws.databinding.InputDialogLayoutBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
/**
 * Type: Class.
 * Access: Public.
 * Name: DialogInput.
 *
 * @Description.
 * @EndDescription.
 */
class DialogInput : DialogFragment(), CoroutineScope {
    /**
     * Input dialog layout binding
     */
    private lateinit var inputDialogLayoutBinding: InputDialogLayoutBinding

    /**
     * Bandera
     */
    private var bandera: String = ""

    /**
     * Text
     */
    private var text: String = ""

    /**
     * M fragment manager
     */
    private var mFragmentManager: FragmentManager? = null

    /**
     * Job
     */
    private val job = Job()

    companion object {
        private const val ARG_BANDERA = "bandera"
        private const val ARG_TEXT = "text"

        fun newInstance(bandera: String, text: String): DialogInput {
            val dialogInput = DialogInput()
            val args = Bundle()
            args.putString(ARG_BANDERA, bandera)
            args.putString(ARG_TEXT, text)
            dialogInput.arguments = args
            return dialogInput
        }

    }

    /**
     *
     * @param inflater LayoutInflater
     * @param container ViewGroup?
     * @param savedInstanceState Bundle?
     * @return View
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        inputDialogLayoutBinding = InputDialogLayoutBinding.inflate(inflater, container, false)
        return inputDialogLayoutBinding.root
    }

    /**
     *
     * @param savedInstanceState Bundle?
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { arguments ->
            bandera = arguments.getString(ARG_BANDERA, "")
            text = arguments.getString(ARG_TEXT, "")
        } ?: run {
            Toast.makeText(activity, "Error savedInstanceState DialogInput", Toast.LENGTH_LONG)
                .show()
        }
    }

    /**
     * On view created
     *
     * @param view
     * @param savedInstanceState
     */
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mFragmentManager = fragmentManager
        ContentFragment.imm!!.toggleSoftInput(
            InputMethodManager.SHOW_FORCED,
            InputMethodManager.HIDE_IMPLICIT_ONLY
        )
        inputDialogLayoutBinding.aceptarButton.text = "Siguiente"
        inputDialogLayoutBinding.dialogMessage.text = "Estafeta Autorizador:"
        inputDialogLayoutBinding.dialogTitle.text = "Ingrese Estafeta Autorizador"
        inputDialogLayoutBinding.aceptarButton.setOnClickListener {
            if (inputDialogLayoutBinding.inputEditText.text.toString() != "") {
                ContentFragment.estafetaAutorizadora =
                    inputDialogLayoutBinding.inputEditText.text.toString()
                    UserInteraction.showTokenInputDialog(
                        fragmentManager,
                        bandera,
                        inputDialogLayoutBinding.inputEditText.text.toString()
                    )
                    dismiss()
            } else {
                UserInteraction.snackyWarning(null, getView(), "Ingrese la estafeta autorizadora")
            }
        }

        inputDialogLayoutBinding.cancelarButton.setOnClickListener {
            activity?.let { it1 -> Utils.hideSoftKeyboard(view, it1) }
            if (bandera == "6") launch { VerifoneTaskManager.limpiarTerminal() }
            dismiss()
        }

    }

    /**
     * On resume
     *
     */
    override fun onResume() {
        super.onResume()
        dialog?.let {
            //dialog.window?.setLayout(500, ViewGroup.LayoutParams.WRAP_CONTENT)
        }
    }

    /**
     * On activity created
     *
     * @param savedInstanceState
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog?.let { dialog ->
            dialog.setCancelable(false)
            dialog.window?.let { window ->
                window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                window.attributes.windowAnimations = R.style.DialogTheme
            }
        }
    }

    /**
     * Coroutine context
     */
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

}
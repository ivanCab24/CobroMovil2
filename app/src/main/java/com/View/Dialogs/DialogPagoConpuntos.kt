/*
 * *
 *  * Created by Gerardo Ruiz on 11/11/20 5:06 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 11/10/20 8:26 PM
 *
 */
package com.View.Dialogs

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.Interfaces.PagoConPuntosSelectionListner
import com.innovacion.eks.beerws.R
import com.innovacion.eks.beerws.databinding.QuestionDialogLayoutBinding

/**
 * Type: Class.
 * Access: Public.
 * Name: DialogPagoConpuntos.
 *
 * @Description.
 * @EndDescription.
 */
class DialogPagoConpuntos : DialogFragment() {
    private var questionDialogLayoutBinding: QuestionDialogLayoutBinding? = null
    private var pagoConPuntosSelectionListner: PagoConPuntosSelectionListner? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        questionDialogLayoutBinding =
            QuestionDialogLayoutBinding.inflate(inflater, container, false)
        return questionDialogLayoutBinding!!.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (targetFragment != null) pagoConPuntosSelectionListner =
            targetFragment as PagoConPuntosSelectionListner?
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        questionDialogLayoutBinding!!.dialogTitle.text = "Aviso"
        questionDialogLayoutBinding!!.dialogMessage.text = "¿Desea pagar con puntos?"
        questionDialogLayoutBinding!!.dialogButton.text = "Sí"
        questionDialogLayoutBinding!!.cancelarButton.text = "No"
        questionDialogLayoutBinding!!.dialogButton.setOnClickListener {
            dismiss()
            pagoConPuntosSelectionListner!!.pagoConPuntos()
        }
        questionDialogLayoutBinding!!.cancelarButton.setOnClickListener {
            dismiss()
            pagoConPuntosSelectionListner!!.pagoSinPuntos()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (dialog != null) {
            dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog!!.window!!.attributes.windowAnimations = R.style.DialogTheme
            dialog!!.setCancelable(false)
        }
    }

    companion object {
        /**
         * Type: Method.
         * Parent: DialogPagoConpuntos.
         * Name: newInstance.
         *
         * @return dialog pago conpuntos
         * @Description.
         * @EndDescription. dialog pago conpuntos.
         */
        @JvmStatic
        fun newInstance(): DialogPagoConpuntos {
            val args = Bundle()
            val fragment = DialogPagoConpuntos()
            fragment.arguments = args
            return fragment
        }
    }
}
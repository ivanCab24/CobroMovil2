/*
 * *
 *  * Created by Gerardo Ruiz on 11/11/20 4:54 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 11/11/20 4:54 PM
 *
 */
package com.View.Dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.Verifone.VerifoneTaskManager
import com.View.Fragments.ContentFragment
import com.View.UserInteraction
import com.innovacion.eks.beerws.R
import com.innovacion.eks.beerws.databinding.QuestionDialogLayoutBinding

/**
 * Type: Class.
 * Access: Public.
 * Name: DialogConfirmacionMetodoDePago.
 *
 * @Description.
 * @EndDescription.
 */
class DialogConfirmacionMetodoDePago : DialogFragment() {
    private var questionDialogLayoutBinding: QuestionDialogLayoutBinding? = null
    private var mensaje: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mensaje = arguments!!.getString(ARG_MENSAJE)
        } else {
            Toast.makeText(
                context,
                "Error instatiating DialogConfirmacionMetodoDePago",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        questionDialogLayoutBinding =
            QuestionDialogLayoutBinding.inflate(inflater, container, false)
        return questionDialogLayoutBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        questionDialogLayoutBinding!!.dialogTitle.text = "Confirmación"
        questionDialogLayoutBinding!!.dialogMessage.text = mensaje
        questionDialogLayoutBinding!!.dialogButton.setOnClickListener {
            dismiss()
            if(ContentFragment.contentFragment!!.presenter!!.revisa_saldo()){
                ContentFragment.contentFragment!!.realizarPago()
            }else{
                dismiss()
                VerifoneTaskManager.restPinpad()
                VerifoneTaskManager.desconectaPinpad()
                ContentFragment.contentFragment!!.binding!!.buttonCobrar.isEnabled = true
                UserInteraction.snackyException(activity!!,null,"El monto a cobrar es mayor al de la cuenta o ya se encuentra cerrada, vuelve a consultar la mesa")
            }


        }
        questionDialogLayoutBinding!!.cancelarButton.setOnClickListener {
            dismiss()
            VerifoneTaskManager.restPinpad()
            VerifoneTaskManager.desconectaPinpad()
            ContentFragment.contentFragment!!.binding!!.buttonCobrar.isEnabled = true
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog!!.window!!.attributes.windowAnimations = R.style.DialogTheme
        dialog!!.setCancelable(false)
    }

    companion object {
        private const val ARG_MENSAJE = "mensaje"

        /**
         * Type: Method.
         * Parent: DialogConfirmacionMetodoDePago.
         * Name: newInstance.
         *
         * @param mensaje @PsiType:String.
         * @return DialogConfirmacionMetodoDePago.
         * @Description.
         * @EndDescription.
         */
        @JvmStatic
        fun newInstance(mensaje: String?): DialogConfirmacionMetodoDePago {
            val args = Bundle()
            args.putString(ARG_MENSAJE, mensaje)
            val fragment = DialogConfirmacionMetodoDePago()
            fragment.arguments = args
            return fragment
        }
    }
}
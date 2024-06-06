package com.View.Dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.innovacion.eks.beerws.R
import com.innovacion.eks.beerws.databinding.CustomDialogLayoutBinding

/**
 * Type: Class.
 * Access: Public.
 * Name: CustomDialog.
 *
 * @Description.
 * Cuadro de diálogo Dummy, para poder enviar mensajes de cualquier tipo.
 * @EndDescription.
 */
class CustomDialog : DialogFragment() {

    lateinit var binding: CustomDialogLayoutBinding
    lateinit var dialogClickListener: DialogButtonClickListener

    private var titleText = ""
    private var positiveButtonText = ""
    private var descriptionText = ""

    companion object {
        const val ARG_POSTIVE_BUTTON_TEXT = "positiveButtonText"
        const val ARG_TITLE_TEXT = "titleText"
        const val ARG_DESCRIPTION_TEXT = "descriptionText"

        /**
         * Type: Method.
         * Parent: CustomDialog.
         * Name: newInstance.
         * @param positiveButtonText @PsiType:String.
         * @param titleText @PsiType:String.
         * @param descriptionText @PsiType:String.
         * @return CustomDialog.
         * @Description.
         * Está  función crea una nueva instancia de CustomDialog.
         * @EndDescription.
         */

        fun newInstance(
            positiveButtonText: String,
            titleText: String,
            descriptionText: String
        ): CustomDialog {
            val args = Bundle()
            args.putString(ARG_POSTIVE_BUTTON_TEXT, positiveButtonText)
            args.putString(ARG_TITLE_TEXT, titleText)
            args.putString(ARG_DESCRIPTION_TEXT, descriptionText)

            val fragment = CustomDialog()
            fragment.arguments = args
            return fragment
        }
    }

    interface DialogButtonClickListener {
        fun onPositiveButton()
    }

    /**
     * Type: Method.
     * Parent: CustomDialog.
     * Name: onCreate.
     * @param savedInstanceState @PsiType:Bundle.
     * @Description.
     * @EndDescription.
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            this.titleText = it.getString(ARG_TITLE_TEXT, "")
            this.descriptionText = it.getString(ARG_DESCRIPTION_TEXT, "")
            this.positiveButtonText = it.getString(ARG_POSTIVE_BUTTON_TEXT, "")
        } ?: run {
            Toast.makeText(context, "Error instatiating CustomDialog", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Type: Method.
     * Parent: CustomDialog.
     * Name: onCreateView.
     * @param inflater @PsiType:LayoutInflater.
     * @param container @PsiType:ViewGroup?.
     * @param savedInstanceState @PsiType:Bundle?.
     * @Description.
     * @EndDescription.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CustomDialogLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Type: Method.
     * Parent: CustomDialog.
     * Name: onViewCreated.
     * @param view @PsiType:View.
     * @param savedInstanceState @PsiType:Bundle?.
     * @Description.
     * @EndDescription.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.dialogTitle.text = titleText
        binding.dialogMessage.text = descriptionText
        binding.dialogButton.text = positiveButtonText
        binding.dialogButton.setOnClickListener { dialogClickListener.onPositiveButton() }

    }

    /**
     * Type: Method.
     * Parent: CustomDialog.
     * Name: onActivityCreated.
     * @param savedInstanceState @PsiType:Bundle?.
     * @Description.
     * @EndDescription.
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        dialog?.let { it ->
            it.window?.let {
                it.attributes.windowAnimations = R.style.DialogTheme
                it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }
        }
    }

    /**
     * Type: Method.
     * Parent: CustomDialog.
     * Name: setListener.
     * @param listener @PsiType:DialogButtonClickListener.
     * @Description.
     * Setea el listener con el que trabajará el diálogo.
     * @EndDescription.
     */

    fun setListener(listener: DialogButtonClickListener) {
        dialogClickListener = listener
    }
}
package com.View.Dialogs

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.Constants.ConstantsMarcas
import com.Constants.ConstantsPreferences
import com.DI.BaseApplication
import com.Utilities.PreferenceHelper
import com.github.ybq.android.spinkit.style.DoubleBounce
import com.innovacion.eks.beerws.R
import com.innovacion.eks.beerws.databinding.WaitDialogBinding
import javax.inject.Inject

/**
 * Type: Class.
 * Access: Public.
 * Name: DialogWaiting.
 *
 * @Description.
 * @EndDescription.
 */
class DialogWaiting : DialogFragment() {

    lateinit var binding: WaitDialogBinding
    lateinit var doubleBounce: DoubleBounce
    private var message: String? = ""
    private var btnText: String? = ""
    private var showbtn:Boolean = false
    private var dialogButtonClickListener: DialogButtonClickListener? = null
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
        fun onPositiveButton()
    }
    @Inject
    lateinit var preferenceHelper: PreferenceHelper

    companion object {
        fun newInstance(message: String): DialogWaiting = DialogWaiting().apply {
            this.message=message
        }
        fun newInstance(message: String,btnText:String, action:DialogButtonClickListener): DialogWaiting = DialogWaiting().apply {
            this.message=message
            this.btnText = btnText
            this.dialogButtonClickListener = action
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = WaitDialogBinding.inflate(inflater, container, false)
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        //message = arguments?.getString(ARG_MESSAGE)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context.applicationContext as BaseApplication).appComponent.inject(this)

        doubleBounce = DoubleBounce()

        when (preferenceHelper.getString(ConstantsPreferences.PREF_MARCA_SELECCIONADA, null)) {
            ConstantsMarcas.MARCA_BEER_FACTORY -> doubleBounce.color =
                ContextCompat.getColor(context, R.color.progressbar_color_beer)
            ConstantsMarcas.MARCA_TOKS -> doubleBounce.color =
                ContextCompat.getColor(context, R.color.progressbar_color_toks)
            ConstantsMarcas.MARCA_EL_FAROLITO -> doubleBounce.color =
                ContextCompat.getColor(context, R.color.progressbar_color_farolito)
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.progressBar.indeterminateDrawable = doubleBounce
        binding.dialogMessage.text = message
        if(this.dialogButtonClickListener!=null){
            binding.buttonLiberar.text = this.btnText
            binding.buttonLiberar.visibility = View.VISIBLE
            binding.buttonLiberar.setOnClickListener {
                dialogButtonClickListener!!.onPositiveButton()
                }
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        dialog?.let { it ->
            it.setCancelable(false)
            it.window?.let {
                it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                it.attributes.windowAnimations = R.style.DialogTheme
            }
        }

    }

    override fun onResume() {
        super.onResume()
        dialog?.window?.setLayout(700, ViewGroup.LayoutParams.WRAP_CONTENT)
    }
}
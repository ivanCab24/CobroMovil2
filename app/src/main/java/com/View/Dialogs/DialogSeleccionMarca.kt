package com.View.Dialogs

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.Constants.ConstantsMarcas
import com.Constants.ConstantsPreferences
import com.DI.BaseApplication
import com.Utilities.PreferenceHelper
import com.innovacion.eks.beerws.R
import com.innovacion.eks.beerws.databinding.CustomDialogSeleccionaTemaMarcaBinding
import javax.inject.Inject
/**
 * Type: Class.
 * Access: Public.
 * Name: DialogSeleccionMarca.
 *
 * @Description.
 * @EndDescription.
 */
class DialogSeleccionMarca : DialogFragment() {

    lateinit var binding: CustomDialogSeleccionaTemaMarcaBinding
    lateinit var mContext: Context

    @Inject
    lateinit var preferenceHelper: PreferenceHelper

    companion object {

        fun newInstance(): DialogSeleccionMarca {
            val args = Bundle()

            val fragment = DialogSeleccionMarca()
            fragment.arguments = args
            return fragment
        }

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CustomDialogSeleccionaTemaMarcaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context.applicationContext as BaseApplication).appComponent.inject(this)
        mContext = context
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imageButtonBFF.setOnClickListener {
            restartActivity()
            preferenceHelper.putString(
                ConstantsPreferences.PREF_MARCA_SELECCIONADA,
                ConstantsMarcas.MARCA_BEER_FACTORY
            )
        }

        binding.imageButtonToks.setOnClickListener {
            restartActivity()
            preferenceHelper.putString(
                ConstantsPreferences.PREF_MARCA_SELECCIONADA,
                ConstantsMarcas.MARCA_TOKS
            )
        }

        binding.imageButtonFarolito.setOnClickListener {
            restartActivity()
            preferenceHelper.putString(
                ConstantsPreferences.PREF_MARCA_SELECCIONADA,
                ConstantsMarcas.MARCA_EL_FAROLITO
            )
        }


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        dialog?.let { it ->
            it.setCancelable(false)
            it.window?.let {
                it.attributes.windowAnimations = R.style.DialogTheme
                it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }
        }

    }

    private fun restartActivity() {

        activity?.let {
            val intent = it.intent
            it.finish()
            it.startActivity(intent)
        }

    }

}
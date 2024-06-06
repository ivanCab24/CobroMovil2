package com.View.Dialogs

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.Constants.ConstantsPreferences
import com.DI.BaseApplication
import com.Interfaces.ResetKeysMVP
import com.Utilities.PreferenceHelper
import com.Utilities.Utils
import com.github.ybq.android.spinkit.style.DoubleBounce
import com.innovacion.eks.beerws.R
import com.innovacion.eks.beerws.databinding.WaitDialogButtonsBinding
import javax.inject.Inject
/**
 * Type: Class.
 * Access: Public.
 * Name: DialogWaitingResetKeys.
 *
 * @Description.
 * @EndDescription.
 */
class DialogWaitingResetKeys : DialogFragment(), ResetKeysMVP.View {

    lateinit var binding: WaitDialogButtonsBinding
    lateinit var doubleBounce: DoubleBounce

    companion object {

        fun newInstance(): DialogWaitingResetKeys {
            val args = Bundle()

            val fragment = DialogWaitingResetKeys()
            fragment.arguments = args
            return fragment
        }

    }

    @Inject
    lateinit var preferenceHelper: PreferenceHelper

    @Inject
    lateinit var presenter: ResetKeysMVP.Presenter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = WaitDialogButtonsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (context.applicationContext as BaseApplication).plusResetKeysSubcomponent().inject(this)

        doubleBounce = Utils.getDoubleBounce(
            context,
            preferenceHelper.getString(ConstantsPreferences.PREF_MARCA_SELECCIONADA, null)
        )

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter.setView(this)

        with(binding) {

            progressBar.indeterminateDrawable = doubleBounce

            dialogMessage.text = "Esperando a la pinpad..."

            btnReintentar.setOnClickListener { presenter.initResetKeysTask() }
            btnCancelar.setOnClickListener { dismiss() }

        }

        presenter.initResetKeysTask()


    }

    override fun onResume() {
        super.onResume()
        dialog?.window?.setLayout(700, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog?.let { dialog ->
            isCancelable = false
            dialog.window?.let { window ->
                window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                window.attributes.windowAnimations = R.style.DialogTheme
            }
        }
    }

    //================================================================================
    // Inicia Presenter ResetKeys
    //================================================================================
    override fun setMessage(message: String) {
        binding.dialogMessage.text = message
    }

    override fun updateUI(isUpdating: Boolean) {
        binding.progressBar.visibility = if (isUpdating) View.GONE else View.VISIBLE
        binding.layoutButtons.visibility = if (isUpdating) View.VISIBLE else View.GONE
    }

    override fun updateUIFinished(isFinished: Boolean) {

        with(binding) {

            layoutButtons.visibility = View.VISIBLE
            btnCancelar.visibility = View.GONE
            progressBar.visibility = View.GONE
            btnReintentar.text = "Aceptar"
            btnReintentar.setOnClickListener { dismiss() }

        }

    }

    override fun onExceptionResult(onException: String) {
        binding.dialogMessage.text = onException
    }

    override fun onFailResult(onFail: String) {
        binding.dialogMessage.text = onFail
    }

    override fun onSuccessResult(onSuccess: String) {
        binding.dialogMessage.text = onSuccess
    }

}
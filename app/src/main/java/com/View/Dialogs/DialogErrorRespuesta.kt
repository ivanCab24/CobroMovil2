package com.View.Dialogs

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.Constants.ConstantsPreferences
import com.DI.BaseApplication
import com.Interfaces.DialogErrorRespuestaMVP
import com.Utilities.PreferenceHelper
import com.Utilities.Utils
import com.View.Fragments.ContentFragment
import com.View.UserInteraction
import com.github.ybq.android.spinkit.style.DoubleBounce
import com.innovacion.eks.beerws.R
import com.innovacion.eks.beerws.databinding.CustomDialogLayoutBinding
import javax.inject.Inject
/**
 * Type: Class.
 * Access: Public.
 * Name: DialogErrorRespuesta.
 *
 * @Description.
 * @EndDescription.
 */
class DialogErrorRespuesta : DialogFragment(), DialogErrorRespuestaMVP.View {

    private val TAG = "DIalogErrorRespuestaK"
    private lateinit var binding: CustomDialogLayoutBinding

    private var title = ""
    private var message = ""

    private var doubleBounce: DoubleBounce? = null

    @Inject
    lateinit var preferenceHelper: PreferenceHelper

    @Inject
    lateinit var dialogErrorRespuestaPresenter: DialogErrorRespuestaMVP.Presenter

    companion object {

        private const val ARG_TITULO = "titulo"
        private const val ARG_MENSAJE = "mensaje"
        /**
         * Type: Method.
         * Parent: DialogErrorRespuesta.
         * Name: newInstance.
         *
         * @param mensaje @PsiType:String.
         * @param titulo @PsiType:String.
         * @return DialogErrorRespuesta.
         * @Description.
         * @EndDescription.
         */
        fun newInstance(titulo: String, mensaje: String): DialogErrorRespuesta {
            val args = Bundle()
            args.putString(ARG_TITULO, titulo)
            args.putString(ARG_MENSAJE, mensaje)

            val fragment = DialogErrorRespuesta()
            fragment.arguments = args
            return fragment
        }


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { arguments ->
            this.title = arguments.getString(ARG_TITULO, "")
            this.message = arguments.getString(ARG_MENSAJE, "")
        } ?: run {
            Toast.makeText(
                context,
                "Error al inicializar DialogErrorRespuesta",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CustomDialogLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        activity?.let {

            (context.applicationContext as BaseApplication).plusDialogErrorRespuestaSubComponent()
                .inject(this)

        }

        doubleBounce = Utils.getDoubleBounce(
            context,
            dialogErrorRespuestaPresenter.getMarca()
        )

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initDI()
        initUI()

    }

    private fun initDI() {
        dialogErrorRespuestaPresenter.setView(this)
        dialogErrorRespuestaPresenter.setActivity(activity)
    }


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

    private fun initUI() {

        with(binding) {
            progressBarImprimirError.indeterminateDrawable = doubleBounce
            dialogButton.transformationMethod = null
            dialogTitle.text = title
            dialogMessage.text = message

            dialogButton.setOnClickListener {

                if (preferenceHelper.getString(ConstantsPreferences.PREF_MODELO_IMPRESORA, null) == "EPSON") {

                    dialogErrorRespuestaPresenter.printComprobante()

                } else {

                    dialogErrorRespuestaPresenter.printComprobanteBixolon()

                }

            }
        }
    }

    //================================================================================
    // Inicia Presenter DialogErrorRespuesta
    //================================================================================
    override fun changeDialogMessage(message: String) {
        binding.dialogMessage.text = message
    }

    override fun updateDialogUI() {
        with(binding) {
            progressBarImprimirError.visibility = View.VISIBLE
            dialogButton.visibility = View.INVISIBLE
        }
    }

    override fun updateContentFragmentUI() {
        ContentFragment.contentFragment!!.controlsSwitch(false)
    }

    override fun ocultarDialog() {
        dismiss()
    }

    override fun onExceptionResultPrinter(onException: String) {
        activity?.let { UserInteraction.snackyException(it, null, onException) }
    }

    override fun onFailResultPrinter(onFail: String) {
        activity?.let { UserInteraction.snackyFail(it, null, onFail) }
    }

    override fun onWarningResultPrinter(onWarning: String) {
        activity?.let { UserInteraction.snackyWarning(it, null, onWarning) }
    }

    //================================================================================
    // Termina Presenter DialogErrorRespuesta
    //================================================================================

}
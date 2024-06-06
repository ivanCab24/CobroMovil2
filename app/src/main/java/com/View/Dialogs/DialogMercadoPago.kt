package com.View.Dialogs

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.Constants.ConstantsMarcas
import com.Constants.ConstantsPreferences
import com.DI.BaseApplication
import com.Interfaces.LevantaPedidoMVP
import com.Utilities.Files
import com.Utilities.PreferenceHelper
import com.Utilities.PreferenceHelperLogs
import com.View.Fragments.ContentFragment
import com.View.UserInteraction
import com.github.ybq.android.spinkit.style.DoubleBounce
import com.innovacion.eks.beerws.R
import com.innovacion.eks.beerws.databinding.CustomDialogMercadopagoLayoutBinding
import com.webservicestasks.ToksWebServiceErrors
import com.webservicestasks.ToksWebServicesConnection
import javax.inject.Inject
import javax.inject.Named

/**
 * Type: Class.
 * Access: Public.
 * Name: DialogMercadoPago.
 *
 * @Description.
 * @EndDescription.
 */
class DialogMercadoPago : DialogFragment(), ToksWebServicesConnection, ToksWebServiceErrors,
    LevantaPedidoMVP.View {
    /**
     * Double bounce
     */
    private var doubleBounce: DoubleBounce? = null

    /**
     * Custom dialog mercadopago layout binding
     */
    private var customDialogMercadopagoLayoutBinding: CustomDialogMercadopagoLayoutBinding? = null

    /**
     * Fragment manager2
     */
    private var fragmentManager2: FragmentManager? = null

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
     * The Levanta pedido presenter.
     */
    @JvmField
    @Inject
    var levantaPedidoPresenter: LevantaPedidoMVP.Presenter? = null

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
        customDialogMercadopagoLayoutBinding =
            CustomDialogMercadopagoLayoutBinding.inflate(inflater, container, false)
        return customDialogMercadopagoLayoutBinding!!.root
    }

    /**
     * On attach
     *
     * @param context
     */
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
        customDialogMercadopagoLayoutBinding!!.progressBarQrMercadoPago.indeterminateDrawable =
            doubleBounce
        customDialogMercadopagoLayoutBinding!!.progressBarQrMercadoPago.visibility = View.VISIBLE
        customDialogMercadopagoLayoutBinding!!.dialogTitle.text = "Mercado Pago"
        customDialogMercadopagoLayoutBinding!!.dialogMessage.text = "Levantando pedido"
        levantaPedidoPresenter!!.executeLevantaPedido()
        customDialogMercadopagoLayoutBinding!!.imageButtonClose.setOnClickListener { v: View? ->
            ContentFragment.contentFragment!!.binding!!.buttonCobrar.isEnabled = true
            dismiss()
        }
    }

    /**
     * Init d i
     *
     */
    private fun initDI() {
        levantaPedidoPresenter!!.setView(this)
        levantaPedidoPresenter!!.setPreferences(sharedPreferences, preferenceHelper)
        levantaPedidoPresenter!!.setLogsInfo(preferenceHelperLogs, files)
    }

    /**
     * Update u i
     *
     * @param isExecuting
     */
    private fun updateUI(isExecuting: Boolean) {
        customDialogMercadopagoLayoutBinding!!.progressBarQrMercadoPago.visibility =
            if (isExecuting) View.VISIBLE else View.INVISIBLE
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
     * On levanta pedido exception result
     *
     * @param onExceptionResult
     *///================================================================================
    // Inicia Presenter LevantaPedido
    //================================================================================
    override fun onLevantaPedidoExceptionResult(onExceptionResult: String?) {
        updateUI(false)
        UserInteraction.snackyException(null, view, onExceptionResult)
    }

    /**
     * On levanta pedido fail result
     *
     * @param onFailResult
     */
    override fun onLevantaPedidoFailResult(onFailResult: String?) {
        updateUI(false)
        UserInteraction.snackyFail(null, view, onFailResult)
    }

    /**
     * On success levanta pedido result
     *
     * @param id
     * @param clientID
     */
    override fun onSuccessLevantaPedidoResult(id: String?, clientID: String?) {
        updateUI(false)
        UserInteraction.showQRMercadoPagoDialog(fragmentManager, id, clientID)
        dismiss()
    } //================================================================================

    // Termina Presenter LevantaPedido
    //================================================================================
    companion object {
        private const val TAG = "DialogMercadoPago"

        /**
         * Type: Method.
         * Parent: DialogMercadoPago.
         * Name: newInstance.
         *
         * @return dialog mercado pago
         * @Description.
         * @EndDescription. dialog mercado pago.
         */
        @JvmStatic
        fun newInstance(): DialogMercadoPago {
            val args = Bundle()
            val fragment = DialogMercadoPago()
            fragment.arguments = args
            return fragment
        }
    }
}
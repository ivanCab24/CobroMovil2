package com.View.Dialogs

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.Constants.ConstantsMarcas
import com.Constants.ConstantsPreferences
import com.Controller.BD.DAO.CatalogoBinesDAO
import com.Controller.BD.DAO.CuentaCobradaDAO
import com.DI.BaseApplication
import com.DataModel.SaleVtol
import com.Interfaces.AplicaPagoTarjetaPresenter
import com.Interfaces.C54TaskMPV
import com.Interfaces.InsertaCuentaCobradaMVP
import com.Interfaces.PagoConPuntosSelectionListner
import com.Interfaces.PrinterMessagePresenter
import com.Interfaces.SaleVtolMVP
import com.Interfaces.SelectedOptionRetryPagoConTarjeta
import com.Interfaces.SolicitaTarjetaMVP
import com.Interfaces.TercerMensajeMVP
import com.Utilities.Files
import com.Utilities.PreferenceHelper
import com.Utilities.PreferenceHelperLogs
import com.Utilities.Utils.imprimeTicketError
import com.Verifone.EMVResponse
import com.Verifone.VerifoneTaskManager
import com.View.Fragments.ContentFragment
import com.View.UserInteraction
import com.github.ybq.android.spinkit.style.DoubleBounce
import com.innovacion.eks.beerws.R
import com.innovacion.eks.beerws.databinding.WaitDialogBinding
import javax.inject.Inject
import javax.inject.Named

/**
 * Type: Class.
 * Access: Public.
 * Name: DialogWaitingPagoTarjeta.
 *
 * @Description.
 * @EndDescription.
 */
class DialogWaitingPagoTarjeta : DialogFragment(), SolicitaTarjetaMVP.View,
    SaleVtolMVP.View, C54TaskMPV.View, PagoConPuntosSelectionListner,
    AplicaPagoTarjetaPresenter.View, TercerMensajeMVP.View, SelectedOptionRetryPagoConTarjeta,
    PrinterMessagePresenter.View, InsertaCuentaCobradaMVP.View {
    private var waitDialogBinding: WaitDialogBinding? = null
    private var doubleBounce: DoubleBounce? = null
    private var response: EMVResponse? = null
    private var year: String? = null
    private var month: String? = null
    private var day: String? = null
    private var transactionType: String? = null
    private var tag9f27: String? = null
    private var accionTercerMensaje: String? = null
    private var banderaReintentar: String? = null
    private var idFPGO = 0
    private var tipoFPGO = 0
    private var isTransaccionExitosa = false
    private var saleVtol: SaleVtol? = null
    private var fragmentManager2: FragmentManager? = null
    private var DIALOG_DURATION = 5000

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
     * The Cuenta cobrada dao.
     */
    @JvmField
    @Inject
    var cuentaCobradaDAO: CuentaCobradaDAO? = null

    /**
     * The Catalogo bines dao.
     */
    @JvmField
    @Inject
    var catalogoBinesDAO: CatalogoBinesDAO? = null

    /**
     * The Solicita tarjeta presenter.
     */
    @JvmField
    @Inject
    var solicitaTarjetaPresenter: SolicitaTarjetaMVP.Presenter? = null

    /**
     * The Sale vtol presenter.
     */
    @JvmField
    @Inject
    var saleVtolPresenter: SaleVtolMVP.Presenter? = null

    /**
     * The C 54 task presenter.
     */
    @JvmField
    @Inject
    var c54TaskPresenter: C54TaskMPV.Presenter? = null

    /**
     * The Aplica pago presenter.
     */
    @JvmField
    @Inject
    var presenter: AplicaPagoTarjetaPresenter.Presenter? = null

    /**
     * The Tercer mensaje presenter.
     */
    @JvmField
    @Inject
    var tercerMensajePresenter: TercerMensajeMVP.Presenter? = null

    /**
     * The Printer message presenter.
     */
    @JvmField
    @Named("ImprimeTicket")
    @Inject
    var printerMessagePresenter: PrinterMessagePresenter.Presenter? = null

    /**
     * The Cuenta cobrada presenter.
     */
    @JvmField
    @Inject
    var cuentaCobradaPresenter: InsertaCuentaCobradaMVP.Presenter? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        waitDialogBinding = WaitDialogBinding.inflate(inflater, container, false)
        return waitDialogBinding!!.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context.applicationContext as BaseApplication).plusPagoConTarjetaSubComponent(activity!!)
            .inject(this)
        doubleBounce = DoubleBounce()
        initDI()
        presenter!!.getMarca()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentManager2 = getFragmentManager()
        waitDialogBinding!!.progressBar.indeterminateDrawable = doubleBounce
      //  waitDialogBinding!!.dialogTitle.text = "Aviso"
        waitDialogBinding!!.dialogMessage.text = "Realizando operaciones en la pinpad"
        solicitaTarjetaPresenter!!.executeSolicitaTarjeta()
    }

    private fun initDI() {
        printerMessagePresenter!!.setView(this)
        printerMessagePresenter!!.setPreferences(preferenceHelper)
        solicitaTarjetaPresenter!!.setView(this)
        solicitaTarjetaPresenter!!.setPreferences(sharedPreferences, preferenceHelper)
        solicitaTarjetaPresenter!!.setLogsInfo(preferenceHelperLogs, files)
        solicitaTarjetaPresenter!!.setBinesDao(catalogoBinesDAO)
        solicitaTarjetaPresenter!!.setActivity(activity)
        saleVtolPresenter!!.setView(this)
        saleVtolPresenter!!.setPreferences(sharedPreferences, preferenceHelper)
        saleVtolPresenter!!.setLogsInfo(preferenceHelperLogs, files)
        c54TaskPresenter!!.setView(this)
        c54TaskPresenter!!.setPreferences(sharedPreferences, preferenceHelper)
        c54TaskPresenter!!.setLogsInfo(preferenceHelperLogs, files)
        presenter!!.setView(this)
        presenter!!.setPreferences(sharedPreferences, preferenceHelper)
        presenter!!.setLogsInfo(preferenceHelperLogs, files)
        tercerMensajePresenter!!.setView(this)
        tercerMensajePresenter!!.setPreferences(sharedPreferences, preferenceHelper)
        tercerMensajePresenter!!.setLogsInfo(preferenceHelperLogs, files)
        cuentaCobradaPresenter!!.setView(this)
        cuentaCobradaPresenter!!.setActivity(activity)
        cuentaCobradaPresenter!!.setPreferences(sharedPreferences, preferenceHelper)
        cuentaCobradaPresenter!!.setLogsInfo(preferenceHelperLogs, files)
        cuentaCobradaPresenter!!.setCuentaCobradaDao(cuentaCobradaDAO)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog!!.window!!.attributes.windowAnimations = R.style.DialogTheme
        dialog!!.setCancelable(false)
    }

    override fun onResume() {
        super.onResume()
        dialog!!.window!!.setLayout(700, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    //================================================================================
    // Inicia Presenter SolicitaTarjeta
    //================================================================================
    override fun onExceptionSolicitaTarjetaResult(onException: String?) {
        dismiss()
        UserInteraction.showDialogErrorRespuesta(fragmentManager, "Exception", onException)
    }

    override fun onFailSolicitaTarjetaResult(onFail: String?) {
        dismiss()
        UserInteraction.showDialogErrorRespuesta(fragmentManager, "Aviso", onFail)
        dismissDialogAfterDelay()
    }

    override fun onSuccessSolicitaTarjetaResult(
        emvResponse: EMVResponse?,
        year: String?,
        month: String?,
        dia: String?,
        idFPGO: Int,
        tipoFPGO: Int,
        banderaPuntos: String?,
        transactionType: String?
    ) {
        response = emvResponse
        this.year = year
        this.month = month
        day = dia
        this.idFPGO = idFPGO
        this.tipoFPGO = tipoFPGO
        this.transactionType = transactionType
        waitDialogBinding!!.dialogMessage.text = "Enviando SaleVtol"
        if (banderaPuntos == "0") {
            saleVtolPresenter!!.executeSaleVtol(emvResponse, "0", transactionType, year, month, dia)
        } else {
            UserInteraction.showCustomPagoConPuntosDialog(this, fragmentManager)
        }
    }

    override fun pagoConPuntos() {
        saleVtolPresenter!!.executeSaleVtol(response, "1S", transactionType, year, month, day)
    }

    override fun pagoSinPuntos() {
        saleVtolPresenter!!.executeSaleVtol(response, "1N", transactionType, year, month, day)
    }

    //================================================================================
    // Termina Presenter SolicitaTarjeta
    //================================================================================
    //================================================================================
    // Inicia Presenter SaleVtol
    //================================================================================
    override fun onExceptionSaleVtolResult(onExceptionResult: String?) {

        UserInteraction.showDialogErrorRespuesta(fragmentManager, "Exception", onExceptionResult)
        dismiss()
    }

    override fun onFailSaleVtolResult(onFailResult: String?) {
        UserInteraction.showDialogErrorRespuesta(
            fragmentManager,
            "Sale vtol",
            "Error $onFailResult"
        )
        dismiss()
    }

    override fun onSuccessVtolResult(saleVtol: SaleVtol?) {
        this.saleVtol = saleVtol
        ContentFragment.saleVtol = saleVtol
        waitDialogBinding!!.dialogMessage.text = "Enviando C54"
        c54TaskPresenter!!.executeC54Task(
            saleVtol,
            ContentFragment.contentFragment!!.btManager,
            transactionType
        )
    }

    //================================================================================
    // Termina Presenter SaleVtol
    //================================================================================
    //================================================================================
    // Inicia Presenter C54Task
    //================================================================================
    override fun onExceptionC54Result(onExceptionResult: String?) {
        UserInteraction.showDialogErrorRespuesta(fragmentManager, "Error", onExceptionResult)
        dismiss()
    }

    override fun onFailC54Result(
        onFailResult: String?,
        tag9f27: String?,
        isTransaccionExitosa: Boolean
    ) {
        UserInteraction.showCustomDialog(
            fragmentManager,
            onFailResult,
            object : CustomDialog.DialogButtonClickListener {
                override fun onPositiveButton() {
                    ContentFragment.contentFragment!!.binding!!.buttonCobrar.isEnabled = true
                    UserInteraction.getCustomDialog.dismiss()
                }
            })
        tercerMensajePresenter!!.executeTercerMensaje(
            "Rollback",
            response,
            saleVtol,
            tag9f27,
            isTransaccionExitosa
        )
        if (!preferenceHelper!!.getString(ConstantsPreferences.PREF_IMPRESORA, null).isEmpty()) {
            preferenceHelper!!.removePreference(ConstantsPreferences.PREF_CODBARRAS)
            printerMessagePresenter!!.imprimirTest(null, imprimeTicketError())
        } else {
            UserInteraction.snackyWarning(activity, null, "Impresora no configurada")
        }
        dismiss()
    }

    override fun onSuccessC54Result(
        isTransaccionExitosa: Boolean,
        outpuError: String?,
        tag9f27: String?
    ) {
        this.tag9f27 = tag9f27
        this.isTransaccionExitosa = isTransaccionExitosa
        cuentaCobradaPresenter!!.executeinsertaCuentaCobradaTask(saleVtol, response)
        waitDialogBinding!!.dialogMessage.text = "Aplicando Pago"
        presenter!!.setVariables(saleVtol, response, idFPGO, tipoFPGO)
        presenter!!.executeAplicaPago()
    }

    //================================================================================
    // Termina Presenter C54Task
    //================================================================================
    //================================================================================
    // Inicia Presenter AplicaPago
    //================================================================================
    override fun onExceptionAplicaPagoResult(onExceptionResult: String?) {
        banderaReintentar = "1"
        UserInteraction.showDialogReintentar(
            fragmentManager,
            this,
            "Aplica Pago",
            "Exception $onExceptionResult"
        )
    }

    override fun onFailAplicaPagoResult(onFailResult: String?) {
        banderaReintentar = "1"
        UserInteraction.showDialogReintentar(
            fragmentManager,
            this,
            "Aplica Pago",
            "Error $onFailResult"
        )
    }

    override fun onSuccessAplicaPagoResult() {
        preferenceHelper!!.putString(
            ConstantsPreferences.PREF_FOLIO_BOUCHER,
            preferenceHelper!!.getString(ConstantsPreferences.PREF_FOLIO, null)
        )
        waitDialogBinding!!.dialogMessage.text = "Ejecutando Tercer Mensaje"
        if (transactionType == "CHIP") {
            if (tag9f27 != "00") {
                accionTercerMensaje = "Commit"
                tercerMensajePresenter!!.executeTercerMensaje(
                    "Commit",
                    response,
                    saleVtol,
                    tag9f27,
                    isTransaccionExitosa
                )
            } else {
                accionTercerMensaje = "Rollback"
                tercerMensajePresenter!!.executeTercerMensaje(
                    "Rollback",
                    response,
                    saleVtol,
                    tag9f27,
                    isTransaccionExitosa
                )
            }
        } else {
            accionTercerMensaje = "Commit"
            tercerMensajePresenter!!.executeTercerMensaje(
                "Commit",
                response,
                saleVtol,
                tag9f27,
                isTransaccionExitosa
            )
        }
    }

    override fun getCurrentMarca(value: String) {
        when (value) {
            ConstantsMarcas.MARCA_BEER_FACTORY -> doubleBounce?.color =
                ContextCompat.getColor(context!!, R.color.doradoBeer)
            ConstantsMarcas.MARCA_EL_FAROLITO, ConstantsMarcas.MARCA_TOKS -> doubleBounce?.color =
                ContextCompat.getColor(context!!, R.color.white)
        }
    }

    //================================================================================
    // Termina Presenter AplicaPago
    //================================================================================
    //================================================================================
    // Inicia Presenter TercerMensaje
    //================================================================================
    override fun onTercerMensajeExceptionResult(onExceptionResult: String?) {
        banderaReintentar = "2"
        UserInteraction.showDialogReintentar(
            fragmentManager,
            this,
            "Tercer Mensaje",
            "Exception $onExceptionResult"
        )
    }

    override fun onTercerMensajeFailResult(onFailResult: String?) {
        banderaReintentar = "2"
        UserInteraction.showDialogReintentar(
            fragmentManager,
            this,
            "Tercer Mensaje",
            "Error $onFailResult"
        )
    }

    override fun onTercerMensajeDeclinadaEMV(onDeclinadaResult: String?) {
        if (onDeclinadaResult == "Commit") {
            VerifoneTaskManager.restPinpad()
            VerifoneTaskManager.desconectaPinpad()
            UserInteraction.showDialogImprimeVoucher(fragmentManager, saleVtol, null)
        }
        dismiss()
    }
    fun dismissDialogAfterDelay() {
        Handler().postDelayed({

            UserInteraction.getDialogWaitingPrint?.dismiss()
        }, DIALOG_DURATION.toLong())
    }

    override fun onTercerMensajeSuccessResult(onSuccessResult: String?) {
        if (onSuccessResult == "Commit") {
            VerifoneTaskManager.restPinpad()
            VerifoneTaskManager.desconectaPinpad()

            // TODO: 30/11/21 Descomentar la linea de abajo para imprimir
            UserInteraction.showDialogImprimeVoucher(fragmentManager, saleVtol, null)
           // dismissDialogAfterDelay()
        }
        dismiss()
    }

    //================================================================================
    // Termina Presenter TercerMensaje
    //================================================================================
    override fun onRetrySelected() {
        when (banderaReintentar) {
            "1" -> {
                waitDialogBinding!!.dialogMessage.text = "Aplicando Pago"
                presenter!!.executeAplicaPago()
            }
            "2" -> {
                waitDialogBinding!!.dialogMessage.text = "Ejecutando Tercer Mensaje"
                tercerMensajePresenter!!.executeTercerMensaje(
                    accionTercerMensaje,
                    response,
                    saleVtol,
                    tag9f27,
                    isTransaccionExitosa
                )
            }
        }
    }

    override fun onCacelSelected() {}
    override fun onExceptionResult(onExceptionResult: String?) {
        UserInteraction.snackyException(activity, null, onExceptionResult)
    }

    override fun onFailResult(onFailResult: String?) {
        UserInteraction.snackyFail(activity, null, onFailResult)
    }

    override fun onWarningResult(onWarningResult: String?) {
        UserInteraction.snackyWarning(activity, null, onWarningResult)
    }

    override fun onSuccessResult(onSuccessResult: String?) {
        Log.i(TAG, "onSuccessResult: Impresion correcta")
    }

    override fun onFinishedPrintJob() {
        dismiss()
        VerifoneTaskManager.restPinpad()
        VerifoneTaskManager.desconectaPinpad()
    }

    companion object {
        private const val TAG = "DialogWaitingPagoTarjet"

        /**
         * Type: Method.
         * Parent: DialogWaitingPagoTarjeta.
         * Name: newInstance.
         *
         * @return dialog waiting pago tarjeta
         * @Description.
         * @EndDescription. dialog waiting pago tarjeta.
         */
        @JvmStatic
        fun newInstance(): DialogWaitingPagoTarjeta {
            val args = Bundle()
            val fragment = DialogWaitingPagoTarjeta()
            fragment.arguments = args
            return fragment
        }
    }
}
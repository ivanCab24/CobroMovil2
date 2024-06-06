package com.View.Fragments

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.Constants.ConstantsAComer.MEMBRESIA_SUBTIPO_CORPORATIVA
import com.Constants.ConstantsAComer.MEMBRESIA_SUBTIPO_INAPAM
import com.Constants.ConstantsDescuentos
import com.Constants.ConstantsPreferences
import com.Constants.ConstantsTareasPendientes.PENDIENTE_IMPRESION_COPIA_VOUCHER
import com.Controller.Adapters.DescuentosAdapter
import com.Controller.Adapters.DescuentosAdapter.DiscountViewHolder
import com.Controller.Adapters.PagosAdapter
import com.Controller.BD.DAO.CuentaCerradaDAO
import com.Controller.BD.DAO.TicketsNoRegistradosDAO
import com.DI.BaseApplication
import com.DataModel.Caja
import com.DataModel.Cuenta
import com.DataModel.Descuentos
import com.DataModel.DescuentosAplicados
import com.DataModel.FormasDePago
import com.DataModel.Miembro
import com.DataModel.Miembro.Response.Cupones
import com.DataModel.Pagos
import com.DataModel.SaleVtol
import com.Interfaces.AplicaDescuentoMVP
import com.Interfaces.AplicaPagoMVP
import com.Interfaces.CajaMVP
import com.Interfaces.CancelaDescuentoMVP
import com.Interfaces.CancelarPagoMVP
import com.Interfaces.CierraCuentaMVP
import com.Interfaces.ContentFragmentMVP
import com.Interfaces.DescuentoFavoritoMVP
import com.Interfaces.DescuentoSelectionListner
import com.Interfaces.DescuentosMVP
import com.Interfaces.DialogVisualizadorMVP
import com.Interfaces.FormasDePagoMPV
import com.Interfaces.InformacionLocalMVP
import com.Interfaces.InsertaCuentaCerradaMVP
import com.Interfaces.InsertaTicketMVP
import com.Interfaces.PagoSelectionListner
import com.Interfaces.PagosMVP
import com.Interfaces.PinpadTaskMVP
import com.Utilities.Files
import com.Utilities.PreferenceHelper
import com.Utilities.PreferenceHelperLogs
import com.Utilities.Utilities
import com.Utilities.Utils.catalogDescuentosCorporativos
import com.Utilities.Utils.catalogDescuentosCorporativosINV
import com.Utilities.Utils.hideSoftKeyboard
import com.Verifone.BtManager
import com.Verifone.VerifoneTaskManager
import com.View.Activities.ActividadPrincipal
import com.View.Dialogs.DialogInputNipAComer
import com.View.Dialogs.DialogQuestion
import com.View.Dialogs.DialogWaiting
import com.View.UserInteraction
import com.github.ybq.android.spinkit.style.DoubleBounce
import com.google.zxing.integration.android.IntentIntegrator
import com.innovacion.eks.beerws.R
import com.innovacion.eks.beerws.databinding.FragmentContentBinding
import com.orhanobut.hawk.Hawk
import com.webservicestasks.ToksWebServiceErrors
import com.webservicestasks.ToksWebServicesConnection
import java.text.DecimalFormat
import javax.inject.Inject
import javax.inject.Named

/**
 * Type: Class.
 * Access: Public.
 * Name: ContentFragment.
 *
 * @Description.
 * En está clase se inicializa el fragmento de la pantalla principal, en este encontramos 5 secciones, las cuales nos sirven para realizar el cobro, buscar una mesa, aplicar descuentos, agregar propina y visualizar los pagos realizados de la cuenta.
 * @EndDescription.
 */
class ContentFragment
/**
 * Type: Method.
 * Parent: ContentFragment.
 * Name: ContentFragment.
 *
 * @Description.
 * @EndDescription.
 */
    : Fragment(), ToksWebServicesConnection, ToksWebServiceErrors, DescuentoSelectionListner,
    PagoSelectionListner, ContentFragmentMVP.View, PagosMVP.View, CajaMVP.View,
    DescuentosMVP.View, DescuentoFavoritoMVP.View, AplicaDescuentoMVP.View,
    FormasDePagoMPV.View, AplicaPagoMVP.View, CancelarPagoMVP.View,
    CierraCuentaMVP.View, CancelaDescuentoMVP.View, PinpadTaskMVP.View,
    InsertaTicketMVP.View, InsertaCuentaCerradaMVP.View, InformacionLocalMVP.View,DialogVisualizadorMVP.View,
    DialogQuestion.DialogButtonClickListener.AplicaCuponDescuento,DialogQuestion.DialogButtonClickListener.LiberarAction {
    var fragmentManager2: FragmentManager? = null
    private var marca: String = ""
    private lateinit var threadPagos:Thread
    var pos = "0"
    var copiaprinted : Boolean = false
    var conteoImpresiones = 0

    /**
     * The Bt manager.
     */
    @JvmField
    var btManager: BtManager? = null
    private var banderaResetKeys = ""

    /**
     * The Binding.
     */
    @JvmField
    var binding: FragmentContentBinding? = null
    private var doubleBounce: DoubleBounce? = null


    /**
     * The Shared preferences.
     */
    @JvmField
    @Named("Preferencias")
    @Inject
    var sharedPreferences: SharedPreferences? = null

    /**
     * The Shared preferences logs.
     */
    @JvmField
    @Named("Logs")
    @Inject
    var sharedPreferencesLogs: SharedPreferences? = null

    @JvmField
    @Inject
    var presenterVisualizador: DialogVisualizadorMVP.Presenter? = null
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
     * The Cuenta cerrada dao.
     */
    @JvmField
    @Inject
    var cuentaCerradaDAO: CuentaCerradaDAO? = null

    /**
     * The Tickets no registrados dao.
     */
    @JvmField
    @Inject
    var ticketsNoRegistradosDAO: TicketsNoRegistradosDAO? = null

    /**
     * The Cuenta presenter.
     */
    @JvmField
    @Inject
    var presenter: ContentFragmentMVP.Presenter? = null

    /**
     * The Pagos presenter.
     */
    @JvmField
    @Inject
    var pagosPresenter: PagosMVP.Presenter? = null

    /**
     * The Caja presenter.
     */
    @JvmField
    @Inject
    var cajaPresenter: CajaMVP.Presenter? = null

    /**
     * The Descuentos presenter.
     */
    @JvmField
    @Inject
    var descuentosPresenter: DescuentosMVP.Presenter? = null

    /**
     * The Descuento favorito presenter.
     */
    @JvmField
    @Inject
    var descuentoFavoritoPresenter: DescuentoFavoritoMVP.Presenter? = null

    /**
     * The Aplica descuento presenter.
     */
    @JvmField
    @Inject
    var aplicaDescuentoPresenter: AplicaDescuentoMVP.Presenter? = null

    /**
     * The Formas de pago presenter.
     */
    @JvmField
    @Inject
    var formasDePagoPresenter: FormasDePagoMPV.Presenter? = null

    /**
     * The Aplica pago presenter.
     */
    @JvmField
    @Inject
    var aplicaPagoPresenter: AplicaPagoMVP.Presenter? = null

    /**
     * The Cancela pago presenter.
     */
    @JvmField
    @Inject
    var cancelaPagoPresenter: CancelarPagoMVP.Presenter? = null

    /**
     * The Cierra cuenta presenter.
     */
    @JvmField
    @Inject
    var cierraCuentaPresenter: CierraCuentaMVP.Presenter? = null

    /**
     * The Cancela descuento presenter.
     */
    @JvmField
    @Inject
    var cancelaDescuentoPresenter: CancelaDescuentoMVP.Presenter? = null

    /**
     * The Pinpad task presenter.
     */
    @JvmField
    @Inject
    var pinpadTaskPresenter: PinpadTaskMVP.Presenter? = null

    /**
     * The Inserta ticket presenter.
     */
    @JvmField
    @Inject
    var insertaTicketPresenter: InsertaTicketMVP.Presenter? = null

    /**
     * The Cuenta cerrada presenter.
     */
    @JvmField
    @Inject
    var cuentaCerradaPresenter: InsertaCuentaCerradaMVP.Presenter? = null

    /**
     * The Informacion local presenter.
     */
    @JvmField
    @Inject
    var informacionLocalPresenter: InformacionLocalMVP.Presenter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity!!.applicationContext as BaseApplication).plusCuentaSubComponent(this).inject(this)
        Companion.activity = activity
        contentFragment = this
        fragment = ContentFragment()
        formatter = DecimalFormat("#0.00")
        pinpad = VerifoneTaskManager()
        fragmentManager2 = getFragmentManager()!!
        presenter!!.getMarca()
        doubleBounce = DoubleBounce()
        doubleBounce?.color = Utilities.setUpDoubleBounce(activity!!, marca)
        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        if(miembroCuentaA==null)
            miembroCuentaA = if(Hawk.contains("membresias"))
                Hawk.get("membresias")
            else HashMap<String, Miembro>()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentContentBinding.inflate(inflater, container, false)
        val view: View = binding!!.root
        initDependencyInjection()
        initDI()
        informacionLocalPresenter!!.executeInfoLocal()
        intentIntegrator = IntentIntegrator.forSupportFragment(fragment)
        UIInit()
        return view
    }

    override fun onResume() {
        super.onResume()
        if (tareaPendiente == PENDIENTE_IMPRESION_COPIA_VOUCHER) {
            UserInteraction.showImprimeCopiaClienteDialog(
                fragmentManager,
                banderaTareaPendiente,
                arrayListTareaPendiente as ArrayList<String>?,1
            )
        }
    }

    private fun initDI() {
        presenter!!.setView(this)
        presenter!!.setFM(fragmentManager!!)
        presenter!!.preferences(sharedPreferences,preferenceHelper)
        presenter!!.setLogsInfo(preferenceHelperLogs, files)
        presenter!!.setPinPadPresenter(pinpadTaskPresenter!!)
        presenter!!.setCajaPresenter(cajaPresenter!!)

    }

    // Inicializa la interfaz presentada al usuario
    private fun UIInit() {
        imm = Companion.activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        binding!!.editTextMesa.imeOptions = EditorInfo.IME_ACTION_DONE
        binding!!.progressBarDescuentos.indeterminateDrawable = doubleBounce
        binding!!.progressBarPagos.indeterminateDrawable = doubleBounce
        binding!!.buttonSend.setOnClickListener {
            ActividadPrincipal.isDescuentoGRG = false
            isComprobantePuntosPrintable = false
            isComprobanteDescuentoGRGPrintable = false
            binding!!.recyclerViewPagos.visibility = View.GONE
            binding!!.recyclerViewDiscounts.visibility = View.GONE
            presenter!!.buscarMesa()
        }
        binding!!.buttonCobrar.setOnClickListener {
            binding!!.buttonCobrar.isEnabled = false
            presenter!!.payButtonAction()
        }

        val listener = View.OnClickListener { v13: View ->
            percent = true
            val id = v13.id
            propinaPorcentaje = 0.0
            when (id) {
                R.id.buttonFivePercent -> {
                    propinaPorcentaje = 5.0
                    binding!!.textViewMontoPropina.text = "5%"
                }
                R.id.buttonTenPercent -> {
                    propinaPorcentaje = 10.0
                    binding!!.textViewMontoPropina.text = "10%"
                }
                R.id.buttonFifteenPercent -> {
                    propinaPorcentaje = 15.0
                    binding!!.textViewMontoPropina.text = "15%"
                }
                R.id.buttonTwentyPercent -> {
                    propinaPorcentaje = 20.0
                    binding!!.textViewMontoPropina.text = "20%"
                }
            }
            binding!!.buttonNoTip.visibility = View.VISIBLE
        }
        binding!!.buttonFivePercent.setOnClickListener(listener)
        binding!!.buttonTenPercent.setOnClickListener(listener)
        binding!!.buttonFifteenPercent.setOnClickListener(listener)
        binding!!.buttonTwentyPercent.setOnClickListener(listener)

        //**********************************************************************************
        val listenerPorcentaje = View.OnClickListener {
            percent = true
            propinaPorcentaje = 0.0
            imm!!.hideSoftInputFromWindow(binding!!.customTipEditText.windowToken, 0)
            if (binding!!.customTipEditText.text.toString() != "") {
                val valor = binding!!.customTipEditText.text.toString().toDouble()
                if (valor > 100) {
                    UserInteraction.snackyWarning(
                        activity,
                        null,
                        "El porcentaje no debe ser mayor a 100"
                    )
                } else {
                    propinaPorcentaje = valor
                    binding!!.textViewMontoPropina.text = "$valor%"
                    binding!!.buttonNoTip.visibility = View.VISIBLE
                }
            } else {
                UserInteraction.snackyWarning(activity, null, "Ingrese un porcentaje de propina")
            }
            binding!!.customTipEditText.setText("")
        }
        binding!!.buttonCustomPercentTip.setOnClickListener(listenerPorcentaje)

        // **********************************************************************************
        binding!!.buttonNoTip.setOnClickListener {
            imm!!.hideSoftInputFromWindow(binding!!.customTipEditText.windowToken, 0)
            if (tipAmount > 0.0 || propinaPorcentaje > 0.0) {
                tipAmount = 0.0
                propinaPorcentaje = 0.0
                percent = false
                binding!!.customTipEditText.setText(getString(R.string.empty))
                binding!!.textViewMontoPropina.text = getString(R.string.zero)
                binding!!.buttonNoTip.visibility = View.INVISIBLE
            }
        }

        // Agregar Propina
        binding!!.buttonCustomPesosTip.setOnClickListener {
            percent = false
            imm!!.hideSoftInputFromWindow(binding!!.customTipEditText.windowToken, 0)
            if (binding!!.customTipEditText.text.toString() != "") {
                tipAmount = binding!!.customTipEditText.text.toString().toDouble()
                binding!!.textViewMontoPropina.text = formatter!!.format(tipAmount)
                binding!!.buttonNoTip.visibility = View.VISIBLE
                binding!!.customTipEditText.setText("")
            } else {
                UserInteraction.snackyWarning(activity, null, "Ingrese un monto de propina")
            }
        }
        binding!!.buttonNoDiscount.setOnClickListener {
            UserInteraction.showDescuentosAplicadosDialog(
                fragmentManager
            )
        }
    }

    /**
     * Type: Method.
     * Parent: ContentFragment.
     * Name: controlsSwitch.
     *
     * @param flag @PsiType:boolean.
     * @Description.
     * @EndDescription.
     */
    fun controlsSwitch(flag: Boolean) {
        binding!!.customTipEditText.isEnabled = flag
        binding!!.buttonCustomPercentTip.isEnabled = flag
        binding!!.buttonCustomPesosTip.isEnabled = flag
        binding!!.buttonNoTip.isEnabled = flag
        binding!!.buttonNoDiscount.isEnabled = flag
        binding!!.buttonCobrar.isEnabled = flag
        binding!!.buttonFivePercent.isEnabled = flag
        binding!!.buttonTenPercent.isEnabled = flag
        binding!!.buttonFifteenPercent.isEnabled = flag
        binding!!.buttonTwentyPercent.isEnabled = flag
        if (!flag) {
            binding!!.textViewImporte.text = "0.00"
            binding!!.textViewMontoDescuento.text = "0.00"
            binding!!.textViewMontoTotal.text = "0.00"
            binding!!.textViewMontoPropina.text = "0.00"
            binding!!.editTextMesa.setText("")
            isTip = false
            binding!!.textViewFolio.text = ""
            binding!!.buttonCobrar.text = "Por Cobrar $0.00"
            binding!!.buttonNoDiscount.visibility = View.INVISIBLE
            tipAmount = 0.0
            binding!!.recyclerViewPagos.visibility = View.GONE
            binding!!.recyclerViewDiscounts.visibility = View.GONE
            binding!!.checkBoxSeparateAccount.isChecked = false
            binding!!.numberPickerDiner.value = 1
        }
    }

    /**
     * Type: Method.
     * Parent: ContentFragment.
     * Name: redeem.
     *
     * @param referencia @PsiType:String.
     * @Description.
     * @EndDescription.
     */
    fun redeem(referencia: String?) {
        aplicaDescuentoPresenter!!.executeAplicaDescuentoRequest(referencia)
    }

    /**
     * Type: Method.
     * Parent: ContentFragment.
     * Name: cancelaDescuento.
     *
     * @Description.
     * @EndDescription.
     */
    fun cancelaDescuento() {
        UserInteraction.showWaitingDialog(fragmentManager, "Cancelando descuento")
        cancelaDescuentoPresenter!!.executeCancelaDescuento(descuentosAplicados)
    }

    /**
     * Type: Method.
     * Parent: ContentFragment.
     * Name: insertaTicket.
     *
     * @Description.
     * @EndDescription.
     */
    fun insertaTicket() {
        insertaTicketPresenter!!.executeInsertaTicket()
    }

    /**
     * Type: Method.
     * Parent: ContentFragment.
     * Name: aplicaPagoMercadoPago.
     *
     * @param autorizacion @PsiType:String.
     * @Description.
     * @EndDescription.
     */
    fun aplicaPagoMercadoPago(autorizacion: String?) {
        UserInteraction.showWaitingDialog(fragmentManager, "Aplicando Pago")
        aplicaPagoPresenter!!.onExecuteAplicaPago(autorizacion)
    }

    /**
     * Type: Method.
     * Parent: ContentFragment.
     * Name: sendButtonAction.
     *
     * @Description.
     * @EndDescription.
     */
    fun sendButtonAction() {
        presenter!!.buscarMesa()
    }


    /**
     * Type: Method.
     * Parent: ContentFragment.
     * Name: payButtonAction.
     *
     * @Description.
     * @EndDescription.
     */


    /**
     * Type: Method.
     * Parent: ContentFragment.
     * Name: iniciaProcesoDePago.
     *
     * @Description.
     * @EndDescription.
     */
    fun iniciaProcesoDePago() {
        hideSoftKeyboard(binding!!.root, activity)
        UserInteraction.showWaitingDialog(fragmentManager, "Obteniendo formas de pago")
        formasDePagoPresenter!!.executeFormasDePago()
    }

    /**
     * Type: Method.
     * Parent: ContentFragment.
     * Name: cancelarPago.
     *
     * @Description.
     * @EndDescription.
     */
    fun cancelarPago() {
        if (pagos!!.idFpgo == 2) {
            pinpadTaskPresenter!!.executeTaskConnection()
        } else {
            cancelaPagoPresenter!!.setLogsInfo(preferenceHelperLogs, files)
            cancelaPagoPresenter!!.executeCancelarPago()
        }
    }

    /**
     * Type: Method.
     * Parent: ContentFragment.
     * Name: getCuenta.
     *
     * @Description.
     * @EndDescription.
     */
    val cuenta: Unit
        get() {
            presenter!!.tipControls()
            imm!!.hideSoftInputFromWindow(binding!!.editTextMesa.windowToken, 0)
            presenter!!.buscarMesa()
        }

    /**
     * Type: Method.
     * Parent: ContentFragment.
     * Name: realizarPago.
     *
     * @Description.
     * @EndDescription.
     */
    fun realizarPago() {
        val amnt = Companion.cuenta.importe
        amountPublico = amnt
        when (formasDePago!!.idFpgo) {
            0 -> {
                //UserInteraction.showDialogImprimeInfoEfectivo(activity);
                if (tipAmount > 0.0) {
                    //UserInteraction.showCustomDialog(activity, "No es posible aplicar propina\ncon esta forma de pago", () -> UserInteraction.getCustomDialog.dismiss());
                    binding!!.textViewMontoTotal.text =
                        formatter!!.format(newTotal - tipAmount) //tenia un -
                    binding!!.buttonCobrar.text =
                        "Por Cobrar $" + formatter!!.format(newTotal - tipAmount) //
                    tipAmount = 0.0
                    binding!!.customTipEditText.setText(Companion.activity!!.getString(R.string.empty))
                    binding!!.textViewMontoPropina.text =
                        Companion.activity!!.getString(R.string.zero)
                    binding!!.buttonNoTip.visibility = View.INVISIBLE
                    isTip = false
                }
                UserInteraction.showWaitingDialog(fragmentManager, "Aplicando Pago")
                aplicaPagoPresenter!!.onExecuteAplicaPago("")
            }
            2 -> UserInteraction.showDialogWaitingPagoTarjeta(fragmentManager)
            30 -> UserInteraction.showMercadoPagoDialog(fragmentManager)
            else -> UserInteraction.snackyException(
                activity, null, """Forma de pago desconocida. 
Posible configuración erronea. ${formasDePago!!.idFpgo} ${formasDePago!!.formaPago}"""
            )
        }
    }

    //================================================================================
    // Inicia Presenter Cuenta
    //================================================================================
    override fun onExceptionCuentaResult(onExceptionResult: String?) {
        controlsSwitch(false)
        ocultarDialog()
        UserInteraction.snackyException(activity, null, onExceptionResult)
    }

    override fun onWarningCuentaResult(onWarningResult: String?) {
        ocultarDialog()
        UserInteraction.snackyWarning(activity, null, onWarningResult)
    }

    override fun onFailCuentaResult(onFailResult: String?) {
        controlsSwitch(false)
        ocultarDialog()
        UserInteraction.snackyFail(activity, null, onFailResult)
    }

    override fun onSuccessCuenta(cuenta: Cuenta?) {
        banderaCuentabuscada = true
        haveGRGDescuento=false
        descuentoSelect=false
        ocultarDialog()
        Companion.cuenta = cuenta!!
        Companion.cuenta.saldo2 = Companion.cuenta.saldo
        preferenceHelper!!.putString(
            ConstantsPreferences.PREF_NUM_COMENSALES,
            Companion.cuenta.numcomensales
        )
        preferenceHelper!!.putString(ConstantsPreferences.PREF_FOLIO, Companion.cuenta.folio)
        amountPublico = Companion.cuenta.saldo

        if((cuenta.acc!=2 && cuenta.acc!=5)  || cuenta.saldo==0.0){
            controlsSwitch(true)
            if (amountPublico == 0.0) {
                UserInteraction.showWaitingDialog(fragmentManager, "Cerrando Cuenta")
                cierraCuentaPresenter!!.executeCierraCuenta()
            } else {
                binding!!.progressBarPagos.visibility = View.VISIBLE
                pagosPresenter!!.executePagosRequest()
            }
        }else{
            threadPagos = Thread {
                try {
                    while(!presenter!!.have_pagos()){
                        println("sin pagos")
                    }
                    threadPagos.interrupt()

                    UserInteraction.getDialogWaiting.dismiss()
                    presenter!!.buscarMesa()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            UserInteraction.showWaitingDialog(fragmentManager2, "La cuenta está en proceso de pago por Payclub. \nEsperando pago","Liberar para POS",object:
                DialogWaiting.DialogButtonClickListener{
                override fun onPositiveButton() {
                    UserInteraction.showQuestionDialog(fragmentManager,"AVISO","¿Seguro que quieres liberar la cuenta para POS?","Liberar", "Cancelar",null,"4")
                }
            })
            threadPagos.start()
        }
    }



    //================================================================================
    // Termina Presenter Cuenta
    //================================================================================
    //================================================================================
    // Inicia Presenter Pagos
    //================================================================================
    override fun onExceptionPagosResult(onExceptionResult: String?) {
        binding!!.progressBarPagos.visibility = View.GONE
        UserInteraction.snackyException(activity, null, onExceptionResult)
    }

    override fun onWarningPagosResult(onWarningResult: String?) {
        binding!!.progressBarPagos.visibility = View.GONE
        UserInteraction.snackyWarning(activity, null, onWarningResult)
    }

    override fun onFailPagosResult(onFailResult: String?) {
        binding!!.progressBarPagos.visibility = View.GONE
        binding!!.progressBarDescuentos.visibility = View.VISIBLE
        descuentosPresenter!!.executeDescuentosRequest()
        UserInteraction.snackyFail(activity, null, onFailResult)
    }

    override fun onSuccessPagosResult(pagosArrayList: ArrayList<Pagos?>?) {
        binding!!.progressBarPagos.visibility = View.GONE
        binding!!.recyclerViewPagos.visibility = View.VISIBLE
        discountsControls(false)
        binding!!.recyclerViewPagos.layoutManager =
            LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        binding!!.recyclerViewPagos.setHasFixedSize(true)
        val pagosAdapter = PagosAdapter(activity, pagosArrayList, this)
        pagosAdapter.notifyDataSetChanged()
        binding!!.recyclerViewPagos.adapter = pagosAdapter
    }

    override fun selectedPagoItem(pagos: Pagos?) {
        val mensaje = "¿Realmente desea cancelar este pago por $" + pagos!!.abonoMn + "?"
        banderaCobroRefound = 1
        Companion.pagos = pagos
        UserInteraction.showDialogDeletePayment(fragmentManager, mensaje)
    }

    //================================================================================
    // Termina Presenter Pagos
    //================================================================================
    //================================================================================
    // Inicia Presenter Caja
    //================================================================================
    override fun onExceptionCajaResult(onExceptionResult: String?) {
        binding!!.buttonCobrar.isEnabled = true
        ocultarDialog()
        UserInteraction.snackyException(activity, null, onExceptionResult)
    }

    override fun onFailCajaResult(onFailResult: String?) {
        binding!!.buttonCobrar.isEnabled = true
        ocultarDialog()
        UserInteraction.snackyFail(activity, null, onFailResult)
    }

    override fun onSuccess(caja: Caja?) {
        ocultarDialog()
       // UserInteraction.showImpresoraDialog(fragmentManager, caja.toString()) //Interacion agregada
        UserInteraction.showChargeDialog(fragmentManager)
    }

    //================================================================================
    // Fin Presenter Caja
    //================================================================================
    //================================================================================
    // Inicia Presenter Descuentos
    //================================================================================
    override fun onExceptionResult(onExceptionResult: String?) {
        UserInteraction.snackyException(activity, null, onExceptionResult)
    }

    override fun onFailResult(onFailResult: String?) {
        UserInteraction.snackyFail(activity, null, onFailResult)
    }

    override fun onSuccess(descuentosArrayList: ArrayList<Descuentos?>?) {

        binding!!.progressBarDescuentos.visibility = View.GONE
        binding!!.recyclerViewDiscounts.visibility = View.VISIBLE
        binding!!.recyclerViewDiscounts.layoutManager =
            LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        binding!!.recyclerViewDiscounts.setHasFixedSize(true)
        var removeDescuento: ArrayList<Descuentos> = ArrayList<Descuentos>()
        for (i in descuentosArrayList!!.indices) {
            if (descuentosArrayList[i]!!.idDesc == 994) {
                descuentoCuponAComer = descuentosArrayList[i]
                removeDescuento.add(descuentoCuponAComer!!)
            }
        }
        for (i in removeDescuento.indices)
            descuentosArrayList.remove(removeDescuento[i])
        discountViewHolderAdapter = DescuentosAdapter(
            Companion.activity,
            descuentosArrayList,
            "0",
            null,
            this,
            preferenceHelper
        )
        binding!!.recyclerViewDiscounts.adapter = discountViewHolderAdapter
    }

    override fun onSelectedItem(descuentos: Descuentos?, bandera: String?) {
        Utilities.getJulianDate()
        presenter!!.requiereAuth()
        if(true){
            if(isDescuentoRedimido){
                UserInteraction.snackyWarning(activity,null,"Ya hay un descuento en ejecución")
            }else

            if(ContentFragment.contentFragment!!.presenter!!.revisa_saldo()){
                descuentoSelect = true

                getDescuento = descuentos
                Log.i("Descuento", getDescuento.toString())
                val catalogoCorp = catalogDescuentosCorporativos()
                ActividadPrincipal.isDescuentoGRG = descuentos!!.tipo == 2 && descuentos.idDesc == ConstantsDescuentos.DESCUENTO_EMP_GIGANTE
                if (descuentos.tipo == 99) {
                    UserInteraction.showMultipleDiscountsDialog(fragmentManager)
                } else if (descuentos.tipo == 999) {
                    if (ActividadPrincipal.banderaDialogMostrar != "1") {
                        //UserInteraction.showDialogBuscarAfiliado(fragmentManager);
                        UserInteraction.snackyWarning(
                            activity,
                            null,
                            "Por favor presione el botón de identificar miembro."
                        )
                    } else {

                        descuentoSelect = false
                        UserInteraction.showRedencionDePuntosDialog(fragmentManager)
                    }
                } else if (catalogoCorp.containsKey(descuentos.idDesc)) {
                    if (ActividadPrincipal.banderaDialogMostrar != "1") {
                        //UserInteraction.showDialogBuscarAfiliado(fragmentManager);
                        UserInteraction.snackyWarning(
                            activity,
                            null,
                            "Por favor presione el botón de identificar miembro."
                        )
                    } else {
                        val membresia = miembro!!.response.membresia
                        if (membresia != null) {
                            if (membresia.empleadorMembresiaCorp != null) {
                                if (membresia.subTipoMembresia == MEMBRESIA_SUBTIPO_CORPORATIVA && catalogoCorp.containsKey(descuentos.idDesc) ) {
                                    var idDesc = catalogDescuentosCorporativosINV()[membresia.empleadorMembresiaCorp!!.toUpperCase()]
                                    if(idDesc!=null) {
                                        isDescuentoRedimido=true
                                        getDescuento!!.idDesc = idDesc
                                        redeem(miembro!!.response.membresia!!.estafeta)
                                        }else{
                                            UserInteraction.snackyFail(ContentFragment.activity,null,"No se reconoce el empelador")
                                        }
                                } else if (membresia.subTipoMembresia == MEMBRESIA_SUBTIPO_INAPAM ){
                                    isDescuentoRedimido=true
                                        redeem(membresia.inapam)

                                }else {
                                    UserInteraction.snackyFail(
                                        activity, null, "El descuento seleccionado no es aplicable para " +
                                                miembro!!.response.membresia!!.nombre
                                    )
                                }
                            } else {
                                UserInteraction.snackyWarning(
                                    activity,
                                    null,
                                    "Esta membresía no es corporativa."
                                )
                            }
                        }
                    }
                } else {
                    if(presenter!!.requiereAuth())
                        UserInteraction.showInputDialog(fragmentManager, "0", "Estafeta")
                    else
                        UserInteraction.showInputDialogReferencia(
                            fragmentManager,
                            ContentFragment.getDescuento!!.textReferencia,
                            bandera
                        )
                }
            }else{

                VerifoneTaskManager.restPinpad()
                VerifoneTaskManager.desconectaPinpad()
                ContentFragment.contentFragment!!.binding!!.buttonCobrar.isEnabled = true
                UserInteraction.snackyException(activity!!,null,"El monto a cobrar es mayor al de la cuenta o ya se encuentra cerrada")
            }


        }else{
            UserInteraction.snackyWarning(activity,null,"La cuenta ya ha seleccionado un descuento")
        }

    }

    override fun executeFavorito(descuentos: Descuentos?) {
        if(descuentos!!.favorito==1)
            UserInteraction.snackyWarning(activity, null, "Se ha removido el descuento "+descuentos!!.descuento + " de favoritos")
        else
            UserInteraction.snackyWarning(activity, null, "Se ha añadido el descuento "+descuentos!!.descuento + " a favoritos")

        discountsControls(true)
        descuentoFavoritoPresenter!!.executeDescuentoFavorito(descuentos)
    }

    override fun onDescuentoFavoritoExceptionResult(onExceptionResult: String?) {
        discountsControls(false)
        UserInteraction.snackyException(activity, null, onExceptionResult)
    }

    override fun onDescuentoFavoritoFailResult(onFailResult: String?) {
        discountsControls(false)
        UserInteraction.snackyFail(activity, null, onFailResult)
    }

    override fun onDescuentoFavoritoSuccess() {
        discountsControls(false)
        descuentosPresenter!!.executeDescuentosRequest()
    }

    //================================================================================
    // Fin Presenter Descuentos
    //================================================================================
    //================================================================================
    // Inicia Presenter AplicaDescuento
    //================================================================================
    override fun onExceptionAplicaDescuentoResult(onExceptionResult: String?) {
        ocultarDialog()
        UserInteraction.snackyException(activity, null, onExceptionResult)
    }

    override fun onFailAplicaDescuentoResult(onFailResult: String?) {
        ocultarDialog()
        UserInteraction.snackyFail(activity, null, onFailResult)
    }

    override fun onSuccess(onSuccessResult: String?) {
        ocultarDialog()
        cuenta
        UserInteraction.snackySuccess(activity, null, onSuccessResult)
    }

    //================================================================================
    // Termina Presenter AplicaDescuento
    //================================================================================
    //================================================================================
    // Inicia Presenter FormasDePago
    //================================================================================
    override fun onExceptionFormasDePagoResult(onExceptionResult: String?) {
        ocultarDialog()
        UserInteraction.snackyException(activity, null, onExceptionResult)
    }

    override fun onFailFormasDePagpResult(onFailResult: String?) {
        ocultarDialog()
        UserInteraction.snackyFail(activity, null, onFailResult)
    }

    override fun onSuccessFormasDePagoResult(formasDePagoArrayList: ArrayList<FormasDePago?>?) {
        ocultarDialog()
        UserInteraction.showMetodosDePagoDialog(fragmentManager, formasDePagoArrayList)
    }

    //================================================================================
    // Termina Presenter FormasDePago
    //================================================================================
    //================================================================================
    // Inicia Presenter AplicaPago
    //================================================================================
    override fun onExceptionAplicaPagoResult(onExceptionResult: String?) {
        binding!!.buttonCobrar.isEnabled = true
        ocultarDialog()
        UserInteraction.snackyException(activity, null, onExceptionResult)
    }

    override fun onFailAplicaPagoResult(onFailResult: String?) {
        binding!!.buttonCobrar.isEnabled = true
        ocultarDialog()
        UserInteraction.snackyException(activity, null, onFailResult)
    }

    override fun onSuccessAplicaPagoResult(onSuccessResult: String?) {
        ocultarDialog()
        cuenta
        UserInteraction.snackySuccess(activity, null, onSuccessResult)
    }

    //================================================================================
    // Termina Presenter AplicaPago
    //================================================================================
    //================================================================================
    // Inicia Presenter CancelaPago
    //================================================================================
    override fun onExceptionCancelarPagoResult(onExceptionResult: String?) {
        ocultarDialog()
        UserInteraction.snackyException(activity, null, onExceptionResult)
    }

    override fun onFailCancelarPagoResult(onFailResult: String?) {
        ocultarDialog()
        UserInteraction.snackyFail(activity, null, onFailResult)
    }

    override fun onSuccessCancelarPagoResult(onSuccessResult: String?) {
        ocultarDialog()
        cuenta
        UserInteraction.snackySuccess(activity, null, onSuccessResult)
    }

    //================================================================================
    // Termina Presenter CancelaPago
    //================================================================================
    //================================================================================
    // Inicia Presenter CierraCuenta
    //================================================================================
    override fun onExceptionCierraCuentaResult(onExceptionResult: String?) {
        ocultarDialog()
        UserInteraction.snackyException(activity, null, onExceptionResult)
    }

    override fun onFailCierraCuentaResult(onFailResult: String?) {
        ocultarDialog()
        UserInteraction.snackyFail(activity, null, onFailResult)
    }

    override fun onSuccessCierraCuentaResult(onSuccessResult: String?) {
        cuentaCerrada = Companion.cuenta
        cuentaCerradaPresenter!!.executeinsertaCuentaCerradaTask()
        ocultarDialog()
        UserInteraction.snackySuccess(activity, null, onSuccessResult)
        ActividadPrincipal.banderaDialogMostrar = ""

        if (preferenceHelper!!.getString(ConstantsPreferences.PREF_BANDERA_AFILIADO, null) == "1" && Companion.cuenta.total.toInt()>0) {
            miembro = miembroCuentaA!![ContentFragment.cuenta.folio]
            UserInteraction.showDialogAcumularPuntos(fragmentManager2)
        } else {
            miembro = null
            insertaTicket()
            preferenceHelper!!.removePreference(ConstantsPreferences.PREF_BANDERA_AFILIADO)
            // TODO: 02/08/21 Descomentar la linea de abajo para imprimir
            UserInteraction.showImprimeTicketDialog(fragmentManager)
        }
        presenter!!.tipControls()
        controlsSwitch(false)
    }

    //================================================================================
    // Termina Presenter CierraCuenta
    //================================================================================
    //================================================================================
    // Inicia Presenter CancelaDescuento
    //================================================================================
    override fun onExceptionCancelaDescuentoResult(onExceptionResult: String?) {
        ocultarDialog()
        UserInteraction.snackyException(activity, null, onExceptionResult)
    }

    override fun onFailCancelaDescuentoResult(onFailResult: String?) {
        ocultarDialog()
        UserInteraction.snackyFail(activity, null, onFailResult)
    }

    override fun onSuccessCancelaDescuentoResult(onSuccessResult: String?) {
        ocultarDialog()
        cuenta
        UserInteraction.snackySuccess(activity, null, onSuccessResult)
    }

    //================================================================================
    // Termina Presenter CancelaDescuento
    //================================================================================
    //================================================================================
    // Inicia Presenter PinpadTaskConnection
    //================================================================================
    override fun onExceptionPinpadTaskConnection(onExceptionResult: String?) {}

    override fun onFailPinpadTaskConnection(onFailResult: String?) {
        ocultarDialog()
        binding!!.buttonCobrar.isEnabled = true
        UserInteraction.snackyFail(activity, null, onFailResult)
    }

    override fun onSuccessPinpadTaskConnection(onSuccessResult: String?, btManager: BtManager?) {
        ocultarDialog()
        UserInteraction.snackySuccess(activity, null, onSuccessResult)
        this.btManager = btManager
        if (banderaResetKeys == "1") {
            banderaResetKeys = ""
            UserInteraction.showWaitingDialog(fragmentManager, "Generando ChipTokens")
        } else if (banderaCobroRefound == 0) {
            UserInteraction.showWaitingDialog(fragmentManager, "Obteniendo información de la caja")
            cajaPresenter!!.executeCaja()
        } else {
            UserInteraction.showDialogWaitingCancelaPagoTarjeta(fragmentManager)
        }
    }

    //================================================================================
    // Termina Presenter PinpadTaskConnection
    //================================================================================
    override fun onExceptionInsertaTicketResult(onExceptionResult: String?) {
        UserInteraction.snackyException(activity, null, onExceptionResult)
    }

    override fun onFailInsertaTicketResult(onFailResult: String?) {
        UserInteraction.snackyFail(activity, null, onFailResult)
    }

    override fun onSuccessInsertaTicketResult(onSuccessResult: String?) {
        UserInteraction.snackySuccess(activity, null, onSuccessResult)
    }

    //================================================================================
    // Termina Presenter InsertaTicket
    //================================================================================
    private fun initDependencyInjection() {
        presenterVisualizador!!.setView(this)
        presenter!!.setView(this)
        presenter!!.preferences(sharedPreferences, preferenceHelper)
        presenter!!.setLogsInfo(preferenceHelperLogs, files)
        presenter!!.setTip(tipAmount)
        presenter!!.setBinding(binding)
        pagosPresenter!!.setView(this)
        pagosPresenter!!.setBinding(binding)
        pagosPresenter!!.setPreferences(sharedPreferences, preferenceHelper)
        pagosPresenter!!.setLogsInfo(preferenceHelperLogs, files)
        cajaPresenter!!.setView(this)
        cajaPresenter!!.setPreferences(sharedPreferences, preferenceHelper)
        cajaPresenter!!.setLogsInfo(preferenceHelperLogs, files)
        descuentosPresenter!!.setView(this)
        descuentosPresenter!!.setPreferences(sharedPreferences, preferenceHelper)
        descuentosPresenter!!.setLogsInfo(preferenceHelperLogs, files)
        descuentoFavoritoPresenter!!.setView(this)
        descuentoFavoritoPresenter!!.setPreferences(sharedPreferences, preferenceHelper)
        descuentoFavoritoPresenter!!.setLogsInfo(preferenceHelperLogs, files)
        aplicaDescuentoPresenter!!.setView(this)
        aplicaDescuentoPresenter!!.setPreferences(sharedPreferences, preferenceHelper)
        aplicaDescuentoPresenter!!.setLogsInfo(preferenceHelperLogs, files)
        aplicaDescuentoPresenter!!.setContentFragment(this)
        formasDePagoPresenter!!.setView(this)
        formasDePagoPresenter!!.setPreferences(sharedPreferences, preferenceHelper)
        formasDePagoPresenter!!.setLogsInfo(preferenceHelperLogs, files)
        aplicaPagoPresenter!!.setView(this)
        aplicaPagoPresenter!!.setPreferences(sharedPreferences, preferenceHelper)
        aplicaPagoPresenter!!.setLogsInfo(preferenceHelperLogs, files)
        cancelaPagoPresenter!!.setView(this)
        cancelaPagoPresenter!!.setPreferences(sharedPreferences, preferenceHelper)
        cancelaDescuentoPresenter!!.setLogsInfo(preferenceHelperLogs, files)
        cierraCuentaPresenter!!.setView(this)
        cierraCuentaPresenter!!.setPreferences(sharedPreferences, preferenceHelper)
        cierraCuentaPresenter!!.setLogsInfo(preferenceHelperLogs, files)
        cancelaDescuentoPresenter!!.setView(this)
        cancelaDescuentoPresenter!!.setPreferences(sharedPreferences, preferenceHelper)
        cancelaDescuentoPresenter!!.setLogsInfo(preferenceHelperLogs, files)
        pinpadTaskPresenter!!.setView(this)
        pinpadTaskPresenter!!.setActivity(activity)
        pinpadTaskPresenter!!.setPreferences(sharedPreferences, preferenceHelper)
        pinpadTaskPresenter!!.setLogsInfo(preferenceHelperLogs, files)
        insertaTicketPresenter!!.setView(this)
        insertaTicketPresenter!!.setContext(context)
        insertaTicketPresenter!!.setPreferences(sharedPreferences, preferenceHelper)
        insertaTicketPresenter!!.setLogsInfo(preferenceHelperLogs, files)
        insertaTicketPresenter!!.setInsertaTicketsDao(ticketsNoRegistradosDAO)
        cuentaCerradaPresenter!!.setView(this)
        cuentaCerradaPresenter!!.setActivity(activity)
        cuentaCerradaPresenter!!.setPreferences(sharedPreferences, preferenceHelper)
        cuentaCerradaPresenter!!.setLogsInfo(preferenceHelperLogs, files)
        cuentaCerradaPresenter!!.setCuentaCerradaDao(cuentaCerradaDAO)
        informacionLocalPresenter!!.setView(this)
        informacionLocalPresenter!!.setPreferences(sharedPreferences, preferenceHelper)
        informacionLocalPresenter!!.setLogsInfo(preferenceHelperLogs, files)
    }

    private fun ocultarDialog() {
        if (UserInteraction.getDialogWaiting != null && UserInteraction.getDialogWaiting.dialog != null) {
            UserInteraction.getDialogWaiting.dialog!!.dismiss()
        }
    }

    private fun discountsControls(isExecuted: Boolean) {
        binding!!.recyclerViewDiscounts.visibility =
            if (isExecuted) View.INVISIBLE else View.VISIBLE
        binding!!.progressBarDescuentos.visibility =
            if (isExecuted) View.VISIBLE else View.INVISIBLE
    }

    /**
     * Type: Method.
     * Parent: ContentFragment.
     * Name: resetKeys.
     *
     * @Description.
     * @EndDescription.
     */
    fun resetKeys() {
        UserInteraction.showResetKeysDialog(fragmentManager)
    }

    companion object {
        var miembroCuentaA: HashMap<String, Miembro>? = null
        var descuentoSelect = false
        var isDescuentoRedimido = false
         var haveGRGDescuento = false
         var descuentoPorcentaje:Descuentos = Descuentos(
            5,
            997,
            "PUNTOS A COMER",
            "descripcion",
            20.0,
            "textReferencia",1

        )
        lateinit var cuenta: Cuenta
        var folioHistorico  = "folio";
        var catalogoDescuentosSelect: ArrayList<Descuentos?>? = ArrayList<Descuentos?>()
        var offline = false
        private const val TAG = "ContentFragment"
        private var discountViewHolderAdapter: RecyclerView.Adapter<DiscountViewHolder>? = null

        /**
         * The constant activity.
         */
        @JvmField
        var activity: Activity? = null
        /**
         * The constant imm.
         */
        @JvmField
        var imm: InputMethodManager? = null

        /**
         * The constant tipAmount.
         */
        @JvmField
        var tipAmount = 0.0
        var banderaCobroRefound = 0
        /**
         * Type: Method.
         * Parent: ContentFragment.
         * Name: getMontoCobro.
         *
         * @return the monto cobro
         * @Description.
         * @EndDescription.
         */
        /**
         * Type: Method.
         * Parent: ContentFragment.
         * Name: setMontoCobro.
         *
         * @Description.
         * @EndDescription.
         * @param: monto @PsiType:double.
         */
        @JvmStatic
        var montoCobro = 0.0

        /**
         * The constant propinaPorcentaje.
         */
        @JvmField
        var propinaPorcentaje = 0.0
        var nipA:String = ""
        private const val newTotal = 0.0
        var isTip = false
        private var formatter: DecimalFormat? = null

        /**
         * The constant percent.
         */
        @JvmField
        var percent = false

        /**
         * The constant pinpad.
         */
        var pinpad: VerifoneTaskManager? = null

        var banderaCuentabuscada:Boolean = false

        /**
         * The constant textNumComensales.
         */
        @JvmField
        var textNumComensales = ""

        /**
         * The Array list comensal.
         */
        @JvmField
        var arrayListComensal: ArrayList<String>? = null

        /**
         * The constant fragment.
         */
        var fragment: Fragment? = null

        /**
         * The constant contentFragment.
         */
        @JvmField
        var contentFragment: ContentFragment? = null

        /**
         * The constant amountPublico.
         */
        var amountPublico: Double? = null


        /**
         * The constant cuentaCerrada.
         */
        @JvmField
        var cuentaCerrada: Cuenta? = null

        /**
         * The constant getDescuento.
         */
        @JvmField
        var getDescuento: Descuentos? = null

        /**
         * The constant formasDePago.
         */
        @JvmField
        var formasDePago: FormasDePago? = null

        /**
         * The constant pagos.
         */
        @JvmField
        var pagos: Pagos? = null

        /**
         * The constant descuentosAplicados.
         */
        @JvmField
        var descuentosAplicados: DescuentosAplicados? = null

        /**
         * The constant miembro.
         */
        @JvmField
        var miembro: Miembro? = null

        /**
         * The Copia miembro.
         */
        @JvmField
        var copiaMiembro: Miembro? = null

        /**
         * The Miembro grg.
         */
        @JvmField
        var miembroGRG: Miembro? = null

        /**
         * The constant descuentoMultiple.
         */
        @JvmField
        var descuentoMultiple: Descuentos? = null

        /**
         * The constant saleVtol.
         */
        @JvmField
        var saleVtol: SaleVtol? = null

        /**
         * The constant descuentoCuponAComer.
         */
        @JvmField
        var descuentoCuponAComer: Descuentos? = null

        /**
         * The constant cuponMiembro.
         */
        @JvmField
        var cuponMiembro: Cupones? = null

        /**
         * The constant comprobantePuntos.
         */
        @JvmField
        var comprobantePuntos = ArrayList<String>()

        @JvmField
        var comprobantePuntosOffline = ArrayList<String>()

        /**
         * The constant isComprobantePuntosPrintable.
         */
        @JvmField
        var isComprobantePuntosPrintable = false

        /**
         * The constant estafetaAutorizadora.
         */
        @JvmField
        var estafetaAutorizadora = ""

        /**
         * The constant isComprobanteDescuentoGRGPrintable.
         */
        @JvmField
        var isComprobanteDescuentoGRGPrintable = false

        /**
         * The constant isConsultaMiembro.
         */
        @JvmField
        var isConsultaMiembro = false

        /**
         * The constant tareaPendiente.
         */
        @JvmField
        var tareaPendiente = ""

        /**
         * The constant banderaTareaPendiente.
         */
        @JvmField
        var banderaTareaPendiente = ""

        /**
         * The constant arrayListTareaPendiente.
         */
        @JvmField
        var arrayListTareaPendiente: ArrayList<*> = ArrayList<Any?>()

        /**
         * The constant intentIntegrator.
         */
        var intentIntegrator: IntentIntegrator? = null
        //    Inicio Getters & Setters
        /**
         * Type: Method.
         * Parent: ContentFragment.
         * Name: getTipAmount.
         *
         * @return the tip amount
         * @Description.
         * @EndDescription.
         */
        @JvmStatic
        fun getTipAmount(): Double {
            return tipAmount
        }

        /**
         * Type: Method.
         * Parent: ContentFragment.
         * Name: setTipAmount.
         *
         * @Description.
         * @EndDescription.
         * @param: tipAmount @PsiType:double.
         */
        @JvmStatic
        fun setTipAmount(tipAmount: Double) {
            Companion.tipAmount = tipAmount
        }

        /**
         * Type: Method.
         * Parent: ContentFragment.
         * Name: setActivity.
         *
         * @Description.
         * @EndDescription.
         * @param: activity @PsiType:Activity.
         */
        fun setActivity(activity: Activity?) {
            Companion.activity = activity
        }
    }

    override fun onExceptionVisualizadorResult(onExceptionResult: String) {
        UserInteraction.getDialogWaiting.dismiss()
        UserInteraction.snackyException(null, binding?.root, onExceptionResult)
    }

    override fun onFailVisualizadorResult(onFailResult: String) {
        UserInteraction.getDialogWaiting.dismiss()
        UserInteraction.snackyFail(null, binding?.root, onFailResult)
    }

    override fun onSuccessVisualizadorResult(registros: ArrayList<ArrayList<String>>) {

     //  UserInteraction.showImpresoraDialog(fragmentManager2, registros.toString())

        UserInteraction.getDialogWaiting.dismiss()
        UserInteraction.showVisualizadorDialog(fragmentManager2,registros)
//        presenterVisualizador!!.agregarContenido(registros)
    }

    override fun getCurrentMarca(value: String) {
        marca = value
    }

    fun getVisualizadorPresenter(): DialogVisualizadorMVP.Presenter? {
        return presenterVisualizador
    }

    override fun onPositiveButtonAplicaDes() {
        UserInteraction.getQuestionDialog.dismiss()
        Log.i("Cupon","Aplica Cupon")
        UserInteraction.showNipAComerDialog(fragmentManager, miembro!!.response.membresia!!.numeroMembresia,
            object : DialogInputNipAComer.DialogButtonClickListener {
                override fun onPositiveButton(nip: String?) {
                    run {
                        nipA=nip!!
                        if(descuentoPorcentaje!=null){
                            descuentoPorcentaje.idDesc = 996
                            descuentoPorcentaje.descuento = nip+"-"+ descuentoPorcentaje.descuento
                            descuentoPorcentaje.descripcion = "CUPON A COMER"
                            getDescuento = descuentoPorcentaje
                            redeem(getDescuento!!.textReferencia)
                        }
                    }
                }

            }
        )
    }

    override fun onCancelButtonAplicaDes() {
        Log.i("Cupon","Cancela")
        UserInteraction.getQuestionDialog.dismiss()
    }

    override fun onPositiveButton() {
        threadPagos.interrupt()
        UserInteraction.getDialogWaiting.dismiss()
        UserInteraction.getQuestionDialog.dismiss()
        UserInteraction.showWaitingDialog(fragmentManager2,"Liberando cuenta")
        presenter!!.liberarCuenta()

    }

    override fun onCancelButton() {
        UserInteraction.getQuestionDialog.dismiss()
    }


}
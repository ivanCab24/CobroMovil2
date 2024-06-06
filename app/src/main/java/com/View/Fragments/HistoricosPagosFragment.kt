/*
 * *
 *  * Created by Gerardo Ruiz on 12/17/20 11:01 AM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 12/17/20 11:01 AM
 *
 */
package com.View.Fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.Constants.ConstantsPreferences
import com.Controller.Adapters.AdapterCuentaCerrada
import com.Controller.Adapters.AdapterCuentaCobrada
import com.Controller.BD.DAO.CuentaCerradaDAO
import com.Controller.BD.DAO.CuentaCobradaDAO
import com.Controller.BD.Entity.CuentaCerrada
import com.Controller.BD.Entity.CuentaCobrada
import com.Controller.TouchHelpers.SwipeToRePrintCobro
import com.Controller.TouchHelpers.SwipeToRePrintCuenta
import com.DI.BaseApplication
import com.Interfaces.ActividadPrincipalMVP
import com.Interfaces.DateSelectedListener
import com.Interfaces.SwipedRePrintCobroPosition
import com.Interfaces.SwipedRePrintCuentaPosition
import com.Interfaces.TimeSelectedListener
import com.Interfaces.getCuentaCerradaMVP
import com.Interfaces.getCuentaCobradaMVP
import com.Utilities.Curl
import com.Utilities.Files
import com.Utilities.Filters
import com.Utilities.PreferenceHelper
import com.Utilities.PreferenceHelperLogs
import com.Utilities.Utils.hideKeyboard
import com.View.Activities.ActividadPrincipal
import com.View.Dialogs.DialogQuestion.DialogButtonClickListener.HistoricoActions
import com.View.UserInteraction
import com.innovacion.eks.beerws.R
import com.innovacion.eks.beerws.databinding.FragmentHistoricosPagosBinding
import com.jakewharton.rxbinding4.widget.queryTextChanges
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import org.json.JSONArray
import java.security.GeneralSecurityException
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Named

/**
 * Type: Class.
 * Access: Public.
 * Name: HistoricosPagosFragment.
 *
 * @Description.
 * @EndDescription.
 */
class HistoricosPagosFragment : Fragment, HistoricoActions, getCuentaCerradaMVP.View,
    SwipedRePrintCuentaPosition, SwipedRePrintCobroPosition, DateSelectedListener,
    TimeSelectedListener, getCuentaCobradaMVP.View {
    private var binding: FragmentHistoricosPagosBinding? = null
    private var handler: Handler? = null
    private var runnable: Runnable? = null
    private var estafeta: String? = null
    private var actividadPrincipal: ActividadPrincipal? = null
    private var compositeDisposable: CompositeDisposable? = null
    private var adapterCuentaCerrada: AdapterCuentaCerrada? = null
    private var adapterCuentaCobrada: AdapterCuentaCobrada? = null
    private var itemTouchHelper: ItemTouchHelper? = null
    private var swipeToRePrintCuenta: SwipeToRePrintCuenta? = null
    private var swipeToRePrintCobro: SwipeToRePrintCobro? = null
    private var cuentaCerradaList: List<CuentaCerrada?>? = null
    private var cuentaCobradaList: List<CuentaCobrada?>? = null
    private var selectedFilter = 0
    private var date: String? = null
    private var hour: String? = ""
    private var arrayListReporte: ArrayList<String>? = null
    private var arrayListSumas: List<Double?>? = null
    private var arrayListPropinas: List<Double?>? = null
    private var propinaGlobal = 0.0
    private var totalGlobal = 0.0
    private var contadorGlobal = 0
    private var contadorEstafetas = 0
    private var banderaBusqueda: String? = null
    private var fragmentManager2: FragmentManager? = null
    private var DIALOG_DURATION = 3000
    private var token2 : String? = null

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
     * The Cuenta cerrada dao.
     */
    @JvmField
    @Inject
    var cuentaCerradaDAO: CuentaCerradaDAO? = null

    /**
     * The Cuenta cobrada dao.
     */
    @JvmField
    @Inject
    var cuentaCobradaDAO: CuentaCobradaDAO? = null

    @JvmField
    @Inject
    var actividadPrincipalPresenter: ActividadPrincipalMVP.Presenter? = null

    /**
     * The Get cuenta cerrada presenter.
     */
    @JvmField
    @Inject
    var getCuentaCerradaPresenter: getCuentaCerradaMVP.Presenter? = null

    /**
     * The Get cuenta cobrada presenter.
     */
    @JvmField
    @Inject
    var getCuentaCobradaPresenter: getCuentaCobradaMVP.Presenter? = null

    /**
     * Type: Method.
     * Parent: HistoricosPagosFragment.
     * Name: HistoricosPagosFragment.
     *
     * @Description.
     * @EndDescription.
     */
    constructor() {
        // Required empty public constructor
    }

    /**
     * Type: Method.
     * Parent: HistoricosPagosFragment.
     * Name: HistoricosPagosFragment.
     *
     * @param actividadPrincipal @PsiType:ActividadPrincipal.
     * @Description.
     * @EndDescription.
     */
    constructor(actividadPrincipal: ActividadPrincipal?) {
        this.actividadPrincipal = actividadPrincipal
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context.applicationContext as BaseApplication).plusGetHistoricoPagosSubcomponent()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHistoricosPagosBinding.inflate(inflater, container, false) //FragmentHistoricosPagosBinding
        val view: View = binding!!.root
        fragmentManager2 = getFragmentManager()
        formatter = DecimalFormat("#0.00")
        compositeDisposable = CompositeDisposable()
        initDI()
        getCuentaCobradaPresenter!!.executeCountRecordsTask()
        getCuentaCerradaPresenter!!.executeCountRecordsTask()
        updateUI(binding!!.searchView, false)
        selectedFilter = 0
        binding!!.radioButtonFolio.isChecked = true
        binding!!.imageButtonCuentaCerrada.isEnabled = false
        binding!!.buttonContinuar.setOnClickListener {
            val texto = binding!!.editTextEstafetaToken.text.toString()
            if (!texto.isEmpty()) {
                //getToken()
                estafeta = texto
                executeToken()
                UIControls(false)
                binding!!.editTextToken.requestFocus()
            } else {
                UserInteraction.snackyWarning(activity, null, "Ingresa la estafeta/token")
            }
        }
        binding!!.buttonConfirmar.setOnClickListener {
            if (binding!!.editTextToken.text.toString() == getToken()
            ) {
                hideKeyboard(activity!!)
                binding!!.imageButtonCuentaCerrada.isEnabled = true
                updateUI(binding!!.searchView, true)
                handler!!.removeCallbacks(runnable!!)
                getCuentaCerradaPresenter!!.executeGetCuentasCerradasTask()
            } else {
                Toast.makeText(activity, "Token incorrecto o expirado", Toast.LENGTH_SHORT).show()
            }
        }
        binding!!.imageButtonHome.setOnClickListener {
            if (handler != null && runnable != null) {
                handler!!.removeCallbacks(runnable!!)
            }
            val fragmentManager = activity!!.supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            val contentFragment = ContentFragment()
            fragmentTransaction.replace(R.id.fragment_container, contentFragment) //TENIA EL fragment_container
            fragmentTransaction.commit()
            actividadPrincipal!!.binding.barraLateralConstraint.visibility = View.VISIBLE
        }
        binding!!.searchView.setOnCloseListener {
            hideKeyboard(activity!!)
            filterBy("")
            binding!!.radioButtonFolio.isChecked = true
            selectedFilter = 0
            false
        }
        binding!!.imageButtonCuentaCerrada.setOnClickListener {
            switchControlsCuentas(false)
            Log.i(TAG, "Cuenta Cerrada")
            getCuentaCerradaPresenter!!.executeGetCuentasCerradasTask()
        }
        binding!!.imageButtonReporte.setOnClickListener {

            UserInteraction.showQuestionDialog(
                fragmentManager,
                "Impresión de historico",
                "¿Desea imprimir el reporte de pagos?",
                "Sí",
                "No",
                this,
                "1"
            )
            if (binding!!.recyclerViewCuentasCerradas.visibility == View.VISIBLE) {
                getCuentaCobradaPresenter!!.executeGetAllCuentasCobradasTask()
            }
        }
        binding!!.btnBoucher.setOnClickListener {
            preferenceHelper!!.putBoolean(ConstantsPreferences.PREF_IS_VOUCHER, true)
            UserInteraction.showDialogWaitingPrint(
                fragmentManager,
                "1",
                "Obteniendo información del Voucher",
                "",
                ""
            )
            dismissDialogAfterDelay()
        }
        binding!!.btnTicket.setOnClickListener {
            preferenceHelper!!.putBoolean(ConstantsPreferences.PREF_IS_VOUCHER, false)
            UserInteraction.showDialogWaitingPrint(fragmentManager, "2", "Obteniendo información del Ticket", "", "")
            dismissDialogAfterDelay()
        }
        binding!!.radioButtonFolio.setOnClickListener {
            enableSearchView(binding!!.searchView, true)
            selectedFilter = 0
        }
        binding!!.radioButtonEstafeta.setOnClickListener {
            enableSearchView(binding!!.searchView, true)
            selectedFilter = 1
        }
        binding!!.radioButtonVendedor.setOnClickListener {
            enableSearchView(binding!!.searchView, true)
            selectedFilter = 2
        }
        binding!!.radioButtonDia.setOnClickListener {
            selectedFilter = 3
            enableSearchView(binding!!.searchView, false)
            UserInteraction.showDialogDatePicker(fragmentManager, this)
        }
        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable!!.clear()
    }

    private fun searchMethod(bandera: String) {
        compositeDisposable!!.add(
            binding!!.searchView.queryTextChanges()
                .debounce(500, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .map { obj: CharSequence -> obj.toString() }
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { onNext: String? ->
                        if (bandera == "1") filterBy(onNext)
                        else filterCuentaCobradaBy(onNext)
                    }) { obj: Throwable -> obj.printStackTrace() })
    }

    //================================================================================
    // Inicia Filters
    //================================================================================
    private fun filterCuentaCobradaBy(query: String?) {
        if (!query!!.isEmpty()) {
            var filteredList: MutableList<CuentaCobrada?> = ArrayList()
            when (selectedFilter) {
                0 -> {
                    filteredList.clear()
                    filteredList = Filters.filterByFolioCuentaCobrada(query,
                        cuentaCobradaList as List<CuentaCobrada>
                    ).toMutableList()
                }
                1 -> {
                    filteredList.clear()
                    filteredList = Filters.filterByEstafetaCuentaCobrada(query,
                        cuentaCobradaList as List<CuentaCobrada>
                    ).toMutableList()
                }
                2 -> {
                    filteredList.clear()
                    filteredList = Filters.filterByVendedorCuentaCobrada(query,
                        cuentaCobradaList as List<CuentaCobrada>
                    ).toMutableList()
                }
                3 -> {
                    filteredList.clear()
                    filteredList =
                        hour?.let {
                            Filters.filterByDateCuentaCobrada(query,
                                cuentaCobradaList as List<CuentaCobrada>, it
                            ).toMutableList()
                        }!!
                }
            }
            swipeToRePrintCobro!!.filter(filteredList)
            adapterCuentaCobrada!!.filter(filteredList)
        } else {
            swipeToRePrintCobro!!.filter(cuentaCobradaList)
            adapterCuentaCobrada!!.filter(cuentaCobradaList)
        }
    }

    private fun filterBy(query: String?) {
        if (!query!!.isEmpty()) {
            var filteredList: MutableList<CuentaCerrada?> = ArrayList()
            when (selectedFilter) {
                0 -> {
                    filteredList.clear()
                    filteredList = Filters.filterByFolio(query,
                        cuentaCerradaList as List<CuentaCerrada>
                    ).toMutableList()
                }
                1 -> {
                    filteredList.clear()
                    filteredList = Filters.filterByEstafeta(query,
                        cuentaCerradaList as List<CuentaCerrada>
                    ).toMutableList()
                }
                2 -> {
                    filteredList.clear()
                    filteredList = Filters.filterByVendedor(query,
                        cuentaCerradaList as List<CuentaCerrada>
                    ).toMutableList()
                }
                3 -> {
                    filteredList.clear()
                    filteredList = hour?.let {
                        Filters.filterByDate(query,
                            cuentaCerradaList as List<CuentaCerrada>, it
                        ).toMutableList()
                    }!!
                }
            }
            swipeToRePrintCuenta!!.filter(filteredList)
            adapterCuentaCerrada!!.filter(filteredList)
        } else {
            swipeToRePrintCuenta!!.filter(cuentaCerradaList)
            adapterCuentaCerrada!!.filter(cuentaCerradaList)
        }
    }

    //================================================================================
    // Termina Filters
    //================================================================================
    //================================================================================
    // Inicia UIControls
    //================================================================================
    private fun UIControls(isShowing: Boolean) {
        binding!!.buttonContinuar.visibility =
            if (isShowing) View.VISIBLE else View.GONE
        binding!!.editTextEstafetaToken.visibility = if (isShowing) View.VISIBLE else View.GONE
        binding!!.editTextToken.visibility =
            if (!isShowing) View.VISIBLE else View.GONE
        binding!!.buttonConfirmar.visibility = if (!isShowing) View.VISIBLE else View.GONE
    }

    private fun updateUI(searchView: SearchView, isUpdated: Boolean) {
        for (i in 0 until binding!!.radioGroup.childCount) {
            binding!!.radioGroup.getChildAt(i).isEnabled = isUpdated
        }
        enableSearchView(searchView, isUpdated)
    }

    private fun enableSearchView(view: View, enabled: Boolean) {
        view.isEnabled = enabled
        if (view is ViewGroup) {
            val viewGroup = view
            for (i in 0 until viewGroup.childCount) {
                val child = viewGroup.getChildAt(i)
                enableSearchView(child, enabled)
            }
        }
    }

    private fun switchControls() {
        binding!!.buttonContinuar.visibility = View.GONE
        binding!!.editTextEstafetaToken.visibility = View.GONE
        binding!!.editTextToken.visibility = View.GONE
        binding!!.buttonConfirmar.visibility = View.GONE
        binding!!.recyclerViewCuentasCerradas.visibility = View.VISIBLE
    }

    private fun switchControlsCuentas(isVisible: Boolean) {
        binding!!.imageButtonCuentaCerrada.isEnabled = isVisible
        binding!!.recyclerViewCuentasCerradas.visibility =
            if (isVisible) View.GONE else View.VISIBLE
        binding!!.recyclerViewCuentasCobradas.visibility =
            if (isVisible) View.VISIBLE else View.GONE
    }

    //================================================================================
    // Termina UIControls
    //================================================================================
    private fun initDI() {
        getCuentaCerradaPresenter!!.setView(this)
        getCuentaCerradaPresenter!!.setActivity(activity)
        getCuentaCerradaPresenter!!.setPreferences(sharedPreferences, preferenceHelper)
        getCuentaCerradaPresenter!!.logsInfo(preferenceHelperLogs, files)
        getCuentaCerradaPresenter!!.setCuentaCerradaDao(cuentaCerradaDAO)
        getCuentaCobradaPresenter!!.setView(this)
        getCuentaCobradaPresenter!!.setActivity(activity)
        getCuentaCobradaPresenter!!.setPreferences(sharedPreferences, preferenceHelper)
        getCuentaCobradaPresenter!!.logsInfo(preferenceHelperLogs, files)
        getCuentaCobradaPresenter!!.setCuentaCobradaDao(cuentaCobradaDAO)
    }

    private fun executeToken() {
        handler = Handler()
        runnable = Runnable {
            handler!!.postDelayed({
                try {
                    val tokenGenerado = getToken()
                    //Log.i("TOKEN", tokenGenerado)
                } catch (e: GeneralSecurityException) {
                    e.printStackTrace()
                }
            }, 0)
            handler!!.postDelayed(runnable!!, 0)
        }
        handler!!.postDelayed(runnable!!, 0)
    }

    fun getToken(): String{
       var  texto = binding!!.editTextEstafetaToken.text.toString()
        var params = HashMap<String, String>()
        params["Llave"] = "e936c17f7b54f02d32@@5463e0c3f749080b9"
        params["Usuario"] = texto
        var reponse = Curl.sendPostRequest(params, "Consulta_Token", preferenceHelper!!, 7)
        val jsonArray = JSONArray(reponse)
        val token = jsonArray.getJSONObject(0).getString("TOKEN")
        val codigo = jsonArray.getJSONObject(0).getString("CODIGO")
        token2 = token
        Log.i("token2", token2.toString())
        Log.i("Response2", jsonArray.toString())
        return token2.toString()

    }

    //================================================================================
    // Inicia Presenter CuentasCerradas
    //================================================================================
    override fun onReceiveCuentasCerradas(cuentaCerradaList: List<CuentaCerrada?>?) {
        switchControls()
        this.cuentaCerradaList = cuentaCerradaList
        binding!!.recyclerViewCuentasCerradas.layoutManager =
            LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        binding!!.recyclerViewCuentasCerradas.setHasFixedSize(true)
        adapterCuentaCerrada = AdapterCuentaCerrada(cuentaCerradaList, activity)
        adapterCuentaCerrada!!.notifyDataSetChanged()
        swipeToRePrintCuenta = SwipeToRePrintCuenta(
            adapterCuentaCerrada,
            cuentaCerradaList,
            activity,
            this,
            preferenceHelper!!.getString(ConstantsPreferences.PREF_MARCA_SELECCIONADA, null)
        )
        itemTouchHelper = ItemTouchHelper(swipeToRePrintCuenta!!)
        itemTouchHelper!!.attachToRecyclerView(binding!!.recyclerViewCuentasCerradas)
        binding!!.recyclerViewCuentasCerradas.adapter = adapterCuentaCerrada
        banderaBusqueda = "1"
        searchMethod("1")
    }

    override fun onReceiveCountRecords(countRecords: Int) {
        if (countRecords > 0) getCuentaCerradaPresenter!!.executeDeleteRecordsTask(
            SimpleDateFormat(
                "MM"
            ).format(Date()).toInt()
        )
    }

    //================================================================================
    // Termina Presenter CuentasCerradas
    //================================================================================
    //================================================================================
    // Inicia Presenter SwipeCuentaCerrada
    //================================================================================

    fun dismissDialogAfterDelay() {
        Handler().postDelayed({
            UserInteraction.getDialogWaitingPrint.dismiss()
        }, DIALOG_DURATION.toLong())
    }

    override fun onSwipedRightCuentaPosition(cuentaCerrada: CuentaCerrada?) {
        preferenceHelper!!.putBoolean(ConstantsPreferences.PREF_IS_VOUCHER, true)
        ContentFragment.folioHistorico=cuentaCerrada!!.FOLIOCTA
        UserInteraction.showDialogWaitingPrint(
            fragmentManager,
            "1",
            "Obteniendo información del Voucher",
            cuentaCerrada!!.FOLIOCTA,
            cuentaCerrada.campo32
        )
        dismissDialogAfterDelay()
    }

    override fun onSwipedLeftCuentaPosition(cuentaCerrada: CuentaCerrada?) {
        preferenceHelper!!.putBoolean(ConstantsPreferences.PREF_IS_VOUCHER, false)
        ContentFragment.folioHistorico=cuentaCerrada!!.FOLIOCTA
        UserInteraction.showDialogWaitingPrint(
            fragmentManager,
            "2",
            "Obteniendo información del Ticket",
            cuentaCerrada!!.FOLIOCTA,
            ""
        )
        dismissDialogAfterDelay()
    }

    //================================================================================
    // Termina Presenter SwipeCuentaCerrada
    //================================================================================
    //================================================================================
    // Inicia Presenter Date&Time
    //================================================================================
    override fun selectedDate(date: String?) {
        this.date = date
        UserInteraction.showDialogTimePicker(fragmentManager, this)
    }

    override fun selectedDateCancel() {
        if (banderaBusqueda == "1") {
            filterBy("")
        } else {
            filterCuentaCobradaBy("")
        }
    }

    override fun onTimeSelected(hora: String?) {
        hour = hora
        if (banderaBusqueda == "1") {
            filterBy(date + hora)
        } else {
            filterCuentaCobradaBy(date + hora)
        }
    }

    override fun onTimeCancelSelected() {
        if (banderaBusqueda == "1") {
            filterBy(date)
        } else {
            filterCuentaCobradaBy(date)
        }
    }

    //================================================================================
    // Termina Presenter Date&Time
    //================================================================================
    //================================================================================
    // Inicia Presenter CuentasCobradas
    //================================================================================
    override fun onReciveSumas(sumasList: List<Double?>?) {
        arrayListSumas = sumasList
    }

    override fun onRecivePropinas(propinasList: List<Double?>?) {
        arrayListPropinas = propinasList
    }

    override fun onReciveTotalGlobal(total: Double) {
        totalGlobal = total
    }

    override fun onRecivePropinasGlobal(propina: Double) {
        propinaGlobal = propina
    }

    override fun onReciveCountRecord(count: Int) {
        if (count > 0) {
            getCuentaCobradaPresenter!!.executeDeleteRecords(
                SimpleDateFormat("MM").format(Date()).toInt()
            )
        }
    }

    override fun onReceiveCuentasCobradas(cuentaCobradaList: List<CuentaCobrada?>?) {

        System.out.println("-->Al oprimir el boton de imprimir")
        if (cuentaCobradaList!!.size > 0) {
            for (i in cuentaCobradaList.indices) {
                Log.i(TAG, "onReceiveCuentasCobradas: " + cuentaCobradaList[i].toString())
                arrayListReporte!!.add(
                    cuentaCobradaList[i]!!.pan + "             $" + formatter!!.format(
                        cuentaCobradaList[i]!!.CONSUMO
                    ) + "         $" + formatter!!.format(
                        cuentaCobradaList[i]!!.PROPINA
                    )
                )
            }
            contadorEstafetas++
            if (cuentaCobradaList[0]!!.ESTAFETA.length == 5) {
                arrayListReporte!!.add(
                    "Mesero: " + cuentaCobradaList[0]!!.ESTAFETA + "    $" + formatter!!.format(
                        arrayListSumas!![contadorEstafetas - 1]!!
                    ) + "         $" + formatter!!.format(
                        arrayListPropinas!![contadorEstafetas - 1]!!
                    )
                )
            } else {
                arrayListReporte!!.add(
                    "Mesero:" + cuentaCobradaList[0]!!.ESTAFETA + "    $" + formatter!!.format(
                        arrayListSumas!![contadorEstafetas - 1]!!
                    ) + "        $" + formatter!!.format(
                        arrayListPropinas!![contadorEstafetas - 1]!!
                    )
                )
            }
            arrayListReporte!!.add("")
            arrayListReporte!!.add("")
            if (contadorGlobal == contadorEstafetas) {
                arrayListReporte!!.add(
                    "TOTAL            $" + formatter!!.format(totalGlobal) + "         $" + formatter!!.format(
                        propinaGlobal
                    )
                )
                arrayListReporte!!.add("")
                arrayListReporte!!.add("")
                arrayListReporte!!.add("GLOBAL           $" + formatter!!.format(totalGlobal))
                arrayListReporte!!.add("")
                arrayListReporte!!.add("")
                UserInteraction.showDialogWaitingPrintHistorico(
                    fragmentManager,
                    "3",
                    "Imprimiendo historico",
                    arrayListReporte)

                contadorGlobal = 0
                contadorEstafetas = 0
            }
            for (i in arrayListReporte!!.indices) {
                Log.i(TAG, "onReciveEstafetas: " + arrayListReporte!![i])
            }
        } else {
            UserInteraction.snackyFail(activity, null, "No hay información para consultar")
        }
    }

    override fun onReceiveAllCuentasCobradas(cuentaCobradaList: List<CuentaCobrada?>?) {
        switchControlsCuentas(true)
        this.cuentaCobradaList = cuentaCobradaList
        binding!!.recyclerViewCuentasCobradas.layoutManager =
            LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        binding!!.recyclerViewCuentasCobradas.setHasFixedSize(true)
        adapterCuentaCobrada = AdapterCuentaCobrada(activity, cuentaCobradaList)
        adapterCuentaCobrada!!.notifyDataSetChanged()
        swipeToRePrintCobro =
            SwipeToRePrintCobro(adapterCuentaCobrada, cuentaCobradaList, this, activity)
        itemTouchHelper = ItemTouchHelper(swipeToRePrintCobro!!)
        itemTouchHelper!!.attachToRecyclerView(binding!!.recyclerViewCuentasCobradas)
        binding!!.recyclerViewCuentasCobradas.adapter = adapterCuentaCobrada
        banderaBusqueda = "2"
        searchMethod("2")
    }

    override fun onReciveEstafetas(estafetasList: List<String?>?) {
        if (estafetasList!!.size > 0) {
            contadorGlobal = estafetasList.size
            arrayListReporte = ArrayList()
            arrayListReporte!!.add("REPORTE DE MESEROS")
            arrayListReporte!!.add("")
            arrayListReporte!!.add("OPERACIONES AJUSTADAS")
            arrayListReporte!!.add("TARJETA          CONSUMO         PROPINA")
            for (i in estafetasList.indices) {
                getCuentaCobradaPresenter!!.executeGetCuentasCobradasTask(estafetasList[i])
            }
        } else {
            UserInteraction.snackyFail(activity, null, "No hay información para consultar")
        }
    }

    //================================================================================
    // Termina Presenter CuentasCobradas
    //================================================================================
    //================================================================================
    // Inicia Presenter SwipeCuentaCobrada
    //================================================================================
    override fun onSwipedLeftCobroPosition(cuentaCobrada: CuentaCobrada?) {
        preferenceHelper!!.putBoolean(ConstantsPreferences.PREF_IS_VOUCHER, true)
        ContentFragment.folioHistorico = cuentaCobrada!!.FOLIO
        UserInteraction.showDialogWaitingPrint(
            fragmentManager,
            "1",
            "Obteniendo información del Voucher",
            cuentaCobrada!!.FOLIO,
            cuentaCobrada.campo32
        )
    }

    //================================================================================
    // Termina Presenter SwipeCuentaCobrada
    //================================================================================
    //================================================================================
    // Inicia DialogQuestion Action
    //================================================================================
    override fun onPositiveButton() {
        UserInteraction.getQuestionDialog.dismiss()
        getCuentaCobradaPresenter!!.executeGetEstafetasTask()
    }

    override fun onCancelButton() {
        UserInteraction.getQuestionDialog.dismiss()
    }

    companion object {
        private const val TAG = "HistoricosPagosFragment"
        private var formatter: DecimalFormat? = null
    }
}
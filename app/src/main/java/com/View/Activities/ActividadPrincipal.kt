package com.View.Activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowInsets
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.Constants.ConstantsPreferences
import com.DI.BaseApplication
import com.DataModel.Producto
import com.Interfaces.ActividadPrincipalMVP
import com.Interfaces.EscaneoAfiliadoResult
import com.Interfaces.SettingsMVP
import com.Utilities.Files
import com.Utilities.PreferenceHelper
import com.Utilities.PreferenceHelperLogs
import com.Utilities.Utilities
import com.Verifone.VerifoneTaskManager
import com.View.Dialogs.DialogQuestion.DialogButtonClickListener.ExitActions
import com.View.Fragments.ContentFragment
import com.View.UserInteraction
import com.github.ybq.android.spinkit.style.DoubleBounce
import com.google.zxing.integration.android.IntentIntegrator
import com.innovacion.eks.beerws.R
import com.innovacion.eks.beerws.databinding.ActividadPrincipalBinding
import com.orhanobut.hawk.Hawk
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.json.JSONArray
import org.json.JSONException
import java.util.*
import javax.inject.Inject


/**
 * Type: Class.
 * Access: Public.
 * Name: ActividadPrincipal.
 *
 * @Description.
 * @EndDescription.
 */
class ActividadPrincipal : AppCompatActivity(), ActividadPrincipalMVP.View, ExitActions {

    lateinit var binding: ActividadPrincipalBinding
    private var inputMethodManager: InputMethodManager? = null
    private var doubleBounce: DoubleBounce? = null
    private var marca: String = ""
    lateinit var disposable: CompositeDisposable
    lateinit var fragmentManager: FragmentManager


    @Inject
    lateinit var presenterConfig: SettingsMVP.Presenter
    /**
     * The Binding.
     */

    /**
     * The Preference helper.
     */

    @Inject
    lateinit var preferenceHelper: PreferenceHelper

    /**
     * The Preference helper logs.
     */

    @Inject
    lateinit var preferenceHelperLogs: PreferenceHelperLogs

    /**
     * The Files.
     */
    @Inject
    lateinit var files: Files

    @Inject
    lateinit var presenter: ActividadPrincipalMVP.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        (applicationContext as BaseApplication).appComponent.inject(this)
        initDI()
        Hawk.init(this).build();
        presenter.setDefaultSharedPreferences()
        presenter.getMarca()
        setTheme(Utilities.setUpTheme(marca))
        doubleBounce = DoubleBounce()
        doubleBounce?.color = Utilities.setUpDoubleBounce(this, marca)
        binding = ActividadPrincipalBinding.inflate(
            layoutInflater
        )
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(binding.root)
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        fragmentManager = supportFragmentManager
        presenter.setFM(fragmentManager)
        fm = supportFragmentManager
        val ft = fm!!.beginTransaction()
        val contentFragment = ContentFragment()
        ft.replace(R.id.fragment_container, contentFragment)
        ft.commit()
        binding.imageButtonIdentificarMiembro.setOnClickListener {
            if(ContentFragment.banderaCuentabuscada)
                if(ContentFragment.cuenta.saldo> 0 )
                    presenter.btnIdentificarMiembro()
                else
                    UserInteraction.snackyWarning(this,null,"Es necesario buscar una mesa primero")
            else
                UserInteraction.snackyWarning(this,null,"Es necesario buscar una mesa primero")
        }
        binding.imageButtonResetKeys.setOnClickListener {
            presenter.btnResetKey()
        }
        binding.imageButtonReimprimir.setOnClickListener { //imageButtonReimprimir
            presenter.btnHistorico()
        }
        binding.imageButton3.setOnClickListener {
            presenter.btnSettings()
        }

        binding.imageButton.setOnClickListener {
            exitButtonAction()
        }
        binding.imageBtnPayclub.setOnClickListener {
            presenter.btnVisualizador()
        }
        Utilities.preferenceHelper = preferenceHelper
        if(preferenceHelper.getBoolean(ConstantsPreferences.SWITCH_TRAINING))
            UserInteraction.snackyWarning(this,null,"Modo de pruebas activado")
    }

    override fun onBackPressed() {}

    override fun onStop() {
        super.onStop()
        VerifoneTaskManager.restPinpad()
        VerifoneTaskManager.desconectaPinpad()
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //Lectura del codigo QR
        Log.i("Al leercodigo QR--->", resultCode.toString())
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null && result.contents != null) {
            Log.i(TAG, "onActivityResult: " + result.contents)
            if (result.contents.startsWith("A") && result.contents.length == 10) escaneoAfiliadoResult!!.onResultAfiliado(
                result.contents
            ) else try {
                val jsonArray = JSONArray("[" + result.contents + "]")
                Log.i("Valor del JSONArray", jsonArray.toString())
                val memberNumber = jsonArray.getJSONObject(0).getString("memberNumber") //tomando la membresia
                if (memberNumber != "") escaneoAfiliadoResult!!.onResultAfiliado(memberNumber) //si es afiliado
                else escaneoAfiliadoResult!!.onFailResultAfiliado(
                    "QR invalido"
                )
            } catch (e: JSONException) {
                try{
                    val jsonArray = JSONArray("[" + result.contents + "]")
                    val cupon = jsonArray.getJSONObject(0).getString("cupon")
                    val plu = jsonArray.getJSONObject(0).getString("plu")
                    if(!plu.equals("999999")){
                        val producto:Producto
                        val plus:ArrayList<String> = ArrayList()
                        plus.add(plu)
                        if(ContentFragment.cuenta.detalle.containPLU(plus)){
                            producto=ContentFragment.cuenta.detalle.getMinPrice(plus)!!
                            escaneoAfiliadoResult!!.onResultCuponEncuestas(cupon,producto)
                        }
                        else
                            escaneoAfiliadoResult!!.onFailCupon()
                    }else{
                        escaneoAfiliadoResult!!.onResultCuponEncuestas20(cupon)
                    }

                }catch (e: Exception){
                    if(result.contents.length==10)
                        escaneoAfiliadoResult!!.onResultCuponGRG(result.contents)
                    else{
                        try {// colocar una nueva condicional para el cupon de la licuadora
                            val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
                            val jsonArray = JSONArray("[" + result.contents + "]")
                            val licuadoraFolio = jsonArray.getJSONObject(0).getString("folio")
                            if(licuadoraFolio.isNotEmpty()){
                                escaneoAfiliadoResult!!.onResultCuponEngachement(licuadoraFolio) //evaluacion del cupon
                            }else{
                                escaneoAfiliadoResult!!.onExceptionResultAfiliado("Codigo QR no valido") //evaluacion del cupon
                            }
                            //evaluacion por si el cupon no el valido
                        }catch (e: Exception){
                            escaneoAfiliadoResult!!.onExceptionResultAfiliado("Codigo QR no valido")
                        }
                    }
                    e.printStackTrace()
                }

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }



    /**
     * Type: Method.
     * Parent: ActividadPrincipal.
     * Name: exitButtonAction.
     *
     * @param v @PsiType:View.
     * @Description.
     * @EndDescription.
     */
    fun exitButtonAction() {
        ContentFragment.miembro = null
        ContentFragment.copiaMiembro = null
        files.registerLogs(
            "Boton de salir presionado",
            "", preferenceHelperLogs, preferenceHelper
        )
        UserInteraction.showQuestionDialog(
            fragmentManager,
            applicationContext.resources.getString(R.string.aviso),
            applicationContext.resources.getString(R.string.pregunta),
            "Salir",
            "Cancelar",
            null,
            "0"
        )
    }

    /**
     * Type: Method.
     * Parent: ActividadPrincipal.
     * Name: settingsButtonAction.
     *
     * @Description.
     * @EndDescription.
     * @param: v @PsiType:View.
     */

    override fun onPositiveButton() {
        preferenceHelper.removePreference(ConstantsPreferences.PREF_BANDERA_AFILIADO)
        files.registerLogs(
            "Boton de salir si presionado",
            "", preferenceHelperLogs, preferenceHelper
        )
        UserInteraction.getQuestionDialog.dismiss()
        startActivity(Intent(this@ActividadPrincipal, LoginActivity::class.java))
        finish()
        VerifoneTaskManager.restPinpad()
        VerifoneTaskManager.desconectaPinpad()
        ContentFragment.contentFragment!!.controlsSwitch(false)
    }

    override fun onCancelButton() {
        files.registerLogs(
            "Boton de salir no presionado",
            "", preferenceHelperLogs, preferenceHelper
        )
        UserInteraction.getQuestionDialog.dismiss()
    }


    companion object {
        private const val TAG = "ActividadPrincipal"

        /**
         * The constant fm.
         */
        var fm: FragmentManager? = null

        /**
         * The constant puntos.
         */
        var puntos = ""

        /**
         * The Bandera dialog mostrar.
         */
        @JvmField
        var banderaDialogMostrar = ""

        /**
         * The constant escaneoAfiliadoResult.
         */
        @JvmField
        var escaneoAfiliadoResult: EscaneoAfiliadoResult? = null

        /**
         * The constant isDescuentoGRG.
         */
        @JvmField
        var isDescuentoGRG = false
    }

    override fun getCurrentMarca(value: String) {
        marca = value
    }

    private fun initDI() {
        presenter.setView(this)
        presenter.setPreferenceHelper(preferenceHelper)
        presenter.setPreferenceHelperLogs(preferenceHelperLogs)
        presenter.setActivity(this)
        presenter.setFiles(files)
    }
}
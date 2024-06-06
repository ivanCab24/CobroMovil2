package com.View.Dialogs
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.Constants.ConstantsIntents
import com.Constants.ConstantsMarcas
import com.Constants.ConstantsPreferences
import com.DI.BaseApplication
import com.Interfaces.SettingsMVP
import com.Utilities.Curl
import com.Utilities.PreferenceHelper
import com.View.Activities.PinpadE285BtActivity
import com.View.UserInteraction
import com.github.ybq.android.spinkit.style.DoubleBounce
import com.innovacion.eks.beerws.R
import com.innovacion.eks.beerws.databinding.SettingsDialogLayoutBinding
import com.webservicestasks.ToksWebServicesConnection
import org.json.JSONArray
import java.util.Calendar
import javax.inject.Inject

/**
 * Type: Class.
 * Access: Public.
 * Name: DialogSettings.
 *
 * @Description.
 * @EndDescription.
 */
class DialogSettings : DialogFragment(), SettingsMVP.View {

    private val TAG = "DialogSettings"
    private var binding: SettingsDialogLayoutBinding? = null
    private var doubleBounce: DoubleBounce? = null
    private var mListener: View.OnClickListener? = null
    private var banderaActivity: String = ""

    fun checkTime():Boolean{
        val time = Calendar.getInstance().time
        val horas = time.hours
        val minutos =time.minutes
        val tot = horas*60+minutos
        Log.i("ññññ",tot.toString())
        return (tot in 180..690)

    }
    lateinit var thread:Thread

    var currentTime = Calendar.getInstance().time
    @Inject
    lateinit var preferenceHelper: PreferenceHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SettingsDialogLayoutBinding.inflate(inflater, container, false) //SettingsDialogLayoutBinding
        return binding?.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context.applicationContext as BaseApplication).appComponent.inject(this)
        doubleBounce = DoubleBounce()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { args ->
            this.banderaActivity = args.getString(ARG_BANDERA_ACTIVITY, "")
        } ?: run {
            Toast.makeText(context, "Error instatiating DialogSettings", Toast.LENGTH_SHORT).show()
        }
    }

    @Inject
    lateinit var presenter: SettingsMVP.Presenter

    companion object {
        const val ARG_BANDERA_ACTIVITY = "banderaActivity"
        fun newInstance(banderaActivity: String): DialogSettings {
            val args = Bundle()
            args.putString(ARG_BANDERA_ACTIVITY, banderaActivity)
            val dialog = DialogSettings()
            dialog.arguments = args
            return dialog
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.setView(this)
        presenter.getCurrentConfig()
        presenter.getMarca()

        setUpListener()
        binding?.let {
            with(it) {
                progressBarBines.indeterminateDrawable = doubleBounce

                saveDialogButton.setOnClickListener(mListener)

                btnBuscarPinpad.setOnClickListener(mListener)

                btnBuscarImpresora.setOnClickListener(mListener)

                updateBinButton.setOnClickListener(mListener)

                themeButton.setOnClickListener(mListener)

                switchTraining.setOnClickListener (mListener)
            }
        }

    }

    private fun setUpListener() {
        mListener = View.OnClickListener {
            when (it) {
                binding?.saveDialogButton -> {
                    presenter.saveCurrentConfig()
                    dismiss()
                }
                binding?.btnBuscarPinpad -> {
                    val intent = Intent(activity, PinpadE285BtActivity::class.java).apply {
                        putExtra(ConstantsIntents.INTENT_PINPAD, banderaActivity)
                    }
                    startActivity(intent)
                }

                binding?.updateBinButton -> {
                    presenter.onUpdateBinesButtonClick()
                }

                binding?.btnBuscarImpresora -> {
                    presenter.saveCurrentConfig()
                    UserInteraction.showDialogSelectPrinter(fragmentManager, banderaActivity)
                    dismiss()
                }

                binding?.themeButton -> {
                    UserInteraction.showSeleccionaMarcaDialog(fragmentManager)
                }

                binding?.switchTraining ->{
                    if (checkTime()){


                        if (!binding?.switchTraining!!.isChecked){
                            preferenceHelper.putString(ConstantsPreferences.PREF_SERVER, preferenceHelper.getString(ConstantsPreferences.PREF_SERVER_COPY, null))
                            preferenceHelper.putString(ConstantsPreferences.PREF_UNIDAD, preferenceHelper.getString(ConstantsPreferences.PREF_UNIDAD_COPY, null))
                            preferenceHelper.putBoolean(ConstantsPreferences.SWITCH_TRAINING, false)
                            setUnidad(preferenceHelper.getString(ConstantsPreferences.PREF_UNIDAD_COPY, null))
                            setServerIp(preferenceHelper.getString(ConstantsPreferences.PREF_SERVER_COPY, null))
                            setSwitchTraining(false)
                            UserInteraction.snackyWarning(null,binding?.root,"Modo de pruebas desactivado")
                            UnockFields()
                        }else{
                            binding?.switchTraining!!.isChecked = false
                            UserInteraction.showWaitingDialog(fragmentManager,"Obteniendo Token")
                            thread = Thread {
                                try {
                                    var params= HashMap<String,String>()
                                    params["Llave"] = "e936c17f7b54f02d32@@5463e0c3f749080b9"
                                    params["Cadena"] = ToksWebServicesConnection.STRING

                                    params["Unidad"] = preferenceHelper!!.getString(ConstantsPreferences.PREF_UNIDAD, null)

                                    val reponse= Curl.sendPostRequest(params,"Autoriza_training",preferenceHelper,7)
                                    val jsonArray = JSONArray(reponse)
                                    val nip = jsonArray.getJSONObject(0).getString("nip")

                                    preferenceHelper.putString(
                                        ConstantsPreferences.PREF_TOKEN,nip
                                    )
                                    Log.i("Revisa_saldo", reponse)
                                    UserInteraction.getDialogWaiting.dismiss()
                                    UserInteraction.showTokenInputDialog(fragmentManager,"77","99")
                                    preferenceHelper.getString(
                                            ConstantsPreferences.PREF_TOKEN,
                                            null
                                    )
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                    UserInteraction.getDialogWaiting.dismiss()
                                }
                            }
                            thread.start()

                        }
                    }else{
                        preferenceHelper.putString(ConstantsPreferences.PREF_SERVER, preferenceHelper.getString(ConstantsPreferences.PREF_SERVER_COPY, null))
                        preferenceHelper.putString(ConstantsPreferences.PREF_UNIDAD, preferenceHelper.getString(ConstantsPreferences.PREF_UNIDAD_COPY, null))
                        preferenceHelper.putBoolean(ConstantsPreferences.SWITCH_TRAINING, false)
                        setUnidad(preferenceHelper.getString(ConstantsPreferences.PREF_UNIDAD_COPY, null))
                        setServerIp(preferenceHelper.getString(ConstantsPreferences.PREF_SERVER_COPY, null))
                        setSwitchTraining(false)

                        UnockFields()
                        UserInteraction.snackyWarning(null,binding?.root,"Modo de pruebas fuera de horario")
                    }


                }
            }

        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        dialog?.let { it ->
            it.window?.let {
                it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                it.attributes.windowAnimations = R.style.DialogTheme
                it.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
            }
        }
    }

    override fun onDestroyView() {
        mListener = null
        doubleBounce = null
        binding = null
        super.onDestroyView()
    }

    override fun getServerIp(): String {
        return binding?.IPEditText?.text.toString()
    }

    override fun getIpTerminal(): String {
        return binding?.IPPindpad?.text.toString()
    }

    override fun getNumeroTerminal(): String {
        return binding?.numeroTerminal?.text.toString()
    }

    fun getToken():String{
        var params= HashMap<String,String>()
        params["Llave"] = "e936c17f7b54f02d32@@5463e0c3f749080b9"
        params["Cadena"] = ToksWebServicesConnection.STRING
        params["Unidad"] = preferenceHelper!!.getString(ConstantsPreferences.PREF_UNIDAD, null)
        val a = Curl.Companion.sendPostRequest(params,"Autoriza_training",preferenceHelper,2)

        println(a)
        return  a.toString()
    }

    fun setTraining(){
        preferenceHelper.putString(ConstantsPreferences.PREF_SERVER, "172.20.239.17")
        preferenceHelper.putString(ConstantsPreferences.PREF_UNIDAD, "2502")
        preferenceHelper.putBoolean(ConstantsPreferences.SWITCH_TRAINING, true)
        setUnidad("2502")
        setServerIp("172.20.239.17")
        setSwitchTraining(true)
        UserInteraction.snackyWarning(null,binding?.root,"Modo de pruebas activado")
        lockFields()
    }

    override fun getCajaMP(): String {
        return binding?.editTextTextCajaMP?.text.toString()
    }

    override fun getUnidad(): String {
        return binding?.unidadEditText?.text.toString()
    }

    override fun getImpresora(): String {
        return binding?.buscarImpresora?.text.toString()
    }

    override fun setServerIp(value: String) {
        binding?.IPEditText?.setText(value)
    }

    override fun setSwitchTraining(value: Boolean) {
        binding?.switchTraining?.isChecked=value
    }

    override fun lockFields() {
        binding?.IPEditText!!.isEnabled= false
        binding?.editTextTextCajaMP!!.isEnabled= false
        binding?.numeroTerminal!!.isEnabled= false
        binding?.unidadEditText!!.isEnabled= false
    }

    override fun UnockFields() {
        binding?.IPEditText!!.isEnabled= true
        binding?.editTextTextCajaMP!!.isEnabled= true
        binding?.numeroTerminal!!.isEnabled= true
        binding?.unidadEditText!!.isEnabled= true
    }

    override fun setIpTerminal(value: String) {
        binding?.IPPindpad?.setText(value)
    }

    override fun setNumeroTerminal(value: String) {
        binding?.numeroTerminal?.setText(value)
    }

    override fun setCajaMP(value: String) {
        binding?.editTextTextCajaMP?.setText(value)
    }

    override fun setUnidad(value: String) {
        binding?.unidadEditText?.setText(value)
    }

    override fun setImpresora(value: String) {
        binding?.buscarImpresora?.setText(value)
    }

    override fun getCurrentMarca(value: String) {
        when (value) {
            ConstantsMarcas.MARCA_BEER_FACTORY -> doubleBounce?.color =
                ContextCompat.getColor(context!!, R.color.doradoBeer)
            ConstantsMarcas.MARCA_EL_FAROLITO, ConstantsMarcas.MARCA_TOKS -> doubleBounce?.color =
                ContextCompat.getColor(context!!, R.color.white)
        }
    }

    override fun updateUI(isUpdated: Boolean) {
        dialog?.setCancelable(!isUpdated)
        binding?.let {
            with(it) {
                progressBarBines.visibility = if (isUpdated) View.VISIBLE else View.INVISIBLE
                updateBinButton.isEnabled = !isUpdated
                themeButton.isEnabled = !isUpdated
                btnBuscarImpresora.isEnabled = !isUpdated
                saveDialogButton.isEnabled = !isUpdated
            }
        }

    }

    override fun onExceptionResult(onExceptionResult: String) {
        UserInteraction.snackyException(null, binding?.root, onExceptionResult)
    }

    override fun onFailResult(onFailResult: String) {
        UserInteraction.snackyFail(null, binding?.root, onFailResult)
    }

    override fun onSuccessResult(onSuccessResult: String) {
        UserInteraction.snackySuccess(null, binding?.root, onSuccessResult)
    }

}
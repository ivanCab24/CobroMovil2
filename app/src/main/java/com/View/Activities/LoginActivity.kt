package com.View.Activities

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.provider.Settings
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.Constants.ConstantsPreferences
import com.Constants.ConstantsPreferencesLogs
import com.DI.BaseApplication
import com.Interfaces.LoginAcitivityMVP
import com.Interfaces.SettingsMVP
import com.Utilities.*
import com.View.Dialogs.CustomDialog
import com.View.Dialogs.DialogWaiting
import com.View.UserInteraction
import com.github.ybq.android.spinkit.style.DoubleBounce
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.BuildConfig
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.get
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.innovacion.eks.beerws.databinding.ActivityLoginBinding
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.json.JSONArray
import javax.inject.Inject


class LoginActivity : AppCompatActivity(), LoginAcitivityMVP.View {
    private val PERMISSIONS_STORAGE = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH_PRIVILEGED
    )
    private val PERMISSIONS_LOCATION = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH_PRIVILEGED
    )

    data class Feature(
            val version: String,
            val description: String
    )

    val features = arrayOf(
            Feature(
                    version = "4.0.5.0",
                    description = "Implementación del modo Training para modo pruebas \nCambio de proceso de generación de NIP"
            ),
            Feature(
                    version = "4.0.4.9",
                    description = "Temporizador de nip para poder validar por token gerencial"
            ),
            Feature(
                    version = "4.0.4.8",
                    description = "Corrección de cupón en cache, omite nip en identificar miembro corporativo"
            )
    )

    lateinit var binding: ActivityLoginBinding
    private var inputMethodManager: InputMethodManager? = null
    private var doubleBounce: DoubleBounce? = null
    private var marca: String = ""
    lateinit var disposable: CompositeDisposable
    lateinit var fragmentManager: FragmentManager

    @Inject
    lateinit var presenterConfig: SettingsMVP.Presenter

    private val LOCATION_RQ = 103
    private val WRITE_MANAGE_RQ = 104
    private val WRITE_RQ = 101
    private val READ_RQ = 102

    private lateinit var nsdManager:NsdManager

    // Instantiate a new DiscoveryListener
    private val discoveryListener = object : NsdManager.DiscoveryListener {

        // Called as soon as service discovery begins.
        override fun onDiscoveryStarted(regType: String) {
            Log.d(TAG, "Service discovery started")
        }

        override fun onServiceFound(service: NsdServiceInfo) {
            // A service was found! Do something with it.
            Log.d(TAG, "Service discovery success$service")
        }

        override fun onServiceLost(service: NsdServiceInfo) {
            // When the network service is no longer available.
            // Internal bookkeeping code goes here.
            Log.e(TAG, "service lost: $service")
        }

        override fun onDiscoveryStopped(serviceType: String) {
            Log.i(TAG, "Discovery stopped: $serviceType")
        }

        override fun onStartDiscoveryFailed(serviceType: String, errorCode: Int) {
            Log.e(TAG, "Discovery failed: Error code:$errorCode")
            nsdManager.stopServiceDiscovery(this)
        }

        override fun onStopDiscoveryFailed(serviceType: String, errorCode: Int) {
            Log.e(TAG, "Discovery failed: Error code:$errorCode")
            nsdManager.stopServiceDiscovery(this)
        }
    }



    @Inject
    lateinit var preferenceHelper: PreferenceHelper

    @Inject
    lateinit var preferenceHelperLogs: PreferenceHelperLogs

    @Inject
    lateinit var files: Files

    @Inject
    lateinit var presenter: LoginAcitivityMVP.Presenter

    private val TAG = "LoginActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as BaseApplication).appComponent.inject(this)
        presenter.setView(this)
        presenter.setDefaultSharedPreferences()
        presenter.getMarca()

        presenter.setActivity(this@LoginActivity)
        setTheme(Utilities.setUpTheme(marca))

        doubleBounce = DoubleBounce()
        doubleBounce?.color = Utilities.setUpDoubleBounce(this, marca)

        binding = ActivityLoginBinding.inflate(layoutInflater)
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
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_DENIED)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
            {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.BLUETOOTH_CONNECT), 2)
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.BLUETOOTH_SCAN), 2)
                return
            }
        }

        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3
        }
        checkPermissions()
        val remoteConfig = Firebase.remoteConfig
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.fetchAndActivate()
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val con = Firebase.remoteConfig.get("config")
                        var jsonArray = JSONArray(con.asString())
                        for (jsonIndex in 0..(jsonArray.length() - 1)) {
                            var unidad = jsonArray.getJSONObject(jsonIndex).getString("unidad")
                            if (unidad.equals(preferenceHelper.getString(ConstantsPreferences.PREF_UNIDAD, null))) {
                                //var ip = jsonArray.getJSONObject(jsonIndex).getString("ip")
                                //var mp = jsonArray.getJSONObject(jsonIndex).getString("MP")
                                //setConfigRemote(ip,mp)
                            }
                        }
                    } else {
                        Toast.makeText(
                                this, "Fetch failed",
                                Toast.LENGTH_SHORT
                        ).show()
                    }
                }

        remoteConfig.fetchAndActivate()
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val mensaje = Firebase.remoteConfig.get("mensaje")
                        if (mensaje.asString().length != 0)
                            UserInteraction.snackyWarning(this, null, mensaje.asString())
                    } else {
                        Toast.makeText(
                                this, "Fetch failed",
                                Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        Firebase.crashlytics.setCrashlyticsCollectionEnabled(!BuildConfig.DEBUG)
        Firebase.crashlytics.setUserId(
                "${preferenceHelper.getString(ConstantsPreferences.PREF_UNIDAD, null)} ${
                    preferenceHelper.getString(
                            ConstantsPreferences.PREF_NUMERO_TERMINAL,
                            null
                    )
                }"
        )
        if (preferenceHelper.getString(ConstantsPreferences.PREF_BANDERA_AFILIADO, null) == "1")
            preferenceHelper.removePreference(ConstantsPreferences.PREF_BANDERA_AFILIADO)
        fragmentManager = supportFragmentManager
        if (Build.VERSION.SDK_INT >= 23) presenter.requestPermission()
        if (SDK_INT > 8) {
            val policy = ThreadPolicy.Builder()
                    .permitAll().build()
            StrictMode.setThreadPolicy(policy)
            //your codes here
        }
        if (marca.isEmpty()) UserInteraction.showSeleccionaMarcaDialog(fragmentManager)
        disposable = CompositeDisposable()
        Utils.checkIfBluetoothIsOn(this)
        with(binding) {
            textViewVersion.text = Utilities.getVersionName()
            progressBarLogin.indeterminateDrawable = doubleBounce
            userEditText.isFocusable = true
            passwordEditText.setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE -> {
                        inputMethodManager?.hideSoftInputFromWindow(
                                binding.root.windowToken,
                                0
                        )
                        buttonLogin.isEnabled = false
                        buttonLoginAction()
                        true
                    }
                    else -> false
                }
            }

            buttonLogin.setOnClickListener {
                runOnUiThread {
                    try {
                        buttonLoginAction()

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

            }

            textViewRevision.setOnClickListener {
                var text = ""
                for(feature in features){
                    text+="\n"+feature.version+"\n"+feature.description+"\n"
                }
                UserInteraction.showCustomDialog(fragmentManager,text,object : CustomDialog.DialogButtonClickListener {
                    override fun onPositiveButton() {

                        UserInteraction.getCustomDialog.dismiss()
                    }
                })}


            imageButtonSettings.setOnClickListener {
                presenter.disableButtons()
                inputMethodManager?.hideSoftInputFromWindow(userEditText.windowToken, 0)
                UserInteraction.showSettingsDialog(fragmentManager, "0")
            }

            imageButtonUpdate.setOnClickListener {
                presenter.disableButtons()
                UserInteraction.showDialogDonwloadFile(
                        fragmentManager
                )
            }

            imageButtonSubirArchivos.setOnClickListener {
                presenter.disableButtons()
                UserInteraction.showWaitingDialog(fragmentManager, "Verificando la conectividad con el WebService","Cerrar",object:
                        DialogWaiting.DialogButtonClickListener{
                    override fun onPositiveButton() {
                        UserInteraction.getDialogWaiting.dismiss()
                    }
                })
                nsdManager.discoverServices("_http._tcp",NsdManager.PROTOCOL_DNS_SD,discoveryListener)
                //(uploadFilesAction()
            }
        }
        presenter.sendTickets()
        presenter.sendLogs()
        inputMethodManager =
                this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        nsdManager = applicationContext.getSystemService(NSD_SERVICE) as NsdManager
        if(preferenceHelper.getBoolean(ConstantsPreferences.SWITCH_TRAINING))
            UserInteraction.snackyWarning(this,null,"Modo de pruebas activado")

    }

    override fun onPause() {
        super.onPause()
        disposable.clear()
    }

    override fun onStop() {
        super.onStop()
        disposable.clear()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.clear()
    }

    private fun checkPermissions() {
        val permission1 =
                ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val permission2 =
                ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN)
        if (permission1 != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_STORAGE,
                    1
            )
        } else if (permission2 != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_LOCATION,
                    1
            )
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        Utils.hideSoftKeyboard(binding.root, this)
        return super.dispatchTouchEvent(ev)
    }


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        permissions.forEachIndexed { index, item ->
            if (item == Manifest.permission.ACCESS_FINE_LOCATION && grantResults[index] == -1) {
                presenter.requestFineLocationPermission()
            } else if (item == Manifest.permission.WRITE_EXTERNAL_STORAGE && grantResults[index] == -1 ||
                    item == Manifest.permission.READ_EXTERNAL_STORAGE && grantResults[index] == -1
            ) {
                presenter.requestWriteReadPermission()
            }
        }
    }

    private fun showAskPermissionDialog(description: String) {
        UserInteraction.showCustomDialog(
                fragmentManager,
                description,
                object : CustomDialog.DialogButtonClickListener {
                    override fun onPositiveButton() {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            val uri = Uri.fromParts("package", packageName, null)
                            data = uri
                        }
                        startActivityForResult(intent, 1022)
                    }
                })

    }

    private fun uploadFilesAction() {
        if (files.allFiles.isNotEmpty() || preferenceHelperLogs.getString(ConstantsPreferencesLogs.PREF_LOGS).isNotEmpty()
        ) {
            files.createLogFileFromPreferences(preferenceHelper, preferenceHelperLogs)
            UserInteraction.showUploadFilesDialog(fragmentManager)
        } else {
            UserInteraction.snackyWarning(this, null, "No hay archivos a subir")
        }
    }

    private fun buttonLoginAction() {
        try {
            if (!presenter.requiereUpdate()){
                inputMethodManager?.hideSoftInputFromWindow(binding.root.windowToken, 0)
                presenter.onLoginButtonClicked()
            }

            else {
                UserInteraction.snackyWarning(this, null, "Actualice la versión")
                UserInteraction.showDialogDonwloadFile(
                        fragmentManager
                )

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        /*inputMethodManager?.hideSoftInputFromWindow(binding.root.windowToken, 0)
        presenter.onLoginButtonClicked()*/
    }

    //================================================================================
    // Inicia Presenter Login
    //================================================================================

    override fun getEstafeta(): String {
        return binding.userEditText.text.toString()
    }

    override fun getPassword(): String {
        return binding.passwordEditText.text.toString()
    }

    override fun getCurrentMarca(value: String) {
        marca = value
    }

    override fun uiControl(isTrue: Boolean) {
        binding.buttonLogin.isEnabled = isTrue
        binding.progressBarLogin.visibility = if (!isTrue) View.VISIBLE else View.INVISIBLE
    }

    override fun clearFields() {
        binding.userEditText.setText("")
        binding.passwordEditText.setText("")
    }

    override fun requestAllPermissions() {
        ActivityCompat.requestPermissions(
                this,
                arrayOf(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION
                ),
                WRITE_RQ
        )
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun requestWriteAndReadPermissions() {

        if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            showAskPermissionDialog("Necesitamos acceso al almacenamiento para poder dar soporte adecuado en caso de alguna falla.")
        } else {
            ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                    ),
                    WRITE_RQ
            )

        }

    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun requestFineLocation() {
        if (!shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
            showAskPermissionDialog("Necesitamos acceso a la ubicación para poder conectarnos con las impresoras y terminales.")
        } else {
            ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_RQ
            )
        }
    }

    override fun areWritePermissionGranted(): Boolean = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    ) == PackageManager.PERMISSION_GRANTED

    override fun areReadPermissionGranted(): Boolean = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
    ) == PackageManager.PERMISSION_GRANTED

    override fun areFineLocationPermissionGranted(): Boolean = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED


    override fun onExceptionLoginResult(onExceptionResult: String) {
        UserInteraction.snackyException(this, null, onExceptionResult)
    }

    override fun onFailLoginResult(onFailResult: String) {
        UserInteraction.snackyFail(this, null, onFailResult)
    }

    override fun onWarningLoginResult(onWarningResult: String) {
        UserInteraction.snackyWarning(this, null, onWarningResult)
    }

    override fun onSuccessLoginResult() {
        val intent = Intent(this, ActividadPrincipal::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

}
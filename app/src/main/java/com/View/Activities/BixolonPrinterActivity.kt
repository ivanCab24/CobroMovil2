package com.View.Activities

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.Constants.ConstantsIntents
import com.Constants.ConstantsPreferences
import com.Controller.Adapters.AdapterBondedE285
import com.Controller.Adapters.AdapterPinpadE285
import com.DI.BaseApplication
import com.Interfaces.PinpadE285BondedSelectionListener
import com.Interfaces.PinpadE285MVP
import com.Interfaces.PinpadE285SelectionListener
import com.Utilities.Files
import com.Utilities.PreferenceHelper
import com.Utilities.Utilities
import com.View.Dialogs.DialogQuestion
import com.View.UserInteraction
import com.github.ybq.android.spinkit.style.DoubleBounce
import com.innovacion.eks.beerws.databinding.ActivityPinpadE285BtBinding
import javax.inject.Inject

class BixolonPrinterActivity : AppCompatActivity(), PinpadE285MVP.View, PinpadE285SelectionListener,
    PinpadE285BondedSelectionListener, DialogQuestion.DialogButtonClickListener.RemoveBondActions {

    private val TAG = "DialogPinpadE285"
    private lateinit var binding: ActivityPinpadE285BtBinding
    private var bluetoothAdapter: BluetoothAdapter? = null
    private var btDevices: MutableList<BluetoothDevice> = mutableListOf()
    private var btPairedDevices: MutableList<BluetoothDevice> = mutableListOf()
    private var deviceToRemoveBond: BluetoothDevice? = null

    private var adapterPinpadE285: AdapterPinpadE285? = null
    private var adapterBondedE285: AdapterBondedE285? = null

    private var handlerStopDiscovery: Handler? = null
    private var runnaStopDiscovery: Runnable? = null
    private val timeOut: Long = 30000

    private var marca: String = ""
    private var btAddress: String = ""
    private var banderaOrigen: String? = null

    private var doubleBounce: DoubleBounce? = null

    private lateinit var fragmentManager: FragmentManager

    @Inject
    lateinit var files: Files

    @Inject
    lateinit var preferenceHelper: PreferenceHelper

    @Inject
    lateinit var presenter: PinpadE285MVP.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as BaseApplication).appComponent.inject(this)

        presenter.setView(this)
        presenter.getBrand()

        setTheme(Utilities.setUpTheme(marca))

        doubleBounce = DoubleBounce()
        doubleBounce?.color = Utilities.setUpDoubleBounce(this, marca)

        requestWindowFeature(Window.FEATURE_NO_TITLE)

        binding = ActivityPinpadE285BtBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Utilities.fullScreenActivity(window)

        fragmentManager = supportFragmentManager

        banderaOrigen = intent.getStringExtra(ConstantsIntents.INTENT_PINPAD)

        with(binding) {

            progressBarPinpad.indeterminateDrawable = doubleBounce

            recyclerViewPinpad.layoutManager =
                LinearLayoutManager(this@BixolonPrinterActivity, RecyclerView.VERTICAL, false)
            recyclerViewPinpad.setHasFixedSize(true)

            recyclerViewBondedDevices.layoutManager =
                LinearLayoutManager(this@BixolonPrinterActivity, RecyclerView.VERTICAL, false)
            recyclerViewBondedDevices.setHasFixedSize(true)

            progressBarPinpad.visibility = View.VISIBLE

            textViewTitle.text = "Buscando impresoras"

            btnClosePinpad.setOnClickListener {
                finish()
                returnToActivity()
            }

            btnRestartSearch.setOnClickListener {
            System.out.println("--->Boton que selecciona la busqueda de impresora<---")
                stopSearch()
                Utilities.stopHandler(runnaStopDiscovery, handlerStopDiscovery)
                Utilities.startHandler(runnaStopDiscovery, handlerStopDiscovery, timeOut)
                btDevices.clear()
                adapterPinpadE285?.notifyDataSetChanged()
                updateUI(true, "Buscando impresoras")
                restartDiscovery()

            }

        }

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

        adapterPinpadE285 = AdapterPinpadE285(btDevices, this)
        adapterPinpadE285?.notifyDataSetChanged()
        binding.recyclerViewPinpad.adapter = adapterPinpadE285

        adapterBondedE285 = AdapterBondedE285(btPairedDevices, this)
        adapterBondedE285?.notifyDataSetChanged()
        binding.recyclerViewBondedDevices.adapter = adapterBondedE285

        getPairedDevices()

        handlerStopDiscovery = Handler(Looper.getMainLooper())
        runnaStopDiscovery = Runnable { stopSearch() }
        runnaStopDiscovery?.let { handlerStopDiscovery?.postDelayed(it, timeOut) }

        try {

            startSearch()

        } catch (e: Exception) {
            e.printStackTrace()
            files.createFileException(
                "View/Activities/PinpadE285Activity/startSearch/${
                    Log.getStackTraceString(
                        e
                    )
                }", preferenceHelper
            )
        }

    }

    override fun onStop() {
        super.onStop()

        try {

            unregisterReceiver(mReceiver)
            Utilities.stopHandler(runnaStopDiscovery, handlerStopDiscovery)

        } catch (e: Exception) {
            e.printStackTrace()
        }


    }

    override fun onBackPressed() {}

    //================================================================================
    // General Methods
    //================================================================================

    private fun returnToActivity() {

        when {
            banderaOrigen.isNullOrEmpty() -> {
                startActivity(Intent(this@BixolonPrinterActivity, LoginActivity::class.java))
            }
            banderaOrigen == "0" -> {
                startActivity(Intent(this@BixolonPrinterActivity, LoginActivity::class.java))
            }
            else -> {
                startActivity(
                    Intent(
                        this@BixolonPrinterActivity,
                        ActividadPrincipal::class.java
                    )
                )
            }
        }

    }

    private fun startSearch() {
        val intentFilter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        registerReceiver(mReceiver, intentFilter)
        bluetoothAdapter?.startDiscovery()
    }

    private var mReceiver: BroadcastReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent?) {

            intent?.let { intent ->

                val action = intent.action

                Log.i(TAG, "onReceive: $action")

                if (BluetoothDevice.ACTION_FOUND == action) {

                    val device: BluetoothDevice? =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)

                    device?.let { device ->

                        if (!device.name.isNullOrEmpty()) {

                            if (device.name.startsWith("SPP")) {

                                btDevices.add(device)
                                adapterPinpadE285?.notifyDataSetChanged()
                                binding.progressBarPinpad.visibility = View.GONE

                            }

                        }

                    }
                } else {

                    val device: BluetoothDevice? =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)

                    device?.let { device ->

                        when (device.bondState) {
                            BluetoothDevice.BOND_BONDED -> {

                                preferenceHelper.putString(ConstantsPreferences.PREF_MODELO_IMPRESORA, "BIXOLON")
                                UserInteraction.snackySuccess(
                                    this@BixolonPrinterActivity,
                                    null,
                                    "Emparejamiento exitoso"
                                )
                                btAddress = device.address
                                btPairedDevices.add(device)
                                btDevices.remove(device)

                                adapterPinpadE285?.notifyDataSetChanged()

                                binding.textView12.visibility = View.VISIBLE
                                binding.recyclerViewBondedDevices.visibility = View.VISIBLE

                                //presenter.savePinpadAddress()
                                preferenceHelper.putString(ConstantsPreferences.PREF_IMPRESORA, btAddress)
                                preferenceHelper.putString(ConstantsPreferences.PREF_IMPRESORA_NAME, device.name)

                            }
                            BluetoothDevice.BOND_BONDING -> {
                                Toast.makeText(
                                    this@BixolonPrinterActivity,
                                    "Emparejando...",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                            else -> {
                                UserInteraction.snackyFail(
                                    this@BixolonPrinterActivity,
                                    null,
                                    "Emparejamiento no exitoso"
                                )
                            }
                        }
                    }

                }
            }


        }

    }

    private fun stopSearch() {

        try {

            runnaStopDiscovery?.let { handlerStopDiscovery?.removeCallbacks(it) }
            updateUI(false, "Dispositivos encontrados")
            updateUIButtons(true)
            bluetoothAdapter?.cancelDiscovery()

        } catch (e: Exception) {
            e.printStackTrace()
            files.createFileException(
                "View/Activities/PinpadE285Activity/stopSearch/${
                    Log.getStackTraceString(
                        e
                    )
                }", preferenceHelper
            )
        }

    }

    private fun restartDiscovery() {

        btDevices.clear()
        adapterPinpadE285?.notifyDataSetChanged()

        try {

            bluetoothAdapter?.startDiscovery()

        } catch (e: Exception) {

            e.printStackTrace()
            files.createFileException(
                "View/Activities/PinpadE285Activity/restartDiscovery/${
                    Log.getStackTraceString(
                        e
                    )
                }", preferenceHelper
            )

        }

    }

    private fun removeBond(device: BluetoothDevice): Boolean {

        val btClass = Class.forName("android.bluetooth.BluetoothDevice")
        val removeBondMethod = btClass.getMethod("removeBond")

        return removeBondMethod.invoke(device) as Boolean

    }

    private fun waitForResult() {

        val intentFilter = IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED)
        registerReceiver(mReceiver, intentFilter)

    }

    private fun getPairedDevices() {

        bluetoothAdapter?.let { bluetoothAdapter ->

            val pairedDevice = bluetoothAdapter.bondedDevices

            for (device in pairedDevice) {
                if (device.name.startsWith("SPP")) {
                    btPairedDevices.add(device)
                    adapterBondedE285?.notifyDataSetChanged()
                }
            }

            if (btPairedDevices.size > 0) {
                binding.textView12.visibility = View.VISIBLE
                binding.recyclerViewBondedDevices.visibility = View.VISIBLE
            }

        }


    }

    //================================================================================
    // Inicia PinpadE285 Presenter
    //================================================================================
    override fun getPinpadAddress(): String = btAddress

    override fun getCurrentMarca(value: String) {
        marca = value
    }

    override fun updateUI(isChanged: Boolean, text: String) {

        with(binding) {
            progressBarPinpad.visibility = if (isChanged) View.VISIBLE else View.INVISIBLE
            textViewTitle.text = text
        }

    }

    override fun updateUIButtons(isChanged: Boolean) {

        with(binding) {
            btnRestartSearch.isEnabled = isChanged
            btnClosePinpad.isEnabled = isChanged
        }

    }

    //================================================================================
    // Termina PinpadE285 Presenter
    //================================================================================

    //================================================================================
    // Inicia Pinpad SelectionListener
    //================================================================================

    override fun onSelectedPinpadE285(btDevice: BluetoothDevice) {

        var isBonded = false

        try {

            isBonded = btDevice.createBond()

            if (isBonded) {

                Toast.makeText(this, "Iniciando Emparejamiento", Toast.LENGTH_SHORT).show()

                waitForResult()

            } else {

                UserInteraction.snackyWarning(this, null, "Dispositivo ya vinculado")

            }

        } catch (e: Exception) {
            e.printStackTrace()
            files.createFileException(
                "View/Activities/PinpadE285Activity/onSelectedPinpadE285/${
                    Log.getStackTraceString(
                        e
                    )
                }", preferenceHelper
            )

        }

    }

    //================================================================================
    // Termina Pinpad SelectionListener
    //================================================================================

    //================================================================================
    // Inicia Pinpad Bonded Selection Listener
    //================================================================================

    override fun selectedBondedDevice(device: BluetoothDevice) {

        deviceToRemoveBond = device

        UserInteraction.showQuestionDialog(
            fragmentManager, "Aviso",
            "¿Realmente desea desvincular la terminal ${device.address}?", "Sí", "No", null, "2"
        )

    }

    // ================================================================================
    // Termina Pinpad Bonded Selection Listener
    //================================================================================

    //================================================================================
    // Inicia DialogQuestion Actions
    //================================================================================
    override fun onPositiveButton() {

        UserInteraction.getQuestionDialog.dismiss()

        deviceToRemoveBond?.let { device ->
            if (removeBond(device)) {
                preferenceHelper.removePreference(ConstantsPreferences.PREF_IMPRESORA)
                preferenceHelper.removePreference(ConstantsPreferences.PREF_IMPRESORA_NAME)
                btPairedDevices.remove(device)
                adapterBondedE285?.notifyDataSetChanged()
                if (btPairedDevices.size <= 0) {
                    binding.textView12.visibility = View.GONE
                    binding.recyclerViewBondedDevices.visibility = View.GONE
                }
            }
        } ?: run {
            UserInteraction.snackyException(
                this@BixolonPrinterActivity,
                null,
                "No se pudo desvincular, reinicie la aplicación."
            )
        }

    }

    override fun onCancelButton() {

        UserInteraction.getQuestionDialog.dismiss()

    }

}

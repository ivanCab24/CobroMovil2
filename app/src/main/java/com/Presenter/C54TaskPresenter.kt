package com.Presenter

import android.content.SharedPreferences
import com.DataModel.SaleVtol
import com.Interfaces.C54TaskMPV
import com.Utilities.Files
import com.Utilities.PreferenceHelper
import com.Utilities.PreferenceHelperLogs
import com.Verifone.BtManager
import com.Verifone.VerifonePinpadInterface
import java.lang.ref.WeakReference
import javax.inject.Inject

/**
 * Type: Class.
 * Access: Public.
 * Name: C54Task.
 *
 * @Description.
 * @EndDescription.
 */
class C54TaskPresenter @Inject constructor(
    private val model: C54TaskMPV.Model
) : C54TaskMPV.Presenter, VerifonePinpadInterface {
    override fun setView(getView: C54TaskMPV.View?) {
        view = getView
    }

    override fun setPreferences(
        sharedPreferences: SharedPreferences?,
        preferenceHelper: PreferenceHelper?
    ) {
        getSharedPreferences = sharedPreferences
        Companion.preferenceHelper = preferenceHelper
    }

    override fun setLogsInfo(preferenceHelperLogs: PreferenceHelperLogs?, files: Files?) {
        Companion.preferenceHelperLogs = preferenceHelperLogs
        filesWeakReference = WeakReference(files)
    }

    override fun executeC54Task(
        saleVtol: SaleVtol?,
        btManager: BtManager?,
        transactionType: String?
    ) {
        model.executeC54(saleVtol, btManager, transactionType)
    }

    companion object {
        const val TAG = "C54Task"
        var view: C54TaskMPV.View? = null
        var getSharedPreferences: SharedPreferences? = null
        var preferenceHelper: PreferenceHelper? = null
        var preferenceHelperLogs: PreferenceHelperLogs? = null
        var filesWeakReference: WeakReference<Files?>? = null
        var regreso = ArrayList<String>()
        var tag9f27 = ""
        var tag9f27Aux = ""
        var tag9f27Aux1 = ""
        var OutPutError = ""
        var outPutByte = ""
        var mensaje = ""
        var transaccionExitosa = false
        var outpuError = ""
        val chDataIn = ByteArray(1024)
        val chAck = byteArrayOf(0x06)
        var msg = byteArrayOf()
    }
}
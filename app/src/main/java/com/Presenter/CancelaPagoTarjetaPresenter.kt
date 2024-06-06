package com.Presenter

import android.content.SharedPreferences
import com.Interfaces.CancelaPagoTarjetaMVP
import com.Utilities.Files
import com.Utilities.PreferenceHelper
import com.Utilities.PreferenceHelperLogs
import com.webservicestasks.ToksWebServicesConnection
import java.lang.ref.WeakReference
import javax.inject.Inject

/**
 * Type: Class.
 * Access: Public.
 * Name: CancelaPagoTarjetaRequest.
 *
 * @Description.
 * @EndDescription.
 */
class CancelaPagoTarjetaPresenter @Inject constructor(
    private val model: CancelaPagoTarjetaMVP.Model
): ToksWebServicesConnection, CancelaPagoTarjetaMVP.Presenter {
    override fun setView(getView: CancelaPagoTarjetaMVP.View?) {
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

    override fun executeCancelaPago() {
        model.executeCancelaPagoRequest()
    }
    companion object {
        const val TAG = "CancelaPagoTarjetaReque"
        var view: CancelaPagoTarjetaMVP.View? = null
        var getSharedPreferences: SharedPreferences? = null
        var preferenceHelper: PreferenceHelper? = null
        var preferenceHelperLogs: PreferenceHelperLogs? = null
        var filesWeakReference: WeakReference<Files?>? = null

    }
}
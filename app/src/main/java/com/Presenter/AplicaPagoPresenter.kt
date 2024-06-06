package com.Presenter

import android.content.SharedPreferences
import com.Interfaces.AplicaPagoMVP
import com.Utilities.Files
import com.Utilities.PreferenceHelper
import com.Utilities.PreferenceHelperLogs
import com.webservicestasks.ToksWebServicesConnection
import java.lang.ref.WeakReference
import javax.inject.Inject

/**
 * Type: Class.
 * Access: Public.
 * Name: AplicaPagoRequest.
 *
 * @Description.
 * @EndDescription.
 */
class AplicaPagoPresenter @Inject constructor(
    private val model: AplicaPagoMVP.Model): ToksWebServicesConnection, AplicaPagoMVP.Presenter {
    override fun setView(getView: AplicaPagoMVP.View?) {
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

    override fun onExecuteAplicaPago(autorizacion: String?) {
        model.executeAplicaPagoRequest(autorizacion)
    }



    companion object {
        const val TAG = "AplicaPagoRequest"
        var view: AplicaPagoMVP.View? = null
        var getSharedPreferences: SharedPreferences? = null
        var preferenceHelper: PreferenceHelper? = null
        var preferenceHelperLogs: PreferenceHelperLogs? = null
        var filesWeakReference: WeakReference<Files?>? = null
    }
}
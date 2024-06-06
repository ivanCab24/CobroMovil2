package com.Presenter

import android.content.SharedPreferences
import com.Interfaces.CierraCuentaMVP
import com.Utilities.Files
import com.Utilities.PreferenceHelper
import com.Utilities.PreferenceHelperLogs
import com.webservicestasks.ToksWebServicesConnection
import java.lang.ref.WeakReference
import javax.inject.Inject

/**
 * Type: Class.
 * Access: Public.
 * Name: CierraCuentaRequest.
 *
 * @Description.
 * @EndDescription.
 */
class CierraCuentaPresenter @Inject constructor(
    private val model: CierraCuentaMVP.Model
) : ToksWebServicesConnection, CierraCuentaMVP.Presenter {
    override fun setView(getView: CierraCuentaMVP.View?) {
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

    override fun executeCierraCuenta() {
        model.executeCierraCuentaRequest()
    }


    companion object {
        const val TAG = "CierraCuentaRequest"
        var view: CierraCuentaMVP.View? = null
        var getSharedPreferences: SharedPreferences? = null
        var preferenceHelper: PreferenceHelper? = null
        var preferenceHelperLogs: PreferenceHelperLogs? = null
        var filesWeakReference: WeakReference<Files?>? = null
    }
}
package com.Presenter

import android.content.SharedPreferences
import com.Interfaces.CancelarPagoMVP
import com.Utilities.Files
import com.Utilities.PreferenceHelper
import com.Utilities.PreferenceHelperLogs
import com.webservicestasks.ToksWebServicesConnection
import java.lang.ref.WeakReference
import javax.inject.Inject

/**
 * Type: Class.
 * Access: Public.
 * Name: CancelarPagoRequest.
 *
 * @Description.
 * @EndDescription.
 */
class CancelarPagoPresenter @Inject constructor(
    private val model: CancelarPagoMVP.Model
): ToksWebServicesConnection, CancelarPagoMVP.Presenter {
    override fun setView(getView: CancelarPagoMVP.View?) {
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

    override fun executeCancelarPago() {
        model.executeCancelarPagoRequest()
    }

    companion object {
        const val TAG = "CancelarPagoRequest"
        var view: CancelarPagoMVP.View? = null
        var getSharedPreferences: SharedPreferences? = null
        var preferenceHelper: PreferenceHelper? = null
        var preferenceHelperLogs: PreferenceHelperLogs? = null
        var filesWeakReference: WeakReference<Files?>? = null
    }
}
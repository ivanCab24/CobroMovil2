package com.Presenter

import android.content.SharedPreferences
import com.Interfaces.DescuentosMVP
import com.Utilities.Files
import com.Utilities.PreferenceHelper
import com.Utilities.PreferenceHelperLogs
import com.webservicestasks.ToksWebServicesConnection
import java.lang.ref.WeakReference
import javax.inject.Inject

/**
 * Type: Class.
 * Access: Public.
 * Name: DescuentosRequest.
 *
 * @Description.
 * @EndDescription.
 */
class DescuentosPresenter @Inject constructor(
    private val model: DescuentosMVP.Model
) : ToksWebServicesConnection, DescuentosMVP.Presenter {
    override fun setView(getView: DescuentosMVP.View?) {
        view = getView
    }

    override fun setPreferences(
        sharedPreferences: SharedPreferences?,
        preferenceHelper: PreferenceHelper?
    ) {
        getSharedPreferences = sharedPreferences
        preferencesHelper = preferenceHelper
    }

    override fun setLogsInfo(preferenceHelperLogs: PreferenceHelperLogs?, files: Files?) {
        Companion.preferenceHelperLogs = preferenceHelperLogs
        filesWeakReference = WeakReference(files)
    }

    override fun executeDescuentosRequest() {
        model.executeDescuentosAplicadosRequest()
        model.executeFormasPagoRequest()
    }

    companion object {
        const val TAG = "DescuentosRequest"
        var view: DescuentosMVP.View? = null
        var getSharedPreferences: SharedPreferences? = null
        var preferencesHelper: PreferenceHelper? = null
        var preferenceHelperLogs: PreferenceHelperLogs? = null
        var filesWeakReference: WeakReference<Files?>? = null
    }
}
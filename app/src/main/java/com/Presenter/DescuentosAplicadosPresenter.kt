package com.Presenter

import android.content.SharedPreferences
import com.Interfaces.DescuentosAplicadosMVP
import com.Utilities.Files
import com.Utilities.PreferenceHelper
import com.Utilities.PreferenceHelperLogs
import com.webservicestasks.ToksWebServicesConnection
import java.lang.ref.WeakReference
import javax.inject.Inject

/**
 * Type: Class.
 * Access: Public.
 * Name: DescuentosAplicadosRequest.
 *
 * @Description.
 * @EndDescription.
 */
class DescuentosAplicadosPresenter  @Inject constructor(
    private val model: DescuentosAplicadosMVP.Model
) : ToksWebServicesConnection,
    DescuentosAplicadosMVP.Presenter {
    override fun setView(getView: DescuentosAplicadosMVP.View?) {
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

    override fun executeDescuentosAplicados() {
        model.executeDescuentosAplicadosRequest()
    }

    companion object {
        const val TAG = "DescuentosAplicadosRequ"
        var view: DescuentosAplicadosMVP.View? = null
        var getSharedPreferences: SharedPreferences? = null
        var preferenceHelper: PreferenceHelper? = null
        var preferenceHelperLogs: PreferenceHelperLogs? = null
        var filesWeakReference: WeakReference<Files?>? = null

    }
}
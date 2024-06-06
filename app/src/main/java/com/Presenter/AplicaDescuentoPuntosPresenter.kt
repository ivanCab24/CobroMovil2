package com.Presenter

import android.content.SharedPreferences
import com.Interfaces.AplicaDescuentoPuntosMVP
import com.Utilities.Files
import com.Utilities.PreferenceHelper
import com.Utilities.PreferenceHelperLogs
import com.webservicestasks.ToksWebServicesConnection
import java.lang.ref.WeakReference
import javax.inject.Inject

/**
 * Type: Class.
 * Access: Public.
 * Name: AplicaDescuentoPuntosRequest.
 *
 * @Description.
 * @EndDescription.
 */
class AplicaDescuentoPuntosPresenter @Inject constructor(
    val model: AplicaDescuentoPuntosMVP.Model
) : ToksWebServicesConnection,
    AplicaDescuentoPuntosMVP.Presenter {
    override fun setView(getView: AplicaDescuentoPuntosMVP.View?) {
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

    override fun executeAplicaDescuento(puntosARedimir: String?) {
        model.executeAplicaDescuentoRequest(puntosARedimir)
    }

    companion object {
        const val TAG = "AplicaDescuentoPuntosRe"
        var view: AplicaDescuentoPuntosMVP.View? = null
        var getSharedPreferences: SharedPreferences? = null
        var preferenceHelper: PreferenceHelper? = null
        var preferenceHelperLogs: PreferenceHelperLogs? = null
        var filesWeakReference: WeakReference<Files?>? = null
    }
}
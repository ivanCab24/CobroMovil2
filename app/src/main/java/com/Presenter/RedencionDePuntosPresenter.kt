package com.Presenter

import android.content.SharedPreferences
import com.Constants.ConstantsPreferences
import com.Interfaces.RedencionDePuntosMVP
import com.Utilities.Files
import com.Utilities.PreferenceHelper
import com.Utilities.PreferenceHelperLogs
import com.webservicestasks.ToksWebServicesConnection
import java.lang.ref.WeakReference
import javax.inject.Inject

/**
 * Type: Class.
 * Access: Public.
 * Name: RedencionDePuntosRequest.
 *
 * @Description.
 * @EndDescription.
 */
class RedencionDePuntosPresenter @Inject constructor(
    private val model: RedencionDePuntosMVP.Model
) : ToksWebServicesConnection, RedencionDePuntosMVP.Presenter {

    override fun setView(getView: RedencionDePuntosMVP.View?) {
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

    override fun executeRedencionDePuntos(puntosARedimir: String?, nip: String?) {
        if (model.executeValidaInformacion(puntosARedimir)) model.executeRedencionPRequest(
            puntosARedimir,
            nip
        )
    }

    override fun getMarca() {
        view!!.getCurrentMarca(preferenceHelper!!.getString(ConstantsPreferences.PREF_MARCA_SELECCIONADA, null))
    }

    companion object {
        const val TAG = "AplicaPagoRequest"
        var view: RedencionDePuntosMVP.View? = null
        var getSharedPreferences: SharedPreferences? = null
        var preferenceHelper: PreferenceHelper? = null
        var preferenceHelperLogs: PreferenceHelperLogs? = null
        var filesWeakReference: WeakReference<Files?>? = null
    }
}
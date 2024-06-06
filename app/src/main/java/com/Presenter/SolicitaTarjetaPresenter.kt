package com.Presenter

import android.app.Activity
import android.content.SharedPreferences
import com.Controller.BD.DAO.CatalogoBinesDAO
import com.Interfaces.SolicitaTarjetaMVP
import com.Utilities.Files
import com.Utilities.PreferenceHelper
import com.Utilities.PreferenceHelperLogs
import com.Verifone.EMVResponse
import com.Verifone.VerifonePinpadInterface
import java.lang.ref.WeakReference
import javax.inject.Inject

/**
 * Type: Class.
 * Access: Public.
 * Name: SolicitaTarjetaPresenter.
 *
 * @Description.
 * @EndDescription.
 */
class SolicitaTarjetaPresenter @Inject constructor(
    private val model: SolicitaTarjetaMVP.Model
) : VerifonePinpadInterface, SolicitaTarjetaMVP.Presenter {
    override fun getMarca(): String {
        return ""
    }

    override fun setView(getView: SolicitaTarjetaMVP.View?) {
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

    override fun setBinesDao(binesDao: CatalogoBinesDAO?) {
        binesDAOWeakReference = WeakReference(binesDao)
    }

    override fun setActivity(activity: Activity?) {
        activityWeakReference = WeakReference(activity)
    }

    override fun executeSolicitaTarjeta() {
        model.executeSolicitaTarjetaPresenter()
    }

    companion object {
        const val TAG = "SolicitaTarjetaPresenter"
        var view: SolicitaTarjetaMVP.View? = null
        var getSharedPreferences: SharedPreferences? = null
        var preferenceHelper: PreferenceHelper? = null
        var preferenceHelperLogs: PreferenceHelperLogs? = null
        var filesWeakReference: WeakReference<Files?>? = null
        var activityWeakReference: WeakReference<Activity?>? = null
        var binesDAOWeakReference: WeakReference<CatalogoBinesDAO?>? = null
        var response: EMVResponse? = null
        var modoLect = "N"
        var auxPan = ""
        var mensaje = ""
        var year: String? = null
        var month: String? = null
        var day: String? = null
        var puntos = 0
        var idFPGO = 0
        var tipoFPGO = 0
    }
}
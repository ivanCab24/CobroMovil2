package com.Presenter

import android.app.Activity
import android.content.SharedPreferences
import com.Controller.BD.DAO.CatalogoBinesDAO
import com.Interfaces.SolicitaRefoundMVP
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
 * Name: SolicitaRefoundTask.
 *
 * @Description.
 * @EndDescription.
 */
class SolicitaRefoundPresenter @Inject constructor(
    private val model: SolicitaRefoundMVP.Model
): VerifonePinpadInterface, SolicitaRefoundMVP.Presenter {

    override fun setView(getView: SolicitaRefoundMVP.View?) {
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

    override fun setCatalogoBinesDao(catalogoBinesDao: CatalogoBinesDAO?) {
        catalogoBinesDAOWeakReference = WeakReference(catalogoBinesDao)
    }

    override fun setActivity(activity: Activity?) {
        activityWeakReference = WeakReference(activity)
    }

    override fun executeSolicitaRefound() {
        model.executeC51RefoundTask()
    }

    companion object {
        const val TAG = "SolicitaRefoundTask"
        var mensaje = ""
        var auxPan = ""
        var year = ""
        var month = ""
        var day = ""
        const val ticket = ""
        var response: EMVResponse? = null
        var view: SolicitaRefoundMVP.View? = null
        var getSharedPreferences: SharedPreferences? = null
        var preferenceHelper: PreferenceHelper? = null
        var preferenceHelperLogs: PreferenceHelperLogs? = null
        var filesWeakReference: WeakReference<Files?>? = null
        var activityWeakReference: WeakReference<Activity?>? = null
        var catalogoBinesDAOWeakReference: WeakReference<CatalogoBinesDAO?>? = null
    }
}
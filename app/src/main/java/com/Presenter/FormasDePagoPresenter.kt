package com.Presenter

import android.content.SharedPreferences
import com.DataModel.FormasDePago
import com.Interfaces.FormasDePagoMPV
import com.Utilities.Files
import com.Utilities.PreferenceHelper
import com.Utilities.PreferenceHelperLogs
import com.webservicestasks.ToksWebServicesConnection
import java.lang.ref.WeakReference
import javax.inject.Inject

/**
 * Type: Class.
 * Access: Public.
 * Name: FormasDePagoRequest.
 *
 * @Description.
 * @EndDescription.
 */
class FormasDePagoPresenter @Inject constructor(
    private val model: FormasDePagoMPV.Model
) : ToksWebServicesConnection, FormasDePagoMPV.Presenter {
    override fun setView(getView: FormasDePagoMPV.View?) {
        view = getView
    }

    override fun setPreferences(sharedPreferences: SharedPreferences?, preferenceHelper: PreferenceHelper?) {
        getSharedPreferences = sharedPreferences
        preferencesHelper = preferenceHelper
    }

    override fun setLogsInfo(preferenceHelperLogs: PreferenceHelperLogs?, files: Files?) {
        Companion.preferenceHelperLogs = preferenceHelperLogs
        filesWeakReference = WeakReference(files)
    }

    override fun executeFormasDePago() {
        model.executeFormasPagoRequest()
    }

    companion object {
        const val TAG = "FormasDePagoRequest"
        var view: FormasDePagoMPV.View? = null
        var getSharedPreferences: SharedPreferences? = null
        var preferencesHelper: PreferenceHelper? = null
        var preferenceHelperLogs: PreferenceHelperLogs? = null
        var filesWeakReference: WeakReference<Files?>? = null
        val formasDePagoArrayList = ArrayList<FormasDePago?>()
    }
}
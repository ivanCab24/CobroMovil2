package com.Presenter

import android.content.SharedPreferences
import com.Interfaces.CancelacionVtolMVP
import com.Utilities.Files
import com.Utilities.PreferenceHelper
import com.Utilities.PreferenceHelperLogs
import com.Verifone.EMVResponse
import com.webservicestasks.ToksWebServicesConnection
import java.lang.ref.WeakReference
import javax.inject.Inject

/**
 * Type: Class.
 * Access: Public.
 * Name: CancelacionVtolRequest.
 *
 * @Description.
 * @EndDescription.
 */
class CancelacionVtolPresenter @Inject constructor(
    private val model: CancelacionVtolMVP.Model
): ToksWebServicesConnection, CancelacionVtolMVP.Presenter {
    override fun setView(getView: CancelacionVtolMVP.View?) {
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

    override fun executeCancelacionVtol(
        response: EMVResponse?,
        year: String?,
        month: String?,
        day: String?,
        ticket: String?
    ) {
        model.executeCancelacionVtolRequest(response, year, month, day, ticket)
    }

    companion object {
        const val TAG = "CancelacionVtolRequest"
        var view: CancelacionVtolMVP.View? = null
        var getSharedPreferences: SharedPreferences? = null
        var preferenceHelper: PreferenceHelper? = null
        var preferenceHelperLogs: PreferenceHelperLogs? = null
        var filesWeakReference: WeakReference<Files?>? = null
    }
}
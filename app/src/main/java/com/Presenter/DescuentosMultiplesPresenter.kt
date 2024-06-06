package com.Presenter

import android.content.SharedPreferences
import com.Interfaces.DescuentosMultiplesMVP
import com.Utilities.Files
import com.Utilities.PreferenceHelper
import com.Utilities.PreferenceHelperLogs
import com.webservicestasks.ToksWebServicesConnection
import java.lang.ref.WeakReference
import javax.inject.Inject

/**
 * Type: Class.
 * Access: Public.
 * Name: DescuentosMultiplesRequest.
 *
 * @Description.
 * @EndDescription.
 */
class DescuentosMultiplesPresenter @Inject constructor(
    private val model: DescuentosMultiplesMVP.Model
): ToksWebServicesConnection,
    DescuentosMultiplesMVP.Presenter {
    override fun setView(getView: DescuentosMultiplesMVP.View?) {
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

    override fun executeMultiplesDescuentos() {
        model.executeDescuentosMultiplesRequest()
    }
    companion object {
        const val TAG = "DescuentosMultiplesRequ"
        var view: DescuentosMultiplesMVP.View? = null
        var getSharedPreferences: SharedPreferences? = null
        var preferenceHelper: PreferenceHelper? = null
        var preferenceHelperLogs: PreferenceHelperLogs? = null
        var filesWeakReference: WeakReference<Files?>? = null
    }
}
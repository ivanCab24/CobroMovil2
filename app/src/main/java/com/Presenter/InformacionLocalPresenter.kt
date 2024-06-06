package com.Presenter

import android.content.SharedPreferences
import com.Interfaces.InformacionLocalMVP
import com.Utilities.Files
import com.Utilities.PreferenceHelper
import com.Utilities.PreferenceHelperLogs
import com.webservicestasks.ToksWebServicesConnection
import java.lang.ref.WeakReference
import javax.inject.Inject

/**
 * Type: Class.
 * Access: Public.
 * Name: InformacionLocalRequest.
 *
 * @Description.
 * @EndDescription.
 */
class InformacionLocalPresenter @Inject constructor(
    private val model: InformacionLocalMVP.Model
): ToksWebServicesConnection, InformacionLocalMVP.Presenter {
    override fun setView(getView: InformacionLocalMVP.View?) {
        view = getView
    }

    override fun setPreferences(
        sharedPreferences: SharedPreferences?,
        preferenceHelper: PreferenceHelper?
    ) {
        Companion.preferenceHelper = preferenceHelper
    }

    override fun setLogsInfo(preferenceHelperLogs: PreferenceHelperLogs?, files: Files?) {
        Companion.preferenceHelperLogs = preferenceHelperLogs
        filesWeakReference = WeakReference(files)
    }

    override fun executeInfoLocal() {
        model.executeInformacionLocalRequest()
    }

    companion object {
        var view: InformacionLocalMVP.View? = null
        var preferenceHelper: PreferenceHelper? = null
        var preferenceHelperLogs: PreferenceHelperLogs? = null
        var filesWeakReference: WeakReference<Files?>? = null
    }
}
package com.Presenter

import android.app.Activity
import android.content.SharedPreferences
import com.Interfaces.PinpadTaskMVP
import com.Utilities.Files
import com.Utilities.PreferenceHelper
import com.Utilities.PreferenceHelperLogs
import com.Verifone.BtManager
import java.lang.ref.WeakReference
import javax.inject.Inject

/**
 * Type: Class.
 * Access: Public.
 * Name: PinpadTaskConnection.
 *
 * @Description.
 * @EndDescription.
 */
class PinpadTaskPresenter @Inject constructor(
    private val model: PinpadTaskMVP.Model
) : PinpadTaskMVP.Presenter {
    private val btManager: BtManager? = null
    override fun setView(getView: PinpadTaskMVP.View?) {
        view = getView
    }

    override fun setActivity(activity: Activity?) {
        activityWeakReference = WeakReference(activity)
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

    override fun executeTaskConnection() {
        model.executeTaskPinpadConection(btManager)
    }

    companion object {
        var view: PinpadTaskMVP.View? = null
        var getSharedPreferences: SharedPreferences? = null
        var preferenceHelper: PreferenceHelper? = null
        var preferenceHelperLogs: PreferenceHelperLogs? = null
        var filesWeakReference: WeakReference<Files?>? = null
        var activityWeakReference: WeakReference<Activity?>? = null
    }
}
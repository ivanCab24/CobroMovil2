package com.Presenter

import android.content.SharedPreferences
import com.DataModel.VtolCancelResponse
import com.Interfaces.C54CancelacionTaskMVP
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
 * Name: C54CancelacionTask.
 *
 * @Description.
 * @EndDescription.
 */
class C54CancelacionTaskPresenter @Inject constructor(
    private val model: C54CancelacionTaskMVP.Model
): VerifonePinpadInterface, C54CancelacionTaskMVP.Presenter {
    override fun setView(getView: C54CancelacionTaskMVP.View?) {
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

    override fun executeC54Cancelacion(
        vtolCancelResponse: VtolCancelResponse?,
        response: EMVResponse?
    ) {
        model.executeSendC54Cancelacion(vtolCancelResponse, response)
    }

    companion object {
        var view: C54CancelacionTaskMVP.View? = null
        var mensajeTexto = ""
        var errorOutput = ""
        var getSharedPreferences: SharedPreferences? = null
        var preferenceHelper: PreferenceHelper? = null
        var preferenceHelperLogs: PreferenceHelperLogs? = null
        var filesWeakReference: WeakReference<Files?>? = null
    }
}
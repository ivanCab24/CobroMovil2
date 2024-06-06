package com.Presenter

import android.content.SharedPreferences
import com.DataModel.VtolCancelResponse
import com.Interfaces.CancelacionTercerMensajeMVP
import com.Utilities.Files
import com.Utilities.PreferenceHelper
import com.Utilities.PreferenceHelperLogs
import com.webservicestasks.ToksWebServicesConnection
import java.lang.ref.WeakReference
import javax.inject.Inject

/**
 * Type: Class.
 * Access: Public.
 * Name: CancelacionTercerMensajeRequest.
 *
 * @Description.
 * @EndDescription.
 */
class CancelacionTercerMensajePresenter @Inject constructor(
    private val model: CancelacionTercerMensajeMVP.Model
): ToksWebServicesConnection,
    CancelacionTercerMensajeMVP.Presenter {
    override fun setView(getView: CancelacionTercerMensajeMVP.View?) {
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

    override fun executeCancelacionTercerMensaje(vtolCancelResponse: VtolCancelResponse?) {
        model.executeCancelacionTercerMensajeRequest(vtolCancelResponse)
    }

    companion object {
        const val TAG = "CancelacionTercerMensaj"
        var view: CancelacionTercerMensajeMVP.View? = null
        var getSharedPreferences: SharedPreferences? = null
        var preferenceHelper: PreferenceHelper? = null
        var preferenceHelperLogs: PreferenceHelperLogs? = null
        var filesWeakReference: WeakReference<Files?>? = null
    }
}
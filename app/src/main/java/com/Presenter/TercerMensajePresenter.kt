package com.Presenter

import android.content.SharedPreferences
import com.DataModel.SaleVtol
import com.Interfaces.TercerMensajeMVP
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
 * Name: TercerMensajeRequest.
 *
 * @Description.
 * @EndDescription.
 */
class TercerMensajePresenter @Inject constructor(
    private val model: TercerMensajeMVP.Model
): ToksWebServicesConnection, TercerMensajeMVP.Presenter {
    override fun setView(getView: TercerMensajeMVP.View?) {
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

    override fun executeTercerMensaje(
        accion: String?,
        response: EMVResponse?,
        saleVtol: SaleVtol?,
        tag9f27: String?,
        isTransaccionExitosa: Boolean
    ) {
        getTaf9f27 = tag9f27
        transaccionExitosa = isTransaccionExitosa
        model.executeTercerMensajeRequest(accion, response, saleVtol)
    }



    companion object {
        const val TAG = "TercerMensajeRequest"
        var view: TercerMensajeMVP.View? = null
        var getSharedPreferences: SharedPreferences? = null
        var preferenceHelper: PreferenceHelper? = null
        var preferenceHelperLogs: PreferenceHelperLogs? = null
        var filesWeakReference: WeakReference<Files?>? = null
        var getTaf9f27: String? = null
        var accion: String? = null
        var transaccionExitosa = false

    }
}
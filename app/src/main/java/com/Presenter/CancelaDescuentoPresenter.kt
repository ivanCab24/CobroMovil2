package com.Presenter

import android.content.SharedPreferences
import com.DataModel.DescuentosAplicados
import com.Interfaces.CancelaDescuentoMVP
import com.Utilities.Files
import com.Utilities.PreferenceHelper
import com.Utilities.PreferenceHelperLogs
import com.webservicestasks.ToksWebServicesConnection
import java.lang.ref.WeakReference
import javax.inject.Inject

/**
 * Type: Class.
 * Access: Public.
 * Name: CancelaDescuentoRequest.
 *
 * @Description.
 * @EndDescription.
 */
class CancelaDescuentoPresenter @Inject constructor(
    private val model: CancelaDescuentoMVP.Model
): ToksWebServicesConnection, CancelaDescuentoMVP.Presenter {
    override fun setView(getView: CancelaDescuentoMVP.View?) {
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

    override fun executeCancelaDescuento(descuentosAplicados: DescuentosAplicados?) {
        model.executeCancelaDescuentoRequest(descuentosAplicados)
    }

    companion object {
        const val TAG = "CancelaDescuentoRequest"
        var view: CancelaDescuentoMVP.View? = null
        var getSharedPreferences: SharedPreferences? = null
        var preferenceHelper: PreferenceHelper? = null
        var preferenceHelperLogs: PreferenceHelperLogs? = null
        var filesWeakReference: WeakReference<Files?>? = null
    }
}
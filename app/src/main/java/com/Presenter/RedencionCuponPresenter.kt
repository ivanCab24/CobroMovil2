package com.Presenter

import android.content.SharedPreferences
import com.Interfaces.RedencionCuponMVP
import com.Utilities.Files
import com.Utilities.PreferenceHelper
import com.Utilities.PreferenceHelperLogs
import com.webservicestasks.ToksWebServicesConnection
import java.lang.ref.WeakReference
import java.text.DecimalFormat
import javax.inject.Inject

/**
 * Type: Class.
 * Access: Public.
 * Name: RedencionCuponRequest.
 *
 * @Description.
 * @EndDescription.
 */
class RedencionCuponPresenter @Inject constructor(
    private val model: RedencionCuponMVP.Model
) : ToksWebServicesConnection, RedencionCuponMVP.Presenter {
    override fun setView(getView: RedencionCuponMVP.View?) {
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

    override fun executeRedimeCupon(nip: String?) {
        cuponPorcentaje = "10"
        model.executeRedimeCuponRequest(nip)
    }

    companion object {
        const val TAG = "RedencionCuponRequest"
        var view: RedencionCuponMVP.View? = null
        var preferenceHelper: PreferenceHelper? = null
        var preferenceHelperLogs: PreferenceHelperLogs? = null
        var filesWeakReference: WeakReference<Files?>? = null
        var cuponPorcentaje: String? = null
        val formatter = DecimalFormat("#0.00")
    }
}
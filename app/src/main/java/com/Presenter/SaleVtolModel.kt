package com.Presenter

import android.content.SharedPreferences
import com.Interfaces.SaleVtolMVP
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
 * Name: SaleVtolRequest.
 *
 * @Description.
 * @EndDescription.
 */
class SaleVtolModel @Inject constructor(
    private val model: SaleVtolMVP.Model
): ToksWebServicesConnection, SaleVtolMVP.Presenter {
    override fun setView(getView: SaleVtolMVP.View?) {
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

    override fun executeSaleVtol(
        response: EMVResponse?,
        modoLectura: String?,
        transactionType: String?,
        year: String?,
        month: String?,
        day: String?
    ) {
        model.executeSaleVtolRequest(response, modoLectura, transactionType, year, month, day)
    }

    companion object {
        const val TAG = "SaleVtolRequest"
        var view: SaleVtolMVP.View? = null
        var getSharedPreferences: SharedPreferences? = null
        var preferenceHelper: PreferenceHelper? = null
        var preferenceHelperLogs: PreferenceHelperLogs? = null
        var filesWeakReference: WeakReference<Files?>? = null
    }
}
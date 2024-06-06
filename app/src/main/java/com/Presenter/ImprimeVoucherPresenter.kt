package com.Presenter

import android.content.SharedPreferences
import com.DataModel.SaleVtol
import com.DataModel.VtolCancelResponse
import com.Interfaces.ImprimeVoucherMVP
import com.Utilities.Files
import com.Utilities.PreferenceHelper
import com.Utilities.PreferenceHelperLogs
import com.webservicestasks.ToksWebServicesConnection
import java.lang.ref.WeakReference
import javax.inject.Inject

/**
 * Type: Class.
 * Access: Public.
 * Name: ImprimeVoucherRequest.
 *
 * @Description.
 * @EndDescription.
 */
class ImprimeVoucherPresenter @Inject constructor(
    private val model: ImprimeVoucherMVP.Model
) : ToksWebServicesConnection, ImprimeVoucherMVP.Presenter {
    override fun setView(getView: ImprimeVoucherMVP.View?) {
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

    override fun executeImprimeVoucher(
        saleVtol: SaleVtol?,
        vtolCancelResponse: VtolCancelResponse?
    ) {
        model.executeImprimeVoucherRequest(saleVtol, vtolCancelResponse, "", "")
    }

    override fun executeReimprimeVoucher(folio: String?, campo32: String?) {
        model.executeImprimeVoucherRequest(null, null, folio, campo32)
    }

    companion object {
        const val TAG = "ImprimeVoucherRequest"
        var view: ImprimeVoucherMVP.View? = null
        var getSharedPreferences: SharedPreferences? = null
        var preferenceHelper: PreferenceHelper? = null
        var preferenceHelperLogs: PreferenceHelperLogs? = null
        var filesWeakReference: WeakReference<Files?>? = null
    }
}
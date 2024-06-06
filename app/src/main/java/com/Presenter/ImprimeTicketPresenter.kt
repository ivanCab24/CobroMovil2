package com.Presenter

import android.content.SharedPreferences
import com.Interfaces.ImprimeTicketMVP
import com.Utilities.Files
import com.Utilities.PreferenceHelper
import com.Utilities.PreferenceHelperLogs
import com.webservicestasks.ToksWebServicesConnection
import java.lang.ref.WeakReference
import javax.inject.Inject

/**
 * Type: Class.
 * Access: Public.
 * Name: ImprimeTicketRequest.
 *
 * @Description.
 * @EndDescription.
 */
class ImprimeTicketPresenter @Inject constructor(
    private val model: ImprimeTicketMVP.Model
) : ToksWebServicesConnection, ImprimeTicketMVP.Presenter {
    override fun setView(getView: ImprimeTicketMVP.View?) {
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

    override fun executeReImprimeTicket(folio: String?) {
        model.executeImprimeTicketRequest(folio)
    }

    override fun executeImprimeTicket() {
        model.executeImprimeTicketRequest("")
    }

    companion object {
        const val TAG = "ImprimeTicketRequest"
        var view: ImprimeTicketMVP.View? = null
        var getSharedPreferences: SharedPreferences? = null
        var preferenceHelper: PreferenceHelper? = null
        var preferenceHelperLogs: PreferenceHelperLogs? = null
        var filesWeakReference: WeakReference<Files?>? = null
    }
}
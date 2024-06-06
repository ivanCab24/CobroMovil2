package com.Presenter

import android.content.Context
import android.content.SharedPreferences
import com.Controller.BD.DAO.TicketsNoRegistradosDAO
import com.DataModel.MiembroAuxiliar
import com.Interfaces.InsertaTicketMVP
import com.Utilities.Files
import com.Utilities.PreferenceHelper
import com.Utilities.PreferenceHelperLogs
import com.webservicestasks.ToksWebServicesConnection
import java.lang.ref.WeakReference
import javax.inject.Inject

/**
 * Type: Class.
 * Access: Public.
 * Name: InsertaTicketRequest.
 *
 * @Description.
 * @EndDescription.
 */
class InsertaTicketPresenter @Inject constructor(
    private val model: InsertaTicketMVP.Model
): InsertaTicketMVP.Presenter, ToksWebServicesConnection {
    override fun setView(getView: InsertaTicketMVP.View?) {
        view = getView
    }

    override fun setContext(context: Context?) {
        contextWeakReference = WeakReference(context)
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

    override fun setInsertaTicketsDao(ticketsNoRegistradosDAO: TicketsNoRegistradosDAO?) {
        ticketsNoRegistradosDAOWeakReference = WeakReference(ticketsNoRegistradosDAO)
    }

    override fun executeInsertaTicket() {
        model.executeInsertaTicketRequet()
    }

    companion object {
        const val TAG = "InsertaTicketRequest"
        var view: InsertaTicketMVP.View? = null
        var getSharedPreferences: SharedPreferences? = null
        var preferenceHelper: PreferenceHelper? = null
        var preferenceHelperLogs: PreferenceHelperLogs? = null
        var filesWeakReference: WeakReference<Files?>? = null
        var ticketsNoRegistradosDAOWeakReference: WeakReference<TicketsNoRegistradosDAO?>? = null
        var miembroAuxiliar: MiembroAuxiliar? = null
        var contextWeakReference: WeakReference<Context?>? = null
    }
}
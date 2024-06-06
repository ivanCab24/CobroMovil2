package com.Presenter

import android.content.Context
import android.content.SharedPreferences
import androidx.fragment.app.FragmentManager
import com.Constants.ConstantsPreferences
import com.Controller.BD.DAO.TicketsNoRegistradosDAO
import com.DataModel.MiembroAuxiliar
import com.Interfaces.AcumularPuntosMVP
import com.Utilities.Files
import com.Utilities.PreferenceHelper
import com.Utilities.PreferenceHelperLogs
import com.squareup.moshi.Moshi
import com.webservicestasks.ToksWebServicesConnection
import java.lang.ref.WeakReference
import javax.inject.Inject

/**
 * Type: Class.
 * Access: Public.
 * Name: AcumularPuntosRequest.
 *
 * @Description.
 * @EndDescription.
 */
class AcumularPuntosPresenter @Inject constructor(private var model: AcumularPuntosMVP.Model): ToksWebServicesConnection, AcumularPuntosMVP.Presenter {
    private var fragmentManager: FragmentManager? = null
    lateinit private var preferenceHelper: PreferenceHelper
    lateinit private var preferenceHelperLogs: PreferenceHelperLogs
    override fun setView(getView: com.Interfaces.AcumularPuntosMVP.View?) {
        view = getView
        Companion.view = getView
    }

    override fun getMarca(): String {
        return this.preferenceHelper.getString(ConstantsPreferences.PREF_MARCA_SELECCIONADA, null)
    }

    override fun setContext(context: Context?) {
        contextWeakReference = WeakReference(context)
        Companion.contextWeakReference = WeakReference(context)
    }

    override fun setPreferences(
        sharedPreferences: SharedPreferences?,
        preferenceHelper: PreferenceHelper?
    ) {
        getSharedPreferences = sharedPreferences
        this.preferenceHelper = preferenceHelper!!
        Companion.preferenceHelper = preferenceHelper
    }

    override fun setLogsInfo(preferenceHelperLogs: PreferenceHelperLogs?, files: Files?) {
        this.preferenceHelperLogs = preferenceHelperLogs!!
        Companion.preferenceHelperLogs = preferenceHelperLogs
        filesWeakReference = WeakReference(files)
    }

    override fun setTicketsNoRegistradosDao(ticketsNoRegistradosDAO: TicketsNoRegistradosDAO?) {
        ticketsNoRegistradosDAOWeakReference = WeakReference(ticketsNoRegistradosDAO)
        ticketsNoRegistradosDAOWeakReference = ticketsNoRegistradosDAOWeakReference
    }

    override fun executeAcumulaPuntos() {
        model.executeAcumulaPuntosRequest()
    }


    companion object {
        const val TAG = "AcumularPuntosRequest"
        var view: AcumularPuntosMVP.View? = null
        var getSharedPreferences: SharedPreferences? = null
        var preferenceHelper: PreferenceHelper? = null
        var preferenceHelperLogs: PreferenceHelperLogs? = null
        var filesWeakReference: WeakReference<Files?>? = null
        var miembroAuxiliar: MiembroAuxiliar? = null
        var contextWeakReference: WeakReference<Context?>? = null
        var ticketsNoRegistradosDAOWeakReference: WeakReference<TicketsNoRegistradosDAO?>? = null
        val moshi = Moshi.Builder().build()
    }
}
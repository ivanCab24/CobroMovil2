package com.Presenter

import android.app.Activity
import android.content.SharedPreferences
import com.Controller.BD.DAO.CuentaCobradaDAO
import com.Interfaces.getCuentaCobradaMVP
import com.Utilities.Files
import com.Utilities.PreferenceHelper
import com.Utilities.PreferenceHelperLogs
import java.lang.ref.WeakReference
import javax.inject.Inject

/**
 * Type: Class.
 * Access: Public.
 * Name: getCuentaCobradaTask.
 *
 * @Description.
 * @EndDescription.
 */
class GetCuentaCobradaPresenter @Inject constructor(
    private val model: getCuentaCobradaMVP.Model
) : getCuentaCobradaMVP.Presenter {
    override fun setView(getView: getCuentaCobradaMVP.View?) {
        view = getView
    }

    override fun setActivity(activity: Activity?) {
        activityWeakReference = WeakReference(activity)
    }

    override fun setPreferences(
        sharedPreferences: SharedPreferences?,
        preferenceHelper: PreferenceHelper?
    ) {
        getSharedPreferences = sharedPreferences
        Companion.preferenceHelper = preferenceHelper
    }

    override fun logsInfo(preferenceHelperLogs: PreferenceHelperLogs?, files: Files?) {
        var preferenceHelperLogs = preferenceHelperLogs
        Companion.preferenceHelperLogs = preferenceHelperLogs
        filesWeakReference = WeakReference(files)
    }

    override fun setCuentaCobradaDao(cuentaCobradaDAO: CuentaCobradaDAO?) {
        cuentaCobradaDAOWeakReference = WeakReference(cuentaCobradaDAO)
    }

    override fun executeGetAllCuentasCobradasTask() {
        model.getAllCuentasCobradas()
    }

    override fun executeGetCuentasCobradasTask(estafeta: String?) {
        model.getCuentasCobradas(estafeta)
    }

    override fun executeGetEstafetasTask() {
        model.getEstafetas()
    }

    override fun executeCountRecordsTask() {
        model.countRecords()
    }

    override fun executeDeleteRecords(month: Int) {
        model.deleteRecords(month)
    }

    companion object {
        const val TAG = "getCuentaCobradaTask"
        var view: getCuentaCobradaMVP.View? = null
        var activityWeakReference: WeakReference<Activity?>? = null
        var getSharedPreferences: SharedPreferences? = null
        var preferenceHelper: PreferenceHelper? = null
        var preferenceHelperLogs: PreferenceHelperLogs? = null
        var filesWeakReference: WeakReference<Files?>? = null
        var cuentaCobradaDAOWeakReference: WeakReference<CuentaCobradaDAO?>? = null
    }
}
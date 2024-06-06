package com.Presenter

import android.app.Activity
import android.content.SharedPreferences
import com.Controller.BD.DAO.CuentaCobradaDAO
import com.DataModel.SaleVtol
import com.Interfaces.InsertaCuentaCobradaMVP
import com.Utilities.Files
import com.Utilities.PreferenceHelper
import com.Utilities.PreferenceHelperLogs
import com.Verifone.EMVResponse
import java.lang.ref.WeakReference
import javax.inject.Inject

/**
 * Type: Class.
 * Access: Public.
 * Name: InsertCuentaCobradaTask.
 *
 * @Description.
 * @EndDescription.
 */
class InsertCuentaCobradaPresenter  @Inject constructor(
    private val model: InsertaCuentaCobradaMVP.Model
): InsertaCuentaCobradaMVP.Presenter {
    override fun setView(getView: InsertaCuentaCobradaMVP.View?) {
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

    override fun setLogsInfo(preferenceHelperLogs: PreferenceHelperLogs?, files: Files?) {
        Companion.preferenceHelperLogs = preferenceHelperLogs
        filesWeakReference = WeakReference(files)
    }

    override fun setCuentaCobradaDao(cuentaCobradaDAO: CuentaCobradaDAO?) {
        cuentaCobradaDAOWeakReference = WeakReference(cuentaCobradaDAO)
    }

    override fun executeinsertaCuentaCobradaTask(saleVtol: SaleVtol?, emvResponse: EMVResponse?) {
        model.executeCuentaCobradaTask(saleVtol, emvResponse)
    }

    companion object {
        const val TAG = "InsertCuentaCobradaTask"
        var view: InsertaCuentaCobradaMVP.View? = null
        var activityWeakReference: WeakReference<Activity?>? = null
        var getSharedPreferences: SharedPreferences? = null
        var preferenceHelper: PreferenceHelper? = null
        var preferenceHelperLogs: PreferenceHelperLogs? = null
        var filesWeakReference: WeakReference<Files?>? = null
        var cuentaCobradaDAOWeakReference: WeakReference<CuentaCobradaDAO?>? = null
    }
}
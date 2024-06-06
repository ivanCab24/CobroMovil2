/*
 * *
 *  * Created by Gerardo Ruiz on 12/17/20 5:15 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 12/17/20 5:15 PM
 *
 */
package com.Presenter

import android.app.Activity
import android.content.SharedPreferences
import com.Controller.BD.DAO.CuentaCerradaDAO
import com.Interfaces.InsertaCuentaCerradaMVP
import com.Utilities.Files
import com.Utilities.PreferenceHelper
import com.Utilities.PreferenceHelperLogs
import java.lang.ref.WeakReference
import javax.inject.Inject

/**
 * Type: Class.
 * Access: Public.
 * Name: InsertCuentaCerradaTask.
 *
 * @Description.
 * @EndDescription.
 */
class InsertCuentaCerradaPresenter  @Inject constructor(
    private val model: InsertaCuentaCerradaMVP.Model
): InsertaCuentaCerradaMVP.Presenter {
    override fun setView(getView: InsertaCuentaCerradaMVP.View?) {
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

    override fun setCuentaCerradaDao(cuentaCerradaDAO: CuentaCerradaDAO?) {
        cuentaCerradaDAOWeakReference = WeakReference(cuentaCerradaDAO)
    }

    override fun executeinsertaCuentaCerradaTask() {
        model.executeInsertaCuentaCerrada()
    }

    companion object {
        const val TAG = "InsertCuentaCobradaTask"
        var view: InsertaCuentaCerradaMVP.View? = null
        var activityWeakReference: WeakReference<Activity?>? = null
        var getSharedPreferences: SharedPreferences? = null
        var preferenceHelper: PreferenceHelper? = null
        var preferenceHelperLogs: PreferenceHelperLogs? = null
        var filesWeakReference: WeakReference<Files?>? = null
        var cuentaCerradaDAOWeakReference: WeakReference<CuentaCerradaDAO?>? = null
        var campo32 = ""
    }
}
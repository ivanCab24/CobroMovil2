/*
 * *
 *  * Created by Gerardo Ruiz on 12/18/20 1:24 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 12/18/20 1:24 PM
 *
 */
package com.Presenter

import android.app.Activity
import android.content.SharedPreferences
import com.Controller.BD.DAO.CuentaCerradaDAO
import com.Interfaces.getCuentaCerradaMVP
import com.Utilities.Files
import com.Utilities.PreferenceHelper
import com.Utilities.PreferenceHelperLogs
import java.lang.ref.WeakReference
import javax.inject.Inject

/**
 * Type: Class.
 * Access: Public.
 * Name: getCuentaCerradaTask.
 *
 * @Description.
 * @EndDescription.
 */
class GetCuentaCerradaPresenter @Inject constructor(
    private val model: getCuentaCerradaMVP.Model
) : getCuentaCerradaMVP.Presenter {
    override fun setView(getView: getCuentaCerradaMVP.View?) {
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

    override fun setCuentaCerradaDao(cuentaCerradaDAO: CuentaCerradaDAO?) {
        cuentaCerradaDAOWeakReference = WeakReference(cuentaCerradaDAO)
    }

    override fun executeGetCuentasCerradasTask() {
        model.executeGetCuentasCerradas()
    }

    override fun executeDeleteRecordsTask(month: Int) {
        model.executeDeleteRecords(month)
    }

    override fun executeCountRecordsTask() {
        model.executeCountRecords()
    }
    companion object {
        var view: getCuentaCerradaMVP.View? = null
        var activityWeakReference: WeakReference<Activity?>? = null
        var getSharedPreferences: SharedPreferences? = null
        var preferenceHelper: PreferenceHelper? = null
        var preferenceHelperLogs: PreferenceHelperLogs? = null
        var filesWeakReference: WeakReference<Files?>? = null
        var cuentaCerradaDAOWeakReference: WeakReference<CuentaCerradaDAO?>? = null
    }
}
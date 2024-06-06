/*
 * *
 *  * Created by Gerardo Ruiz on 12/18/20 1:24 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 12/18/20 1:24 PM
 *
 */
package com.Interfaces

import android.app.Activity
import android.content.SharedPreferences
import com.Controller.BD.DAO.CuentaCerradaDAO
import com.Controller.BD.Entity.CuentaCerrada
import com.Utilities.Files
import com.Utilities.PreferenceHelper
import com.Utilities.PreferenceHelperLogs

/**
 * Type: Interface.
 * Parent: getCuentaCerradaPresenter.java.
 * Name: getCuentaCerradaPresenter.
 */
interface getCuentaCerradaMVP {
    /**
     * Type: Interface.
     * Parent: getCuentaCerradaPresenter.
     * Name: View.
     */
    interface View {
        /**
         * Type: Method.
         * Parent: View.
         * Name: onReceiveCuentasCerradas.
         *
         * @param cuentaCerradaList @PsiType:List<CuentaCerrada>.
         * @Description.
         * @EndDescription.
        </CuentaCerrada> */
        fun onReceiveCuentasCerradas(cuentaCerradaList: List<CuentaCerrada?>?)

        /**
         * Type: Method.
         * Parent: View.
         * Name: onReceiveCountRecords.
         *
         * @param countRecords @PsiType:int.
         * @Description.
         * @EndDescription.
         */
        fun onReceiveCountRecords(countRecords: Int)
    }

    /**
     * Type: Interface.
     * Parent: getCuentaCerradaPresenter.
     * Name: Presenter.
     */
    interface Presenter {
        /**
         * Type: Method.
         * Parent: Presenter.
         * Name: setView.
         *
         * @Description.
         * @EndDescription.
         * @param: getView @PsiType:View.
         */
        fun setView(getView: View?)

        /**
         * Type: Method.
         * Parent: Presenter.
         * Name: setActivity.
         *
         * @Description.
         * @EndDescription.
         * @param: activity @PsiType:Activity.
         */
        fun setActivity(activity: Activity?)

        /**
         * Type: Method.
         * Parent: Presenter.
         * Name: setPreferences.
         *
         * @Description.
         * @EndDescription.
         * @param: sharedPreferences @PsiType:SharedPreferences.
         * @param: preferenceHelper @PsiType:PreferenceHelper.
         */
        fun setPreferences(
            sharedPreferences: SharedPreferences?,
            preferenceHelper: PreferenceHelper?
        )

        /**
         * Type: Method.
         * Parent: Presenter.
         * Name: logsInfo.
         *
         * @param preferenceHelperLogs @PsiType:PreferenceHelperLogs.
         * @param files                @PsiType:Files.
         * @Description.
         * @EndDescription.
         */
        fun logsInfo(preferenceHelperLogs: PreferenceHelperLogs?, files: Files?)

        /**
         * Type: Method.
         * Parent: Presenter.
         * Name: setCuentaCerradaDao.
         *
         * @Description.
         * @EndDescription.
         * @param: cuentaCerradaDAO @PsiType:CuentaCerradaDAO.
         */
        fun setCuentaCerradaDao(cuentaCerradaDAO: CuentaCerradaDAO?)

        /**
         * Type: Method.
         * Parent: Presenter.
         * Name: executeGetCuentasCerradasTask.
         *
         * @Description.
         * @EndDescription.
         */
        fun executeGetCuentasCerradasTask()

        /**
         * Type: Method.
         * Parent: Presenter.
         * Name: executeDeleteRecordsTask.
         *
         * @param month @PsiType:int.
         * @Description.
         * @EndDescription.
         */
        fun executeDeleteRecordsTask(month: Int)

        /**
         * Type: Method.
         * Parent: Presenter.
         * Name: executeCountRecordsTask.
         *
         * @Description.
         * @EndDescription.
         */
        fun executeCountRecordsTask()
    }

    interface Model{
        /**
         * Type: Method.
         * Parent: Presenter.
         * Name: executeGetCuentasCerradasTask.
         *
         * @Description.
         * @EndDescription.
         */
        fun executeGetCuentasCerradas()

        /**
         * Type: Method.
         * Parent: Presenter.
         * Name: executeDeleteRecordsTask.
         *
         * @param month @PsiType:int.
         * @Description.
         * @EndDescription.
         */
        fun executeDeleteRecords(month: Int)

        /**
         * Type: Method.
         * Parent: Presenter.
         * Name: executeCountRecordsTask.
         *
         * @Description.
         * @EndDescription.
         */
        fun executeCountRecords()
    }
}
/*
 * *
 *  * Created by Gerardo Ruiz on 12/17/20 5:17 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 12/17/20 5:17 PM
 *
 */
package com.Interfaces

import android.app.Activity
import android.content.SharedPreferences
import com.Controller.BD.DAO.CuentaCerradaDAO
import com.Utilities.Files
import com.Utilities.PreferenceHelper
import com.Utilities.PreferenceHelperLogs

/**
 * Type: Interface.
 * Parent: CuentaCerradaPresenter.java.
 * Name: CuentaCerradaPresenter.
 */
interface InsertaCuentaCerradaMVP {
    /**
     * Type: Interface.
     * Parent: CuentaCerradaPresenter.
     * Name: View.
     */
    interface View

    /**
     * Type: Interface.
     * Parent: CuentaCerradaPresenter.
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
         * Name: setLogsInfo.
         *
         * @Description.
         * @EndDescription.
         * @param: preferenceHelperLogs @PsiType:PreferenceHelperLogs.
         * @param: files @PsiType:Files.
         */
        fun setLogsInfo(preferenceHelperLogs: PreferenceHelperLogs?, files: Files?)

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
         * Name: executeinsertaCuentaCerradaTask.
         *
         * @Description.
         * @EndDescription.
         */
        fun executeinsertaCuentaCerradaTask()
    }

    interface Model{
        fun executeInsertaCuentaCerrada()
    }
}
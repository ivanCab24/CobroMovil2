/*
 * *
 *  * Created by Gerardo Ruiz on 11/26/20 10:55 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 11/26/20 10:55 PM
 *
 */
package com.Interfaces

import android.content.SharedPreferences
import com.Utilities.Files
import com.Utilities.PreferenceHelper
import com.Utilities.PreferenceHelperLogs

/**
 * Type: Interface.
 * Parent: ConsultaPagoMercadoPagoPresenter.java.
 * Name: ConsultaPagoMercadoPagoPresenter.
 */
interface ConsultaPagoMercadoPagoMVP {
    /**
     * Type: Interface.
     * Parent: ConsultaPagoMercadoPagoPresenter.
     * Name: View.
     */
    interface View {
        /**
         * Type: Method.
         * Parent: View.
         * Name: onExceptionConsultaPagoMercadoPagoResult.
         *
         * @param onExceptionResult @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        fun onExceptionConsultaPagoMercadoPagoResult(onExceptionResult: String?)

        /**
         * Type: Method.
         * Parent: View.
         * Name: onFailConsultaPagoMercadoPagoResult.
         *
         * @param onFailResult @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        fun onFailConsultaPagoMercadoPagoResult(onFailResult: String?)

        /**
         * Type: Method.
         * Parent: View.
         * Name: onSuccessConsultaPagoMercadoPagoResult.
         *
         * @param onSuccessResult @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        fun onSuccessConsultaPagoMercadoPagoResult(onSuccessResult: String?)
    }

    /**
     * Type: Interface.
     * Parent: ConsultaPagoMercadoPagoPresenter.
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
         * Name: setPreferences.
         *
         * @Description.
         * @EndDescription.
         * @param: sharedPreferences @PsiType:SharedPreferences.
         * @param: preferenceHelper @PsiType:PreferenceHelper.
         */

        fun doMercadoPagoRequest()

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
         * Name: setTicketsNoRegistradosDao.
         *
         * @Description.
         * @EndDescription.
         * @param: ticketsNoRegistradosDAO @PsiType:TicketsNoRegistradosDAO.
         */

    }

    interface Model {
        fun executeConsultaPagoRequest()
    }
}
/*
 * *
 *  * Created by Gerardo Ruiz on 11/26/20 12:29 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 11/26/20 12:29 PM
 *
 */
package com.Interfaces

import android.content.SharedPreferences
import com.Utilities.Files
import com.Utilities.PreferenceHelper
import com.Utilities.PreferenceHelperLogs

/**
 * Type: Interface.
 * Parent: LevantaPedidoPresenter.java.
 * Name: LevantaPedidoPresenter.
 */
interface LevantaPedidoMVP {
    /**
     * Type: Interface.
     * Parent: LevantaPedidoPresenter.
     * Name: View.
     */
    interface View {
        /**
         * Type: Method.
         * Parent: View.
         * Name: onLevantaPedidoExceptionResult.
         *
         * @param onExceptionResult @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        fun onLevantaPedidoExceptionResult(onExceptionResult: String?)

        /**
         * Type: Method.
         * Parent: View.
         * Name: onLevantaPedidoFailResult.
         *
         * @param onFailResult @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        fun onLevantaPedidoFailResult(onFailResult: String?)

        /**
         * Type: Method.
         * Parent: View.
         * Name: onSuccessLevantaPedidoResult.
         *
         * @param id       @PsiType:String.
         * @param clientID @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        fun onSuccessLevantaPedidoResult(id: String?, clientID: String?)
    }

    /**
     * Type: Interface.
     * Parent: LevantaPedidoPresenter.
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
         * Name: executeLevantaPedido.
         *
         * @Description.
         * @EndDescription.
         */
        fun executeLevantaPedido()
    }

    interface Model{
        fun executeLevantaPedidoRequest()
    }
}
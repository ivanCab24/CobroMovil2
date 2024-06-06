package com.Interfaces

import android.content.SharedPreferences
import com.Utilities.Files
import com.Utilities.PreferenceHelper
import com.Utilities.PreferenceHelperLogs

/**
 * Type: Interface.
 * Parent: AplicaDescuentoPuntosPresenter.java.
 * Name: AplicaDescuentoPuntosPresenter.
 */
interface AplicaDescuentoPuntosMVP {
    /**
     * Type: Interface.
     * Parent: AplicaDescuentoPuntosPresenter.
     * Name: View.
     */
    interface View {
        /**
         * Type: Method.
         * Parent: View.
         * Name: onExceptionAplicaDescuentoResult.
         *
         * @param onExceptionResult @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        fun onExceptionAplicaDescuentoResult(onExceptionResult: String?)

        /**
         * Type: Method.
         * Parent: View.
         * Name: onFailAplicaDescuentoResult.
         *
         * @param onFailResult @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        fun onFailAplicaDescuentoResult(onFailResult: String?)

        /**
         * Type: Method.
         * Parent: View.
         * Name: onSuccessAplicaDescuentoResult.
         *
         * @param onSuccessResult @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        fun onSuccessAplicaDescuentoResult(onSuccessResult: String?)
    }

    /**
     * Type: Interface.
     * Parent: AplicaDescuentoPuntosPresenter.
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
         * Name: executeAplicaDescuento.
         *
         * @param puntosARedimir @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        fun executeAplicaDescuento(puntosARedimir: String?)
    }

    interface Model {
        fun executeAplicaDescuentoRequest(puntosARedimir: String?)

        fun aplicaDescunetoPuntosResult(jsonResult: String)
    }
}
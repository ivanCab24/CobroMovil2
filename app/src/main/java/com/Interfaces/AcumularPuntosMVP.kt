package com.Interfaces

import android.content.Context
import android.content.SharedPreferences
import com.Controller.BD.DAO.TicketsNoRegistradosDAO
import com.Utilities.Files
import com.Utilities.PreferenceHelper
import com.Utilities.PreferenceHelperLogs

/**
 * Type: Interface.
 * Parent: AcumularPuntosPresenter.java.
 * Name: AcumularPuntosPresenter.
 */
interface AcumularPuntosMVP {
    /**
     * Type: Interface.
     * Parent: AcumularPuntosPresenter.
     * Name: View.
     */
    interface View {
        /**
         * Type: Method.
         * Parent: View.
         * Name: onExceptionAcumularPuntosResult.
         *
         * @param onExceptionResult @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        fun onExceptionAcumularPuntosResult(onExceptionResult: String?)

        /**
         * Type: Method.
         * Parent: View.
         * Name: onFailAcumularPuntosResult.
         *
         * @param onFailResult @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        fun onFailAcumularPuntosResult(onFailResult: String?)

        /**
         * Type: Method.
         * Parent: View.
         * Name: onSuccessAcumularPuntosResult.
         *
         * @param onSuccessResult @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        fun onSuccessAcumularPuntosResult(onSuccessResult: String?)

        fun getCurrentMarca(value: String)
    }

    /**
     * Type: Interface.
     * Parent: AcumularPuntosPresenter.
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

        fun getMarca(): String

        /**
         * Type: Method.
         * Parent: Presenter.
         * Name: setContext.
         *
         * @Description.
         * @EndDescription.
         * @param: context @PsiType:Context.
         */
        fun setContext(context: Context?)

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
        fun setTicketsNoRegistradosDao(ticketsNoRegistradosDAO: TicketsNoRegistradosDAO?)

        /**
         * Type: Method.
         * Parent: Presenter.
         * Name: executeAcumulaPuntos.
         *
         * @Description.
         * @EndDescription.
         */
        fun executeAcumulaPuntos()
    }

    interface Model{
        fun executeAcumulaPuntosRequest()
    }
}
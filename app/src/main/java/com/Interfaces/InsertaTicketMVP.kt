package com.Interfaces

import android.content.Context
import android.content.SharedPreferences
import com.Controller.BD.DAO.TicketsNoRegistradosDAO
import com.Utilities.Files
import com.Utilities.PreferenceHelper
import com.Utilities.PreferenceHelperLogs

/**
 * Type: Interface.
 * Parent: InsertaTicketPresenter.java.
 * Name: InsertaTicketPresenter.
 */
interface InsertaTicketMVP {
    /**
     * Type: Interface.
     * Parent: InsertaTicketPresenter.
     * Name: View.
     */
    interface View {
        /**
         * Type: Method.
         * Parent: View.
         * Name: onExceptionInsertaTicketResult.
         *
         * @param onExceptionResult @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        fun onExceptionInsertaTicketResult(onExceptionResult: String?)

        /**
         * Type: Method.
         * Parent: View.
         * Name: onFailInsertaTicketResult.
         *
         * @param onFailResult @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        fun onFailInsertaTicketResult(onFailResult: String?)

        /**
         * Type: Method.
         * Parent: View.
         * Name: onSuccessInsertaTicketResult.
         *
         * @param onSuccessResult @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        fun onSuccessInsertaTicketResult(onSuccessResult: String?)
    }

    /**
     * Type: Interface.
     * Parent: InsertaTicketPresenter.
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
         * Name: setInsertaTicketsDao.
         *
         * @Description.
         * @EndDescription.
         * @param: ticketsNoRegistradosDAO @PsiType:TicketsNoRegistradosDAO.
         */
        fun setInsertaTicketsDao(ticketsNoRegistradosDAO: TicketsNoRegistradosDAO?)

        /**
         * Type: Method.
         * Parent: Presenter.
         * Name: executeInsertaTicket.
         *
         * @Description.
         * @EndDescription.
         */
        fun executeInsertaTicket()
    }

    interface Model{
        fun executeInsertaTicketRequet()
    }
}
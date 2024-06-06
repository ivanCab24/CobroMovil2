package com.Interfaces

import android.content.SharedPreferences
import com.Utilities.Files
import com.Utilities.PreferenceHelper
import com.Utilities.PreferenceHelperLogs

/**
 * Type: Interface.
 * Parent: ImprimeTicketPresenter.java.
 * Name: ImprimeTicketPresenter.
 */
interface ImprimeTicketMVP {
    /**
     * Type: Interface.
     * Parent: ImprimeTicketPresenter.
     * Name: View.
     */
    interface View {
        /**
         * Type: Method.
         * Parent: View.
         * Name: onExceptionImprimeTicketResult.
         *
         * @param onExceptionResult @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        fun onExceptionImprimeTicketResult(onExceptionResult: String?)

        /**
         * Type: Method.
         * Parent: View.
         * Name: onFailExceptionImprimeTicketResult.
         *
         * @param onFailResult @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        fun onFailExceptionImprimeTicketResult(onFailResult: String?)

        /**
         * Type: Method.
         * Parent: View.
         * Name: onSuccessImprimeTicketResult.
         *
         * @param onSuccessResult @PsiType:ArrayList<String>.
         * @Description.
         * @EndDescription.
        </String> */
        fun onSuccessImprimeTicketResult(onSuccessResult: ArrayList<String?>?)
    }

    /**
     * Type: Interface.
     * Parent: ImprimeTicketPresenter.
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
         * Name: executeReImprimeTicket.
         *
         * @param folio @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        fun executeReImprimeTicket(folio: String?)

        /**
         * Type: Method.
         * Parent: Presenter.
         * Name: executeImprimeTicket.
         *
         * @Description.
         * @EndDescription.
         */
        fun executeImprimeTicket()
    }

    interface Model {
        fun executeImprimeTicketRequest(folio: String?)
    }
}
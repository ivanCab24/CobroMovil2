package com.Interfaces

import android.content.SharedPreferences
import com.DataModel.FormasDePago
import com.Utilities.Files
import com.Utilities.PreferenceHelper
import com.Utilities.PreferenceHelperLogs

/**
 * Type: Interface.
 * Parent: FormasDePagoPresenter.java.
 * Name: FormasDePagoPresenter.
 */
interface FormasDePagoMPV {
    /**
     * Type: Interface.
     * Parent: FormasDePagoPresenter.
     * Name: View.
     */
    interface View {
        /**
         * Type: Method.
         * Parent: View.
         * Name: onExceptionFormasDePagoResult.
         *
         * @param onExceptionResult @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        fun onExceptionFormasDePagoResult(onExceptionResult: String?)

        /**
         * Type: Method.
         * Parent: View.
         * Name: onFailFormasDePagpResult.
         *
         * @param onFailResult @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        fun onFailFormasDePagpResult(onFailResult: String?)

        /**
         * Type: Method.
         * Parent: View.
         * Name: onSuccessFormasDePagoResult.
         *
         * @param formasDePagoArrayList @PsiType:ArrayList<FormasDePago>.
         * @Description.
         * @EndDescription.
        </FormasDePago> */
        fun onSuccessFormasDePagoResult(formasDePagoArrayList: ArrayList<FormasDePago?>?)
    }

    /**
     * Type: Interface.
     * Parent: FormasDePagoPresenter.
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
         * Name: executeFormasDePagoRequest.
         *
         * @Description.
         * @EndDescription.
         */
        fun executeFormasDePago()
    }

    interface Model {
        fun executeFormasPagoRequest()
    }
}
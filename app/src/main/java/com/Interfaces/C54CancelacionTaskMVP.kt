package com.Interfaces

import android.content.SharedPreferences
import com.DataModel.VtolCancelResponse
import com.Utilities.Files
import com.Utilities.PreferenceHelper
import com.Utilities.PreferenceHelperLogs
import com.Verifone.EMVResponse

/**
 * Type: Interface.
 * Parent: C54CancelacionTaskPresenter.java.
 * Name: C54CancelacionTaskPresenter.
 */
interface C54CancelacionTaskMVP {
    /**
     * Type: Interface.
     * Parent: C54CancelacionTaskPresenter.
     * Name: View.
     */
    interface View {
        /**
         * Type: Method.
         * Parent: View.
         * Name: onC54CancelacionExceptionResult.
         *
         * @param onException @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        fun onC54CancelacionExceptionResult(onException: String?)

        /**
         * Type: Method.
         * Parent: View.
         * Name: onC54CancelacionFailResult.
         *
         * @param onFailResult @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        fun onC54CancelacionFailResult(onFailResult: String?)

        /**
         * Type: Method.
         * Parent: View.
         * Name: onC54CancelacionSuccessResult.
         *
         * @param onSuccesResult @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        fun onC54CancelacionSuccessResult(onSuccesResult: String?)
    }

    /**
     * Type: Interface.
     * Parent: C54CancelacionTaskPresenter.
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
         * Name: executeC54Cancelacion.
         *
         * @param vtolCancelResponse @PsiType:VtolCancelResponse.
         * @param response           @PsiType:EMVResponse.
         * @Description.
         * @EndDescription.
         */
        fun executeC54Cancelacion(vtolCancelResponse: VtolCancelResponse?, response: EMVResponse?)
    }
    interface Model{
        fun executeSendC54Cancelacion(vtolCancelResponse: VtolCancelResponse?, response: EMVResponse?)
    }
}
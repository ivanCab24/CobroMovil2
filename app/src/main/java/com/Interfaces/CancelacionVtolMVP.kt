package com.Interfaces

import android.content.SharedPreferences
import com.DataModel.VtolCancelResponse
import com.Utilities.Files
import com.Utilities.PreferenceHelper
import com.Utilities.PreferenceHelperLogs
import com.Verifone.EMVResponse

/**
 * Type: Interface.
 * Parent: CancelacionVtolPresenter.java.
 * Name: CancelacionVtolPresenter.
 */
interface CancelacionVtolMVP {
    /**
     * Type: Interface.
     * Parent: CancelacionVtolPresenter.
     * Name: View.
     */
    interface View {
        /**
         * Type: Method.
         * Parent: View.
         * Name: onCancelacionVtolExceptionResult.
         *
         * @param onExceptionResult @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        fun onCancelacionVtolExceptionResult(onExceptionResult: String?)

        /**
         * Type: Method.
         * Parent: View.
         * Name: onCancelacionVtolFailResult.
         *
         * @param onFailResult @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        fun onCancelacionVtolFailResult(onFailResult: String?)

        /**
         * Type: Method.
         * Parent: View.
         * Name: onCancelacionVtolSuccessResult.
         *
         * @param vtolCancelResponse @PsiType:VtolCancelResponse.
         * @Description.
         * @EndDescription.
         */
        fun onCancelacionVtolSuccessResult(vtolCancelResponse: VtolCancelResponse?)
    }

    /**
     * Type: Interface.
     * Parent: CancelacionVtolPresenter.
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
         * Name: executeCancelacionVtol.
         *
         * @param response @PsiType:EMVResponse.
         * @param year     @PsiType:String.
         * @param month    @PsiType:String.
         * @param day      @PsiType:String.
         * @param ticket   @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        fun executeCancelacionVtol(
            response: EMVResponse?,
            year: String?,
            month: String?,
            day: String?,
            ticket: String?
        )
    }

    interface Model{
        fun executeCancelacionVtolRequest(response: EMVResponse?,
                                          year: String?,
                                          month: String?,
                                          day: String?,
                                          ticket: String?)
    }
}
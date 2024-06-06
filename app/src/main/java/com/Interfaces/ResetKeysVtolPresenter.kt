package com.Interfaces

import android.content.SharedPreferences
import com.DataModel.VtolResetKeys
import com.Utilities.Files
import com.Utilities.PreferenceHelper
import com.Utilities.PreferenceHelperLogs

/**
 * Type: Interface.
 * Parent: ResetKeysVtolPresenter.java.
 * Name: ResetKeysVtolPresenter.
 */
interface ResetKeysVtolPresenter {
    /**
     * Type: Interface.
     * Parent: ResetKeysVtolPresenter.
     * Name: View.
     */
    interface View {
        /**
         * Type: Method.
         * Parent: View.
         * Name: onExceptionResetKeysVtolResult.
         *
         * @param onExceptionResult @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        fun onExceptionResetKeysVtolResult(onExceptionResult: String?)

        /**
         * Type: Method.
         * Parent: View.
         * Name: onFailResetKeysVtolResult.
         *
         * @param onFailResult @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        fun onFailResetKeysVtolResult(onFailResult: String?)

        /**
         * Type: Method.
         * Parent: View.
         * Name: onSuccessResetKeysVtolResult.
         *
         * @param vtolResetKeys @PsiType:VtolResetKeys.
         * @Description.
         * @EndDescription.
         */
        fun onSuccessResetKeysVtolResult(vtolResetKeys: VtolResetKeys?)
    }

    /**
     * Type: Interface.
     * Parent: ResetKeysVtolPresenter.
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
         * Name: executeResetKeysVtol.
         *
         * @param chipTokens @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        fun executeResetKeysVtol(chipTokens: String?)
    }
}
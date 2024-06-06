package com.Interfaces

import android.content.SharedPreferences
import com.Utilities.Files
import com.Utilities.PreferenceHelper
import com.Utilities.PreferenceHelperLogs

/**
 * Type: Interface.
 * Parent: ChipTokensPresenter.java.
 * Name: ChipTokensPresenter.
 */
interface ChipTokensPresenter {
    /**
     * Type: Interface.
     * Parent: ChipTokensPresenter.
     * Name: View.
     */
    interface View {
        /**
         * Type: Method.
         * Parent: View.
         * Name: onFailChipTokensTask.
         *
         * @param onFailResult @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        fun onFailChipTokensTask(onFailResult: String?)

        /**
         * Type: Method.
         * Parent: View.
         * Name: onSuccessChipTokensTask.
         *
         * @param chipTokens @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        fun onSuccessChipTokensTask(chipTokens: String?)
    }

    /**
     * Type: Interface.
     * Parent: ChipTokensPresenter.
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
         * Name: executeChipTokensTask.
         *
         * @Description.
         * @EndDescription.
         */
        fun executeChipTokensTask()
    }
}
package com.Interfaces

import android.app.Activity
import android.content.SharedPreferences
import com.Utilities.Files
import com.Utilities.PreferenceHelper
import com.Utilities.PreferenceHelperLogs

/**
 * Type: Interface.
 * Parent: MFPGOPresenter.java.
 * Name: MFPGOPresenter.
 */
interface MFPGOPresenter {
    /**
     * Type: Interface.
     * Parent: MFPGOPresenter.
     * Name: View.
     */
    interface View {
        /**
         * Type: Method.
         * Parent: View.
         * Name: onExceptionMFPGOResult.
         *
         * @param onExceptionResult @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        fun onExceptionMFPGOResult(onExceptionResult: String?)

        /**
         * Type: Method.
         * Parent: View.
         * Name: onFailMFPGOResult.
         *
         * @param onFailResult @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        fun onFailMFPGOResult(onFailResult: String?)

        /**
         * Type: Method.
         * Parent: View.
         * Name: onSuccessMFPGOResult.
         *
         * @Description.
         * @EndDescription.
         */
        fun onSuccessMFPGOResult()
    }

    /**
     * Type: Interface.
     * Parent: MFPGOPresenter.
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
         * Name: setActivity.
         *
         * @Description.
         * @EndDescription.
         * @param: activity @PsiType:Activity.
         */
        fun setActivity(activity: Activity?)

        /**
         * Type: Method.
         * Parent: Presenter.
         * Name: ejecutarMFPGO.
         *
         * @Description.
         * @EndDescription.
         */
        fun ejecutarMFPGO()
    }
}
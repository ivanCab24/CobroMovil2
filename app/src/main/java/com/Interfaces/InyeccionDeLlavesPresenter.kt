package com.Interfaces

import android.content.SharedPreferences
import com.DataModel.VtolResetKeys
import com.Utilities.Files
import com.Utilities.PreferenceHelper
import com.Utilities.PreferenceHelperLogs

/**
 * Type: Interface.
 * Parent: InyeccionDeLlavesPresenter.java.
 * Name: InyeccionDeLlavesPresenter.
 */
interface InyeccionDeLlavesPresenter {
    /**
     * Type: Interface.
     * Parent: InyeccionDeLlavesPresenter.
     * Name: View.
     */
    interface View {
        /**
         * Type: Method.
         * Parent: View.
         * Name: onFailInyeccionDeLlavesResult.
         *
         * @param onFailResult @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        fun onFailInyeccionDeLlavesResult(onFailResult: String?)

        /**
         * Type: Method.
         * Parent: View.
         * Name: onSuccessInyeccionDeLlavesResult.
         *
         * @param onSuccessResult @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        fun onSuccessInyeccionDeLlavesResult(onSuccessResult: String?)
    }

    /**
     * Type: Interface.
     * Parent: InyeccionDeLlavesPresenter.
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
         * Name: executeInyeccionDeLlavesTask.
         *
         * @param vtolResetKeys @PsiType:VtolResetKeys.
         * @Description.
         * @EndDescription.
         */
        fun executeInyeccionDeLlavesTask(vtolResetKeys: VtolResetKeys?)
    }
}
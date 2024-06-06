package com.Interfaces

import android.content.SharedPreferences
import com.Utilities.Files
import com.Utilities.PreferenceHelper
import com.Utilities.PreferenceHelperLogs

/**
 * Type: Interface.
 * Parent: DescuentosMultiplesPresenter.java.
 * Name: DescuentosMultiplesPresenter.
 */
interface DescuentosMultiplesMVP {
    /**
     * Type: Interface.
     * Parent: DescuentosMultiplesPresenter.
     * Name: View.
     */
    interface View {
        /**
         * Type: Method.
         * Parent: View.
         * Name: onExceptionMultiplesDescuentosResult.
         *
         * @param onExceptionResult @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        fun onExceptionMultiplesDescuentosResult(onExceptionResult: String?)

        /**
         * Type: Method.
         * Parent: View.
         * Name: onFailMultiplesDescuentosResult.
         *
         * @param onFailResult @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        fun onFailMultiplesDescuentosResult(onFailResult: String?)

        /**
         * Type: Method.
         * Parent: View.
         * Name: onSuccessMultiplesDescuentosResult.
         *
         * @param onSuccessResult @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        fun onSuccessMultiplesDescuentosResult(onSuccessResult: String?)
    }

    /**
     * Type: Interface.
     * Parent: DescuentosMultiplesPresenter.
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
         * Name: executeMultiplesDescuentos.
         *
         * @Description.
         * @EndDescription.
         */
        fun executeMultiplesDescuentos()
    }

    interface Model{
        fun executeDescuentosMultiplesRequest()
    }
}
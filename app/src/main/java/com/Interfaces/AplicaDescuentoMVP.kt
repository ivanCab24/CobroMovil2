package com.Interfaces

import android.content.SharedPreferences
import com.Utilities.Files
import com.Utilities.PreferenceHelper
import com.Utilities.PreferenceHelperLogs
import com.View.Fragments.ContentFragment

/**
 * Type: Interface.
 * Parent: AplicaDescuentoPresenter.java.
 * Name: AplicaDescuentoPresenter.
 */
interface AplicaDescuentoMVP {
    /**
     * Type: Interface.
     * Parent: AplicaDescuentoPresenter.
     * Name: View.
     */
    interface View {
        /**
         * Type: Method.
         * Parent: View.
         * Name: onExceptionAplicaDescuentoResult.
         *
         * @param onExceptionResult @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        fun onExceptionAplicaDescuentoResult(onExceptionResult: String?)

        /**
         * Type: Method.
         * Parent: View.
         * Name: onFailAplicaDescuentoResult.
         *
         * @param onFailResult @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        fun onFailAplicaDescuentoResult(onFailResult: String?)

        /**
         * Type: Method.
         * Parent: View.
         * Name: onSuccess.
         *
         * @param onSuccessResult @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        fun onSuccess(onSuccessResult: String?)
    }

    /**
     * Type: Interface.
     * Parent: AplicaDescuentoPresenter.
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
         * Name: setContentFragment.
         *
         * @Description.
         * @EndDescription.
         * @param: contentFragment @PsiType:ContentFragment.
         */
        fun setContentFragment(contentFragment: ContentFragment?)

        /**
         * Type: Method.
         * Parent: Presenter.
         * Name: executeAplicaDescuentoRequest.
         *
         * @param referencia @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        fun executeAplicaDescuentoRequest(referencia: String?)
    }

    interface Model{
        fun executeAplicaDescuento(referencia: String?)
    }
}
package com.Interfaces

import android.content.SharedPreferences
import com.DataModel.DescuentosAplicados
import com.Utilities.Files
import com.Utilities.PreferenceHelper
import com.Utilities.PreferenceHelperLogs

/**
 * Type: Interface.
 * Parent: CancelaDescuentoPresenter.java.
 * Name: CancelaDescuentoPresenter.
 */
interface CancelaDescuentoMVP {
    /**
     * Type: Interface.
     * Parent: CancelaDescuentoPresenter.
     * Name: View.
     */
    interface View {
        /**
         * Type: Method.
         * Parent: View.
         * Name: onExceptionCancelaDescuentoResult.
         *
         * @param onExceptionResult @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        fun onExceptionCancelaDescuentoResult(onExceptionResult: String?)

        /**
         * Type: Method.
         * Parent: View.
         * Name: onFailCancelaDescuentoResult.
         *
         * @param onFailResult @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        fun onFailCancelaDescuentoResult(onFailResult: String?)

        /**
         * Type: Method.
         * Parent: View.
         * Name: onSuccessCancelaDescuentoResult.
         *
         * @param onSuccessResult @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        fun onSuccessCancelaDescuentoResult(onSuccessResult: String?)
    }

    /**
     * Type: Interface.
     * Parent: CancelaDescuentoPresenter.
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
         * Name: executeCancelaDescuento.
         *
         * @param descuentosAplicados @PsiType:DescuentosAplicados.
         * @Description.
         * @EndDescription.
         */
        fun executeCancelaDescuento(descuentosAplicados: DescuentosAplicados?)
    }

    interface Model{
        fun executeCancelaDescuentoRequest(descuentosAplicados: DescuentosAplicados?)
    }
}
package com.Interfaces

import android.content.SharedPreferences
import com.DataModel.SaleVtol
import com.Utilities.Files
import com.Utilities.PreferenceHelper
import com.Utilities.PreferenceHelperLogs
import com.Verifone.EMVResponse

/**
 * Type: Interface.
 * Parent: SaleVtolPresenter.java.
 * Name: SaleVtolPresenter.
 */
interface SaleVtolMVP {
    /**
     * Type: Interface.
     * Parent: SaleVtolPresenter.
     * Name: View.
     */
    interface View {
        /**
         * Type: Method.
         * Parent: View.
         * Name: onExceptionSaleVtolResult.
         *
         * @param onExceptionResult @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        fun onExceptionSaleVtolResult(onExceptionResult: String?)

        /**
         * Type: Method.
         * Parent: View.
         * Name: onFailSaleVtolResult.
         *
         * @param onFailResult @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        fun onFailSaleVtolResult(onFailResult: String?)

        /**
         * Type: Method.
         * Parent: View.
         * Name: onSuccessVtolResult.
         *
         * @param saleVtol @PsiType:SaleVtol.
         * @Description.
         * @EndDescription.
         */
        fun onSuccessVtolResult(saleVtol: SaleVtol?)
    }

    /**
     * Type: Interface.
     * Parent: SaleVtolPresenter.
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
         * Name: executeSaleVtol.
         *
         * @param response        @PsiType:EMVResponse.
         * @param modoLectura     @PsiType:String.
         * @param transactionType @PsiType:String.
         * @param year            @PsiType:String.
         * @param month           @PsiType:String.
         * @param day             @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        fun executeSaleVtol(
            response: EMVResponse?,
            modoLectura: String?,
            transactionType: String?,
            year: String?,
            month: String?,
            day: String?
        )
    }

    interface Model{
        fun executeSaleVtolRequest(response: EMVResponse?,
                                   modoLectura: String?,
                                   transactionType: String?,
                                   year: String?,
                                   month: String?,
                                   day: String?)
    }
}
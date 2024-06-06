package com.Interfaces

import android.content.SharedPreferences
import com.DataModel.SaleVtol
import com.DataModel.VtolCancelResponse
import com.Utilities.Files
import com.Utilities.PreferenceHelper
import com.Utilities.PreferenceHelperLogs

/**
 * Type: Interface.
 * Parent: ImprimeVoucherPresenter.java.
 * Name: ImprimeVoucherPresenter.
 */
interface ImprimeVoucherMVP {
    /**
     * Type: Interface.
     * Parent: ImprimeVoucherPresenter.
     * Name: View.
     */
    interface View {
        /**
         * Type: Method.
         * Parent: View.
         * Name: onExceptionImprimeVoucherResult.
         *
         * @param onExceptionResult @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        fun onExceptionImprimeVoucherResult(onExceptionResult: String?)

        /**
         * Type: Method.
         * Parent: View.
         * Name: onFailImprimeVoucherResult.
         *
         * @param onFailResult @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        fun onFailImprimeVoucherResult(onFailResult: String?)

        /**
         * Type: Method.
         * Parent: View.
         * Name: onSuccessImprimeVoucherResult.
         *
         * @param arrayListEmisor  @PsiType:ArrayList<String>.
         * @param arrayListCliente @PsiType:ArrayList<String>.
         * @Description.
         * @EndDescription.
        </String></String> */
        fun onSuccessImprimeVoucherResult(
            arrayListEmisor: ArrayList<String?>?,
            arrayListCliente: ArrayList<String?>?
        )
    }

    /**
     * Type: Interface.
     * Parent: ImprimeVoucherPresenter.
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
         * Name: executeImprimeVoucher.
         *
         * @param saleVtol           @PsiType:SaleVtol.
         * @param vtolCancelResponse @PsiType:VtolCancelResponse.
         * @Description.
         * @EndDescription.
         */
        fun executeImprimeVoucher(saleVtol: SaleVtol?, vtolCancelResponse: VtolCancelResponse?)

        /**
         * Type: Method.
         * Parent: Presenter.
         * Name: executeReimprimeVoucher.
         *
         * @param folio   @PsiType:String.
         * @param campo32 @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        fun executeReimprimeVoucher(folio: String?, campo32: String?)
    }

    interface Model {
        fun executeImprimeVoucherRequest(
            saleVtol: SaleVtol?,
            vtolCancelResponse: VtolCancelResponse?,
            folio: String?,
            campo32: String?
        )
    }
}
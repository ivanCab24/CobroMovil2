package com.Interfaces

import android.content.SharedPreferences
import com.DataModel.SaleVtol
import com.Utilities.Files
import com.Utilities.PreferenceHelper
import com.Utilities.PreferenceHelperLogs
import com.Verifone.BtManager

/**
 * Type: Interface.
 * Parent: C54TaskPresenter.java.
 * Name: C54TaskPresenter.
 */
interface C54TaskMPV {
    /**
     * Type: Interface.
     * Parent: C54TaskPresenter.
     * Name: View.
     */
    interface View {
        /**
         * Type: Method.
         * Parent: View.
         * Name: onExceptionC54Result.
         *
         * @param onExceptionResult @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        fun onExceptionC54Result(onExceptionResult: String?)

        /**
         * Type: Method.
         * Parent: View.
         * Name: onFailC54Result.
         *
         * @param onFailResult         @PsiType:String.
         * @param tag9f27              @PsiType:String.
         * @param isTransaccionExitosa @PsiType:boolean.
         * @Description.
         * @EndDescription.
         */
        fun onFailC54Result(onFailResult: String?, tag9f27: String?, isTransaccionExitosa: Boolean)

        /**
         * Type: Method.
         * Parent: View.
         * Name: onSuccessC54Result.
         *
         * @param isTransaccionExitosa @PsiType:boolean.
         * @param outpuError           @PsiType:String.
         * @param tag9f27              @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        fun onSuccessC54Result(isTransaccionExitosa: Boolean, outpuError: String?, tag9f27: String?)
    }

    /**
     * Type: Interface.
     * Parent: C54TaskPresenter.
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
         * Name: executeC54Task.
         *
         * @param saleVtol        @PsiType:SaleVtol.
         * @param btManager       @PsiType:BtManager.
         * @param transactionType @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        fun executeC54Task(saleVtol: SaleVtol?, btManager: BtManager?, transactionType: String?)
    }

    interface Model{
        fun executeC54(saleVtol: SaleVtol?,
                       btManager: BtManager?,
                       transactionType: String?)
    }
}
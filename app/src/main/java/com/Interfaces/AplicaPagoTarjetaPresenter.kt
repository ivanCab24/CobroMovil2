package com.Interfaces

import android.content.SharedPreferences
import com.DataModel.SaleVtol
import com.Utilities.Files
import com.Utilities.PreferenceHelper
import com.Utilities.PreferenceHelperLogs
import com.Verifone.EMVResponse

/**
 * Type: Interface.
 * Parent: AplicaPagoTarjetaPresenter.java.
 * Name: AplicaPagoTarjetaPresenter.
 */
interface AplicaPagoTarjetaPresenter {
    /**
     * Type: Interface.
     * Parent: AplicaPagoTarjetaPresenter.
     * Name: View.
     */
    interface View {
        /**
         * Type: Method.
         * Parent: View.
         * Name: onExceptionAplicaPagoResult.
         *
         * @param onExceptionResult @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        fun onExceptionAplicaPagoResult(onExceptionResult: String?)

        /**
         * Type: Method.
         * Parent: View.
         * Name: onFailAplicaPagoResult.
         *
         * @param onFailResult @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        fun onFailAplicaPagoResult(onFailResult: String?)

        /**
         * Type: Method.
         * Parent: View.
         * Name: onSuccessAplicaPagoResult.
         *
         * @Description.
         * @EndDescription.
         */
        fun onSuccessAplicaPagoResult()

        fun getCurrentMarca(value: String)
    }

    /**
     * Type: Interface.
     * Parent: AplicaPagoTarjetaPresenter.
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
         * Name: setVariables.
         *
         * @Description.
         * @EndDescription.
         * @param: saleVtol @PsiType:SaleVtol.
         * @param: response @PsiType:EMVResponse.
         * @param: idFPGO @PsiType:int.
         * @param: tipoFPGO @PsiType:int.
         */
        fun setVariables(saleVtol: SaleVtol?, response: EMVResponse?, idFPGO: Int, tipoFPGO: Int)

        /**
         * Type: Method.
         * Parent: Presenter.
         * Name: executeAplicaPago.
         *
         * @Description.
         * @EndDescription.
         */
        fun executeAplicaPago()

        fun getMarca(): String
    }

    interface Model{
        fun executeAplicaPagoRequest()
    }
}
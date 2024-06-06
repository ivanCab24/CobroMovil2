package com.Interfaces

import android.content.SharedPreferences
import com.DataModel.SaleVtol
import com.Utilities.Files
import com.Utilities.PreferenceHelper
import com.Utilities.PreferenceHelperLogs
import com.Verifone.EMVResponse

/**
 * Type: Interface.
 * Parent: TercerMensajePresenter.java.
 * Name: TercerMensajePresenter.
 */
interface TercerMensajeMVP {
    /**
     * Type: Interface.
     * Parent: TercerMensajePresenter.
     * Name: View.
     */
    interface View {
        /**
         * Type: Method.
         * Parent: View.
         * Name: onTercerMensajeExceptionResult.
         *
         * @param onExceptionResult @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        fun onTercerMensajeExceptionResult(onExceptionResult: String?)

        /**
         * Type: Method.
         * Parent: View.
         * Name: onTercerMensajeFailResult.
         *
         * @param onFailResult @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        fun onTercerMensajeFailResult(onFailResult: String?)

        /**
         * Type: Method.
         * Parent: View.
         * Name: onTercerMensajeDeclinadaEMV.
         *
         * @param onDeclinadaResult @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        fun onTercerMensajeDeclinadaEMV(onDeclinadaResult: String?)

        /**
         * Type: Method.
         * Parent: View.
         * Name: onTercerMensajeSuccessResult.
         *
         * @param onSuccessResult @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        fun onTercerMensajeSuccessResult(onSuccessResult: String?)
    }

    /**
     * Type: Interface.
     * Parent: TercerMensajePresenter.
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
         * Name: executeTercerMensaje.
         *
         * @param accion               @PsiType:String.
         * @param response             @PsiType:EMVResponse.
         * @param saleVtol             @PsiType:SaleVtol.
         * @param tag9f27              @PsiType:String.
         * @param isTransaccionExitosa @PsiType:boolean.
         * @Description.
         * @EndDescription.
         */
        fun executeTercerMensaje(
            accion: String?,
            response: EMVResponse?,
            saleVtol: SaleVtol?,
            tag9f27: String?,
            isTransaccionExitosa: Boolean
        )
    }

    interface Model{
        fun executeTercerMensajeRequest(accion: String?,
                                        response: EMVResponse?,
                                        saleVtol: SaleVtol?)
    }
}
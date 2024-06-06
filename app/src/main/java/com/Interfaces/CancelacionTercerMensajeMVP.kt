package com.Interfaces

import android.content.SharedPreferences
import com.DataModel.VtolCancelResponse
import com.Utilities.Files
import com.Utilities.PreferenceHelper
import com.Utilities.PreferenceHelperLogs

/**
 * Type: Interface.
 * Parent: CancelacionTercerMensajePresenter.java.
 * Name: CancelacionTercerMensajePresenter.
 */
interface CancelacionTercerMensajeMVP {
    /**
     * Type: Interface.
     * Parent: CancelacionTercerMensajePresenter.
     * Name: View.
     */
    interface View {
        /**
         * Type: Method.
         * Parent: View.
         * Name: onTercerMensajeCancelacionExceptionResult.
         *
         * @param onExceptionResult @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        fun onTercerMensajeCancelacionExceptionResult(onExceptionResult: String?)

        /**
         * Type: Method.
         * Parent: View.
         * Name: onTercerMensajeCancelacionFailResult.
         *
         * @param onFailResult @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        fun onTercerMensajeCancelacionFailResult(onFailResult: String?)

        /**
         * Type: Method.
         * Parent: View.
         * Name: onSuccessTercerMensajeCancelacionResult.
         *
         * @Description.
         * @EndDescription.
         */
        fun onSuccessTercerMensajeCancelacionResult()
    }

    /**
     * Type: Interface.
     * Parent: CancelacionTercerMensajePresenter.
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
         * Name: executeCancelacionTercerMensaje.
         *
         * @param vtolCancelResponse @PsiType:VtolCancelResponse.
         * @Description.
         * @EndDescription.
         */
        fun executeCancelacionTercerMensaje(vtolCancelResponse: VtolCancelResponse?)
    }

    interface Model{
        fun executeCancelacionTercerMensajeRequest(vtolCancelResponse: VtolCancelResponse?)
    }
}
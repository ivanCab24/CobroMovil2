package com.Interfaces

import android.content.SharedPreferences
import com.Utilities.Files
import com.Utilities.PreferenceHelper
import com.Utilities.PreferenceHelperLogs

/**
 * Type: Interface.
 * Parent: RedencionDePuntosPresenter.java.
 * Name: RedencionDePuntosPresenter.
 */
interface RedencionDePuntosMVP {
    /**
     * Type: Interface.
     * Parent: RedencionDePuntosPresenter.
     * Name: View.
     */
    interface View {
        /**
         * Type: Method.
         * Parent: View.
         * Name: onExceptionRedencionDePuntosResult.
         *
         * @param onExceptionResult @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        fun onExceptionRedencionDePuntosResult(onExceptionResult: String?)

        /**
         * Type: Method.
         * Parent: View.
         * Name: onWarningRedencionDePuntosResult.
         *
         * @param onWarningResult @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        fun onWarningRedencionDePuntosResult(onWarningResult: String?)

        /**
         * Type: Method.
         * Parent: View.
         * Name: onFailRedencionDePuntosResult.
         *
         * @param onFailResult @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        fun onFailRedencionDePuntosResult(onFailResult: String?)

        /**
         * Type: Method.
         * Parent: View.
         * Name: onSuccessRedencionDePuntosResult.
         *
         * @param onSuccessResult @PsiType:String.
         * @param mensaje         @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        fun onSuccessRedencionDePuntosResult(onSuccessResult: String?, mensaje: String?)

        fun getCurrentMarca(value: String)
    }

    /**
     * Type: Interface.
     * Parent: RedencionDePuntosPresenter.
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
         * Name: executeRedencionDePuntos.
         *
         * @param puntosARedimir @PsiType:String.
         * @param nip            @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        fun executeRedencionDePuntos(puntosARedimir: String?, nip: String?)

        fun getMarca()
    }

    interface Model {
        fun executeRedencionPRequest(puntosARedimir: String?, nip: String?)

        fun executeValidaInformacion(puntosARedimir: String?): Boolean
    }
}
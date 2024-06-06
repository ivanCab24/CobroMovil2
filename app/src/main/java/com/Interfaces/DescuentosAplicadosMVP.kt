package com.Interfaces

import android.content.SharedPreferences
import com.DataModel.DescuentosAplicados
import com.Utilities.Files
import com.Utilities.PreferenceHelper
import com.Utilities.PreferenceHelperLogs

/**
 * Type: Interface.
 * Parent: DescuentosAplicadosPresenter.java.
 * Name: DescuentosAplicadosPresenter.
 */
interface DescuentosAplicadosMVP {
    /**
     * Type: Interface.
     * Parent: DescuentosAplicadosPresenter.
     * Name: View.
     */
    interface View {
        /**
         * Type: Method.
         * Parent: View.
         * Name: onExceptionDescuentosAplicadosResult.
         *
         * @param onExceptionResult @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        fun onExceptionDescuentosAplicadosResult(onExceptionResult: String?)

        /**
         * Type: Method.
         * Parent: View.
         * Name: onFailDescuentosAplicadosResult.
         *
         * @param onFailResult @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        fun onFailDescuentosAplicadosResult(onFailResult: String?)

        /**
         * Type: Method.
         * Parent: View.
         * Name: onSuccessDescuentosAplicadosResult.
         *
         * @param descuentosAplicadosArrayList @PsiType:ArrayList<DescuentosAplicados>.
         * @Description.
         * @EndDescription.
        </DescuentosAplicados> */
        fun onSuccessDescuentosAplicadosResult(descuentosAplicadosArrayList: ArrayList<DescuentosAplicados?>?)
    }

    /**
     * Type: Interface.
     * Parent: DescuentosAplicadosPresenter.
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
         * Name: executeDescuentosAplicadosRequest.
         *
         * @Description.
         * @EndDescription.
         */
        fun executeDescuentosAplicados()
    }

    interface Model{
        fun executeDescuentosAplicadosRequest()
    }
}
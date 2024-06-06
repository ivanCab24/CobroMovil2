package com.Interfaces

import android.content.SharedPreferences
import com.DataModel.Descuentos
import com.Utilities.Files
import com.Utilities.PreferenceHelper
import com.Utilities.PreferenceHelperLogs

/**
 * Type: Interface.
 * Parent: DescuentosPresenter.java.
 * Name: DescuentosPresenter.
 */
interface DescuentosMVP {
    /**
     * Type: Interface.
     * Parent: DescuentosPresenter.
     * Name: View.
     */
    interface View {
        /**
         * Type: Method.
         * Parent: View.
         * Name: onExceptionResult.
         *
         * @param onExceptionResult @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        fun onExceptionResult(onExceptionResult: String?)

        /**
         * Type: Method.
         * Parent: View.
         * Name: onFailResult.
         *
         * @param onFailResult @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        fun onFailResult(onFailResult: String?)

        /**
         * Type: Method.
         * Parent: View.
         * Name: onSuccess.
         *
         * @param descuentosArrayList @PsiType:ArrayList<Descuentos>.
         * @Description.
         * @EndDescription.
        </Descuentos> */
        fun onSuccess(descuentosArrayList: ArrayList<Descuentos?>?)
    }

    /**
     * Type: Interface.
     * Parent: DescuentosPresenter.
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
         * Name: executeDescuentosRequest.
         *
         * @Description.
         * @EndDescription.
         */
        fun executeDescuentosRequest()
    }

    interface Model {
        fun executeFormasPagoRequest()

        fun executeDescuentosAplicadosRequest()
    }


}
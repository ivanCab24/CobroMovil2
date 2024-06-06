package com.Interfaces

import android.content.SharedPreferences
import com.DataModel.Pagos
import com.Utilities.Files
import com.Utilities.PreferenceHelper
import com.Utilities.PreferenceHelperLogs
import com.innovacion.eks.beerws.databinding.FragmentContentBinding

/**
 * Type: Interface.
 * Parent: PagosPresenter.java.
 * Name: PagosPresenter.
 */
interface PagosMVP {
    /**
     * Type: Interface.
     * Parent: PagosPresenter.
     * Name: View.
     */
    interface View {
        /**
         * Type: Method.
         * Parent: View.
         * Name: onExceptionPagosResult.
         *
         * @param onExceptionResult @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        fun onExceptionPagosResult(onExceptionResult: String?)

        /**
         * Type: Method.
         * Parent: View.
         * Name: onWarningPagosResult.
         *
         * @param onWarningResult @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        fun onWarningPagosResult(onWarningResult: String?)

        /**
         * Type: Method.
         * Parent: View.
         * Name: onFailPagosResult.
         *
         * @param onFailResult @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        fun onFailPagosResult(onFailResult: String?)

        /**
         * Type: Method.
         * Parent: View.
         * Name: onSuccessPagosResult.
         *
         * @param pagosArrayList @PsiType:ArrayList<Pagos>.
         * @Description.
         * @EndDescription.
        </Pagos> */
        fun onSuccessPagosResult(pagosArrayList: ArrayList<Pagos?>?)
    }

    /**
     * Type: Interface.
     * Parent: PagosPresenter.
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
         * Name: setBinding.
         *
         * @Description.
         * @EndDescription.
         * @param: fragmentContentBinding @PsiType:FragmentContentBinding.
         */
        fun setBinding(fragmentContentBinding: FragmentContentBinding?)

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
         * Name: executePagosRequest.
         *
         * @Description.
         * @EndDescription.
         */
        fun executePagosRequest()
    }

    interface Model {
        fun executeFormasPagoRequest()
    }
}
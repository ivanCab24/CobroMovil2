package com.Interfaces

import android.content.SharedPreferences
import com.DataModel.Descuentos
import com.Utilities.Files
import com.Utilities.PreferenceHelper
import com.Utilities.PreferenceHelperLogs

/**
 * Type: Interface.
 * Parent: DescuentoFavoritoPresenter.java.
 * Name: DescuentoFavoritoPresenter.
 */
interface DescuentoFavoritoMVP {
    /**
     * Type: Interface.
     * Parent: DescuentoFavoritoPresenter.
     * Name: View.
     */
    interface View {
        /**
         * Type: Method.
         * Parent: View.
         * Name: onDescuentoFavoritoExceptionResult.
         *
         * @param onExceptionResult @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        fun onDescuentoFavoritoExceptionResult(onExceptionResult: String?)

        /**
         * Type: Method.
         * Parent: View.
         * Name: onDescuentoFavoritoFailResult.
         *
         * @param onFailResult @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        fun onDescuentoFavoritoFailResult(onFailResult: String?)

        /**
         * Type: Method.
         * Parent: View.
         * Name: onDescuentoFavoritoSuccess.
         *
         * @Description.
         * @EndDescription.
         */
        fun onDescuentoFavoritoSuccess()
    }

    /**
     * Type: Interface.
     * Parent: DescuentoFavoritoPresenter.
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
         * Name: executeDescuentoFavoritoRequest.
         *
         * @param descuentos @PsiType:Descuentos.
         * @Description.
         * @EndDescription.
         */
        fun executeDescuentoFavorito(descuentos: Descuentos?)
    }

    interface Model{
        fun executeDescuentoFavoritoRequest(descuentos: Descuentos?)
    }
}
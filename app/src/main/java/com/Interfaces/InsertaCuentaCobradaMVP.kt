package com.Interfaces

import android.app.Activity
import android.content.SharedPreferences
import com.Controller.BD.DAO.CuentaCobradaDAO
import com.DataModel.SaleVtol
import com.Utilities.Files
import com.Utilities.PreferenceHelper
import com.Utilities.PreferenceHelperLogs
import com.Verifone.EMVResponse

/**
 * Type: Interface.
 * Parent: InsertCuentaCobradaPresenter.java.
 * Name: InsertCuentaCobradaPresenter.
 */
interface InsertaCuentaCobradaMVP {
    /**
     * Type: Interface.
     * Parent: InsertCuentaCobradaPresenter.
     * Name: View.
     */
    interface View

    /**
     * Type: Interface.
     * Parent: InsertCuentaCobradaPresenter.
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
         * Name: setActivity.
         *
         * @Description.
         * @EndDescription.
         * @param: activity @PsiType:Activity.
         */
        fun setActivity(activity: Activity?)

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
         * Name: setCuentaCobradaDao.
         *
         * @Description.
         * @EndDescription.
         * @param: cuentaCobradaDAO @PsiType:CuentaCobradaDAO.
         */
        fun setCuentaCobradaDao(cuentaCobradaDAO: CuentaCobradaDAO?)

        /**
         * Type: Method.
         * Parent: Presenter.
         * Name: executeinsertaCuentaCobradaTask.
         *
         * @param saleVtol    @PsiType:SaleVtol.
         * @param emvResponse @PsiType:EMVResponse.
         * @Description.
         * @EndDescription.
         */
        fun executeinsertaCuentaCobradaTask(saleVtol: SaleVtol?, emvResponse: EMVResponse?)
    }

    interface Model{
        fun executeCuentaCobradaTask(saleVtol: SaleVtol?, emvResponse: EMVResponse?)
    }
}
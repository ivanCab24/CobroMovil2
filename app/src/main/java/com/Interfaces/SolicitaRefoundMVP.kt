package com.Interfaces

import android.app.Activity
import android.content.SharedPreferences
import com.Controller.BD.DAO.CatalogoBinesDAO
import com.Utilities.Files
import com.Utilities.PreferenceHelper
import com.Utilities.PreferenceHelperLogs
import com.Verifone.EMVResponse

/**
 * Type: Interface.
 * Parent: SolicitaRefoundPresenter.java.
 * Name: SolicitaRefoundPresenter.
 */
interface SolicitaRefoundMVP {
    /**
     * Type: Interface.
     * Parent: SolicitaRefoundPresenter.
     * Name: View.
     */
    interface View {
        /**
         * Type: Method.
         * Parent: View.
         * Name: onExceptionSolicitaRefoundResult.
         *
         * @param onException @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        fun onExceptionSolicitaRefoundResult(onException: String?)

        /**
         * Type: Method.
         * Parent: View.
         * Name: onFailSolicitaRefoundResult.
         *
         * @param onFail @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        fun onFailSolicitaRefoundResult(onFail: String?)

        /**
         * Type: Method.
         * Parent: View.
         * Name: onSuccessSolicitaRefoundResult.
         *
         * @param response @PsiType:EMVResponse.
         * @param year     @PsiType:String.
         * @param month    @PsiType:String.
         * @param day      @PsiType:String.
         * @param ticket   @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        fun onSuccessSolicitaRefoundResult(
            response: EMVResponse?,
            year: String?,
            month: String?,
            day: String?,
            ticket: String?
        )
    }

    /**
     * Type: Interface.
     * Parent: SolicitaRefoundPresenter.
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
         * Name: setCatalogoBinesDao.
         *
         * @Description.
         * @EndDescription.
         * @param: catalogoBinesDao @PsiType:CatalogoBinesDAO.
         */
        fun setCatalogoBinesDao(catalogoBinesDao: CatalogoBinesDAO?)

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
         * Name: executeSolicitaRefound.
         *
         * @Description.
         * @EndDescription.
         */
        fun executeSolicitaRefound()
    }

    interface Model{
        fun executeC51RefoundTask()
    }
}
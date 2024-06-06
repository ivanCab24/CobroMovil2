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
 * Parent: SolicitaTarjetaPresenter.java.
 * Name: SolicitaTarjetaPresenter.
 */
interface SolicitaTarjetaMVP {
    /**
     * Type: Interface.
     * Parent: SolicitaTarjetaPresenter.
     * Name: View.
     */
    interface View {
        /**
         * Type: Method.
         * Parent: View.
         * Name: onExceptionSolicitaTarjetaResult.
         *
         * @param onException @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        fun onExceptionSolicitaTarjetaResult(onException: String?)

        /**
         * Type: Method.
         * Parent: View.
         * Name: onFailSolicitaTarjetaResult.
         *
         * @param onFail @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        fun onFailSolicitaTarjetaResult(onFail: String?)

        /**
         * Type: Method.
         * Parent: View.
         * Name: onSuccessSolicitaTarjetaResult.
         *
         * @param emvResponse     @PsiType:EMVResponse.
         * @param year            @PsiType:String.
         * @param month           @PsiType:String.
         * @param dia             @PsiType:String.
         * @param idFPGO          @PsiType:int.
         * @param tipoFPGO        @PsiType:int.
         * @param banderaPuntos   @PsiType:String.
         * @param transactionType @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        fun onSuccessSolicitaTarjetaResult(
            emvResponse: EMVResponse?,
            year: String?,
            month: String?,
            dia: String?,
            idFPGO: Int,
            tipoFPGO: Int,
            banderaPuntos: String?,
            transactionType: String?
        )
    }

    /**
     * Type: Interface.
     * Parent: SolicitaTarjetaPresenter.
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
         * Name: setBinesDao.
         *
         * @Description.
         * @EndDescription.
         * @param: binesDao @PsiType:CatalogoBinesDAO.
         */
        fun setBinesDao(binesDao: CatalogoBinesDAO?)

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
         * Name: executeSolicitaTarjeta.
         *
         * @Description.
         * @EndDescription.
         */
        fun executeSolicitaTarjeta()

        fun getMarca(): String
    }

    interface Model{
        fun executeSolicitaTarjetaPresenter()
    }
}
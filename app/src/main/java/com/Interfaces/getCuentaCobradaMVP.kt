package com.Interfaces

import android.app.Activity
import android.content.SharedPreferences
import com.Controller.BD.DAO.CuentaCobradaDAO
import com.Controller.BD.Entity.CuentaCobrada
import com.Utilities.Files
import com.Utilities.PreferenceHelper
import com.Utilities.PreferenceHelperLogs

/**
 * Type: Interface.
 * Parent: getCuentaCobradaPresenter.java.
 * Name: getCuentaCobradaPresenter.
 */
interface getCuentaCobradaMVP {
    /**
     * Type: Interface.
     * Parent: getCuentaCobradaPresenter.
     * Name: View.
     */
    interface View {
        /**
         * Type: Method.
         * Parent: View.
         * Name: onReciveSumas.
         *
         * @param sumasList @PsiType:List<Double>.
         * @Description.
         * @EndDescription.
        </Double> */
        fun onReciveSumas(sumasList: List<Double?>?)

        /**
         * Type: Method.
         * Parent: View.
         * Name: onRecivePropinas.
         *
         * @param propinasList @PsiType:List<Double>.
         * @Description.
         * @EndDescription.
        </Double> */
        fun onRecivePropinas(propinasList: List<Double?>?)

        /**
         * Type: Method.
         * Parent: View.
         * Name: onReciveTotalGlobal.
         *
         * @param total @PsiType:double.
         * @Description.
         * @EndDescription.
         */
        fun onReciveTotalGlobal(total: Double)

        /**
         * Type: Method.
         * Parent: View.
         * Name: onRecivePropinasGlobal.
         *
         * @param propina @PsiType:double.
         * @Description.
         * @EndDescription.
         */
        fun onRecivePropinasGlobal(propina: Double)

        /**
         * Type: Method.
         * Parent: View.
         * Name: onReciveCountRecord.
         *
         * @param count @PsiType:int.
         * @Description.
         * @EndDescription.
         */
        fun onReciveCountRecord(count: Int)

        /**
         * Type: Method.
         * Parent: View.
         * Name: onReceiveCuentasCobradas.
         *
         * @param cuentaCobradaList @PsiType:List<CuentaCobrada>.
         * @Description.
         * @EndDescription.
        </CuentaCobrada> */
        fun onReceiveCuentasCobradas(cuentaCobradaList: List<CuentaCobrada?>?)

        /**
         * Type: Method.
         * Parent: View.
         * Name: onReceiveAllCuentasCobradas.
         *
         * @param cuentaCobradaList @PsiType:List<CuentaCobrada>.
         * @Description.
         * @EndDescription.
        </CuentaCobrada> */
        fun onReceiveAllCuentasCobradas(cuentaCobradaList: List<CuentaCobrada?>?)

        /**
         * Type: Method.
         * Parent: View.
         * Name: onReciveEstafetas.
         *
         * @param estafetasList @PsiType:List<String>.
         * @Description.
         * @EndDescription.
        </String> */
        fun onReciveEstafetas(estafetasList: List<String?>?)
    }

    /**
     * Type: Interface.
     * Parent: getCuentaCobradaPresenter.
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
         * Name: logsInfo.
         *
         * @param preferenceHelperLogs @PsiType:PreferenceHelperLogs.
         * @param files                @PsiType:Files.
         * @Description.
         * @EndDescription.
         */
        fun logsInfo(preferenceHelperLogs: PreferenceHelperLogs?, files: Files?)

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
         * Name: executeGetAllCuentasCobradasTask.
         *
         * @Description.
         * @EndDescription.
         */
        fun executeGetAllCuentasCobradasTask()

        /**
         * Type: Method.
         * Parent: Presenter.
         * Name: executeGetCuentasCobradasTask.
         *
         * @param estafeta @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        fun executeGetCuentasCobradasTask(estafeta: String?)

        /**
         * Type: Method.
         * Parent: Presenter.
         * Name: executeGetEstafetasTask.
         *
         * @Description.
         * @EndDescription.
         */
        fun executeGetEstafetasTask()

        /**
         * Type: Method.
         * Parent: Presenter.
         * Name: executeCountRecordsTask.
         *
         * @Description.
         * @EndDescription.
         */
        fun executeCountRecordsTask()

        /**
         * Type: Method.
         * Parent: Presenter.
         * Name: executeDeleteRecords.
         *
         * @param month @PsiType:int.
         * @Description.
         * @EndDescription.
         */
        fun executeDeleteRecords(month: Int)
    }

    interface Model{
        /**
         * Type: Method.
         * Parent: Presenter.
         * Name: executeGetAllCuentasCobradasTask.
         *
         * @Description.
         * @EndDescription.
         */
        fun getAllCuentasCobradas()

        /**
         * Type: Method.
         * Parent: Presenter.
         * Name: executeGetCuentasCobradasTask.
         *
         * @param estafeta @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        fun getCuentasCobradas(estafeta: String?)

        /**
         * Type: Method.
         * Parent: Presenter.
         * Name: executeGetEstafetasTask.
         *
         * @Description.
         * @EndDescription.
         */
        fun getEstafetas()

        /**
         * Type: Method.
         * Parent: Presenter.
         * Name: executeCountRecordsTask.
         *
         * @Description.
         * @EndDescription.
         */
        fun countRecords()

        /**
         * Type: Method.
         * Parent: Presenter.
         * Name: executeDeleteRecords.
         *
         * @param month @PsiType:int.
         * @Description.
         * @EndDescription.
         */
        fun deleteRecords(month: Int)
    }
}
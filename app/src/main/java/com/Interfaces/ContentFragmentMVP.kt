package com.Interfaces

import android.content.SharedPreferences
import androidx.fragment.app.FragmentManager
import com.DataModel.Cuenta
import com.Utilities.Files
import com.Utilities.PreferenceHelper
import com.Utilities.PreferenceHelperLogs
import com.innovacion.eks.beerws.databinding.FragmentContentBinding

/**
 * Type: Interface.
 * Parent: CuentaPresenter.java.
 * Name: CuentaPresenter.
 */
interface ContentFragmentMVP {
    /**
     * Type: Interface.
     * Parent: CuentaPresenter.
     * Name: View.
     */
    interface View {
        /**
         * Type: Method.
         * Parent: View.
         * Name: onExceptionCuentaResult.
         *
         * @param onExceptionResult @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        fun onExceptionCuentaResult(onExceptionResult: String?)

        /**
         * Type: Method.
         * Parent: View.
         * Name: onWarningCuentaResult.
         *
         * @param onWarningResult @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        fun onWarningCuentaResult(onWarningResult: String?)

        /**
         * Type: Method.
         * Parent: View.
         * Name: onFailCuentaResult.
         *
         * @param onFailResult @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        fun onFailCuentaResult(onFailResult: String?)

        /**
         * Type: Method.
         * Parent: View.
         * Name: onSuccessCuenta.
         *
         * @param cuenta @PsiType:Cuenta.
         * @Description.
         * @EndDescription.
         */
        fun onSuccessCuenta(cuenta: Cuenta?)

        fun getCurrentMarca(value: String)
    }

    /**
     * Type: Interface.
     * Parent: CuentaPresenter.
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

        fun have_pagos():Boolean

        fun requiereAuth():Boolean

        fun liberarCuenta()

        fun revisa_saldo():Boolean

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

        fun getMarca()


        fun payButtonAction()

        fun executePayAction()

        fun setFM(fragmentManager: FragmentManager)

        fun setPinPadPresenter(pinpadTaskPresenter: PinpadTaskMVP.Presenter)

        fun setCajaPresenter(cajaPresenter: CajaMVP.Presenter)

        fun buscarMesa()

        fun tipControls()

        /**
         * Type: Method.
         * Parent: Presenter.
         * Name: preferences.
         *
         * @param sharedPreferences @PsiType:SharedPreferences.
         * @param preferenceHelper  @PsiType:PreferenceHelper.
         * @Description.
         * @EndDescription.
         */
        fun preferences(sharedPreferences: SharedPreferences?, preferenceHelper: PreferenceHelper?)

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
         * Name: setTip.
         *
         * @Description.
         * @EndDescription.
         * @param: tipAmount @PsiType:double.
         */
        fun setTip(tipAmount: Double)

        /**
         * Type: Method.
         * Parent: Presenter.
         * Name: cuentaRequestExecute.
         *
         * @param mesa          @PsiType:String.
         * @param comensal      @PsiType:String.
         * @param juntaSeparada @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        fun cuentaRequestExecute(mesa: String?, comensal: String?, juntaSeparada: String?)
    }

    interface Model {
        fun getMarca(): String
    }
}
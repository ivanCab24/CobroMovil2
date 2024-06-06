package com.Interfaces

import com.Utilities.PreferenceHelper

/**
 * Type: Interface.
 * Parent: PrinterMessagePresenter.java.
 * Name: PrinterMessagePresenter.
 */
interface PrinterMessagePresenter {
    /**
     * Type: Interface.
     * Parent: PrinterMessagePresenter.
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
         * Name: onWarningResult.
         *
         * @param onWarningResult @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        fun onWarningResult(onWarningResult: String?)

        /**
         * Type: Method.
         * Parent: View.
         * Name: onSuccessResult.
         *
         * @param onSuccessResult @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        fun onSuccessResult(onSuccessResult: String?)

        /**
         * Type: Method.
         * Parent: View.
         * Name: onFinishedPrintJob.
         *
         * @Description.
         * @EndDescription.
         */
        fun onFinishedPrintJob()
    }

    /**
     * Type: Interface.
     * Parent: PrinterMessagePresenter.
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
         * @param: preferenceHelper @PsiType:PreferenceHelper.
         */
        fun setPreferences(preferenceHelper: PreferenceHelper?)

        /**
         * Type: Method.
         * Parent: Presenter.
         * Name: imprimirTest.
         *
         * @param testText       @PsiType:String.
         * @param textoArrayList @PsiType:ArrayList<String>.
         * @Description.
         * @EndDescription.
        </String> */
        fun imprimirTest(testText: String?, textoArrayList: ArrayList<String>)
    }
}
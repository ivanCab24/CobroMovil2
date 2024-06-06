package com.Interfaces

import android.app.Activity
import android.content.SharedPreferences
import com.Utilities.Files
import com.Utilities.PreferenceHelper
import com.Utilities.PreferenceHelperLogs
import com.Verifone.BtManager

/**
 * Type: Interface.
 * Parent: PinpadTaskPresenter.java.
 * Name: PinpadTaskPresenter.
 */
interface PinpadTaskMVP {
    /**
     * Type: Interface.
     * Parent: PinpadTaskPresenter.
     * Name: View.
     */
    interface View {
        /**
         * Type: Method.
         * Parent: View.
         * Name: onExceptionPinpadTaskConnection.
         *
         * @param onExceptionResult @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        fun onExceptionPinpadTaskConnection(onExceptionResult: String?)

        /**
         * Type: Method.
         * Parent: View.
         * Name: onFailPinpadTaskConnection.
         *
         * @param onFailResult @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        fun onFailPinpadTaskConnection(onFailResult: String?)

        /**
         * Type: Method.
         * Parent: View.
         * Name: onSuccessPinpadTaskConnection.
         *
         * @param onSuccessResult @PsiType:String.
         * @param btManager       @PsiType:BtManager.
         * @Description.
         * @EndDescription.
         */
        fun onSuccessPinpadTaskConnection(onSuccessResult: String?, btManager: BtManager?)
    }

    /**
     * Type: Interface.
     * Parent: PinpadTaskPresenter.
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
         * Name: executeTaskConnection.
         *
         * @Description.
         * @EndDescription.
         */
        fun executeTaskConnection()
    }

    interface Model{
        fun executeTaskPinpadConection(btManager: BtManager?)
    }
}
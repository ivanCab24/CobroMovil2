/*
 * *
 *  * Created by Gerardo Ruiz on 11/19/20 7:25 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 11/19/20 7:25 PM
 *
 */
package com.Interfaces

import android.content.SharedPreferences
import com.Utilities.Files
import com.Utilities.PreferenceHelper
import com.Utilities.PreferenceHelperLogs

/**
 * Type: Interface.
 * Parent: UploadFilesPresenter.java.
 * Name: UploadFilesPresenter.
 */
interface UploadFilesPresenter {
    /**
     * Type: Interface.
     * Parent: UploadFilesPresenter.
     * Name: View.
     */
    interface View {
        /**
         * Type: Method.
         * Parent: View.
         * Name: onFailUploadFileResult.
         *
         * @param onFailResult @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        fun onFailUploadFileResult(onFailResult: String?)

        /**
         * Type: Method.
         * Parent: View.
         * Name: onSuccessUploadFileResult.
         *
         * @param onSuccessResult @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        fun onSuccessUploadFileResult(onSuccessResult: String?)

        /**
         * Type: Method.
         * Parent: View.
         * Name: onPublishProgress.
         *
         * @param text     @PsiType:String.
         * @param progress @PsiType:int.
         * @Description.
         * @EndDescription.
         */
        fun onPublishProgress(text: String?, progress: Int)
    }

    /**
     * Type: Interface.
     * Parent: UploadFilesPresenter.
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
         * Name: onExecuteUploadFilePresenter.
         *
         * @Description.
         * @EndDescription.
         */
        fun onExecuteUploadFilePresenter()
    }
}
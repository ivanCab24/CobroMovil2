package com.Interfaces

interface DialogWaitingFilesMVP {

    interface View {

        fun updateProgressUploadFiles(text: String, progress: Int)

        fun onExceptionUploadFiles(onException: String)
        fun onSuccesUploadFiles(onSuccess: String)

    }

    interface Presenter {

        fun setView(view: View)

        fun doUpdateProgressText(text: String, progress: Int)

        fun doUploadFiles()

        fun onResponseReceived(onResponse: String)

    }

    interface Model {

        fun executeUpdateFilesTask()

    }

}
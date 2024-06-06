package com.Presenter

import com.Interfaces.DialogWaitingFilesMVP
import javax.inject.Inject

class DialogWaitingFilesPresenter @Inject constructor(
    private var model: DialogWaitingFilesMVP.Model
) : DialogWaitingFilesMVP.Presenter {

    private var view: DialogWaitingFilesMVP.View? = null

    override fun setView(view: DialogWaitingFilesMVP.View) {
        this.view = view
    }

    override fun doUpdateProgressText(text: String, progress: Int) {
        view?.updateProgressUploadFiles(text, progress)
    }

    override fun doUploadFiles() {
        model.executeUpdateFilesTask()
    }

    override fun onResponseReceived(onResponse: String) {

        when (onResponse.split(",")[0]) {

            "Success" -> view?.onSuccesUploadFiles(onResponse.split(",")[1])
            "Exception" -> view?.onExceptionUploadFiles(onResponse.split(",")[1])

        }

    }

}
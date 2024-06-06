package com.Presenter

import com.Interfaces.ResetKeysMVP
import javax.inject.Inject

class DialogResetKeysPresenter @Inject constructor(var model: ResetKeysMVP.Model) :
    ResetKeysMVP.Presenter {

    private var view: ResetKeysMVP.View? = null

    override fun setView(view: ResetKeysMVP.View) {
        this.view = view
    }

    override fun initResetKeysTask() {
        view?.updateUI(false)
        model.doResetKeys()
    }

    override fun onResponse(onResponse: String) {

        val message = onResponse.split(",")
        when (message[0]) {

            "Exception" -> {
                view?.let { view ->
                    view.onExceptionResult(message[1])
                    view.updateUI(true)
                }
            }

            "Fail" -> {
                view?.let { view ->
                    view.onFailResult(message[1])
                    view.updateUI(true)
                }
            }

            "Success" -> view?.setMessage(message[1])

            "Finish" -> {
                view?.let { view ->
                    view.onSuccessResult(message[1])
                    view.updateUIFinished(true)
                }
            }

        }

    }

}
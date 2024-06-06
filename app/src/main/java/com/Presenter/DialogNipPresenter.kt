package com.Presenter

import com.Interfaces.DialogNipMVP
import javax.inject.Inject

class DialogNipPresenter @Inject constructor(
    private val model: DialogNipMVP.Model
) : DialogNipMVP.Presenter {
    private var view: DialogNipMVP.View? = null
    override fun setView(view: DialogNipMVP.View) {
        this.view = view
    }

    override fun doNipRequest(membresia: String) {
        model.executeNipRequest(membresia)
    }

    override fun onResponseReceived(onResponse: String) {
        if (onResponse.contains("Success")) {
            view?.onSuccessNipResult(onResponse.split(",")[1])
        } else {
            when (onResponse.split(",")[0]) {
                "Exception" -> view?.onExceptionNipResult(onResponse.split(",")[1])
                "Fail" -> view?.onFailNipResult(onResponse.split(",")[1])
            }
        }
    }

    override fun btnAction() {
        model.exexute_Reenvio_sms()
    }
}
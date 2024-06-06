package com.Presenter

import com.Interfaces.PinpadE285MVP
import javax.inject.Inject

class PinpadE285Presenter @Inject constructor(private var model: PinpadE285MVP.Model) :
    PinpadE285MVP.Presenter {

    private var view: PinpadE285MVP.View? = null

    override fun setView(view: PinpadE285MVP.View) {
        this.view = view
    }

    override fun getBrand() {
        view?.getCurrentMarca(model.getMarca())
    }

    override fun savePinpadAddress() {
        view?.let {
            model.guardarPinpadAddress(it.getPinpadAddress())
        }
    }

}
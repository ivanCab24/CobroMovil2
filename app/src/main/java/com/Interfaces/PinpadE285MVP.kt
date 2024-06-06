package com.Interfaces

interface PinpadE285MVP {

    interface View {

        fun getPinpadAddress(): String

        fun getCurrentMarca(value: String)

        fun updateUI(isChanged: Boolean, text: String)
        fun updateUIButtons(isChanged: Boolean)

    }

    interface Presenter {

        fun setView(view: View)
        fun getBrand()
        fun savePinpadAddress()

    }

    interface Model {

        fun getMarca(): String
        fun guardarPinpadAddress(value: String)

    }

}
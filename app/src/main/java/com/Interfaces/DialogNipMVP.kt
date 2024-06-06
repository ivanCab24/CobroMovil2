package com.Interfaces

interface DialogNipMVP {

    interface View {
        fun onExceptionNipResult(onException: String)
        fun onFailNipResult(onFail: String)
        fun onSuccessNipResult(nip: String)
    }

    interface Presenter {
        fun setView(view: View)
        fun doNipRequest(membresia: String)
        fun onResponseReceived(onResponse: String)
        fun btnAction()
    }

    interface Model {
        fun executeNipRequest(membresia: String)
        fun exexute_Reenvio_sms()
    }
}
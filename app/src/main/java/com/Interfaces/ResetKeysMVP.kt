package com.Interfaces

interface ResetKeysMVP {

    interface View {

        fun setMessage(message: String)
        fun updateUI(isUpdating: Boolean)
        fun updateUIFinished(isFinished: Boolean)

        fun onExceptionResult(onException: String)
        fun onFailResult(onFail: String)
        fun onSuccessResult(onSuccess: String)

    }

    interface Presenter {

        fun setView(view: View)
        fun initResetKeysTask()

        fun onResponse(onResponse: String)

    }

    interface Model {

        fun doResetKeys()

    }

}
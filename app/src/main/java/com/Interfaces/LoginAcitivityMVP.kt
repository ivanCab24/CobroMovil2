package com.Interfaces

import com.View.Activities.LoginActivity

interface LoginAcitivityMVP {

    interface View {
        fun getEstafeta(): String
        fun getPassword(): String

        fun getCurrentMarca(value: String)

        fun uiControl(isTrue: Boolean)
        fun clearFields()

        fun requestAllPermissions()
        fun requestWriteAndReadPermissions()
        fun requestFineLocation()

        fun areWritePermissionGranted(): Boolean
        fun areReadPermissionGranted(): Boolean
        fun areFineLocationPermissionGranted(): Boolean

        fun onExceptionLoginResult(onExceptionResult: String)
        fun onFailLoginResult(onFailResult: String)
        fun onWarningLoginResult(onWarningResult: String)
        fun onSuccessLoginResult()


    }

    interface Presenter {

        //================================================================================
        // View
        //================================================================================
        fun setView(view: View)

        fun setActivity(activity: LoginActivity)
        fun setDefaultSharedPreferences()

        fun requestPermission()
        fun requestFineLocationPermission()
        fun requestWriteReadPermission()

        //================================================================================
        // Model
        //================================================================================
        fun sendTickets()
        fun sendLogs()
        fun getMarca()
        fun onLoginButtonClicked()
        fun requiereUpdate():Boolean
        fun onResponseReceived(onResponse: String)

        fun disableButtons()

    }

    interface Model {
        fun getMarca(): String
        fun executeTicketNoEnviados()
        fun executeLogs()
        fun getVersion():Boolean
        fun executeLoginTask(estafeta: String, password: String)
        fun executeDefaultSharedPreferences()

    }

}
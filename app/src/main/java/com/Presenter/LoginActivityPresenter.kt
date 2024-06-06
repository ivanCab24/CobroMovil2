package com.Presenter

import android.util.Log
import com.Interfaces.LoginAcitivityMVP
import com.Utilities.Utilities
import com.View.Activities.LoginActivity
import javax.inject.Inject

class LoginActivityPresenter @Inject constructor(private var model: LoginAcitivityMVP.Model) :
    LoginAcitivityMVP.Presenter {

    private var view: LoginAcitivityMVP.View? = null
    private var activity: LoginActivity? = null

    override fun setView(view: LoginAcitivityMVP.View) {
        this.view = view
    }

    override fun setActivity(activity: LoginActivity) {
        this.activity = activity
    }

    override fun setDefaultSharedPreferences() {
        model.executeDefaultSharedPreferences()
    }

    override fun disableButtons() {
        Utilities.disableButon(activity!!.binding.imageButtonSettings, activity!!)
        Utilities.disableButon(activity!!.binding.imageButtonSubirArchivos, activity!!)
        Utilities.disableButon(activity!!.binding.imageButtonUpdate, activity!!)
    }

    override fun requestPermission() {

        view?.let { view ->

            if (!view.areWritePermissionGranted() && !view.areReadPermissionGranted() && !view.areFineLocationPermissionGranted()) {

                view.requestAllPermissions()

            } else if (!view.areWritePermissionGranted() && !view.areReadPermissionGranted()) {

                view.requestWriteAndReadPermissions()

            } else if (!view.areFineLocationPermissionGranted()) {

                view.requestFineLocation()

            }

        }

    }

    override fun requestFineLocationPermission() {
        view?.requestFineLocation()
    }

    override fun requestWriteReadPermission() {
        view?.requestWriteAndReadPermissions()
    }

    override fun sendTickets() {
        model.executeTicketNoEnviados()
    }

    override fun sendLogs() {
        model.executeLogs()
    }

    override fun getMarca() {
        view?.getCurrentMarca(model.getMarca())
    }

    override fun onLoginButtonClicked() {
        view?.let {
            if (it.getEstafeta().isNotEmpty() && it.getPassword().isNotEmpty()) {
                it.uiControl(false)
                model.executeLoginTask(it.getEstafeta(), it.getPassword())
            } else {
                it.onWarningLoginResult("Ingrese su estafeta y/o contraseña")
            }
        }
    }

    override fun requiereUpdate(): Boolean {
        Log.i("**model", model.getVersion().toString())
        return model.getVersion()
    }


    override fun onResponseReceived(onResponse: String) {

        view?.uiControl(true)

        if (onResponse == "SUCCESS") {

            view?.clearFields()
            view?.onSuccessLoginResult()

        } else {

            when (onResponse.split(",")[0]) {
                "Exception" -> view?.onExceptionLoginResult(onResponse.split(",")[1])
                "Fail" -> view?.onFailLoginResult(onResponse.split(",")[1])
            }

        }

    }
}
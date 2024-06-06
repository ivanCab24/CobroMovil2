package com.Presenter

import com.Interfaces.SettingsMVP
import javax.inject.Inject

class DialogSettingsPresenter @Inject constructor(private var model: SettingsMVP.Model) :
    SettingsMVP.Presenter {

    private var view: SettingsMVP.View? = null

    override fun setView(view: SettingsMVP.View) {
        this.view = view
    }

    override fun saveCurrentConfig() {
        view?.let {
            model.saveConfig(
                it.getServerIp().filterNot { it.isWhitespace() }, it.getIpTerminal(), it.getNumeroTerminal(),
                it.getCajaMP(), it.getUnidad(), it.getImpresora()
            )
        }

    }

    override fun getCurrentConfig() {

        val config = model.getConfig()
        view?.let {
            it.setServerIp(config[0])
            it.setIpTerminal(config[1])
            it.setNumeroTerminal(config[2])
            it.setCajaMP(config[3])
            it.setUnidad(config[4])
            it.setImpresora(config[5])
            it.setSwitchTraining(model.getTrining())
            if(model.getTrining())it.lockFields() else it.UnockFields()
        }
    }

    override fun getMarca() {
        view?.getCurrentMarca(model.getMarca())
    }

    override fun onUpdateBinesButtonClick() {
        view?.updateUI(true)
        model.executeBinesTask()
    }

    override fun onResponseReceived(onResponse: String) {
        view?.let {
            it.updateUI(false)
            if (onResponse == "SUCCESS") {
                it.onSuccessResult("Actualización de bines completada.")
            } else {
                when (onResponse.split(",")[0]) {
                    "Exception" -> it.onExceptionResult(onResponse.split(",")[1])
                    "Fail" -> it.onFailResult(onResponse.split(",")[1])
                }
            }
        }
    }
}
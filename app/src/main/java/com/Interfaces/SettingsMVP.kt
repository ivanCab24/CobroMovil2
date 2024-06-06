package com.Interfaces

interface SettingsMVP {

    interface View {

        fun getServerIp(): String
        fun getIpTerminal(): String
        fun getNumeroTerminal(): String
        fun getCajaMP(): String
        fun getUnidad(): String
        fun getImpresora(): String

        fun setServerIp(value: String)
        fun setIpTerminal(value: String)
        fun setNumeroTerminal(value: String)
        fun setCajaMP(value: String)
        fun setUnidad(value: String)
        fun setImpresora(value: String)
        fun setSwitchTraining(value: Boolean)

        fun lockFields()
        fun UnockFields()

        fun getCurrentMarca(value: String)

        fun updateUI(isUpdated: Boolean)

        fun onExceptionResult(onExceptionResult: String)
        fun onFailResult(onFailResult: String)
        fun onSuccessResult(onSuccessResult: String)

    }

    interface Presenter {

        fun setView(view: View)
        fun saveCurrentConfig()
        fun getCurrentConfig()
        fun getMarca()
        fun onUpdateBinesButtonClick()
        fun onResponseReceived(onResponse: String)


    }

    interface Model {

        fun executeBinesTask()
        fun saveConfig(
            serverIP: String, ipTerminal: String, numeroTerminal: String,
            cajaMP: String, unidad: String, impresora: String
        )

        fun getConfig(): Array<String>
        fun getTrining():Boolean
        fun getMarca(): String

        fun getToken():String

    }
}
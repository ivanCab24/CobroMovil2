package com.Interfaces

import android.app.Activity
import com.Utilities.PreferenceHelper

interface DialogErrorRespuestaMVP {

    interface View {

        fun changeDialogMessage(message: String)

        fun updateDialogUI()
        fun updateContentFragmentUI()

        fun ocultarDialog()

        fun onExceptionResultPrinter(onException: String)
        fun onFailResultPrinter(onFail: String)
        fun onWarningResultPrinter(onWarning: String)

    }

    interface Presenter {

        fun setView(view: View)
        fun setActivity(activity: Activity?)

        fun printComprobante()

        fun printComprobanteBixolon()

        fun getMarca(): String

        fun limpiarVenta()


    }

    interface Model {

        fun returnMarca(): String
        fun returnImpresora(): String
        fun removeCodBarras()

        fun returnPreferenceHelper(): PreferenceHelper

    }

}
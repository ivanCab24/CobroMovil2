package com.Interfaces

import com.innovacion.eks.beerws.databinding.VisualizadorDialogBinding

interface DialogVisualizadorMVP {
    interface View{
        fun onExceptionVisualizadorResult(onExceptionResult: String)

        fun onFailVisualizadorResult(onFailResult: String)

        fun onSuccessVisualizadorResult(registros: ArrayList<ArrayList<String>>)

        fun getCurrentMarca(value: String)

    }
    interface Presenter{
        fun setView(view: View)
        fun getMarca():String
        fun onResponseReceived(onResponse: String)
        fun agregarCabecera()
        fun agregarContenido(registros:ArrayList<ArrayList<String>>)
        fun setBinding(binding: VisualizadorDialogBinding)
        fun executeCatalogoPayRequest()
    }
    interface Model{
        fun getMarca(): String
        fun executeCatalogoPayRequest()
    }
}
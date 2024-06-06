package com.Interfaces

import android.app.Activity
import android.content.Context
import com.View.Dialogs.DialogEnviarCorreo
import com.innovacion.eks.beerws.databinding.EnviaCorreoBinding

interface CorreoMVP {

    interface View{
        fun mostrarToast(contexto: Context, mensaje: String)
        fun onSuccessSend(text:String)
        fun onFailSend(text:String)
    }
    interface Presenter {
        fun getV() : CorreoMVP.View
        fun setDialogV(dialog: DialogEnviarCorreo)
        fun setActivityV(activity: Activity)
        fun setView(view: View)

        fun executeEnviaTikcet(badera : Int,folio:String, correo:String);
        fun setBindingV(bindinng: EnviaCorreoBinding)
       // fun excuteSetTicket( texto: String, bandera:Int)

    }

    interface Model {
        fun enviaTicketRequest(badera:Int,folio:String, correo:String)
    //    fun EnviaTicket(texto: String, bandera: Int)

    }


}
package com.Interfaces

import android.app.Activity
import com.DataModel.Miembro
import com.DataModel.Producto
import com.View.Dialogs.DialogCuponesDescuentos
import com.innovacion.eks.beerws.databinding.DialogCuponesDescuentosBinding

interface CuponesDescuentosMVP {
    interface View{

    }
    interface Presenter{
        fun checkCupones():Boolean

        fun generaDescuento(cupon: Miembro.Response.Cupones)

        fun genMontoDesc(cupon: Miembro.Response.Cupones)

        fun validaCupon(cupon: Miembro.Response.Cupones?):Boolean

        fun showMessage(message:String)

        fun enviaCupones()

        fun procesaSeleccion()

        fun generaCuenta()

        fun setViewV(view: android.view.View)

        fun getViewV():android.view.View

        fun setDialogV(dialog:DialogCuponesDescuentos)

        fun setBindingV(bindinng: DialogCuponesDescuentosBinding)

        fun setActivityV(activity: Activity)

    }
    interface Model{
        fun executeSendCupones(cupon:Miembro.Response.Cupones,producto: Producto)
    }
}
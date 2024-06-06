package com.Interfaces

import android.app.Activity
import com.View.Dialogs.DialogImpresora
import com.innovacion.eks.beerws.databinding.SeleccionDeImpresoraBinding
import org.json.JSONArray

/**
 * Type: Interface.
 * Parent: ImprimeTicketPresenter.java.
 * Name: ImprimeTicketPresenter.
 */
interface SeleccionImpresoraMVP {
    /**
     * Type: Interface
     * Name: View.
     */
    interface View {
       fun onSelectPrint(id_impresora : String)

       fun onSuccesGetPrint(impresoras :JSONArray)

       fun onResultPrint(result : String)


    }

    interface Presenter {
        fun getV() : SeleccionImpresoraMVP.View

        fun setView(view: View)

        fun setDialogV(dialog: DialogImpresora)
        fun excutegetPrinters()
        fun excuteSetPrinte(id_printer: String?, texto: String?, bandera:Int)

        fun setActivityV(activity: Activity)
        fun setBindingV(bindinng: SeleccionDeImpresoraBinding)

    }

    interface Model {
          fun getPrinters()
          fun imprimePrinte(id_printer: String?, texto: String?,bandera:Int)

    }
}



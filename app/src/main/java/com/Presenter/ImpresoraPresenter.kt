package com.Presenter

import android.app.Activity
import android.content.SharedPreferences
import android.view.View
import com.Interfaces.SeleccionImpresoraMVP
import com.Utilities.Files
import com.Utilities.PreferenceHelper
import com.Utilities.PreferenceHelperLogs
import com.View.Dialogs.DialogImpresora
import com.innovacion.eks.beerws.databinding.SeleccionDeImpresoraBinding
import java.lang.ref.WeakReference
import javax.inject.Inject


class ImpresoraPresenter @Inject constructor
    (private val model: SeleccionImpresoraMVP.Model) :
    SeleccionImpresoraMVP.Presenter  { //manda a imprimir--

    var binding: SeleccionDeImpresoraBinding? = null
    var dialog:DialogImpresora?=null
    var activity: Activity?=null
    var view: View? = null

     fun  getViewV():View {
        return this.view!!

    }

    override fun getV(): SeleccionImpresoraMVP.View {
        return ImpresoraPresenter.view!!
    }


    override fun setView(view: SeleccionImpresoraMVP.View) {
       ImpresoraPresenter.view = view
    }

    override fun setDialogV(dialog: DialogImpresora) {
        this.dialog=dialog
    }

    override fun excutegetPrinters()  {
        model.getPrinters()
    }

    override fun excuteSetPrinte(id_printer: String?, texto: String?, bandera:Int) {
         model.imprimePrinte(id_printer,texto,bandera)
    }


    override fun setActivityV(activity: Activity) {
        this.activity = activity
    }

    override fun setBindingV(binding: SeleccionDeImpresoraBinding) {
        this.binding=binding
    }



    //view?.getCurrentMarca(model.getMarca())

    companion object {
        const val TAG = "ObtenerImpresora"
        var view: SeleccionImpresoraMVP.View? = null
        var getSharedPreferences: SharedPreferences? = null
        var preferenceHelper: PreferenceHelper? = null
        var preferenceHelperLogs: PreferenceHelperLogs? = null
        var filesWeakReference: WeakReference<Files?>? = null
    }
}






package com.Presenter

import android.app.Activity
import android.content.SharedPreferences
import android.view.View
import com.Interfaces.CorreoMVP
import com.Utilities.Files
import com.Utilities.PreferenceHelper
import com.Utilities.PreferenceHelperLogs
import com.View.Dialogs.DialogEnviarCorreo
import com.innovacion.eks.beerws.databinding.EnviaCorreoBinding
import java.lang.ref.WeakReference
import javax.inject.Inject

class CorreoPresenter @Inject constructor (private val model: CorreoMVP.Model) : CorreoMVP.Presenter{

    var binding: EnviaCorreoBinding? = null
    var dialog: DialogEnviarCorreo?=null
    var activity: Activity?=null
    var view: View? = null

    override fun getV(): CorreoMVP.View {
        return CorreoPresenter.view!!
    }
    override fun setView(view: CorreoMVP.View) {
        CorreoPresenter.view = view
    }
    override fun executeEnviaTikcet(badera: Int, folio: String, correo: String) {
        model.enviaTicketRequest(badera,folio,correo)
    }
    override fun setDialogV(dialog: DialogEnviarCorreo) {
        this.dialog=dialog
    }
    override fun setBindingV(binding: EnviaCorreoBinding) {
       this.binding=binding
    }

   /* override fun excuteSetTicket(texto: String, bandera: Int) {
        model.EnviaTicket(texto, bandera)
    }*/

    override fun setActivityV(activity: Activity) {
        this.activity = activity
    }

    companion object {
        const val TAG = "DialogCorreo"
        var view: CorreoMVP.View? = null
        var getSharedPreferences: SharedPreferences? = null
        var preferenceHelper: PreferenceHelper? = null
        var preferenceHelperLogs: PreferenceHelperLogs? = null
        var filesWeakReference: WeakReference<Files?>? = null
    }




}
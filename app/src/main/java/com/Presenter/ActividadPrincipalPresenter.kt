package com.Presenter

import android.view.View
import androidx.fragment.app.FragmentManager
import com.Constants.ConstantsPreferences
import com.Interfaces.ActividadPrincipalMVP
import com.Utilities.Files
import com.Utilities.PreferenceHelper
import com.Utilities.PreferenceHelperLogs
import com.Utilities.Utilities
import com.View.Activities.ActividadPrincipal
import com.View.Fragments.ContentFragment
import com.View.Fragments.HistoricosPagosFragment
import com.View.UserInteraction
import com.innovacion.eks.beerws.R
import java.lang.ref.WeakReference
import javax.inject.Inject

class ActividadPrincipalPresenter @Inject constructor
    (private var model: ActividadPrincipalMVP.Model)  :
    ActividadPrincipalMVP.Presenter {

    private var files: Files? = null
    private var view: ActividadPrincipalMVP.View? = null
    private var activity: ActividadPrincipal? = null
    private var fragmentManager: FragmentManager? = null
    private var preferenceHelper: PreferenceHelper? = null
    private var preferenceHelperLogs: PreferenceHelperLogs? = null
    private val filesWeakReference: WeakReference<Files>? = null


    override fun setView(view: ActividadPrincipalMVP.View) {
        this.view = view
    }

    override fun setFiles(files: Files) {
        this.files = files
    }


    override fun setPreferenceHelperLogs(preferenceHelperLogs: PreferenceHelperLogs) {
        this.preferenceHelperLogs = preferenceHelperLogs
    }

    override fun setPreferenceHelper(preferenceHelper: PreferenceHelper) {
        this.preferenceHelper = preferenceHelper
    }

    override fun setActivity(activity: ActividadPrincipal) {
        this.activity = activity
    }

    override fun setFM(fragmentManager: FragmentManager) {
        this.fragmentManager = fragmentManager
    }

    override fun btnResetKey() {
        if (!preferenceHelper!!.getString(ConstantsPreferences.PREF_PINPAD, null).isEmpty()//knne se le quita el ! y se agrega el isNotEmpty
        ) {
            preferenceHelperLogs?.let {
                files!!.registerLogs(
                    "Boton reset keys presionado",
                    "", it, preferenceHelper!!
                )
            }
            ContentFragment.contentFragment!!.resetKeys()
        } else {
            UserInteraction.snackyWarning(
                activity,
                null,
                "Configure los parametros de la pinpad/terminal"
            )
        }
        this.disableBtns()
    }

    override fun btnHistorico() {
        System.out.println("***al oprimir el boton de historico de pagos***")
        activity!!.binding.barraLateralConstraint.visibility = View.GONE
        val fragmentTransaction = ActividadPrincipal.fm!!.beginTransaction()
        val historicosPagosFragment = HistoricosPagosFragment(activity)
        fragmentTransaction.replace(R.id.fragment_container, historicosPagosFragment)
        fragmentTransaction.commit()
        this.disableBtns()
    }

    override fun btnSettings() {
        preferenceHelperLogs?.let {
            preferenceHelper?.let { it1 ->
                files!!.registerLogs(
                    "Boton de ajustes desde ActividadPrincipal presionado",
                    "", it, it1
                )
            }
        }
        UserInteraction.showSettingsDialog(fragmentManager, "1")
        this.disableBtns()
    }

    override fun btnVisualizador() {
        System.out.println("-->Oprimir el boton de A comer")
        UserInteraction.showWaitingDialog(fragmentManager, "Consultando")
        ContentFragment.contentFragment!!.getVisualizadorPresenter()!!.executeCatalogoPayRequest()
       // UserInteraction.showVisualizadorDialog(fragmentManager);
        //UserInteraction.showVisualizadorDialog(fragmentManager);
        disableBtns()
    }

    override fun setDefaultSharedPreferences() {
        model.executeDefaultSharedPreferences()
    }

    override fun disableBtns() {
        Utilities.disableButon(activity!!.binding.imageButtonReimprimir, activity!!)
        Utilities.disableButon(activity!!.binding.imageButtonIdentificarMiembro, activity!!)
        Utilities.disableButon(activity!!.binding.imageButton, activity!!)
        Utilities.disableButon(activity!!.binding.imageButtonResetKeys, activity!!)
        Utilities.disableButon(activity!!.binding.imageButton3, activity!!)
    }

    override fun btnIdentificarMiembro() {

        preferenceHelperLogs?.let {
            preferenceHelper?.let { it1 ->
                files!!.registerLogs(
                    "Boton de Identificar miembro",
                    "", it, it1
                )
            }
        }
        ActividadPrincipal.banderaDialogMostrar = "1"
        ActividadPrincipal.isDescuentoGRG = false
        ContentFragment.getDescuento = null
        UserInteraction.showDialogBuscarAfiliado(fragmentManager)
        this.disableBtns()
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


}
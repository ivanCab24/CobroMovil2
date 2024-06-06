/*
 * *
 *  * Created by Gerardo Ruiz on 11/11/20 4:48 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 11/11/20 4:48 PM
 *
 */
package com.View.Dialogs

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.Constants.ConstantsMarcas
import com.Constants.ConstantsPreferences
import com.Controller.BD.DAO.TicketsNoRegistradosDAO
import com.DI.BaseApplication
import com.Interfaces.AcumularPuntosMVP
import com.Utilities.Files
import com.Utilities.PreferenceHelper
import com.Utilities.PreferenceHelperLogs
import com.View.Fragments.ContentFragment
import com.View.UserInteraction
import com.github.ybq.android.spinkit.style.DoubleBounce
import com.innovacion.eks.beerws.R
import com.innovacion.eks.beerws.databinding.WaitDialogBinding
import com.orhanobut.hawk.Hawk
import com.webservicestasks.ToksWebServiceErrors
import com.webservicestasks.ToksWebServiceErrorsDescriptions
import com.webservicestasks.ToksWebServicesConnection
import javax.inject.Inject
import javax.inject.Named

/**
 * Type: Class.
 * Access: Public.
 * Name: DialogAcumularPuntos.
 * @filter Dialogos.
 *
 * @Description.
 * Este diálogo se muestra cuando se esta realizando la acumulación de puntos, mostrando el siguiente mensaje "Realizando acumulación de puntos", de no poder realizar la acumulación, igual mostrará un mensaje.
 * @EndDescription.
 */
class DialogAcumularPuntos : DialogFragment(), ToksWebServicesConnection, ToksWebServiceErrors,
    ToksWebServiceErrorsDescriptions, AcumularPuntosMVP.View {
    private var binding: WaitDialogBinding? = null
    private var doubleBounce: DoubleBounce? = null
    private var fragmentManager2: FragmentManager? = null

    /**
     * The Shared preferences.
     */
    @JvmField
    @Named("Preferencias")
    @Inject
    var sharedPreferences: SharedPreferences? = null

    /**
     * The Preference helper.
     */
    @JvmField
    @Inject
    var preferenceHelper: PreferenceHelper? = null

    /**
     * The Preference helper logs.
     */
    @JvmField
    @Inject
    var preferenceHelperLogs: PreferenceHelperLogs? = null

    /**
     * The Files.
     */
    @JvmField
    @Inject
    var files: Files? = null

    /**
     * The Tickets no registrados dao.
     */
    @JvmField
    @Inject
    var ticketsNoRegistradosDAO: TicketsNoRegistradosDAO? = null

    /**
     * The Acumular puntos presenter.
     */
    @JvmField
    @Inject
    var presenter: AcumularPuntosMVP.Presenter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = WaitDialogBinding.inflate(inflater, container, false)
        initDI()
        presenter!!.getMarca()
        return binding!!.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context.applicationContext as BaseApplication).plusAcomerClubSubComponent().inject(this)
        doubleBounce = DoubleBounce()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentManager2 = getFragmentManager()
        binding!!.dialogTitle.text = "Aviso"
        binding!!.dialogMessage.text = "Realizando acumulación de puntos"
        binding!!.progressBar.indeterminateDrawable = doubleBounce
        var miembro2 = ContentFragment.miembroCuentaA!![ContentFragment.cuenta.folio]
        if (miembro2 != null) {
            presenter!!.executeAcumulaPuntos()
        } else {
            dismiss()
            UserInteraction.snackyFail(
                activity,
                null,
                "No fue posible realizar la acumulación \n Indicar al cliente que lo haga manualmente"
            )
        }
    }

    private fun initDI() {
        presenter!!.setView(this)
        presenter!!.setContext(context)
        presenter!!.setPreferences(sharedPreferences, preferenceHelper)
        presenter!!.setLogsInfo(preferenceHelperLogs, files)
        presenter!!.setTicketsNoRegistradosDao(ticketsNoRegistradosDAO)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog!!.window!!.attributes.windowAnimations = R.style.DialogTheme
        dialog!!.setCancelable(false)
    }

    override fun onResume() {
        super.onResume()
        dialog!!.window!!.setLayout(700, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    //================================================================================
    // Inicia Presenter AcumularPuntos
    //================================================================================
    override fun onExceptionAcumularPuntosResult(onExceptionResult: String?) {
        ContentFragment.miembro = null
        ContentFragment.copiaMiembro = null
        dismiss()
        preferenceHelper!!.removePreference(ConstantsPreferences.PREF_BANDERA_AFILIADO)
        UserInteraction.showImprimeTicketDialog(fragmentManager)
        UserInteraction.snackyException(activity, null, onExceptionResult)
    }

    override fun onFailAcumularPuntosResult(onFailResult: String?) {
        ContentFragment.miembro = null
        ContentFragment.copiaMiembro = null
        dismiss()
        preferenceHelper!!.removePreference(ConstantsPreferences.PREF_BANDERA_AFILIADO)
        UserInteraction.showImprimeTicketDialog(fragmentManager)
        UserInteraction.snackyFail(activity, null, onFailResult)
    }

    override fun onSuccessAcumularPuntosResult(onSuccessResult: String?) {
        ContentFragment.miembroCuentaA!!.remove(ContentFragment.cuenta.folio)
        Hawk.put("membresias",ContentFragment.miembroCuentaA)
        ContentFragment.miembro = null
        ContentFragment.copiaMiembro = null
        dismiss()
        preferenceHelper!!.removePreference(ConstantsPreferences.PREF_BANDERA_AFILIADO)
        UserInteraction.showImprimeTicketDialog(fragmentManager)
        UserInteraction.snackySuccess(activity, null, "Se acumularon $onSuccessResult puntos.")
    } //================================================================================

    override fun getCurrentMarca(value: String) {
        when (value) {
            ConstantsMarcas.MARCA_BEER_FACTORY -> doubleBounce?.color =
                ContextCompat.getColor(context!!, R.color.doradoBeer)
            ConstantsMarcas.MARCA_EL_FAROLITO, ConstantsMarcas.MARCA_TOKS -> doubleBounce?.color =
                ContextCompat.getColor(context!!, R.color.white)
        }
    }

    // Termina Presenter AcumularPuntos
    //================================================================================
    companion object {
        private const val TAG = "DialogAcumularPuntos"

        /**
         * Type: Method.
         * Parent: DialogAcumularPuntos.
         * Name: newInstance.
         *
         * @return DialogAcumularPuntos.
         * @Description.
         * @EndDescription.
         */
        @JvmStatic
        fun newInstance(): DialogAcumularPuntos {
            val args = Bundle()
            val fragment = DialogAcumularPuntos()
            fragment.arguments = args
            return fragment
        }
    }
}
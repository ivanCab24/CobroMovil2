package com.Presenter

import android.content.SharedPreferences
import com.DataModel.Pagos
import com.Interfaces.PagosMVP
import com.Utilities.Files
import com.Utilities.PreferenceHelper
import com.Utilities.PreferenceHelperLogs
import com.innovacion.eks.beerws.databinding.FragmentContentBinding
import com.webservicestasks.ToksWebServicesConnection
import java.lang.ref.WeakReference
import javax.inject.Inject

/**
 * Type: Class.
 * Access: Public.
 * Name: PagosRequest.
 *
 * @Description.
 * @EndDescription.
 */
class PagosPresenter @Inject constructor(
    private val model: PagosMVP.Model
) : ToksWebServicesConnection, PagosMVP.Presenter {
    override fun setView(getView: PagosMVP.View?) {
        view = getView
    }

    override fun setBinding(fragmentContentBinding: FragmentContentBinding?) {
        fragmentContentBindingWeakReference = WeakReference(fragmentContentBinding)
    }

    override fun setPreferences(
        sharedPreferences: SharedPreferences?,
        preferenceHelper: PreferenceHelper?
    ) {
        getSharedPreferences = sharedPreferences
        preferencesHelper = preferenceHelper
    }

    override fun setLogsInfo(preferenceHelperLogs: PreferenceHelperLogs?, files: Files?) {
        Companion.preferenceHelperLogs = preferenceHelperLogs
        filesWeakReference = WeakReference(files)
    }

    override fun executePagosRequest() {
        model.executeFormasPagoRequest()
    }

    companion object {
        const val TAG = "PagosRequest"
        var view: PagosMVP.View? = null
        var getSharedPreferences: SharedPreferences? = null
        var preferencesHelper: PreferenceHelper? = null
        var preferenceHelperLogs: PreferenceHelperLogs? = null
        var filesWeakReference: WeakReference<Files?>? = null
        val pagosArrayList = ArrayList<Pagos?>()
        var fragmentContentBindingWeakReference: WeakReference<FragmentContentBinding?>? = null
    }
}
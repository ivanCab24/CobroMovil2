package com.Presenter

import android.content.SharedPreferences
import com.Constants.ConstantsPreferences
import com.DataModel.SaleVtol
import com.Interfaces.AplicaPagoTarjetaPresenter
import com.Utilities.Files
import com.Utilities.PreferenceHelper
import com.Utilities.PreferenceHelperLogs
import com.Verifone.EMVResponse
import com.webservicestasks.ToksWebServicesConnection
import java.lang.ref.WeakReference
import javax.inject.Inject

/**
 * Type: Class.
 * Access: Public.
 * Name: AplicaPagoTarjetaRequest.
 *
 * @Description.
 * @EndDescription.
 */
class AplicaPagoTarjetaPresenter @Inject constructor(
    private val model: AplicaPagoTarjetaPresenter.Model
): ToksWebServicesConnection, AplicaPagoTarjetaPresenter.Presenter {
    override fun setView(getView: AplicaPagoTarjetaPresenter.View?) {
        view = getView
    }

    override fun setPreferences(
        sharedPreferences: SharedPreferences?,
        preferenceHelper: PreferenceHelper?
    ) {
        getSharedPreferences = sharedPreferences
        Companion.preferenceHelper = preferenceHelper
    }

    override fun setLogsInfo(preferenceHelperLogs: PreferenceHelperLogs?, files: Files?) {
        Companion.preferenceHelperLogs = preferenceHelperLogs
        filesWeakReference = WeakReference(files)
    }

    override fun setVariables(
        saleVtol: SaleVtol?,
        response: EMVResponse?,
        idFPGO: Int,
        tipoFPGO: Int
    ) {
        getSaleVtol = saleVtol
        getResponse = response
        getIdFpgo = idFPGO
        getTipoFpgo = tipoFPGO
    }

    override fun executeAplicaPago() {
        model.executeAplicaPagoRequest()
    }

    override fun getMarca(): String {
        return preferenceHelper!!.getString(ConstantsPreferences.PREF_MARCA_SELECCIONADA, null)
    }
    companion object {
        const val TAG = "AplicaPagoTarjetaReques"
        var view: AplicaPagoTarjetaPresenter.View? = null
        var getSharedPreferences: SharedPreferences? = null
        var preferenceHelper: PreferenceHelper? = null
        var preferenceHelperLogs: PreferenceHelperLogs? = null
        var filesWeakReference: WeakReference<Files?>? = null
        var getSaleVtol: SaleVtol? = null
        var getResponse: EMVResponse? = null
        var getIdFpgo = 0
        var getTipoFpgo = 0
    }
}
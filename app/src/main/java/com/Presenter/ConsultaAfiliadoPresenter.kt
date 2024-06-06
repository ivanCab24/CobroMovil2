/*
 * *
 *  * Created by Gerardo Ruiz on 11/13/20 10:39 AM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 11/13/20 10:39 AM
 *
 */
package com.Presenter

import android.content.SharedPreferences
import com.Constants.ConstantsMarcas.MARCA_BEER_FACTORY
import com.Constants.ConstantsMarcas.MARCA_EL_FAROLITO
import com.Constants.ConstantsMarcas.MARCA_TOKS
import com.Constants.ConstantsPreferences
import com.DataModel.Miembro
import com.DataModel.Producto
import com.Interfaces.ConsultaAfiliadoMVP
import com.Utilities.Files
import com.Utilities.PreferenceHelper
import com.Utilities.PreferenceHelperLogs
import com.webservicestasks.ToksWebServicesConnection
import java.lang.ref.WeakReference
import javax.inject.Inject

/**
 * Type: Class.
 * Access: Public.
 * Name: ConsultaAfiliadoRequest.
 *
 * @Description.
 * @EndDescription.
 */
class ConsultaAfiliadoPresenter @Inject constructor(
    private val model: ConsultaAfiliadoMVP.Model
): ToksWebServicesConnection, ConsultaAfiliadoMVP.Presenter {
    override fun executeRedimeCupon(numeroCupon: String?, producto: Producto) {
        model.executeRedimeCuponRequest(numeroCupon, producto)
    }

    override fun executeRedimeCupon2(numeroCupon: String?) {
        model.executeRedimeCuponRequest20(numeroCupon)
    }

    override fun executeVerificaCuponGRG(cupon: String) {
        model.executeVerificaCuponGRGRequest(cupon)
    }

    override fun executeVerificaCuponEngachement(cupon: String) {
        model.executeVerificaCuponEngachement(cupon)
    }

    override fun setView(getView: ConsultaAfiliadoMVP.View?) {
        view = getView
    }

    override fun generaCupones(cupones: List<Miembro.Response.Cupones>,marca:String): ArrayList<Miembro.Response.Cupones> {
        var cuponesList = ArrayList<Miembro.Response.Cupones>()
        var marca2 = ""
        when (marca) {
            MARCA_TOKS -> marca2="LOY_MRC_TOKS"
            MARCA_BEER_FACTORY -> marca2="LOY_MRC_BF"
            MARCA_EL_FAROLITO -> marca2="LOY_MRC_FAROLITO"
        }
        for(cupon in cupones){
            if(cupon.restricciones.tipoCupon=="LOY_TDP_PRODUCTO" && cupon.marcaBeneficio==marca2)
                cuponesList.add(cupon)
        }
        return cuponesList

    }

    override fun generaDescuentos(descuentos: List<Miembro.Response.Cupones>,marca:String): ArrayList<Miembro.Response.Cupones> {
        var descuentosList = ArrayList<Miembro.Response.Cupones>()
        var marca2 = ""
        when (marca) {
            MARCA_TOKS -> marca2="LOY_MRC_TOKS"
            MARCA_BEER_FACTORY -> marca2="LOY_MRC_BF"
            MARCA_EL_FAROLITO -> marca2="LOY_MRC_FAROLITO"
        }
        for(cupon in descuentos){
            if((cupon.restricciones.tipoCupon=="LOY_TDP_DESC_PORCENTAJE" || cupon.restricciones.tipoCupon=="LOY_TDP_DESC_PESOS") && cupon.marcaBeneficio==marca2)
                descuentosList.add(cupon)
        }
        return descuentosList
    }

    override fun setPreferences(
        sharedPreferences: SharedPreferences?,
        preferenceHelper: PreferenceHelper?
    ) {
        getSharedPreferences = sharedPreferences
        modelo.preferenceHelper = preferenceHelper
    }

    override fun setLogsInfo(preferenceHelperLogs: PreferenceHelperLogs?, files: Files?) {
        modelo.preferenceHelperLogs = preferenceHelperLogs
        filesWeakReference = WeakReference(files)
    }

    override fun executeConsultaAfiliado(numeroMiembro: String?) {
        model.executeConsultaAfiliadoRequest(numeroMiembro)
    }

    override fun getMarca(): String {
        return preferenceHelper!!.getString(ConstantsPreferences.PREF_MARCA_SELECCIONADA, null)
    }

    companion object modelo {
        const val TAG = "ConsultaAfiliadoRequest"
        var view: ConsultaAfiliadoMVP.View? = null
        var getSharedPreferences: SharedPreferences? = null
        var preferenceHelper: PreferenceHelper? = null
        var preferenceHelperLogs: PreferenceHelperLogs? = null
        var filesWeakReference: WeakReference<Files?>? = null
    }
}
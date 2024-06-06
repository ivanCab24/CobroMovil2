/*
 * *
 *  * Created by Gerardo Ruiz on 11/26/20 10:54 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 11/26/20 10:54 PM
 *
 */
package com.Presenter

import android.content.SharedPreferences
import com.Interfaces.ConsultaPagoMercadoPagoMVP
import com.Utilities.Files
import com.Utilities.PreferenceHelper
import com.Utilities.PreferenceHelperLogs
import java.lang.ref.WeakReference
import javax.inject.Inject

/**
 * Type: Class.
 * Access: Public.
 * Name: ConsultaPagoMercadoPagoRequest.
 *
 * @Description.
 * @EndDescription.
 */
class ConsultaPagoMercadoPagoPresenter @Inject constructor(
    private val model: ConsultaPagoMercadoPagoMVP.Model
) : ConsultaPagoMercadoPagoMVP.Presenter {
    override fun setView(getView: ConsultaPagoMercadoPagoMVP.View?) {
        view = getView
    }

    override fun doMercadoPagoRequest() {
        model.executeConsultaPagoRequest()
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

    companion object {
        const val TAG = "AplicaPagoRequest"
        var view: ConsultaPagoMercadoPagoMVP.View? = null
        private var getSharedPreferences: SharedPreferences? = null
        var preferenceHelper: PreferenceHelper? = null
        var preferenceHelperLogs: PreferenceHelperLogs? = null
        var filesWeakReference: WeakReference<Files?>? = null
    }
}
/*
 * *
 *  * Created by Gerardo Ruiz on 11/26/20 12:31 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 11/26/20 12:31 PM
 *
 */
package com.Presenter

import android.content.SharedPreferences
import com.Interfaces.LevantaPedidoMVP
import com.Utilities.Files
import com.Utilities.PreferenceHelper
import com.Utilities.PreferenceHelperLogs
import com.webservicestasks.ToksWebServicesConnection
import java.lang.ref.WeakReference
import javax.inject.Inject

/**
 * Type: Class.
 * Access: Public.
 * Name: LevantaPedidoRequest.
 *
 * @Description.
 * @EndDescription.
 */
class LevantaPedidoPresenter @Inject constructor(
    private val model: LevantaPedidoMVP.Model
): ToksWebServicesConnection, LevantaPedidoMVP.Presenter {
    override fun setView(getView: LevantaPedidoMVP.View?) {
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

    override fun executeLevantaPedido() {
        model.executeLevantaPedidoRequest()
    }

    companion object {
        const val TAG = "LevantaPedidoRequest"
        var view: LevantaPedidoMVP.View? = null
        var getSharedPreferences: SharedPreferences? = null
        var preferenceHelper: PreferenceHelper? = null
        var preferenceHelperLogs: PreferenceHelperLogs? = null
        var filesWeakReference: WeakReference<Files?>? = null
    }
}
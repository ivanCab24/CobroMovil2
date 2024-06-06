/*
 * *
 *  * Created by Gerardo Ruiz on 11/11/20 5:33 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 11/11/20 5:33 PM
 *
 */
package com.Presenter

import android.content.SharedPreferences
import com.Interfaces.AplicaDescuentoMVP
import com.Utilities.Files
import com.Utilities.PreferenceHelper
import com.Utilities.PreferenceHelperLogs
import com.View.Fragments.ContentFragment
import com.webservicestasks.ToksWebServicesConnection
import java.lang.ref.WeakReference
import javax.inject.Inject

/**
 * Type: Class.
 * Access: Public.
 * Name: AplicaDescuentoRequest.
 *
 * @Description.
 * @EndDescription.
 */
class AplicaDescuentoPresenter @Inject constructor(private var model: AplicaDescuentoMVP.Model): ToksWebServicesConnection, AplicaDescuentoMVP.Presenter {
    override fun setView(getView: AplicaDescuentoMVP.View?) {
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

    override fun setContentFragment(contentFragment: ContentFragment?) {
        getContentFragment = WeakReference(contentFragment)
    }

    override fun executeAplicaDescuentoRequest(referencia: String?) {
        model.executeAplicaDescuento(referencia)
    }
    companion object {
        const val TAG = "AplicaDescuentoRequest"
        var view: AplicaDescuentoMVP.View? = null
        var getSharedPreferences: SharedPreferences? = null
        var preferenceHelper: PreferenceHelper? = null
        var getContentFragment: WeakReference<ContentFragment?>? = null
        var preferenceHelperLogs: PreferenceHelperLogs? = null
        var filesWeakReference: WeakReference<Files?>? = null
    }
}
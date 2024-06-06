package com.Presenter

import android.content.SharedPreferences
import com.DataModel.Descuentos
import com.Interfaces.DescuentoFavoritoMVP
import com.Utilities.Files
import com.Utilities.PreferenceHelper
import com.Utilities.PreferenceHelperLogs
import com.webservicestasks.ToksWebServicesConnection
import java.lang.ref.WeakReference
import javax.inject.Inject

/**
 * Type: Class.
 * Access: Public.
 * Name: DescuentoFavoritoRequest.
 *
 * @Description.
 * @EndDescription.
 */
class DescuentoFavoritoPresenter @Inject constructor(
    private val model: DescuentoFavoritoMVP.Model
) : ToksWebServicesConnection, DescuentoFavoritoMVP.Presenter {
    override fun setView(getView: DescuentoFavoritoMVP.View?) {
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

    override fun executeDescuentoFavorito(descuentos: Descuentos?) {
        model.executeDescuentoFavoritoRequest(descuentos)
    }

    companion object {
        const val TAG = "DescuentoFavoritoReques"
        var view: DescuentoFavoritoMVP.View? = null
        var getSharedPreferences: SharedPreferences? = null
        var preferenceHelper: PreferenceHelper? = null
        var preferenceHelperLogs: PreferenceHelperLogs? = null
        var filesWeakReference: WeakReference<Files?>? = null
    }
}
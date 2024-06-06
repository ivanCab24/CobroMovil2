package com.Model

import com.Constants.ConstantsPreferences
import com.Interfaces.DialogErrorRespuestaMVP
import com.Utilities.PreferenceHelper
import dagger.Lazy
import javax.inject.Inject

class DialogErrorRespuestaModel @Inject constructor(private var presenter: Lazy<DialogErrorRespuestaMVP.Presenter>, private var preferenceHelper: PreferenceHelper) : DialogErrorRespuestaMVP.Model {

    override fun returnMarca(): String =
        preferenceHelper.getString(ConstantsPreferences.PREF_MARCA_SELECCIONADA, null)

    override fun returnImpresora(): String =
        preferenceHelper.getString(ConstantsPreferences.PREF_IMPRESORA, null)

    override fun removeCodBarras() {
        preferenceHelper.removePreference(ConstantsPreferences.PREF_CODBARRAS)
    }

    override fun returnPreferenceHelper(): PreferenceHelper = preferenceHelper

}
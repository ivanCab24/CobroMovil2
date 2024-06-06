package com.Model

import com.Constants.ConstantsPreferences
import com.Interfaces.PinpadE285MVP
import com.Utilities.PreferenceHelper
import dagger.Lazy
import javax.inject.Inject

class PinpadE285Model @Inject constructor(
    private var presenter: Lazy<PinpadE285MVP.Presenter>,
    private var preferenceHelper: PreferenceHelper,
) : PinpadE285MVP.Model {

    override fun getMarca(): String =
        preferenceHelper.getString(ConstantsPreferences.PREF_MARCA_SELECCIONADA, null)

    override fun guardarPinpadAddress(value: String) {
        preferenceHelper.putString(ConstantsPreferences.PREF_PINPAD, value)
    }

}
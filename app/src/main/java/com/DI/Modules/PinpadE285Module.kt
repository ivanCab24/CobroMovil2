package com.DI.Modules

import com.DI.Scopes.AppComponentScope
import com.Interfaces.PinpadE285MVP
import com.Model.PinpadE285Model
import com.Presenter.PinpadE285Presenter
import com.Utilities.PreferenceHelper
import dagger.Lazy
import dagger.Module
import dagger.Provides

@Module
class PinpadE285Module {

    @AppComponentScope
    @Provides
    fun providesPinpadE285MVP_Presenter(pinpadE285Model: PinpadE285Model): PinpadE285MVP.Presenter =
        PinpadE285Presenter(pinpadE285Model)

    @AppComponentScope
    @Provides
    fun providesPinpadE285MVP_Model(
        presenter: Lazy<PinpadE285MVP.Presenter>, preferenceHelper: PreferenceHelper
    ): PinpadE285MVP.Model = PinpadE285Model(presenter, preferenceHelper)

}
package com.DI.Modules

import com.DI.Scopes.AppComponentScope
import com.DI.Scopes.DialogImpresoraScope
import com.Interfaces.SeleccionImpresoraMVP
import com.Model.ImpresoraModel
import com.Presenter.ImpresoraPresenter
import com.Utilities.PreferenceHelper
import dagger.Lazy
import dagger.Module
import dagger.Provides

@Module
class DialogImpresoraModule {

    @AppComponentScope
    @Provides
    fun providesPresenter(
        model: SeleccionImpresoraMVP.Model
    ): SeleccionImpresoraMVP.Presenter =
        ImpresoraPresenter(model)

    @AppComponentScope
    @Provides
    fun providesModel(
        presenter: Lazy<SeleccionImpresoraMVP.Presenter>,
        preferenceHelper: PreferenceHelper
    ): SeleccionImpresoraMVP.Model = ImpresoraModel(presenter, preferenceHelper)

}
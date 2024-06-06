package com.DI.Modules

import com.DI.Scopes.AppComponentScope
import com.Interfaces.CorreoMVP
import com.Model.CorreoModel
import com.Presenter.CorreoPresenter
import com.Utilities.PreferenceHelper
import dagger.Lazy
import dagger.Module
import dagger.Provides

@Module
class DialogEnviaCorreoModule {
    @AppComponentScope
    @Provides
    fun providesPresenter(
        model: CorreoMVP.Model
    ): CorreoMVP.Presenter =
        CorreoPresenter(model)

    @AppComponentScope
    @Provides
    fun providesModel(
        presenter: Lazy<CorreoMVP.Presenter>,
        preferenceHelper: PreferenceHelper
    ): CorreoMVP.Model = CorreoModel(presenter, preferenceHelper)

}
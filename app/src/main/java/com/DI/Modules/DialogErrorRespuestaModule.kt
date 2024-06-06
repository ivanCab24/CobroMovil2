package com.DI.Modules

import com.DI.Scopes.DialogErrorScope
import com.Interfaces.DialogErrorRespuestaMVP
import com.Model.DialogErrorRespuestaModel
import com.Presenter.DialogErrorRespuestaPresenter
import com.Utilities.PreferenceHelper
import dagger.Lazy
import dagger.Module
import dagger.Provides

@Module
class DialogErrorRespuestaModule {

    @DialogErrorScope
    @Provides
    fun providesPresenter(
        model: DialogErrorRespuestaMVP.Model
    ): DialogErrorRespuestaMVP.Presenter =
        DialogErrorRespuestaPresenter(model)

    @DialogErrorScope
    @Provides
    fun providesModel(
        presenter: Lazy<DialogErrorRespuestaMVP.Presenter>,
        preferenceHelper: PreferenceHelper
    ): DialogErrorRespuestaMVP.Model = DialogErrorRespuestaModel(presenter, preferenceHelper)

}
package com.DI.Modules

import com.DI.Scopes.DialogDescuentosAplicadosScope
import com.Interfaces.DescuentosAplicadosMVP
import com.Model.DescuentosAplicadosModel
import com.Presenter.DescuentosAplicadosPresenter
import dagger.Module
import dagger.Provides

@Module
class DescuentosAplicadosModule {

    @Provides
    @DialogDescuentosAplicadosScope
    fun providesDescuentosAplicadosPresenter(model:DescuentosAplicadosMVP.Model): com.Interfaces.DescuentosAplicadosMVP.Presenter =
        DescuentosAplicadosPresenter(model)

    @Provides
    @DialogDescuentosAplicadosScope
    fun providesDescuentosAplicadosModel(): DescuentosAplicadosMVP.Model = DescuentosAplicadosModel()

}
package com.DI.Modules

import com.DI.Scopes.DescuentosMultiplesScope
import com.Interfaces.DescuentosMultiplesMVP
import com.Model.DescuentosMultiplesModel
import com.Presenter.DescuentosMultiplesPresenter
import dagger.Module
import dagger.Provides

@Module
class DescuentosMultiplesModule {

    @Provides
    @DescuentosMultiplesScope
    fun providesDescuentosMultiplesPresenter(model:DescuentosMultiplesMVP.Model): DescuentosMultiplesMVP.Presenter =
        DescuentosMultiplesPresenter(model)

    @Provides
    @DescuentosMultiplesScope
    fun providesDescuentosMultiplesModel(): DescuentosMultiplesMVP.Model = DescuentosMultiplesModel()
}
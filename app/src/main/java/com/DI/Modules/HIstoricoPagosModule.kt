/*
 * *
 *  * Created by Gerardo Ruiz on 12/18/20 1:43 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 12/18/20 1:43 PM
 *
 */
package com.DI.Modules

import com.DI.Scopes.HistoricoPagosScope
import com.Interfaces.getCuentaCerradaMVP
import com.Interfaces.getCuentaCobradaMVP
import com.Model.CuentaCerradaModel
import com.Model.CuentaCobradaModel
import com.Presenter.GetCuentaCerradaPresenter
import com.Presenter.GetCuentaCobradaPresenter
import dagger.Module
import dagger.Provides

@Module
class HIstoricoPagosModule {

    @Provides
    @HistoricoPagosScope
    fun providesGetCuentaCerradaPresenter(model:getCuentaCerradaMVP.Model): getCuentaCerradaMVP.Presenter =
        GetCuentaCerradaPresenter(model)

    @Provides
    @HistoricoPagosScope
    fun providesGetCuentaCerradaModel(): getCuentaCerradaMVP.Model =
        CuentaCerradaModel()

    @Provides
    @HistoricoPagosScope
    fun providesGetCuentaCobradaPresenter(model:getCuentaCobradaMVP.Model): getCuentaCobradaMVP.Presenter =
        GetCuentaCobradaPresenter(model)

    @Provides
    @HistoricoPagosScope
    fun providesGetCuentaCobradaModel(): getCuentaCobradaMVP.Model =
        CuentaCobradaModel()

}
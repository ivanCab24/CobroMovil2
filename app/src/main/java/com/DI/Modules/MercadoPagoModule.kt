/*
 * *
 *  * Created by Gerardo Ruiz on 11/26/20 12:51 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 11/26/20 12:51 PM
 *
 */
package com.DI.Modules

import com.Controller.ConsultaPagoMPModel
import com.DI.Scopes.MercadoPagoScope
import com.Interfaces.ConsultaPagoMercadoPagoMVP
import com.Interfaces.LevantaPedidoMVP
import com.Model.LevantaPedidoModel
import com.Presenter.ConsultaPagoMercadoPagoPresenter
import com.Presenter.LevantaPedidoPresenter
import dagger.Module
import dagger.Provides

@Module
class MercadoPagoModule {

    @MercadoPagoScope
    @Provides
    fun providesLevantaPedidoPresenter(model:LevantaPedidoMVP.Model): LevantaPedidoMVP.Presenter = LevantaPedidoPresenter(model)

    @Provides
    @MercadoPagoScope
    fun providesLevantaPedidoModel(
    ): LevantaPedidoMVP.Model = LevantaPedidoModel()


    @MercadoPagoScope
    @Provides
    fun providesConsultaPagoMercadoPagoPresenter(model: ConsultaPagoMercadoPagoMVP.Model): ConsultaPagoMercadoPagoMVP.Presenter =
        ConsultaPagoMercadoPagoPresenter(model)

    @Provides
    @MercadoPagoScope
    fun providesConsultaMPPagoModel(
    ): ConsultaPagoMercadoPagoMVP.Model = ConsultaPagoMPModel()

}
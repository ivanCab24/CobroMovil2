/*
 * *
 *  * Created by Gerardo Ruiz on 11/26/20 12:50 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 11/26/20 12:50 PM
 *
 */
package com.DI.Component

import com.DI.Modules.MercadoPagoModule
import com.DI.Scopes.MercadoPagoScope
import com.View.Dialogs.DialogMercadoPago
import com.View.Dialogs.DialogQRMercadoPago
import dagger.Subcomponent

@MercadoPagoScope
@Subcomponent(modules = [MercadoPagoModule::class])
interface MercadoPagoSubComponent {

    fun inject(dialogMercadoPago: DialogMercadoPago)
    fun inject(dialogQRMercadoPago: DialogQRMercadoPago)

}
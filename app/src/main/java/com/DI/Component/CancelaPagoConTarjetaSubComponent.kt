package com.DI.Component

import com.DI.Modules.CancelaPagoConTarjetaModule
import com.DI.Scopes.CancelaPagoConTarjetaScope
import com.View.Dialogs.DialogWaitingCancelaPagoTarjeta
import dagger.Subcomponent

@CancelaPagoConTarjetaScope
@Subcomponent(modules = [CancelaPagoConTarjetaModule::class])
interface CancelaPagoConTarjetaSubComponent {

    fun inject(dialogWaitingCancelaPagoTarjeta: DialogWaitingCancelaPagoTarjeta)

}
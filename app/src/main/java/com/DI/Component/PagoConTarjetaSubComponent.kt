package com.DI.Component

import com.DI.Modules.PagoConTarjetaModule
import com.DI.Scopes.PagoConTarjetaScope
import com.View.Dialogs.DialogWaitingPagoTarjeta
import dagger.Subcomponent

@PagoConTarjetaScope
@Subcomponent(modules = [PagoConTarjetaModule::class])
interface PagoConTarjetaSubComponent {

    fun inject(dialogWaitingPagoTarjeta: DialogWaitingPagoTarjeta)

}
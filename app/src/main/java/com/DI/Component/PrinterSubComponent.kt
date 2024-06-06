package com.DI.Component

import com.DI.Modules.PrinterModule
import com.DI.Scopes.PrinterMessageScope
import com.Presenter.DialogErrorRespuestaPresenter
import com.View.Dialogs.DialogBTPrinter
import com.View.Dialogs.DialogImprimeTicketVenta
import com.View.Dialogs.DialogImprimeVoucher
import com.View.Dialogs.DialogImprimirCopiaCliente
import com.View.Dialogs.DialogPrintiInfoEfectivo
import com.View.Dialogs.DialogWaitingPrint
import dagger.Subcomponent

@PrinterMessageScope
@Subcomponent(modules = [PrinterModule::class])
interface PrinterSubComponent {

    fun inject(dialogBTPrinter: DialogBTPrinter)
    fun inject(dialogImprimeTicketVenta: DialogImprimeTicketVenta)
    fun inject(dialogImprimirCopiaCliente: DialogImprimirCopiaCliente)
    fun inject(dialogImprimeVoucher: DialogImprimeVoucher)
    fun inject(dialogErrorRespuestaPresenter: DialogErrorRespuestaPresenter)
    fun inject(dialogPrintiInfoEfectivo: DialogPrintiInfoEfectivo)
    fun inject(dialogWaitingPrint: DialogWaitingPrint)


}
package com.DI.Modules

import android.app.Activity
import com.DI.Scopes.CancelaPagoConTarjetaScope
import com.Interfaces.*
import com.Model.*
import com.Presenter.*
import com.Utilities.Printer.printTicket
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class CancelaPagoConTarjetaModule(private val activity: Activity) {

    @Provides
    @CancelaPagoConTarjetaScope
    @Named("ImprimeTicket")
    fun providesPrinterTicketMessagePresenter(): PrinterMessagePresenter.Presenter =
        printTicket(activity)

    @Provides
    @CancelaPagoConTarjetaScope
    fun providesSolicitaRefoundPresenter(model:SolicitaRefoundMVP.Model): SolicitaRefoundMVP.Presenter =
        SolicitaRefoundPresenter(model)

    @Provides
    @CancelaPagoConTarjetaScope
    fun providesSolicitaRefoundModel(): SolicitaRefoundMVP.Model = SolicitaRefoundModel()

    @Provides
    @CancelaPagoConTarjetaScope
    fun providesCancelaVtolPresenter(model:CancelacionVtolMVP.Model): CancelacionVtolMVP.Presenter =
        CancelacionVtolPresenter(model)

    @Provides
    @CancelaPagoConTarjetaScope
    fun providesCancelaVtolModel(): CancelacionVtolMVP.Model = CancelacionVtolModel()

    @Provides
    @CancelaPagoConTarjetaScope
    fun providesC54CancelacionPresenter(model:C54CancelacionTaskMVP.Model): C54CancelacionTaskMVP.Presenter =
        C54CancelacionTaskPresenter(model)

    @Provides
    @CancelaPagoConTarjetaScope
    fun providesC54CancelacionModel(): C54CancelacionTaskMVP.Model = C54CanncelacionModel()

    @Provides
    @CancelaPagoConTarjetaScope
    fun providesCancelarPagoPresenter(model:CancelaPagoTarjetaMVP.Model): CancelaPagoTarjetaMVP.Presenter =
        CancelaPagoTarjetaPresenter(model)

    @Provides
    @CancelaPagoConTarjetaScope
    fun pprovidesCancelarPagoModel(): CancelaPagoTarjetaMVP.Model = CancelaPagoTarjetaModel()

    @Provides
    @CancelaPagoConTarjetaScope
    fun providesCancelacionTercerMensajePresenter(model:CancelacionTercerMensajeMVP.Model): CancelacionTercerMensajeMVP.Presenter =
        CancelacionTercerMensajePresenter(model)

    @Provides
    @CancelaPagoConTarjetaScope
    fun providesCancelacionTercerMensajeModel(): CancelacionTercerMensajeMVP.Model = CancelacionTercerMensajeModel()

}
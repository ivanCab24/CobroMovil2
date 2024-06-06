package com.DI.Modules

import android.app.Activity
import com.DI.Scopes.PagoConTarjetaScope
import com.Interfaces.*
import com.Model.*
import com.Presenter.C54TaskPresenter
import com.Presenter.InsertCuentaCobradaPresenter
import com.Presenter.SolicitaTarjetaPresenter
import com.Presenter.TercerMensajePresenter
import com.Utilities.Printer.printTicket
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class PagoConTarjetaModule(private val activity: Activity) {

    @Provides
    @PagoConTarjetaScope
    @Named("ImprimeTicket")
    fun providesPrinterTicketMessagePresenter(): PrinterMessagePresenter.Presenter =
        printTicket(activity)

    @Provides
    @PagoConTarjetaScope
    fun providesSolicitaTarjetaPresenter(model:SolicitaTarjetaMVP.Model): SolicitaTarjetaMVP.Presenter =
        SolicitaTarjetaPresenter(model)

    @Provides
    @PagoConTarjetaScope
    fun providesSolicitaTarjetaModel(
    ): SolicitaTarjetaMVP.Model = SolicitaTarjetaModel()

    @Provides
    @PagoConTarjetaScope
    fun providesSaleVtolPresenter(model:SaleVtolMVP.Model): SaleVtolMVP.Presenter =
        com.Presenter.SaleVtolModel(model)

    @Provides
    @PagoConTarjetaScope
    fun providesSaleVtolModel(
    ): SaleVtolMVP.Model = SaleVtolModel()

    @Provides
    @PagoConTarjetaScope
    fun providesC54TaskPresenter(model:C54TaskMPV.Model): C54TaskMPV.Presenter = C54TaskPresenter(model)

    @Provides
    @PagoConTarjetaScope
    fun providesC54TaskModel(
    ): C54TaskMPV.Model = C54Model()

    @Provides
    @PagoConTarjetaScope
    fun providesAplicaPagoTarjetaPresenter(model:AplicaPagoTarjetaPresenter.Model): AplicaPagoTarjetaPresenter.Presenter =
        com.Presenter.AplicaPagoTarjetaPresenter(model)

    @Provides
    @PagoConTarjetaScope
    fun providesAplicaPagoTarjetaModel(
    ): AplicaPagoTarjetaPresenter.Model = AplicaPagoTarjetaModel()

    @Provides
    @PagoConTarjetaScope
    fun providesTercerMensajePresenter(model:TercerMensajeMVP.Model): TercerMensajeMVP.Presenter = TercerMensajePresenter(model)

    @Provides
    @PagoConTarjetaScope
    fun providesTercerMensajeModel(
    ): TercerMensajeMVP.Model = TercerMensajeModel()

    @Provides
    @PagoConTarjetaScope
    fun providesCuentaCobradaPresenter(model:InsertaCuentaCobradaMVP.Model): InsertaCuentaCobradaMVP.Presenter =
        InsertCuentaCobradaPresenter(model)

    @Provides
    @PagoConTarjetaScope
    fun providesCuentaCobradaModel(
    ): InsertaCuentaCobradaMVP.Model = InsertCuentaCobradaModel()

}
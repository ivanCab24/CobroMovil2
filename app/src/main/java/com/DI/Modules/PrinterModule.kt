package com.DI.Modules

import android.app.Activity
import com.DI.Scopes.PrinterMessageScope
import com.Interfaces.ImprimeTicketMVP
import com.Interfaces.ImprimeVoucherMVP
import com.Interfaces.PrinterMessagePresenter
import com.Model.ImprimeTicketModel
import com.Model.ImprimeVoucherModel
import com.Presenter.ImprimeTicketPresenter
import com.Presenter.ImprimeVoucherPresenter
import com.Utilities.Printer.printHistorico
import com.Utilities.Printer.printTestMessage
import com.Utilities.Printer.printTicket
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class PrinterModule(private val activity: Activity) {

    @Provides
    @PrinterMessageScope
    @Named("ImprimeTest")
    fun providesPrinterMessagePresenter(): PrinterMessagePresenter.Presenter =
        printTestMessage(activity)



    @Provides
    @PrinterMessageScope
    @Named("ImprimeTicket")
    fun providesPrinterTicketMessagePresenter(): PrinterMessagePresenter.Presenter =
        printTicket(activity)


    @Provides
    @PrinterMessageScope
    @Named("ImprimeHistorico")
    fun providesPrinterHistoricoMessagePresenter(): PrinterMessagePresenter.Presenter =
        printHistorico(activity)


    @Provides
    @PrinterMessageScope
    fun providesImprimeTicketPresenter(model: ImprimeTicketMVP.Model): ImprimeTicketMVP.Presenter =
        ImprimeTicketPresenter(model)

    @Provides
    @PrinterMessageScope
    fun providesImprimeTicketModel(): ImprimeTicketMVP.Model = ImprimeTicketModel()


    @Provides
    @PrinterMessageScope
    fun providesImprimeVoucherPresenter(model: ImprimeVoucherMVP.Model): ImprimeVoucherMVP.Presenter =
        ImprimeVoucherPresenter(model)

    @Provides
    @PrinterMessageScope
    fun providesImprimeVoucherModel(): ImprimeVoucherMVP.Model = ImprimeVoucherModel()


}
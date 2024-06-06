package com.DI.Component

import android.content.SharedPreferences
import com.DI.Modules.AComerClubModule
import com.DI.Modules.ActividadPrincinpalModule
import com.DI.Modules.AppModule
import com.DI.Modules.CancelaPagoConTarjetaModule
import com.DI.Modules.ContentFragmentModule
import com.DI.Modules.DataBaseModule
import com.DI.Modules.DescuentosAplicadosModule
import com.DI.Modules.DescuentosMultiplesModule
import com.DI.Modules.DialogEnviaCorreoModule
import com.DI.Modules.DialogErrorRespuestaModule
import com.DI.Modules.DialogImpresoraModule
import com.DI.Modules.DownloadFileModule
import com.DI.Modules.HIstoricoPagosModule
import com.DI.Modules.LoginModule
import com.DI.Modules.MercadoPagoModule
import com.DI.Modules.NetModule
import com.DI.Modules.PagoConTarjetaModule
import com.DI.Modules.PinpadE285Module
import com.DI.Modules.PrinterModule
import com.DI.Modules.ResetKeysModule
import com.DI.Modules.SettingsModule
import com.DI.Modules.UploadFilesModule
import com.DI.Modules.WorkerFactoryModule
import com.DI.Scopes.AppComponentScope
import com.Utilities.Files
import com.View.Activities.ActividadPrincipal
import com.View.Activities.BixolonPrinterActivity
import com.View.Activities.LoginActivity
import com.View.Activities.PinpadE285BtActivity
import com.View.Dialogs.DialogCharge
import com.View.Dialogs.DialogEnviarCorreo
import com.View.Dialogs.DialogImpresora
import com.View.Dialogs.DialogInputReferencia
import com.View.Dialogs.DialogModelSelectionPrinter
import com.View.Dialogs.DialogPaymentMethods
import com.View.Dialogs.DialogSeleccionMarca
import com.View.Dialogs.DialogSettings
import com.View.Dialogs.DialogTokenInput
import com.View.Dialogs.DialogWaiting
import com.View.Pickers.DatePickerDialogFragment
import com.View.Pickers.TimePickerDialogFragment
import com.webservicestasks.workers.WorkerProviderFactory
import dagger.Component
import javax.inject.Named

@AppComponentScope
@Component(modules = [AppModule::class, LoginModule::class,DialogImpresoraModule::class, DialogEnviaCorreoModule::class, SettingsModule::class, NetModule::class, DataBaseModule::class, PinpadE285Module::class, WorkerFactoryModule::class, ActividadPrincinpalModule::class, ])
interface AppComponent {

    fun inject(actividadPrincipal: ActividadPrincipal)
    fun inject(dialogSettings: DialogSettings)
    fun inject(dialogCharge: DialogCharge)
    fun inject(dialogTokenInput: DialogTokenInput)
    fun inject(dialogInputReferencia: DialogInputReferencia)
    fun inject(dialogSeleccionMarca: DialogSeleccionMarca)
    fun inject(dialogWaiting: DialogWaiting)
    //fun inject(dialogBuscarAfiliado: DialogBuscarAfiliado)
    fun inject(dialogPaymentMethods: DialogPaymentMethods)
    fun inject(datePickerDialogFragment: DatePickerDialogFragment)
    fun inject(timePickerDialogFragment: TimePickerDialogFragment)
    fun inject(loginActivity: LoginActivity)
    fun inject(pinpadE285BtActivity: PinpadE285BtActivity)
    fun inject(bixolonPrinterActivity: BixolonPrinterActivity)
    fun inject(dialogSelectPrinterBinding: DialogModelSelectionPrinter)
    fun inject(dialogImpresora: DialogImpresora)

    fun inject(dialogEnviarCorreo: DialogEnviarCorreo)

    fun plusPrinterSubComponent(printerModule: PrinterModule): PrinterSubComponent
    fun plusCuentaSubComponent(cuentaModule: ContentFragmentModule): CuentaSubComponent
    fun plusDescuentosAplicadosSubComponent(descuentosAplicadosModule: DescuentosAplicadosModule): DescuentosAplicadosSubComponent
    fun plusAcomerClubSubComponent(aComerClubModule: AComerClubModule): AComerClubSubComponent
    fun plusDescuentosMultiplesSubComponent(descuentosMultiplesModule: DescuentosMultiplesModule): DescuentosMultiplesSubComponent
    fun plusPagoConTarjetaSubComponent(pagoConTarjetaModule: PagoConTarjetaModule): PagoConTarjetaSubComponent
    fun plusCancelaPagoConTarjetaSubComponent(cancelaPagoConTarjetaModule: CancelaPagoConTarjetaModule): CancelaPagoConTarjetaSubComponent
    fun plusUploadFilesSubComponent(uploadFilesModule: UploadFilesModule): UploadFilesSubComponent
    fun plusMercadoPagoSubComponent(mercadoPagoModule: MercadoPagoModule): MercadoPagoSubComponent
    fun plusHistoricoPagosSubComponent(hIstoricoPagosModule: HIstoricoPagosModule): HistoricoPagosSubcomponent
    fun plusResetKeysSubComponent(resetKeysModule: ResetKeysModule): ResetKeysSubComponent
    fun plusDownloadFileSubComponent(downloadFileModule: DownloadFileModule): DownloadFileSubComponent
    fun plusDialogErrorRespuestaSubComponent(dialogErrorRespuestaModule: DialogErrorRespuestaModule): DialogErrorRespuestaSubComponent

    val files: Files

    @get:Named("Preferencias")
    val sharedPreferences: SharedPreferences

    @get:Named("Logs")
    val sharedPreferencesLogs: SharedPreferences

    fun factory(): WorkerProviderFactory

}
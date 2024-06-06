package com.DI

import android.app.Activity
import android.app.Application
import androidx.work.Configuration
import androidx.work.WorkManager
import com.DI.Component.*
import com.DI.Modules.*
import com.View.Fragments.ContentFragment

class BaseApplication : Application() {

    lateinit var appComponent: AppComponent
    private var printerSubComponent: PrinterSubComponent? = null
    private var cuentaSubComponent: CuentaSubComponent? = null
    private var descuentosAplicadosSubComponent: DescuentosAplicadosSubComponent? = null
    private var aComerClubSubComponent: AComerClubSubComponent? = null
    private var descuentosMultiplesSubComponent: DescuentosMultiplesSubComponent? = null
    private var pagoConTarjetaSubComponent: PagoConTarjetaSubComponent? = null
    private var cancelaPagoConTarjetaSubComponent: CancelaPagoConTarjetaSubComponent? = null
    private var uploadFilesSubComponent: UploadFilesSubComponent? = null
    private var mercadoPagoSubComponent: MercadoPagoSubComponent? = null
    private var historicoPagosSubcomponent: HistoricoPagosSubcomponent? = null
    private var resetKeysSubComponent: ResetKeysSubComponent? = null
    private var downloadFileSubComponent: DownloadFileSubComponent? = null
    private var dialogErrorRespuestaSubComponent: DialogErrorRespuestaSubComponent? = null

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent
            .builder()
            .appModule(AppModule(this))
            .loginModule(LoginModule())
            .netModule(NetModule())
            .build()

        configureWorkManager()

    }

    private fun configureWorkManager() {
        val configuration: Configuration = Configuration.Builder()
            .setWorkerFactory(appComponent.factory())
            .build()
        WorkManager.initialize(this, configuration)

    }

    fun plusPrinterSubComponent(activity: Activity): PrinterSubComponent {
        printerSubComponent?.let { return it } ?: run {
            printerSubComponent = appComponent.plusPrinterSubComponent(PrinterModule(activity))
            return printerSubComponent!!
        }

    }

    fun plusCuentaSubComponent(contentFragment: ContentFragment): CuentaSubComponent {
        cuentaSubComponent?.let { return it } ?: run {
            cuentaSubComponent =
                appComponent.plusCuentaSubComponent(ContentFragmentModule(contentFragment))
            return cuentaSubComponent!!
        }

    }

    fun plusDescuentosAplicadosSubComponent(): DescuentosAplicadosSubComponent {
        descuentosAplicadosSubComponent?.let { return it } ?: run {
            descuentosAplicadosSubComponent =
                appComponent.plusDescuentosAplicadosSubComponent(DescuentosAplicadosModule())
            return descuentosAplicadosSubComponent!!
        }

    }

    fun plusAcomerClubSubComponent(): AComerClubSubComponent {
        aComerClubSubComponent?.let { return it } ?: run {
            aComerClubSubComponent = appComponent.plusAcomerClubSubComponent(AComerClubModule())
            return aComerClubSubComponent!!
        }

    }

    fun plusDescuentosMultiplesSubComponent(): DescuentosMultiplesSubComponent {
        descuentosMultiplesSubComponent?.let { return it } ?: run {
            descuentosMultiplesSubComponent =
                appComponent.plusDescuentosMultiplesSubComponent(DescuentosMultiplesModule())
            return descuentosMultiplesSubComponent!!
        }

    }

    fun plusPagoConTarjetaSubComponent(activity: Activity): PagoConTarjetaSubComponent {
        pagoConTarjetaSubComponent?.let { return it } ?: run {
            pagoConTarjetaSubComponent =
                appComponent.plusPagoConTarjetaSubComponent(PagoConTarjetaModule(activity))
            return pagoConTarjetaSubComponent!!
        }

    }

    fun plusCancelaPagoConTarjetaSubComponent(activity: Activity): CancelaPagoConTarjetaSubComponent {
        cancelaPagoConTarjetaSubComponent?.let { return it } ?: run {
            cancelaPagoConTarjetaSubComponent = appComponent.plusCancelaPagoConTarjetaSubComponent(
                CancelaPagoConTarjetaModule(activity)
            )
            return cancelaPagoConTarjetaSubComponent!!
        }

    }

    fun plusUploadFilesSubComponent(): UploadFilesSubComponent {
        uploadFilesSubComponent?.let { return it } ?: run {
            uploadFilesSubComponent = appComponent.plusUploadFilesSubComponent(UploadFilesModule())
            return uploadFilesSubComponent!!
        }
    }

    fun plusMercadoPagoSubComponent(): MercadoPagoSubComponent {
        mercadoPagoSubComponent?.let { return it } ?: run {
            mercadoPagoSubComponent = appComponent.plusMercadoPagoSubComponent(MercadoPagoModule())
            return mercadoPagoSubComponent!!
        }
    }

    fun plusGetHistoricoPagosSubcomponent(): HistoricoPagosSubcomponent {
        historicoPagosSubcomponent?.let { return it } ?: run {
            historicoPagosSubcomponent =
                appComponent.plusHistoricoPagosSubComponent(HIstoricoPagosModule())
            return historicoPagosSubcomponent!!
        }
    }

    fun plusResetKeysSubcomponent(): ResetKeysSubComponent {
        resetKeysSubComponent?.let { return it } ?: run {
            resetKeysSubComponent = appComponent.plusResetKeysSubComponent(ResetKeysModule())
            return resetKeysSubComponent!!
        }
    }

    fun plusDownloadFileSubComponent(): DownloadFileSubComponent {
        downloadFileSubComponent?.let { return it } ?: run {
            downloadFileSubComponent =
                appComponent.plusDownloadFileSubComponent(DownloadFileModule())
            return downloadFileSubComponent!!
        }
    }

    fun plusDialogErrorRespuestaSubComponent(): DialogErrorRespuestaSubComponent {
        dialogErrorRespuestaSubComponent?.let { return it } ?: run {
            dialogErrorRespuestaSubComponent =
                appComponent.plusDialogErrorRespuestaSubComponent(DialogErrorRespuestaModule())
            return dialogErrorRespuestaSubComponent!!
        }
    }



}
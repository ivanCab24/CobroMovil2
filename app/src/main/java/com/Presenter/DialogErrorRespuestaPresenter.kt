package com.Presenter

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.fragment.app.FragmentManager
import com.DI.BaseApplication
import com.Interfaces.DialogErrorRespuestaMVP
import com.Interfaces.PrinterMessagePresenter
import com.Utilities.Printer.PrinterControl.BixolonPrinter
import com.Utilities.Utils
import com.Verifone.VerifoneTaskManager
import com.bxl.config.editor.BXLConfigLoader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference
import javax.inject.Inject
import javax.inject.Named
import kotlin.coroutines.CoroutineContext

class DialogErrorRespuestaPresenter @Inject constructor(
    private var model: DialogErrorRespuestaMVP.Model) : DialogErrorRespuestaMVP.Presenter, CoroutineScope, PrinterMessagePresenter.View,
    BixolonPrinter.dialogErrorRespuesta {

    private var view: DialogErrorRespuestaMVP.View? = null
    private var activity: WeakReference<Activity>? = null
    private val job = Job()
    private var bxlPrinter: BixolonPrinter? = null
    private val fragmentManager: FragmentManager? = null

    @Named("ImprimeTicket")
    @Inject
    lateinit var printerMessagePresenter: PrinterMessagePresenter.Presenter

    @Inject
    lateinit var context: Context

    override fun setView(view: DialogErrorRespuestaMVP.View) {
        this.view = view
    }

    private fun initDIPrinter() {
        printerMessagePresenter.setView(this)
        printerMessagePresenter.setPreferences(model.returnPreferenceHelper())
    }

    override fun setActivity(activity: Activity?) {
        this.activity = WeakReference(activity)
        this.activity?.let {
            (activity!!.applicationContext as BaseApplication).plusPrinterSubComponent(
                activity
            ).inject(this)
        }
        initDIPrinter()
    }

    override fun printComprobante() {
        System.out.println("--->>>Entrando al metodo de mandar tiket a la impresora")
        if (model.returnImpresora().isNotEmpty()) {

            view?.let { view ->

                with(view) {
                    VerifoneTaskManager.limpiarTerminal()
                    updateContentFragmentUI()
                   // changeDialogMessage("Imprimiendo Comprobante")
                    updateDialogUI()

                }

                model.removeCodBarras()
                printerMessagePresenter.imprimirTest(null, Utils.imprimeTicketError())

            }

        } else {

            this.limpiarVenta()

            view?.let { view ->
                with(view) {

                    //onWarningResultPrinter("Impresora no configurada")
                    ocultarDialog()

                }
            }
        }
    }

    override fun printComprobanteBixolon() {
    System.out.println("--->Configurando la impresora Bixolon")
        val impresora = model.returnImpresora()

        bxlPrinter = BixolonPrinter(context, null, null, null, this)

        if (model.returnImpresora().isNotEmpty()) {

            view?.let { view ->
                VerifoneTaskManager.limpiarTerminal()
                with(view) {

                    updateContentFragmentUI()
                    changeDialogMessage("Imprimiendo Comprobante")
                    updateDialogUI()

                }

                model.removeCodBarras()

                val data = Utils.imprimeTicketError()

                var comprobante = StringBuilder()

                for (item in data) {
                    comprobante.append(item).append("\n")
                }
                comprobante.append("\n")
                comprobante.append("\n")

                launch {
                    bxlPrinter?.printerOpen(
                        BXLConfigLoader.DEVICE_BUS_BLUETOOTH,
                        "SPP-R200III",
                        impresora,
                        true
                    )

                    if(!bxlPrinter?.printText(
                            comprobante.toString(),
                            BixolonPrinter.ALIGNMENT_CENTER,
                            BixolonPrinter.ATTRIBUTE_FONT_B,
                            1
                        )!!
                    )view!!.ocultarDialog()


                }


            }

        } else {

            this.limpiarVenta()

            view?.let { view ->
                with(view) {

                    //onWarningResultPrinter("Impresora no configurada")
                    ocultarDialog()

                }
            }
        }


    }

    override fun getMarca(): String = model.returnMarca()

    override fun limpiarVenta() {
        launch { VerifoneTaskManager.limpiarVenta() }
    }

    //================================================================================
    // Inicia Presenter PrinterMessage
    //================================================================================
    override fun onExceptionResult(onExceptionResult: String?) {
        this.limpiarVenta()
        view?.let { view ->
            with(view) {
                onExceptionResultPrinter(onExceptionResult ?: "")
                ocultarDialog()
            }
        }
    }

    override fun onFailResult(onFailResult: String?) {
        this.limpiarVenta()
        view?.let { view ->
            with(view) {
                onFailResult(onFailResult ?: "")
                ocultarDialog()
            }
        }
    }

    override fun onWarningResult(onWarningResult: String?) {
        this.limpiarVenta()
        view?.let { view ->
            with(view) {
                onWarningResult(onWarningResult ?: "")
                ocultarDialog()
            }
        }
    }

    override fun onSuccessResult(onSuccessResult: String?) {

    }

    override fun onFinishedPrintJob() {
        view?.ocultarDialog()
        this.limpiarVenta()
    }

    //================================================================================
    // Inicia Bixolon Printer
    //================================================================================

    override fun onFinishedComprobanteBixolon() {
        Log.i("TAG", "onFinishedComprobanteBixolon: ")
        bxlPrinter?.printerClose()
        view?.ocultarDialog()
        this.limpiarVenta()
    }

    //================================================================================
    // Termina Bixolon Printer
    //================================================================================

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

}
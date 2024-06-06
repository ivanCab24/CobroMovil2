package com.Interfaces

import com.Controller.Adapters.PrinterAdapter.PrinterViewHolder
import com.DataModel.Printer

/**
 * Type: Interface.
 * Parent: PrinterSelectionListner.java.
 * Name: PrinterSelectionListner.
 */
interface PrinterSelectionListner {
    /**
     * Type: Method.
     * Parent: PrinterSelectionListner.
     * Name: getPrinterItem.
     *
     * @param printer @PsiType:Printer.
     * @Description.
     * @EndDescription.
     */
    fun getPrinterItem(printer: Printer?)

    /**
     * Type: Method.
     * Parent: PrinterSelectionListner.
     * Name: viewHolder.
     *
     * @param printerViewHolder @PsiType:PrinterViewHolder.
     * @Description.
     * @EndDescription.
     */
    fun viewHolder(printerViewHolder: PrinterViewHolder?)
}
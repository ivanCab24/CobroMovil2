package com.Interfaces

import com.DataModel.Pagos

/**
 * Type: Interface.
 * Parent: PagoSelectionListner.java.
 * Name: PagoSelectionListner.
 */
interface PagoSelectionListner {
    /**
     * Type: Method.
     * Parent: PagoSelectionListner.
     * Name: selectedPagoItem.
     *
     * @param pagos @PsiType:Pagos.
     * @Description.
     * @EndDescription.
     */
    fun selectedPagoItem(pagos: Pagos?)
}
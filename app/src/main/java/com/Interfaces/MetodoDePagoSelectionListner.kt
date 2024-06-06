package com.Interfaces

import com.DataModel.FormasDePago

/**
 * Type: Interface.
 * Parent: MetodoDePagoSelectionListner.java.
 * Name: MetodoDePagoSelectionListner.
 */
interface MetodoDePagoSelectionListner {
    /**
     * Type: Method.
     * Parent: MetodoDePagoSelectionListner.
     * Name: onSelectedItem.
     *
     * @param formasDePago @PsiType:FormasDePago.
     * @Description.
     * @EndDescription.
     */
    fun onSelectedItem(formasDePago: FormasDePago?)
}
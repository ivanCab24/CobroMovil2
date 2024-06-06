package com.Interfaces

import com.DataModel.Descuentos

/**
 * Type: Interface.
 * Parent: DescuentoSelectionListner.java.
 * Name: DescuentoSelectionListner.
 */
interface DescuentoSelectionListner {
    /**
     * Type: Method.
     * Parent: DescuentoSelectionListner.
     * Name: onSelectedItem.
     *
     * @param descuentos @PsiType:Descuentos.
     * @param bandera    @PsiType:String.
     * @Description.
     * @EndDescription.
     */
    fun onSelectedItem(descuentos: Descuentos?, bandera: String?)

    /**
     * Type: Method.
     * Parent: DescuentoSelectionListner.
     * Name: executeFavorito.
     *
     * @param descuentos @PsiType:Descuentos.
     * @Description.
     * @EndDescription.
     */
    fun executeFavorito(descuentos: Descuentos?)
}
package com.Interfaces

import com.DataModel.Miembro

/**
 * Type: Interface.
 * Parent: DescuentoSelectionListner.java.
 * Name: DescuentoSelectionListner.
 */
interface CuponSelectionListner {
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
    fun onSelectedItem(cupon: Miembro.Response.Cupones?, bandera: String?)

    /**
     * Type: Method.
     * Parent: DescuentoSelectionListner.
     * Name: executeFavorito.
     *
     * @param descuentos @PsiType:Descuentos.
     * @Description.
     * @EndDescription.
     */
    fun executeFavorito(cupon: Miembro.Response.Cupones?)
}
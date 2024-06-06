package com.Interfaces

import com.Controller.BD.Entity.CuentaCobrada

/**
 * Type: Interface.
 * Parent: SwipedRePrintCobroPosition.java.
 * Name: SwipedRePrintCobroPosition.
 */
interface SwipedRePrintCobroPosition {
    /**
     * Type: Method.
     * Parent: SwipedRePrintCobroPosition.
     * Name: onSwipedLeftCobroPosition.
     *
     * @param cuentaCobrada @PsiType:CuentaCobrada.
     * @Description.
     * @EndDescription.
     */
    fun onSwipedLeftCobroPosition(cuentaCobrada: CuentaCobrada?)
}
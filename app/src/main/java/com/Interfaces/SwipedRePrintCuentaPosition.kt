package com.Interfaces

import com.Controller.BD.Entity.CuentaCerrada

/**
 * Type: Interface.
 * Parent: SwipedRePrintCuentaPosition.java.
 * Name: SwipedRePrintCuentaPosition.
 */
interface SwipedRePrintCuentaPosition {
    /**
     * Type: Method.
     * Parent: SwipedRePrintCuentaPosition.
     * Name: onSwipedRightCuentaPosition.
     *
     * @param cuentaCerrada @PsiType:CuentaCerrada.
     * @Description.
     * @EndDescription.
     */
    fun onSwipedRightCuentaPosition(cuentaCerrada: CuentaCerrada?)

    /**
     * Type: Method.
     * Parent: SwipedRePrintCuentaPosition.
     * Name: onSwipedLeftCuentaPosition.
     *
     * @param cuentaCerrada @PsiType:CuentaCerrada.
     * @Description.
     * @EndDescription.
     */
    fun onSwipedLeftCuentaPosition(cuentaCerrada: CuentaCerrada?)
}
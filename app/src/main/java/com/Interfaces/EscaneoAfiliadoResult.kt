package com.Interfaces

import com.DataModel.Producto

/**
 * Type: Interface.
 * Parent: EscaneoAfiliadoResult.java.
 * Name: EscaneoAfiliadoResult.
 */
interface EscaneoAfiliadoResult {
    /**
     * Type: Method.
     * Parent: EscaneoAfiliadoResult.
     * Name: onExceptionResultAfiliado.
     *
     * @param onException @PsiType:String.
     * @Description.
     * @EndDescription.
     */
    fun onExceptionResultAfiliado(onException: String?)

    /**
     * Type: Method.
     * Parent: EscaneoAfiliadoResult.
     * Name: onFailResultAfiliado.
     *
     * @param onFail @PsiType:String.
     * @Description.
     * @EndDescription.
     */
    fun onFailResultAfiliado(onFail: String?)

    /**
     * Type: Method.
     * Parent: EscaneoAfiliadoResult.
     * Name: onResultAfiliado.
     *
     * @param numeroMiembro @PsiType:String.
     * @Description.
     * @EndDescription.
     */
    fun onResultAfiliado(numeroMiembro: String?)

    fun onResultCuponGRG(numeroCupon: String?)

    fun onResultCuponEngachement(numeroCupon: String?)

    fun onResultCuponEncuestas(numeroCupon:String?, producto:Producto)

    fun onResultCuponEncuestas20(numeroCupon:String?)

    fun onFailCupon()
}
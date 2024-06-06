/*
 * *
 *  * Created by Gerardo Ruiz on 12/15/20 5:41 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 12/15/20 5:41 PM
 *
 */
package com.Constants

object ConstantsAComer {

    const val MARCA_BEER = "BEER FACTORY"
    const val MARCA_TOKS = "TOKS"
    const val MARCA_FAROLITO = "FAROLITO"

    const val MEMBRESIA_SUBTIPO_INAPAM = "LOY_STM_INAPAM"
    const val MEMBRESIA_SUBTIPO_CORPORATIVA = "LOY_STM_CORPORATIVO"

    const val EVENTO_BENEFICIO_ANIVERSARIO = "LOY_EVNT_ANIV"

    const val ESTATUS_SUCCESS = "ORA_TXN_STAT_PROCESSED"
    const val SUB_ESTATUS_SUCCESS = "ORA_TXN_SUB_STAT_SUCCESS"
    const val SUB_ESTATUS_ERROR_TOPE_ACUMULAR_PUNTOS = "ORA_TXN_SUB_STAT_NO_PROMO_QF"

    const val ESTATUS_ERROR = "ORA_TXN_STAT_REJ"
    const val SUB_ESTATUS_ERROR_PUNTOS_INSUFICIENTES = "ORA_TXN_SUB_STAT_INSUF_MEM_BAL"

    const val MENSAJE_INSERTAR_TICKET_EXISTENTE = "unique constraint"
    const val MENSAJE_INSERTAR_TICKET_DATOS_INCORRECTOS = "literal does not match format string"

}
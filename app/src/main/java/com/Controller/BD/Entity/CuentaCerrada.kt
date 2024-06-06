/*
 * *
 *  * Created by Gerardo Ruiz on 12/17/20 3:36 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 12/17/20 3:36 PM
 *
 */
package com.Controller.BD.Entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.Constants.ConstantsBD

@Entity(tableName = ConstantsBD.TABLE_CUENTA_CERRADA)
class CuentaCerrada(

    @ColumnInfo(name = "DAY")
    var DAY: Int,

    @ColumnInfo(name = "MONTH")
    var MONTH: Int,

    @ColumnInfo(name = "YEAR")
    var YEAR: Int,

    @ColumnInfo(name = "HOUR")
    var HOUR: Int,

    @ColumnInfo(name = "MINUTES")
    var MINUTES: Int,

    @ColumnInfo(name = "SECONDS")
    var SECONDS: Int,

    @ColumnInfo(name = "ESTAFETA")
    var ESTAFETA: String,

    @ColumnInfo(name = "NOMBRE")
    var NOMBRE: String,

    @ColumnInfo(name = "FECHA")
    var FECHA: Int,

    @ColumnInfo(name = "ID_LOCAL")
    var ID_LOCAL: Int,

    @ColumnInfo(name = "ID_COMA")
    var ID_COMA: Int,

    @ColumnInfo(name = "ID_POS")
    var ID_POS: Int,

    @ColumnInfo(name = "SALDO")
    var SALDO: Double,

    @ColumnInfo(name = "M_TOTAL")
    var M_TOTAL: Double,

    @ColumnInfo(name = "M_IMPORTE")
    var M_IMPORTE: Double,

    @ColumnInfo(name = "M_DESC")
    var M_DESC: Double,

    @ColumnInfo(name = "DESC_TIPO")
    var DESC_TIPO: Int,

    @ColumnInfo(name = "DESC_ID")
    var DESC_ID: Int,

    @ColumnInfo(name = "REFERENCIA")
    var REFERENCIA: Int,

    @ColumnInfo(name = "CERRADA")
    var CERRADA: Int,

    @ColumnInfo(name = "PUEDECERRAR")
    var PUEDECERRAR: Int,

    @ColumnInfo(name = "FACTURADA")
    var FACTURADA: Int,

    @ColumnInfo(name = "IMPRIME")
    var IMPRIME: Int,

    @ColumnInfo(name = "FOLIOCTA")
    var FOLIOCTA: String,

    @ColumnInfo(name = "NUMCOMENSALES")
    var NUMCOMENSALES: Int,

    @ColumnInfo(name = "campo32")
    var campo32: String

) {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id = 0

    override fun toString(): String {
        return "CuentaCerrada(DAY=$DAY, MONTH=$MONTH, YEAR=$YEAR, HOUR=$HOUR, MINUTES=$MINUTES, SECONDS=$SECONDS, ESTAFETA='$ESTAFETA', NOMBRE='$NOMBRE', FECHA=$FECHA, ID_LOCAL=$ID_LOCAL, ID_COMA=$ID_COMA, ID_POS=$ID_POS, SALDO=$SALDO, M_TOTAL=$M_TOTAL, M_IMPORTE=$M_IMPORTE, M_DESC=$M_DESC, DESC_TIPO=$DESC_TIPO, DESC_ID=$DESC_ID, REFERENCIA=$REFERENCIA, CERRADA=$CERRADA, PUEDECERRAR=$PUEDECERRAR, FACTURADA=$FACTURADA, IMPRIME=$IMPRIME, FOLIOCTA='$FOLIOCTA', NUMCOMENSALES=$NUMCOMENSALES, campo32='$campo32', id=$id)"
    }


}
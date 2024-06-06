/*
 * *
 *  * Created by Gerardo Ruiz on 12/17/20 4:04 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 12/17/20 4:04 PM
 *
 */
package com.Controller.BD.Entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.Constants.ConstantsBD

@Entity(tableName = ConstantsBD.TABLE_CUENTA_COBRADA)
class CuentaCobrada(

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

    @ColumnInfo(name = "NOMBRE")
    var NOMBRE: String,

    @ColumnInfo(name = "CONSUMO")
    var CONSUMO: Double,

    @ColumnInfo(name = "PROPINA")
    var PROPINA: Double,

    @ColumnInfo(name = "ESTAFETA")
    var ESTAFETA: String,

    @ColumnInfo(name = "FOLIO")
    var FOLIO: String,

    @ColumnInfo(name = "campo32")
    var campo32: String,

    @ColumnInfo(name = "autorizacion")
    var autorizacion: String,

    @ColumnInfo(name = "MESA")
    var mesa: String,

    @ColumnInfo(name = "PAN")
    var pan: String

) {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id = 0

    override fun toString(): String {
        return "CuentaCobrada(DAY=$DAY, MONTH=$MONTH, YEAR=$YEAR, HOUR=$HOUR, MINUTES=$MINUTES, SECONDS=$SECONDS, NOMBRE='$NOMBRE', CONSUMO=$CONSUMO, PROPINA=$PROPINA, ESTAFETA='$ESTAFETA', FOLIO='$FOLIO', campo32='$campo32', autorizacion='$autorizacion', mesa='$mesa', pan='$pan', id=$id)"
    }


}
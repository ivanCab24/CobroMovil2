package com.Controller.BD.Entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.Constants.ConstantsBD

@Entity(tableName = ConstantsBD.TABLE_MFPGO)
data class MFPGO(

    @ColumnInfo(name = "TIPO_FPGO")
    var TIPO_FPGO: String?,

    @ColumnInfo(name = "ID_FPGO")
    var ID_FPGO: String?,

    @ColumnInfo(name = "FORMA_PAGO")
    var FORMA_PAGO: String?,

    @ColumnInfo(name = "DESCRIPCION")
    var DESCRIPCION: String?,

    @ColumnInfo(name = "CLAVE")
    var CLAVE: String?,

    @ColumnInfo(name = "FPROPINA")
    var FPROPINA: String?,

    @ColumnInfo(name = "FREFERENCIA")
    var FREFERENCIA: String?,

    @ColumnInfo(name = "FBANCO")
    var FBANCO: String?,

    @ColumnInfo(name = "COMISION")
    var COMISION: String?,

    @ColumnInfo(name = "TIPO_COMISION")
    var TIPO_COMISION: String?,

    @ColumnInfo(name = "FORMA_PAGO_SAT")
    var FORMA_PAGO_SAT: String?,

    @ColumnInfo(name = "FORMA_PAGO_SAT2")
    var FORMA_PAGO_SAT2: String?

) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0
}

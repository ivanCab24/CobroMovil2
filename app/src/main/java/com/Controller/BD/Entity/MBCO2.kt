package com.Controller.BD.Entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.Constants.ConstantsBD

@Entity(tableName = ConstantsBD.TABLE_MBCO2)
data class MBCO2(

    @ColumnInfo(name = "TC_PREFIJO")
    var TC_PREFIJO: Int = 0,

    @ColumnInfo(name = "TIPO_FPGO")
    var TIPO_FPGO: Int = 0,

    @ColumnInfo(name = "SA_IDFPGO")
    var SA_IDFPGO: Int = 0,

    @ColumnInfo(name = "TR_IDFPGO")
    var TR_IDFPGO: String?,

    @ColumnInfo(name = "TC_DEBITO")
    var TC_DEBITO: String?,

    @ColumnInfo(name = "FACTIVO")
    var FACTIVO: Int = 0,

    @ColumnInfo(name = "TC_TIPOCOM")
    var TC_TIPOCOM: Int = 0

) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0
}

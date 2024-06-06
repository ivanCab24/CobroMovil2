package com.Controller.BD.Entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.Constants.ConstantsBD

@Entity(tableName = ConstantsBD.TABLE_USER)
data class UserEntity(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "ESTAFETA")
    var ESTAFETA: Int?,

    @ColumnInfo(name = "PASSWORD")
    var PASSWORD: Int?,

    @ColumnInfo(name = "NOMBRE")
    var NOMBRE: String?,

    @ColumnInfo(name = "APP")
    var APP: Int?,

    @ColumnInfo(name = "INTFALLOS")
    var INTFALLOS: Int?,

    @ColumnInfo(name = "MONTH")
    var MONTH: Int

) {

    override fun toString(): String {
        return "UserEntity(ESTAFETA=$ESTAFETA, PASSWORD=$PASSWORD, NOMBRE=$NOMBRE, APP=$APP, INTFALLOS=$INTFALLOS, MONTH=$MONTH)"
    }

}

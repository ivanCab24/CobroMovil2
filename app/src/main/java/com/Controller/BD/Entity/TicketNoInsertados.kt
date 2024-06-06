package com.Controller.BD.Entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.Constants.ConstantsBD

@Entity(tableName = ConstantsBD.TABLE_TICKET_NO_INSERTADO)
data class TicketNoInsertados(

    @ColumnInfo(name = "ticket")
    var ticket: String,

    @ColumnInfo(name = "monto")
    var monto: Int,

    @ColumnInfo(name = "membresia")
    var membresia: String,

    @ColumnInfo(name = "marca")
    var marca: String,

    @ColumnInfo(name = "fechaConsumo")
    var fechaConsumo: String

) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0

}
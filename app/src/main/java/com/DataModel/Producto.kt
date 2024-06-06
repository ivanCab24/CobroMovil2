package com.DataModel

data class Producto(
    var id_pro: String,
    var producto: String,
    var precio_unit:Double,
    var porciones:Int,
    var m_total:Double,
    var desc_id:Int,
    var desc_tipo:Int,
    var desc_con:String,
    var id_ord:String,
    var id_sec:String
) {
    override fun toString(): String {
        return "Producto(id_pro=$id_pro, producto='$producto', precio_unit=$precio_unit, porciones=$porciones, m_total=$m_total, desc_id=$desc_id, desc_tipo=$desc_tipo, desc_con='$desc_con', id_ord='$id_ord')"
    }
}
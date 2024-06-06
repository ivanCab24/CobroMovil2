package com.DataModel

data class Caja(var idCaja: Int, var idTerm: Int, var responsable: String) {

    override fun toString(): String {
        return "Caja(idCaja=$idCaja, idTerm=$idTerm, responsable='$responsable')"
    }

}
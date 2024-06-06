package com.DataModel

data class Comensal(
    var numeroComensal: String,
    var isSelected: Boolean = false,
    var isReferenced: Boolean = false
) {

    override fun toString(): String {
        return "Comensal(numeroComensal='$numeroComensal', isSelected=$isSelected, isReferenced=$isReferenced)"
    }

}
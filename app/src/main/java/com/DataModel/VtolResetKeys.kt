package com.DataModel

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VtolResetKeys(
    var CODIGO: String,
    var CAMPO24: String,
    var CAMPO27: String,
    var CAMPO28: String,
    var CAMPO102: String
) {
    override fun toString(): String {
        return "VtolResetKeys(CODIGO='$CODIGO', CAMPO24='$CAMPO24', CAMPO27='$CAMPO27', CAMPO28='$CAMPO28', CAMPO102='$CAMPO102')"
    }
}
package com.DataModel

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class User(
    var CODIGO: String,
    var ESTAFETA: Int?,
    var NOMBRE: String?,
    var APP: Int?,
    var INTFALLOS: Int?
)

package com.DataModel

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CatalogoFpgo(

    var CODIGO: String?,
    var TIPO_FPGO: String?,
    var ID_FPGO: String?,
    var FORMA_PAGO: String?,
    var DESCRIPCION: String?,
    var CLAVE: String?,
    var FPROPINA: String?,
    var FREFERENCIA: String?,
    var FBANCO: String?,
    var COMISION: String?,
    var TIPO_COMISION: String?,
    var FORMA_PAGO_SAT: String?,
    var FORMA_PAGO_SAT2: String?

)

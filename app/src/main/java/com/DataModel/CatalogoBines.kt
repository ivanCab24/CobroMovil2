package com.DataModel

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CatalogoBines(
    var CODIGO: String?,
    var TC_PREFIJO: Long?,
    var TIPO_FPGO: Long?,
    var SA_IDFPGO: Long?,
    var TR_IDFPGO: Long?,
    var TC_DEBITO: String?,
    var FACTIVO: Long?,
    var TC_TIPOCOM: Long?
)

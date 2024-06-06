package com.DataModel

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MiembroException(
    var code: Int,
    var userError: String,
    var exceptionMessage: String,
    var success: Boolean,
    var response: ResponseException
) {

    @JsonClass(generateAdapter = true)
    data class ResponseException(
        var parametroBusqueda: String,
        var membresia: String,
        var cupones: List<Miembro.Response.Cupones>
    ) {
        override fun toString(): String {
            return "ResponseException(parametroBusqueda='$parametroBusqueda', membresia='$membresia', cupones=$cupones)"
        }
    }

    override fun toString(): String {
        return "MiembroException(code=$code, userError='$userError', exceptionMessage='$exceptionMessage', success=$success, response=$response)"
    }


}

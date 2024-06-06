package com.DataModel

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AcumulacionPuntosACC(
    var code: Int,
    var userError: String,
    var exceptionMessage: String,
    var success: Boolean,
    var response: Response
) {

    @JsonClass(generateAdapter = true)
    data class Response(
        var estatus: String,
        var subEstatus: String,
        var res: Res
    ) {
        @JsonClass(generateAdapter = true)
        data class Res(
            var numeroTicket: String,
            var numeroTransaccion: String,
            var puntos: Int,
            var puntosRegalados: Int,
            var idTransaccion: Long,
            var montoAplicado: Int,
            var puntosNoCalificados: Int,
            var puntosCalificados: Int,
            var nilvelActual: String,
            var siguienteNivel: String,
            var puntosSiguienteNivel: Int,
            var fechaAcumulacion: String
        )
    }

}

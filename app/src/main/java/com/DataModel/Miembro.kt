/*
 * *
 *  * Created by Gerardo Ruiz on 12/3/20 12:49 AM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 12/3/20 12:49 AM
 *
 */
package com.DataModel

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Miembro(
    var code: Int,
    var userError: String,
    var exceptionMessage: String,
    var success: Boolean,
    var response: Response
) {

    @JsonClass(generateAdapter = true)
    data class Response(
        var parametroBusqueda: String,
        var membresia: Membresia?,
        var cupones: List<Cupones>
    ) {

        @JsonClass(generateAdapter = true)
        data class Membresia(
            var numeroMembresia: String,
            var nombre: String,
            var email: String,
            var telefono: String?,
            var numeroTarjeta: String?,
            var subTipoMembresia: String,
            var estafeta: String?,
            var inapam: String?,
            var puntosCalificados: Int,
            var puntosNoCalificados: Int,
            var nivelActual: String,
            var siguienteNivel: String,
            var puntosSiguienteNivel: Int,
            var empleadorMembresiaCorp: String?
        ) {
            override fun toString(): String {
                return "Membresia(numeroMembresia='$numeroMembresia', nombre='$nombre', email='$email', telefono=$telefono, numeroTarjeta=$numeroTarjeta, subTipoMembresia='$subTipoMembresia', estafeta=$estafeta, inapam=$inapam, puntosCalificados=$puntosCalificados, puntosNoCalificados=$puntosNoCalificados, nivelActual='$nivelActual', siguienteNivel='$siguienteNivel', puntosSiguienteNivel=$puntosSiguienteNivel, empleadorMembresiaCorp=$empleadorMembresiaCorp)"
            }
        }

        @JsonClass(generateAdapter = true)
        data class Cupones(
            var numeroCupon: String,
            var codigoCupon: String,
            var descripcion: String,
            var puntos: Int,
            var marcaBeneficio: String,
            var tipoBeneficio: String,
            var eventoBeneficio: String,
            var fechaCreacion: String,
            var fechaExpiracion: String,
            var restricciones: Restricciones,
            var terminosCondiciones: String,
            var descuentoM:Double=0.0
        ) {
            @JsonClass(generateAdapter = true)
            data class Restricciones(
                var canalAplicacion: String,
                var tipoCupon: String,
                var subTipoCupon: String,
                var descuentoPorcentaje: Double,
                var descuentoMonto: Int,
                var precioPreferencial: Int,
                var compraMinima: Int,
                var cantidadProductos: Int,
                var combinarProductosPromocion: Boolean,
                var incluidoEnCompraMinima: Boolean,
                var aplicaAcumulacion: Boolean,
                var plusParticipantes: String
            ) {

                override fun toString(): String {
                    return "Restricciones(tipoCupon='$tipoCupon', subTipoCupon='$subTipoCupon', descuentoPorcentaje=$descuentoPorcentaje, descuentoMonto=$descuentoMonto, precioPreferencial=$precioPreferencial, compraMinima=$compraMinima, cantidadProductos=$cantidadProductos, combinarProductosPromocion=$combinarProductosPromocion, incluidoEnCompraMinima=$incluidoEnCompraMinima, aplicaAcumulacion=$aplicaAcumulacion, plusParticipantes='$plusParticipantes')"
                }

            }

            override fun toString(): String {
                return "Cupones(numeroCupon='$numeroCupon', codigoCupon='$codigoCupon', descripcion='$descripcion', puntos=$puntos, marcaBeneficio='$marcaBeneficio', tipoBeneficio='$tipoBeneficio', eventoBeneficio='$eventoBeneficio', fechaCreacion='$fechaCreacion', fechaExpiracion='$fechaExpiracion')"
            }

        }

    }

    override fun toString(): String {
        return "Miembro(code=$code, userError='$userError', exceptionMessage='$exceptionMessage', success=$success, response=$response)"
    }

}
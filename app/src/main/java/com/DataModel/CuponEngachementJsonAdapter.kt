package com.DataModel
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CuponEngachementJsonAdapter(
        var message: String,
        var cupon: Cupon
) {
    @JsonClass(generateAdapter = true)
    data class Cupon(
            var numeroCupon: String,
            var CODIGOCUPON: String,
            var TIPO: Int,
            var ID_DESC: Int,
            var DESCUENTO: String,
            var DESCUENTOM:Double=0.0,
            var DESCRIPCION: String,
            var VIGENCIA_INI: String,
            var VIGENCIA_FIN: String,
            var HORARIOS: List<Horario>,
            var DIAS: List<String>,
            var ID_SEC: List<String>,
            var CATEGORIA: Int,
            var ACUMULABLE: Boolean,
            var PORCIONES_MIN: Int,
            var IMPORTE_MIN: Int,
            var PORCENTAJE: Int,
            var MONTO: Int,
            var ACC: String,
            var TYC: String,
            var SUCURSALES: List<Sucursal>,
            var PLUS_PARTICIPANTES: List<Any>,
            var PLUS_EXCLUIDOS: List<Any>,
            var LINEAS_PARTICIPANTES: List<Any>,
            var LINEAS_EXCLUIDAS: List<Any>,
            var CANALES: List<Int>,
            var IMAGEURL: String
    ) {
        @JsonClass(generateAdapter = true)
        data class Horario(
                var HORA_INI: String,
                var HORA_FIN: String
        )

        @JsonClass(generateAdapter = true)
        data class Sucursal(
                var ID_SUCURSAL: Int,
                var MARCA: Int
        )

        override fun toString(): String {
            return "Cupon(numeroCupon='$numeroCupon', CODIGOCUPON='$CODIGOCUPON', TIPO=$TIPO, ID_DESC=$ID_DESC, DESCUENTO='$DESCUENTO', DESCRIPCION='$DESCRIPCION', VIGENCIA_INI='$VIGENCIA_INI', VIGENCIA_FIN='$VIGENCIA_FIN', HORARIOS=$HORARIOS, DIAS=$DIAS, ID_SEC=$ID_SEC, CATEGORIA=$CATEGORIA, ACUMULABLE=$ACUMULABLE, PORCIONES_MIN=$PORCIONES_MIN, IMPORTE_MIN=$IMPORTE_MIN, PORCENTAJE=$PORCENTAJE, MONTO=$MONTO, ACC='$ACC', TYC='$TYC', SUCURSALES=$SUCURSALES, PLUS_PARTICIPANTES=$PLUS_PARTICIPANTES, PLUS_EXCLUIDOS=$PLUS_EXCLUIDOS, LINEAS_PARTICIPANTES=$LINEAS_PARTICIPANTES, LINEAS_EXCLUIDAS=$LINEAS_EXCLUIDAS, CANALES=$CANALES, IMAGEURL='$IMAGEURL')"
        }
    }

    override fun toString(): String {
        return "CuponEngagementJsonAdapter(message='$message', cupon=$cupon)"
    }
}

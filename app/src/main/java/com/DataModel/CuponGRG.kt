package com.DataModel
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true) //datos de respuesta una vez que se lee un QR
data class CuponGRG(var code:Int,
                    var numeroCupon:String,
                    var marcaBeneficio:String,
                    var fechaExpiracion:String,
                    var subTipoCupon:String,
                    var descuentoPorcentaje:Double,
                    var descuentoMonto:Double,
                    var precioPreferencial:Double,
                    var compraMinima:Double,
                    var cantidadProductos:Int,
                    var plusParticipantes:String,
                    var tipo_desc:Int,
                    var id_desc:Int,
                    var descuentoM:Double=0.0,
                    var code_id:String
                    ) {
    override fun toString(): String {
        return "CuponGRG(numeroCupon='$numeroCupon', marcaBeneficio='$marcaBeneficio', fechaExpiracion='$fechaExpiracion', subTipoCupon='$subTipoCupon', descuentoPorcentaje=$descuentoPorcentaje, descuentoMonto=$descuentoMonto, precioPreferencial=$precioPreferencial, compraMinima=$compraMinima, cantidadProductos=$cantidadProductos, plusParticipantes='$plusParticipantes', tipo_desc=$tipo_desc, id_desc=$id_desc)"
    }
}




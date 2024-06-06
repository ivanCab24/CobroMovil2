package com.DataModel

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BusquedaTransaccionRSSN(
    var code: Int,
    var userError: String,
    var exceptionMessage: String,
    var success: Boolean,
    var response: List<Response>
) {
    @JsonClass(generateAdapter = true)
    data class Response(
        var memberNumber: String,
        var transactionNumber: String,
        var ticketNumber: String,
        var puntos: String,
        var tipo: String,
        var subtipo: String,
        var status: String,
        var substatus: String,
        var creationDate: String
    )
}

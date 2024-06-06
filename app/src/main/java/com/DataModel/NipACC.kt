package com.DataModel

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NipACC(
    val code: Long,
    val userError: String,
    val exceptionMessage: String,
    val success: Boolean,
    val response: Response?
) {
    @JsonClass(generateAdapter = true)
    data class Response(
        val nip: String,
        val fechaInicial: String,
        val fechaExpiracion: String
    )
}



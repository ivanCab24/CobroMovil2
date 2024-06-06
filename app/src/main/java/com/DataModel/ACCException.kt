package com.DataModel

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ACCException(
    val Code: Long,
    val UserError: String,
    val ExceptionMessage: String,
    val Success: Boolean,
    val Response: response?,
    val Message: message?
) {

    @JsonClass(generateAdapter = true)
    data class response(val value: String)

    @JsonClass(generateAdapter = true)
    data class message(
        val Content: String,
        val Title: String
    )

}

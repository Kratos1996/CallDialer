package com.artixtise.richdialer.data.call.model.uplodeImage

data class UplodeImageResponse(
    val Message: String,
    val Params: Params,
    val Response: String
)
data class Params(
    val image: String,
    val senderUserId: String
)
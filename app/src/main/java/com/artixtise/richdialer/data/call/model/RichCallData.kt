package com.artixtise.richdialer.data.call.model

data class RichCallData(
    var name: String ?=null,
    var email: String ?=null,
    var userId: String ?=null,
    var mobile: String ?=null,
    var emoji: Int = 0,
    var gif:String ?=null,
    var image:String ?=null,
    var text:String ?=null,
    var location:String ?=null,
    var type:String ?=null
)

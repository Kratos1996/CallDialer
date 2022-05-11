package com.artixtise.richdialer.data.call.model

data class RichCallData(
    var emoji: String ?=null,
    var image: String ?=null,
    var lat: String ?=null,
    var lng: String ?=null,
    var textMsg: String = "",
    var senderUserId:String ?=null,
    var senderName:String ?=null,
    var gif:String ?=null,
    var instagramId:String ?=null,
    var facebookId:String ?=null,
    var twitterId: String ?=null,
    var linkedID: String = "",
    var simNumber:String ?=null,
    var isRichcall:String ?=null,
    var receiverName:String ?=null,
    var receiverUserId:String ?=null,
    var receiverDeveiceId:String ?=null,
    var mobile:String ?=null
)
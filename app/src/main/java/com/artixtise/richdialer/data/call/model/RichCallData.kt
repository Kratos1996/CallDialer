package com.artixtise.richdialer.data.call.model

data class RichCallData(
    var emoji: String ?="",
    var image: String ?="",
    var lat: String ?="",
    var lng: String ?="",
    var textMsg: String = "",
    var senderUserId:String ?="",
    var senderName:String ?="",
    var gif:String ?="",
    var instagramId:String ?="",
    var facebookId:String ?="",
    var twitterId: String ?="",
    var linkedID: String = "",
    var simNumber:String ?="",
    var isRichcall:String ?="",
    var receiverName:String ?="",
    var receiverUserId:String ?="",
    var receiverDeveiceId:String ?="",
    var mobile:String ?=""
)
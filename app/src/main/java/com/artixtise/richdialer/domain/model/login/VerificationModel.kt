package com.artixtise.richdialer.domain.model.login

data class VerificationModel(
    var message:String?=null,
    var userId:String?=null,
    var isLoggedIn:Boolean=false
)

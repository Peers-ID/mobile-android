package com.android.id.peers.util.response

import com.android.id.peers.auth.models.Login

data class LoginResponse (
    val data:Login,
    val status:Int,
    val message:String
    )
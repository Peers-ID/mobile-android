package com.android.id.peers.auth.models

import com.google.gson.JsonObject

data class Login(
    val token:String,
    val user: User,
    val myRole: JsonObject
)

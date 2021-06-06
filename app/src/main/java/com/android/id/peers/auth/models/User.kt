package com.android.id.peers.auth.models

data class User(
    val id: Int,
    val koperasi_id: Int,
    val fullname: String,
    val phone_mobile: String,
    val birthdate: String,
    val email: String,
    val role: String,
    val ak_id: Int,
    val status: String
)


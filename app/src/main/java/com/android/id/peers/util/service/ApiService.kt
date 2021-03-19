package com.android.id.peers.util.service

import com.android.id.peers.util.response.ApiResponse
import com.android.id.peers.util.response.LoginResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @POST("login")
    fun login(@Body body: HashMap<String, String>): Call<LoginResponse>

    @GET("member_config/{id}")
    fun memberConfig(@Header("Authorization") auth: String,@Path("id") id : Int): Call<ApiResponse>

}
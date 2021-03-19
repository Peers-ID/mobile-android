package com.android.id.peers.util.service

import com.android.id.peers.util.response.ApiResponse
import com.android.id.peers.util.response.LoginResponse
import retrofit2.Call
import retrofit2.http.*

interface CollectionService {

    @GET(".")
    fun get(@Header("Authorization") auth: String): Call<ApiResponse>

    @POST("add")
    fun add(@Header("Authorization") auth: String,@Body body: HashMap<String, Any>): Call<ApiResponse>

    @POST("member")
    fun member(@Header("Authorization") auth: String,@Body body: HashMap<String, Any>): Call<ApiResponse>
}
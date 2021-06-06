package com.android.id.peers.util.service

import com.android.id.peers.util.response.ApiResponse
import com.android.id.peers.util.response.LoginResponse
import retrofit2.Call
import retrofit2.http.*

interface ProductService {

    @PUT("status")
    fun status(@Header("Authorization") auth: String,@Body body: HashMap<String, Any>): Call<ApiResponse>

    @GET("active")
    fun active(@Header("Authorization") auth: String): Call<ApiResponse>
    
}
package com.android.id.peers.util.service

import com.android.id.peers.util.response.ApiResponse
import com.android.id.peers.util.response.LoginResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface MemberService {

    @GET(".")
    fun get(@Header("Authorization") auth: String): Call<ApiResponse>

    @GET("nik/{nik}")
    fun getByNik(@Header("Authorization") auth: String,@Path("nik") nik : String): Call<ApiResponse>

    @POST(".")
    fun add(@Header("Authorization") auth: String,@Body body: HashMap<String, Any>): Call<ApiResponse>

    @POST("miscall")
    fun miscall(@Header("Authorization") auth: String,@Body body: HashMap<String, Any>): Call<ApiResponse>

    @Multipart
    @POST("picture")
    fun picture(@Header("Authorization") auth: String,@Part image : MultipartBody.Part): Call<ApiResponse>

    @PUT("{id}")
    fun update(@Header("Authorization") auth: String,@Path("id") id : Int,@Body body: HashMap<String, Any>): Call<ApiResponse>
    
}
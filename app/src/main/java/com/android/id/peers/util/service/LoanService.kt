package com.android.id.peers.util.service

import com.android.id.peers.util.response.ApiResponse
import com.android.id.peers.util.response.LoginResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface LoanService {

    @GET(".")
    fun get(@Header("Authorization") auth: String): Call<ApiResponse>

    @POST("add")
    fun add(@Header("Authorization") auth: String,@Body body: HashMap<String, Any>): Call<ApiResponse>

    @GET("detail/{id_loan}")
    fun detail(@Header("Authorization") auth: String,@Path("id_loan") id_loan: Int): Call<ApiResponse>

    @GET("status/{id_koperasi}/{id_ao}")
    fun status(@Header("Authorization") auth: String,@Path("id_koperasi") id_koperasi: Int,@Path("id_ao") id_ao: Int): Call<ApiResponse>

    @Multipart
    @POST("member/picture/{id}")
    fun picture(@Header("Authorization") auth: String,@Path("id") id: Int,@Part image : MultipartBody.Part): Call<ApiResponse>
    
}
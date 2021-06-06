package com.android.id.peers.util.repository

import com.android.id.peers.BuildConfig
import com.android.id.peers.util.service.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiRepository {
    fun create(): ApiService {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BuildConfig.BASE_URL)
            .build()
        return retrofit.create(ApiService::class.java)
    }
}
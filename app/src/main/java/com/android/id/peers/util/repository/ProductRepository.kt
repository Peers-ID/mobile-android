package com.android.id.peers.util.repository

import com.android.id.peers.util.service.ApiService
import com.android.id.peers.util.service.ProductService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ProductRepository {
    fun create(): ProductService {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("http://api.peers.id/api/v1/product/")
            .build()
        return retrofit.create(ProductService::class.java)
    }
}
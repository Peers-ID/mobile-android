package com.android.id.peers.util.repository

import com.android.id.peers.util.service.ApiService
import com.android.id.peers.util.service.LoanService
import com.android.id.peers.util.service.ProductService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object LoanRepository {
    fun create(): LoanService {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("http://api.peers.id/api/v1/loan/")
            .build()
        return retrofit.create(LoanService::class.java)
    }
}
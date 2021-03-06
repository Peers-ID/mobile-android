package com.android.id.peers.util.repository

import com.android.id.peers.BuildConfig
import com.android.id.peers.util.service.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object CollectionRepository {
    fun create(): CollectionService {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(
                BuildConfig.BASE_URL+"collection/")
            .build()
        return retrofit.create(CollectionService::class.java)
    }
}
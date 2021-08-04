package com.codexo.cryptopeak.network

import com.codexo.cryptopeak.network.Service.CoinCap.BASE_URL
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

interface Service {

    @GET("v2/assets")
    suspend fun getAssets(): Response<CoinDataContainer>

    companion object CoinCap {
        const val BASE_URL = "https://api.coincap.io/"
    }
}

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

object Network {
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(BASE_URL)
        .build()

    val coinCap: Service = retrofit.create(Service::class.java)
}
package com.codexo.cryptopeak.network

import com.codexo.cryptopeak.utils.BASE_URL
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface Service {

    @GET("v2/assets")
    suspend fun getAssets(): Response<CoinDataContainer>

    @GET("v2/assets/{id}/history?interval=d1")
    suspend fun getCoinHistory(@Path("id") id: String?): Response<CoinHistoryContainer>
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
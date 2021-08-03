package com.codexo.cryptopeak.network

import retrofit2.http.GET

interface CoinCapApi {

    @GET("v2/assets")
    suspend fun getAssets(): CoinData



    companion object CoinCap {
        const val BASE_URL = "https://api.coincap.io/"
    }
}


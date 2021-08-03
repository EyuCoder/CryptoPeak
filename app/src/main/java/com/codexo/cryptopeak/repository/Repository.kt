package com.codexo.cryptopeak.repository

import com.codexo.cryptopeak.network.CoinData
import com.codexo.cryptopeak.network.RetrofitInstance

class Repository {
    suspend fun getAssets(): CoinData {
        return RetrofitInstance.COIN_CAP_API.getAssets()
    }
}
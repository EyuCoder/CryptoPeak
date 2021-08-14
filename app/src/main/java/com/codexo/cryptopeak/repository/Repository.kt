package com.codexo.cryptopeak.repository

import androidx.lifecycle.LiveData
import com.codexo.cryptopeak.database.CoinData
import com.codexo.cryptopeak.database.CoinDatabase
import com.codexo.cryptopeak.database.CoinHistory
import com.codexo.cryptopeak.network.Network
import com.codexo.cryptopeak.network.asDatabaseModel
import com.codexo.cryptopeak.network.sorted
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Repository(private val database: CoinDatabase) {

    val coinData: LiveData<List<CoinData>> = database.dao.getAllCoin()
    val favoriteCoin: LiveData<List<CoinData>> = database.dao.getFavCoin()

    suspend fun refreshCoin() {
        withContext(Dispatchers.IO) {
            val coinData = Network.coinCap.getAssets()
            database.dao.insertAll(coinData.body()!!.asDatabaseModel())
        }
    }

    suspend fun getCoinHistory(id: String): List<CoinHistory> {
        return withContext(Dispatchers.IO) {
            Network.coinCap.getCoinHistory(id).body()!!.sorted()
        }
    }

    suspend fun markAsFavorite(flag: Boolean, id: String) {
        withContext(Dispatchers.IO) {
            database.dao.markAsFavorite(flag, id)
        }
    }
}
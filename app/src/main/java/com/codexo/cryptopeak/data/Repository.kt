package com.codexo.cryptopeak.data

import com.codexo.cryptopeak.data.database.CoinData
import com.codexo.cryptopeak.data.database.CoinDatabase
import com.codexo.cryptopeak.data.database.CoinHistory
import com.codexo.cryptopeak.data.network.Network
import com.codexo.cryptopeak.data.network.asDatabaseModel
import com.codexo.cryptopeak.data.network.sorted
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Repository(private val database: CoinDatabase) {

    val coinData = database.dao.getAllCoins()
    val favoriteCoins = database.dao.getFavCoins()
    val coinCount = database.dao.getCount()

    suspend fun getRandomCoin(): CoinData? {
        return database.dao.getRandomCoin()
    }

    fun getCoinDetail(id: String) = database.dao.getCoin(id)

    suspend fun refreshCoin() {
        withContext(Dispatchers.IO) {
            val coinData = Network.coinCap.getAssets()
            if (coinCount.value != null) {
                updateCoin()
            } else database.dao.insertAll(coinData.body()!!.asDatabaseModel())
        }
    }

    suspend fun updateCoin() {
        withContext(Dispatchers.IO) {
            val coinData = Network.coinCap.getAssets()

            for (coin in coinData.body()!!.asDatabaseModel()) {
                database.dao.updateCoin(
                    coin.id,
                    coin.rank,
                    coin.symbol,
                    coin.name,
                    coin.supply,
                    coin.maxSupply,
                    coin.marketCapUsd,
                    coin.volumeUsd24Hr,
                    coin.priceUsd,
                    coin.changePercent24Hr,
                    coin.vwap24Hr,
                    coin.explorer
                )
            }
        }
    }

    suspend fun addCoin() {
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

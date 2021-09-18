package com.codexo.cryptopeak.data

import com.codexo.cryptopeak.data.database.CoinDatabase
import com.codexo.cryptopeak.data.network.Network
import com.codexo.cryptopeak.data.network.asDatabaseModel
import com.codexo.cryptopeak.data.network.sortedByDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Repository(private val database: CoinDatabase) {

    val coinData = database.dao.getAllCoins()

    fun getCoinDetail(id: String) = database.dao.getCoin(id)

    suspend fun getCoinHistory(id: String) = withContext(Dispatchers.IO) {
        Network.coinCap.getCoinHistory(id).body()!!.sortedByDate()
    }

    suspend fun updateCoinData() {
        withContext(Dispatchers.IO) {
            val coinData = Network.coinCap.getAssets()
            for (coin in coinData.body()!!.asDatabaseModel()) {
                val changed = with(coin) {
                    database.dao.updateCoin(
                        id, rank, symbol, name, supply, maxSupply, marketCapUsd,
                        volumeUsd24Hr, priceUsd, changePercent24Hr, vwap24Hr, explorer
                    )
                }
                if (changed == 0)
                    database.dao.insertCoin(coin)
            }
        }
    }

    suspend fun markAsFavorite(flag: Boolean, id: String) {
        withContext(Dispatchers.IO) {
            database.dao.markAsFavorite(flag, id)
        }
    }

    suspend fun getRandomCoin(fave: Boolean = true) = withContext(Dispatchers.IO) {
        database.dao.getRandomCoin(fave)
    }
}

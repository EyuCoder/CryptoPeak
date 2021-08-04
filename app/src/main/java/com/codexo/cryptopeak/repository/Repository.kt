package com.codexo.cryptopeak.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.codexo.cryptopeak.database.CoinData
import com.codexo.cryptopeak.database.CoinDatabase
import com.codexo.cryptopeak.network.CoinDataContainer
import com.codexo.cryptopeak.network.Network
import com.codexo.cryptopeak.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Repository(private val database: CoinDatabase) {

    val coinData: LiveData<List<CoinData>> = database.dao.getAllCoin()

    suspend fun refreshCoin(){
        withContext(Dispatchers.IO){
            val coinData =Network.coinCap.getAssets()
            database.dao.insertAll(coinData.body()!!.asDatabaseModel())
        }
    }
}
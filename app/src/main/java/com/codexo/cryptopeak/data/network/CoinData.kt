package com.codexo.cryptopeak.data.network

import com.codexo.cryptopeak.data.database.CoinData
import com.codexo.cryptopeak.data.database.CoinHistory
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CoinDataContainer(val data: List<CoinData>, val timestamp: Long)

@JsonClass(generateAdapter = true)
data class CoinHistoryContainer(val data: List<CoinHistory>, val timestamp: Long)

fun CoinHistoryContainer.sorted(): List<CoinHistory> {
    return data.sortedByDescending { it.date }
}

fun CoinDataContainer.asDatabaseModel(): List<CoinData> {
    return data.map {
        CoinData(
            id = it.id,
            rank = it.rank,
            symbol = it.symbol,
            name = it.name,
            supply = it.supply,
            maxSupply = it.maxSupply,
            marketCapUsd = it.marketCapUsd,
            volumeUsd24Hr = it.volumeUsd24Hr,
            priceUsd = it.priceUsd,
            changePercent24Hr = it.changePercent24Hr,
            vwap24Hr = it.vwap24Hr,
            explorer = it.explorer,
            favorite = false
        )
    }
}

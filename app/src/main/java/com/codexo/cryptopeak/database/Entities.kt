package com.codexo.cryptopeak.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.codexo.cryptopeak.network.CoinDataContainer

@Entity(tableName = "coin_data")
data class CoinData(
    @PrimaryKey
    val id: String,
    val rank: Int,
    val symbol: String,
    val name: String,
    val supply: String?,
    val maxSupply: String?,
    val marketCapUsd: String?,
    val volumeUsd24Hr: String?,
    val priceUsd: String?,
    val changePercent24Hr: String?,
    val vwap24Hr: String?,
    val explorer: String?
)

fun CoinDataContainer.asDomainModel(): List<CoinData> {
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
            explorer = it.explorer
        )
    }
}

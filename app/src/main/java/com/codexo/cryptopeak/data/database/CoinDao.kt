package com.codexo.cryptopeak.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CoinDao {
    @Query("SELECT * FROM coin_data ORDER BY rank")
    fun getAllCoins(): LiveData<List<CoinData>>

    @Query("SELECT * FROM coin_data WHERE id = :id")
    fun getCoin(id: String): LiveData<CoinData>

    @Query("SELECT COUNT(*) FROM coin_data")
    fun getCount(): LiveData<Int>

    @Query("SELECT * FROM coin_data WHERE favorite = :fave")
    fun getFavCoins(fave: Boolean = true): LiveData<List<CoinData>>

    @Query("SELECT * FROM coin_data WHERE favorite = :fave ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandomCoin(fave: Boolean = true): CoinData?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(coinData: List<CoinData>)

    @Query("UPDATE coin_data SET favorite = :fave WHERE id = :id")
    suspend fun markAsFavorite(fave: Boolean, id: String)

    @Query(
        """
        UPDATE coin_data
        SET rank = :rank,
            symbol = :symbol,
            name = :name,
            supply = :supply,
            maxSupply = :maxSupply,
            marketCapUsd = :marketCapUsd,
            volumeUsd24Hr = :volumeUsd24Hr,
            priceUsd = :priceUsd,
            changePercent24Hr = :changePercent24Hr,
            vwap24Hr = :vwap24Hr,
            explorer = :explorer
        WHERE id = :id
        """
    )
    suspend fun updateCoin(
        id: String,
        rank: Int,
        symbol: String,
        name: String,
        supply: String?,
        maxSupply: String?,
        marketCapUsd: String?,
        volumeUsd24Hr: String?,
        priceUsd: String?,
        changePercent24Hr: String?,
        vwap24Hr: String?,
        explorer: String?
    )
}


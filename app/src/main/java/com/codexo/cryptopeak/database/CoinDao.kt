package com.codexo.cryptopeak.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CoinDao {
    @Query("SELECT * FROM coin_data ORDER BY rank")
    fun getAllCoins(): LiveData<List<CoinData>>

    @Query("SELECT * FROM coin_data WHERE id = :id")
    fun getCoin(id: String): LiveData<CoinData>

    @Query("SELECT * FROM coin_data WHERE favorite = :fave")
    fun getFavCoin(fave: Boolean = true): LiveData<List<CoinData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(coinData: List<CoinData>)

    @Update
    suspend fun updateAll(coinData: List<CoinData>)

    @Query("UPDATE coin_data SET favorite = :fave WHERE id = :id")
    suspend fun markAsFavorite(fave: Boolean, id: String)

}


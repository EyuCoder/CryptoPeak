package com.codexo.cryptopeak.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CoinDao {
    @Query("select * from coin_data order by rank")
    fun getAllCoin(): LiveData<List<CoinData>>

    @Query("select * from coin_data where favorite = :fave")
    fun getFavCoin(fave: Boolean = true): LiveData<List<CoinData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(coinData: List<CoinData>)

    @Query("UPDATE coin_data SET favorite = :fave WHERE id = :id")
    suspend fun markAsFavorite(fave: Boolean, id: String)

}


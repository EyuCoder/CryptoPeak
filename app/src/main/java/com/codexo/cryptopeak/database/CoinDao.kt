package com.codexo.cryptopeak.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CoinDao {
    @Query("select * from coin_data")
    fun getAllCoin(): LiveData<List<CoinData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(coinData: List<CoinData>)
}


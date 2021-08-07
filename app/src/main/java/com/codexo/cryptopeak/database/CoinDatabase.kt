package com.codexo.cryptopeak.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CoinData::class], version = 1, exportSchema = false)
abstract class CoinDatabase : RoomDatabase() {
    abstract val dao: CoinDao

    companion object {
        private lateinit var INSTANCE: CoinDatabase

        fun getDatabase(context: Context): CoinDatabase {

            synchronized(CoinDatabase::class.java) {
                if (!::INSTANCE.isInitialized) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        CoinDatabase::class.java,
                        "crypto_peak_db"
                    ).build()
                }
            }
            return INSTANCE
        }
    }
}




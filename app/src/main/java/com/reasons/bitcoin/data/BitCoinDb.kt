package com.reasons.bitcoin.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [TargetToAchieve::class,CoinRate::class], version = 5, exportSchema = false)
 abstract class BitCoinDb : RoomDatabase() {
    abstract fun target(): TargetDao
    abstract fun coinRate(): CoinRateDao

    companion object {
        @Volatile
        private var INSTANCE:BitCoinDb? = null
        fun getDatabase(context: Context): BitCoinDb {
            val tempInstance = INSTANCE
            if (tempInstance != null)
                return tempInstance
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BitCoinDb::class.java,
                    "BITCOIN_DB"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
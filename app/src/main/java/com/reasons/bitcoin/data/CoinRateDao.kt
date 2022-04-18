package com.reasons.bitcoin.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CoinRateDao {
    @Query("SELECT * FROM coin_rate")
     fun getCoinRate(): Flow<CoinRate>

    @Insert
    suspend fun insert(rate: CoinRate)

    @Update
    suspend fun update(rate: CoinRate)

    @Query("DELETE FROM coin_rate")
    suspend fun deleteAll()
}
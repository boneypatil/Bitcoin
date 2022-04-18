package com.reasons.bitcoin.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TargetDao {

    @Query("SELECT * FROM target")
    suspend fun getTarget(): TargetToAchieve

    @Insert
    suspend fun insert(target: TargetToAchieve)

    @Update
    suspend fun update(target: TargetToAchieve)

    @Query("DELETE FROM target")
    suspend fun deleteAll()
}
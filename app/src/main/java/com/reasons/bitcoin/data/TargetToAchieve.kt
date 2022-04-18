package com.reasons.bitcoin.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "target")
data class TargetToAchieve(
    val minRate: String,
    val maxRate: String,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
    )
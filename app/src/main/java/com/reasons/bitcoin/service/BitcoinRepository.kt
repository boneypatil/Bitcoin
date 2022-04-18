package com.reasons.bitcoin.service

import com.reasons.bitcoin.data.CoinRateDao
import com.reasons.bitcoin.data.TargetDao
import com.reasons.bitcoin.data.TargetToAchieve


class BitcoinRepository(private val targetDao: TargetDao, private val coinRateDao: CoinRateDao) {

    suspend fun getTarget() = targetDao.getTarget()
    fun getCoinRate() = coinRateDao.getCoinRate()

    suspend fun insertTarget(target: TargetToAchieve) {
        targetDao.insert(target)
    }

    suspend fun deleteTarget() {
        targetDao.deleteAll()
    }

}
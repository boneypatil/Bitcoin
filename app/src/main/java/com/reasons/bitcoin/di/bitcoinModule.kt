package com.reasons.bitcoin.di

import android.app.Application
import com.reasons.bitcoin.data.BitCoinDb
import com.reasons.bitcoin.data.CoinRateDao
import com.reasons.bitcoin.data.TargetDao
import com.reasons.bitcoin.service.BitcoinRepository
import com.reasons.bitcoin.viewmodel.MainViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val bitcoinModule = module {
    fun provideDataBase(application: Application): BitCoinDb {
        return BitCoinDb.getDatabase(application)
    }

    fun provideTargetDao(dataBase: BitCoinDb): TargetDao {
        return dataBase.target()
    }

    fun provideCoinRateDao(dataBase: BitCoinDb): CoinRateDao {
        return dataBase.coinRate()
    }
    single { provideDataBase(androidApplication()) }
    single { provideTargetDao(get()) }
    single { provideCoinRateDao(get()) }
    single { BitcoinRepository(get(), get()) }


}
val mainViewModel = module {
    viewModel {
        MainViewModel(get(), get())
    }
}
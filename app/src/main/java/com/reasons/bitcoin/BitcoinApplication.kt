package com.reasons.bitcoin

import android.app.Application
import com.reasons.bitcoin.di.bitcoinModule
import com.reasons.bitcoin.di.mainViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class BitcoinApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@BitcoinApplication)
            modules(
                listOf(
                    bitcoinModule,
                    mainViewModel
                )
            )
        }
    }
}
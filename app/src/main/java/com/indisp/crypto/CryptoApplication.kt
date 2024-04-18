package com.indisp.crypto

import android.app.Application
import com.indisp.crypto.coin.data.di.coinDiDataModule
import com.indisp.crypto.coin.di.coinDiModule
import com.indisp.network.networkDiModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class CryptoApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@CryptoApplication)
            modules(networkDiModule, coinDiModule, coinDiDataModule)
        }
    }
}
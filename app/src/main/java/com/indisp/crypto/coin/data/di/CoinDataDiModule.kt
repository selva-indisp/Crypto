package com.indisp.crypto.coin.data.di

import androidx.room.Room
import com.indisp.crypto.coin.data.CoinsRepositoryImpl
import com.indisp.crypto.coin.data.local.db.CoinDao
import com.indisp.crypto.coin.data.local.db.CoinDatabase
import com.indisp.crypto.coin.data.mapper.mapToCoin
import com.indisp.crypto.coin.data.mapper.mapToCoinEntity
import com.indisp.crypto.coin.data.remote.CoinApiService
import com.indisp.crypto.coin.data.remote.CoinApiServiceImpl
import com.indisp.crypto.coin.domain.repository.CoinsRepository
import org.koin.dsl.module

val coinDiDataModule = module {
    single<CoinDatabase> {
        Room.databaseBuilder(
            context = get(),
            CoinDatabase::class.java,
            "coin_database"
        ).build()
    }

    factory<CoinApiService> { CoinApiServiceImpl(get()) }
    factory<CoinDao> { get<CoinDatabase>().coinDao() }
    factory<CoinsRepository> { CoinsRepositoryImpl(get(), get(), ::mapToCoin, ::mapToCoinEntity) }
}
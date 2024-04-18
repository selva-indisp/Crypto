package com.indisp.network

import org.koin.dsl.module

val networkDiModule = module {
    single<NetworkApiService> { NetworkApiService(networkClient) }
}
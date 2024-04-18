package com.indisp.crypto.coin.data.remote

import com.indisp.core.Result
import com.indisp.network.NetworkApiService
import com.indisp.network.NetworkFailure

interface CoinApiService {
    suspend fun fetchCoinsList(): Result<List<CoinDto>, NetworkFailure>
}

class CoinApiServiceImpl(
    private val networkApiService: NetworkApiService
) : CoinApiService {

    private companion object {
        const val COINS_LIST_API = "https://37656be98b8f42ae8348e4da3ee3193f.api.mockbin.io/"
    }

    override suspend fun fetchCoinsList(): Result<List<CoinDto>, NetworkFailure> {
        return networkApiService.get(COINS_LIST_API)
    }
}
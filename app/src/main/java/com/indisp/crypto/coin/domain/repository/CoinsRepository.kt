package com.indisp.crypto.coin.domain.repository

import com.indisp.core.Result
import com.indisp.crypto.coin.domain.model.Coin
import com.indisp.crypto.coin.domain.model.CoinFilter
import com.indisp.crypto.coin.domain.usecase.FetchCoinsListError
import kotlinx.coroutines.flow.Flow

interface CoinsRepository {
    suspend fun fetchCoinsList(): Result<List<Coin>, FetchCoinsListError>
    suspend fun searchCoin(filters: Set<CoinFilter>): Flow<List<Coin>>
}
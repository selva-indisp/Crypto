package com.indisp.crypto.coin.domain.usecase

import android.util.Log
import com.indisp.crypto.coin.domain.model.Coin
import com.indisp.crypto.coin.domain.model.CoinFilter
import com.indisp.crypto.coin.domain.repository.CoinsRepository
import kotlinx.coroutines.flow.Flow

class SearchCoinsByFilterUseCase(
    private val coinsRepository: CoinsRepository
) {
    private companion object {
        const val TAG = "SearchCoinsByFilter"
    }
    suspend operator fun invoke(filters: Set<CoinFilter>): Flow<List<Coin>> {
        Log.d(TAG, "invoke: $filters")
        return coinsRepository.searchCoin(filters)
    }
}
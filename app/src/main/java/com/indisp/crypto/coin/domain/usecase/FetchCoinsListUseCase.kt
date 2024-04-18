package com.indisp.crypto.coin.domain.usecase

import com.indisp.core.Result
import com.indisp.crypto.coin.domain.model.Coin
import com.indisp.crypto.coin.domain.repository.CoinsRepository
import kotlinx.coroutines.flow.Flow

class FetchCoinsListUseCase(
    private val coinsRepository: CoinsRepository
) {
    suspend operator fun invoke(): Result<List<Coin>, FetchCoinsListError> {
        return coinsRepository.fetchCoinsList()
    }
}

sealed interface FetchCoinsListError {
    data class NetworkError(val cachedData: List<Coin>): FetchCoinsListError
}
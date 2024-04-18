package com.indisp.crypto.coin.data

import com.indisp.core.Result
import com.indisp.crypto.coin.data.local.db.CoinDao
import com.indisp.crypto.coin.data.local.db.CoinEntity
import com.indisp.crypto.coin.data.remote.CoinApiService
import com.indisp.crypto.coin.data.remote.CoinDto
import com.indisp.crypto.coin.domain.model.Coin
import com.indisp.crypto.coin.domain.model.CoinFilter
import com.indisp.crypto.coin.domain.repository.CoinsRepository
import com.indisp.crypto.coin.domain.usecase.FetchCoinsListError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CoinsRepositoryImpl(
    private val coinApi: CoinApiService,
    private val coinDao: CoinDao,
    private val entityToDomainMapper: (List<CoinEntity>) -> List<Coin>,
    private val dtoToEntityMapper: (List<CoinDto>) -> List<CoinEntity>
) : CoinsRepository {

    companion object {
        const val FILTER_TOKEN = "token"
        const val FILTER_COIN = "coin"
    }

    override suspend fun fetchCoinsList(): Result<List<Coin>, FetchCoinsListError> {
        return when (val apiResponse = coinApi.fetchCoinsList()) {
            is Result.Error -> {
                val coins = entityToDomainMapper(coinDao.getAllCoinsList())
                Result.Error(FetchCoinsListError.NetworkError(coins))
            }
            is Result.Success -> {
                val coinEntityList = dtoToEntityMapper(apiResponse.data)
                coinDao.insertAll(coinEntityList)
                val coins = entityToDomainMapper(coinDao.getAllCoinsList())
                Result.Success(coins)
            }
        }
    }

    override suspend fun searchCoin(filters: Set<CoinFilter>): Flow<List<Coin>> {
        val type = when {
            filters.contains(CoinFilter.Tokens) -> FILTER_TOKEN
            filters.contains(CoinFilter.Coins) -> FILTER_COIN
            else -> null
        }
        val query = (filters.find { it is CoinFilter.Search } as? CoinFilter.Search)?.query
        val isActive = if(filters.contains(CoinFilter.Active)) true else {
            if (filters.contains(CoinFilter.InActive))
                false
            else null
        }
        val isNew = if(filters.contains(CoinFilter.New)) true else null
        return coinDao.filter(
            isActive = isActive,
            isNew = isNew,
            type = type,
            name = query,
            symbol = query
        ).map { entityToDomainMapper(it) }
    }
}
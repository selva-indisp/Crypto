package com.indisp.crypto.coin.data

import com.indisp.core.Result
import com.indisp.crypto.coin.data.local.db.CoinDao
import com.indisp.crypto.coin.data.local.db.CoinEntity
import com.indisp.crypto.coin.data.mapper.mapToCoin
import com.indisp.crypto.coin.data.mapper.mapToCoinEntity
import com.indisp.crypto.coin.data.remote.CoinApiService
import com.indisp.crypto.coin.data.remote.CoinDto
import com.indisp.crypto.coin.domain.model.Coin
import com.indisp.crypto.coin.domain.model.CoinFilter
import com.indisp.crypto.coin.domain.repository.CoinsRepository
import com.indisp.crypto.coin.domain.usecase.FetchCoinsListError
import com.indisp.network.NetworkFailure
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.runs
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CoinRepositoryImplTest {
    @get:Rule
    val mockkRule = MockKRule(this)
    @MockK
    private lateinit var coinApi: CoinApiService
    @MockK
    private lateinit var coinDao: CoinDao
    @MockK
    private lateinit var entityToDomainMapper: (List<CoinEntity>) -> List<Coin>
    @MockK
    private lateinit var dtoToEntityMapper: (List<CoinDto>) -> List<CoinEntity>
    private lateinit var coinRepository: CoinsRepository

    @Before
    fun setup() {
        coinRepository = CoinsRepositoryImpl(
            coinApi = coinApi,
            coinDao = coinDao,
            entityToDomainMapper = entityToDomainMapper,
            dtoToEntityMapper = dtoToEntityMapper
        )
    }

    @Test
    fun `fetchCoinsList, should fetch coins list, cache to db and return the cache`() = runTest {
        val coinsDtoList = listOf(CoinDto("Coin1", "C1", true, true, "coin"))
        val coinsEntityList = listOf(CoinEntity("Coin1", "C1", true, true, "coin"))
        val coinsList = listOf(Coin("Coin1", "C1", Coin.Type.Coin, Coin.State.Active, true))

        every { dtoToEntityMapper.invoke(coinsDtoList) } returns coinsEntityList
        every { entityToDomainMapper.invoke(coinsEntityList) } returns coinsList
        coEvery { coinApi.fetchCoinsList() } returns Result.Success(coinsDtoList)
        coEvery { coinDao.insertAll(coinsEntityList) } just runs
        coEvery { coinDao.getAllCoinsList() } returns coinsEntityList

        val actualResult = coinRepository.fetchCoinsList()
        assertEquals(Result.Success(coinsList), actualResult)
    }

    @Test
    fun `fetchCoinsList, should fetch coins list from cache when device is offline`() = runTest {
        val coinsEntityList = listOf(CoinEntity("Coin1", "C1", true, true, "coin"))
        val coinsList = listOf(Coin("Coin1", "C1", Coin.Type.Coin, Coin.State.Active, true))

        every { entityToDomainMapper.invoke(coinsEntityList) } returns coinsList
        coEvery { coinApi.fetchCoinsList() } returns Result.Error(NetworkFailure.NoInternet)
        coEvery { coinDao.getAllCoinsList() } returns coinsEntityList

        val actualResult = coinRepository.fetchCoinsList()
        assertEquals(Result.Error(FetchCoinsListError.NetworkError(coinsList)), actualResult)
    }


    @Test
    fun `searchCoin, should fetch coins list from cache based on list of filters`() = runTest {
        val coinsEntityList = listOf(
            CoinEntity("Coin1", "C1", true, true, "coin"),
            CoinEntity("Coin2", "C2", true, true, "coin"),
        )
        val coinsList = listOf(
            Coin("Coin1", "C1", Coin.Type.Coin, Coin.State.Active, true),
            Coin("Coin2", "C2", Coin.Type.Coin, Coin.State.Active, true)
        )

        every { entityToDomainMapper.invoke(coinsEntityList) } returns coinsList
        coEvery { coinDao.filter(null, true, null, "coin", "coin") } returns flowOf(coinsEntityList)

        val actualResult = coinRepository.searchCoin(setOf(CoinFilter.New, CoinFilter.Search("coin"))).first()

        assertEquals(coinsList, actualResult)
    }
}
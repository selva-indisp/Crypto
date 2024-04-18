package com.indisp.crypto.coin.ui

import com.indisp.core.Result
import com.indisp.crypto.coin.TestDispatcher
import com.indisp.crypto.coin.domain.model.Coin
import com.indisp.crypto.coin.domain.usecase.FetchCoinsListError
import com.indisp.crypto.coin.domain.usecase.FetchCoinsListUseCase
import com.indisp.crypto.coin.ui.model.PresentableCoin
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CoinsListViewModelTest {
    @get:Rule
    val mockkRule = MockKRule(this)
    @MockK
    private lateinit var fetchCoinsListUseCase: FetchCoinsListUseCase
    @MockK
    private lateinit var coinMapper: (List<Coin>) -> PersistentList<PresentableCoin>
    private lateinit var coinsListViewModel: CoinsListViewModel

    @Before
    fun setup() {
        coinsListViewModel = CoinsListViewModel(
            fetchCoinsListUseCase = fetchCoinsListUseCase,
            coinMapper = coinMapper,
            dispatcherProvider = TestDispatcher
        )
    }

    @Test
    fun `OnScreenCreated event should fetch all coins from the repository`() = runTest {
        val coinsList = listOf(
            Coin("Coin1", "C1", Coin.Type.Coin, Coin.State.Active, true)
        )
        val presentableCoinsList = persistentListOf(
            PresentableCoin("Coin1", "C1", true, 1, true, coinsList[0])
        )

        every { coinMapper(coinsList) } returns presentableCoinsList
        coEvery { fetchCoinsListUseCase() } returns Result.Success(coinsList)
        coinsListViewModel.processEvent(CoinsListViewModel.Event.OnScreenCreated)
        val actualState = coinsListViewModel.screenStateFlow.value
        val expectedState = CoinsListViewModel.State(isLoading = false, coinsList = presentableCoinsList)
        assertEquals(expectedState, actualState)
    }

    @Test
    fun `OnScreenCreated event should fetch coins from cache and show error when device is offline`() = runTest {
        val coinsList = listOf(
            Coin("Coin1", "C1", Coin.Type.Coin, Coin.State.Active, true)
        )
        val presentableCoinsList = persistentListOf(
            PresentableCoin("Coin1", "C1", true, 1, true, coinsList[0])
        )

        every { coinMapper(coinsList) } returns presentableCoinsList
        coEvery { fetchCoinsListUseCase() } returns Result.Error(FetchCoinsListError.NetworkError(coinsList))
        coinsListViewModel.processEvent(CoinsListViewModel.Event.OnScreenCreated)
        val actualState = coinsListViewModel.screenStateFlow.value
        val sideEffect = coinsListViewModel.sideEffectFlow.value
        val expectedState = CoinsListViewModel.State(isLoading = false, coinsList = presentableCoinsList)
        assertEquals(expectedState, actualState)
        val expectedSideEffect = CoinsListViewModel.SideEffect.ShowError("Network error. Please try again!")
        assertEquals(expectedSideEffect, sideEffect)
    }

}
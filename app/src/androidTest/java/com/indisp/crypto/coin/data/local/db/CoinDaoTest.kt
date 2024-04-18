package com.indisp.crypto.coin.data.local.db

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*

@RunWith(AndroidJUnit4::class)
@SmallTest
class CoinDaoTest {
    private lateinit var database: CoinDatabase
    private lateinit var coinDao: CoinDao
    private companion object {
        val coin1 = CoinEntity(name = "Coin1", "C1", true, true, "coin")
        val coin2 = CoinEntity(name = "Coin2", "C2", false, false, "token")
        val coins = listOf(coin1, coin2)
    }

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            context = ApplicationProvider.getApplicationContext(),
            CoinDatabase::class.java
        ).build()
        coinDao = database.coinDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertAll() = runTest {
        coinDao.insertAll(coins)
        val actualCoinsList = coinDao.getAllCoinsList()
        assertEquals(coins, actualCoinsList)
    }

    @Test
    fun insertAll_should_overide_existing_items_when_conflict_occurs() = runTest {
        coinDao.insertAll(coins)
        val newCoins = listOf(
            CoinEntity(name = "Coin1", "C1", true, true, "coin"),
            CoinEntity(name = "Coin2", "C2", true, true, "token")
        )
        coinDao.insertAll(newCoins)

        val actualCoinsList = coinDao.getAllCoinsList()
        assertEquals(newCoins, actualCoinsList)
    }

    @Test
    fun filter_should_return_active_coins() = runTest {
        coinDao.insertAll(coins)
        val actualCoinsList = coinDao.filter(isActive = true).first()
        val expectedCoinsList = listOf(coin1)
        assertEquals(expectedCoinsList, actualCoinsList)
    }

    @Test
    fun filter_should_return_coins_containing_given_name_or_symbol() = runTest {
        coinDao.insertAll(coins)
        val actualCoinsList = coinDao.filter(name = "coin2", symbol = "coin2").first()
        val expectedCoinsList = listOf(coin2)
        assertEquals(expectedCoinsList, actualCoinsList)
    }

    @Test
    fun filter_should_return_empty_list_when_no_match_found() = runTest {
        coinDao.insertAll(coins)
        val actualCoinsList = coinDao.filter(name = "some_coin", symbol = "some_coin").first()
        assertEquals(emptyList<CoinEntity>(), actualCoinsList)
    }
}
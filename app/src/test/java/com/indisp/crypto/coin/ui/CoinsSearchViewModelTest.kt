package com.indisp.crypto.coin.ui

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import org.junit.Test

class CoinsSearchViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    @Test
    fun test1() {

    }

    @Test
    fun test2() {
        println("Start")
        runTest(context = testDispatcher) {
            val viewModel = CoinsSearchViewModel(this)
            println("Coroutine Start")
            viewModel.run()
            println("Coroutine End")
        }
        println("End")
    }
}

class CoinsSearchViewModel(
    val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
) {
    val flow1 = MutableStateFlow(0)
    val flow2 = MutableStateFlow(10)
    var dispatcher: CoroutineDispatcher = Dispatchers.IO

    fun run() {
        scope.launch {
            repeat(10) {
                println("${Thread.currentThread().name}  - $it")
                delay(1000)
            }
        }
    }
}
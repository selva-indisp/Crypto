package com.indisp.crypto.coin

import com.indisp.core.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineDispatcher

object TestDispatcher: DispatcherProvider {
    private val dispatcher = TestCoroutineDispatcher()
    override val Main: CoroutineDispatcher
        get() = dispatcher
    override val IO: CoroutineDispatcher
        get() = dispatcher
    override val Default: CoroutineDispatcher
        get() = dispatcher

}
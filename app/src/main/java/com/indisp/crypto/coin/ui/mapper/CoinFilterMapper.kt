package com.indisp.crypto.coin.ui.mapper

import com.indisp.crypto.coin.domain.model.CoinFilter
import com.indisp.crypto.coin.ui.model.PresentableCoinFilter
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList

fun mapToPresentableFilter(filters: List<CoinFilter>): PersistentList<PresentableCoinFilter> {
    return filters.map {
        when (it) {
            CoinFilter.Coins -> PresentableCoinFilter("Coins", it)
            CoinFilter.Tokens -> PresentableCoinFilter("Tokens", it)
            CoinFilter.Active -> PresentableCoinFilter("Active", it)
            CoinFilter.InActive -> PresentableCoinFilter("InActive", it)
            CoinFilter.New -> PresentableCoinFilter("New", it)
            is CoinFilter.Search -> PresentableCoinFilter(it.query, it)
        }
    }.toPersistentList()
}
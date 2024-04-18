package com.indisp.crypto.coin.domain.model

sealed interface CoinFilter {
    data class Search(val query: String): CoinFilter
    object Active: CoinFilter
    object InActive: CoinFilter
    object Coins: CoinFilter
    object Tokens: CoinFilter
    object New: CoinFilter
}
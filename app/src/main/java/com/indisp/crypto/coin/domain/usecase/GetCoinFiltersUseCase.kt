package com.indisp.crypto.coin.domain.usecase

import com.indisp.crypto.coin.domain.model.CoinFilter

class GetCoinFiltersUseCase {

    operator fun invoke(): List<CoinFilter> {
        return listOf(
            CoinFilter.New,
            CoinFilter.Active,
            CoinFilter.InActive,
            CoinFilter.Tokens,
            CoinFilter.Coins
        )
    }
}
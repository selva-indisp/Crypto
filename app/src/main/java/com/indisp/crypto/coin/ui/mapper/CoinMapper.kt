package com.indisp.crypto.coin.ui.mapper

import com.indisp.crypto.R
import com.indisp.crypto.coin.domain.model.Coin
import com.indisp.crypto.coin.ui.model.PresentableCoin
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList

fun mapToPresentableCoin(coins: List<Coin>): PersistentList<PresentableCoin> {
    return coins.map { coin ->
        PresentableCoin(
            name = coin.name,
            symbol = coin.symbol,
            isNew = coin.isNew,
            isActive = coin.state == Coin.State.Active,
            coin = coin,
            imageId = getImageId(coin)
        )
    }.toPersistentList()
}

private fun getImageId(coin: Coin): Int {
    return if (coin.type == Coin.Type.Token) {
        R.drawable.crypto_token
    } else {
        if (coin.state == Coin.State.Active) R.drawable.crypto_coin_active else R.drawable.cryto_coin_inactive
    }
}
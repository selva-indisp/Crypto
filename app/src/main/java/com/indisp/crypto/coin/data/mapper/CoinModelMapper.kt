package com.indisp.crypto.coin.data.mapper

import android.util.Log
import com.indisp.crypto.coin.data.local.db.CoinEntity
import com.indisp.crypto.coin.data.remote.CoinDto
import com.indisp.crypto.coin.domain.model.Coin

fun mapToCoinEntity(coinDtoList: List<CoinDto>): List<CoinEntity> {
    return coinDtoList.map {
        CoinEntity(
            name = it.name ?: "",
            symbol = it.symbol ?: "",
            type = it.type,
            isActive = it.isActive,
            isNew = it.isNew
        )
    }
}

fun mapToCoin(coinEntityList: List<CoinEntity>): List<Coin> {
    Log.d("CoinModelMapper", "mapToCoin: $coinEntityList")
    return coinEntityList.map {
        Coin(
            name = it.name ?: "",
            symbol = it.symbol ?: "",
            type = if (it.type == "coin") Coin.Type.Coin else Coin.Type.Token,
            state = if (it.isActive == true) Coin.State.Active else Coin.State.InActive,
            isNew = it.isNew ?: false
        )
    }
}
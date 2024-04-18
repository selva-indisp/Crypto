package com.indisp.crypto.coin.ui.model

import com.indisp.crypto.coin.domain.model.Coin

data class PresentableCoin(
    val name: String,
    val symbol: String,
    val isActive: Boolean,
    val imageId: Int,
    val isNew: Boolean,
    val coin: Coin
)
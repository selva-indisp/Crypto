package com.indisp.crypto.coin.ui.model

import com.indisp.crypto.coin.domain.model.CoinFilter

data class PresentableCoinFilter(val text: String, val filter: CoinFilter, val isSelected: Boolean = false,)
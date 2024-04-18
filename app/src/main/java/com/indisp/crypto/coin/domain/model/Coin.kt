package com.indisp.crypto.coin.domain.model

data class Coin(val name: String, val symbol: String, val type: Type, val state: State, val isNew: Boolean) {
    sealed class Type {
        data object Coin : Type()
        data object Token: Type()
    }

    sealed class State {
        data object Active: State()
        data object InActive: State()
    }
}

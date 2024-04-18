package com.indisp.crypto.coin.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CoinDto(
    val name: String?,
    val symbol: String?,
    @SerialName("is_new")
    val isNew: Boolean?,
    @SerialName("is_active")
    val isActive: Boolean?,
    val type: String?
)

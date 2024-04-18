package com.indisp.crypto.coin.data.local.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(primaryKeys = ["name", "symbol"])
data class CoinEntity(
    val name: String,
    val symbol: String,
    @ColumnInfo("is_new") val isNew: Boolean?,
    @ColumnInfo("is_active") val isActive: Boolean?,
    @ColumnInfo("type") val type: String?
)
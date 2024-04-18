package com.indisp.crypto.coin.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CoinDao {
    @Query("SELECT * FROM coinentity")
    suspend fun getAllCoinsList(): List<CoinEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(coinEntity: List<CoinEntity>)

    @Query(
        "SELECT * FROM coinentity WHERE " +
                "(:isActive IS NULL OR is_active = :isActive) AND" +
                "(:isNew IS NULL OR is_new = :isNew) AND" +
                "(:type IS NULL OR type = :type) AND" +
                "((:name IS NULL OR LOWER(name) LIKE '%' || LOWER(:name) || '%') OR (:symbol IS NULL OR LOWER(symbol) LIKE '%' || LOWER(:symbol) || '%'))"
    )
    fun filter(
        isActive: Boolean? = null,
        isNew: Boolean? = null,
        type: String? = null,
        name: String? = null,
        symbol: String? = null
    ): Flow<List<CoinEntity>>
}
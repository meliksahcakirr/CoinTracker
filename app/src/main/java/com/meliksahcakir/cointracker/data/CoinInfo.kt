package com.meliksahcakir.cointracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Locale

@Entity(tableName = "coinInfo")
data class CoinInfo(
    @PrimaryKey
    val id: String,
    val symbol: String,
    val name: String
) {
    override fun toString(): String {
        return "$name (${symbol.toUpperCase(Locale.getDefault())})"
    }
}

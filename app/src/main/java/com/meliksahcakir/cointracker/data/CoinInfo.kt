package com.meliksahcakir.cointracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "coinInfo")
data class CoinInfo(
    @PrimaryKey
    val id: String,
    val symbol: String,
    val name: String
)

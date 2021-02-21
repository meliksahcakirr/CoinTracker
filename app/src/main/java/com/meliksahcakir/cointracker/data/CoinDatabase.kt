package com.meliksahcakir.cointracker.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Coin::class, CoinDetails::class, CoinInfo::class],
    version = 1,
    exportSchema = false
)
abstract class CoinDatabase : RoomDatabase() {
    abstract fun coinDao(): CoinDao
    abstract fun coinDetailsDao(): CoinDetailsDao
}

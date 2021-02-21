package com.meliksahcakir.cointracker.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface CoinDao {

    @Query("SELECT * FROM coinInfo")
    fun getCoinInfoList(): List<CoinInfo>

    @Query("SELECT * FROM coinInfo WHERE name LIKE :q OR symbol LIKE :q ORDER BY name ASC")
    fun searchForCoinInfo(q: String): List<CoinInfo>

    @Query("SELECT * FROM coins")
    fun observeCoins(): LiveData<List<Coin>>

    @Query("SELECT * FROM coins WHERE id = :id")
    fun observeCoinById(id: String): LiveData<Coin>

    @Query("SELECT * FROM coins")
    suspend fun getCoins(): List<Coin>

    @Query("SELECT * FROM coins WHERE id = :id")
    suspend fun getCoinById(id: String): Coin?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCoin(coin: Coin)

    @Update
    suspend fun updateCoin(coin: Coin)

    @Delete
    suspend fun deleteCoin(coin: Coin)

    @Query("DELETE FROM coins WHERE id = :id")
    suspend fun deleteCoinById(id: String)
}

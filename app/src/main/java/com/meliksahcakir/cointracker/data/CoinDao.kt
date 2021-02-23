package com.meliksahcakir.cointracker.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface CoinDao {

    @Query("SELECT * FROM coinInfo")
    suspend fun getCoinInfoList(): List<CoinInfo>

    @Insert
    suspend fun insertAllCoinInfo(vararg coinInfo: CoinInfo)

    @Query("DELETE FROM coinInfo")
    suspend fun deleteAllCoinInfo()

    @Query(
        "SELECT * FROM coinInfo WHERE name LIKE '%' || :q || '%' OR " +
            "symbol LIKE '%' || :q || '%' ORDER BY name ASC"
    )
    suspend fun searchForCoinInfo(q: String): List<CoinInfo>

    @Query("SELECT * FROM coins")
    fun observeCoins(): LiveData<List<Coin>>

    @Query("SELECT * FROM coins")
    suspend fun getCoins(): List<Coin>

    @Query("SELECT * FROM coins WHERE id = :id")
    suspend fun getCoinById(id: String): Coin?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllCoins(vararg coins: Coin)

    @Update
    suspend fun updateCoin(coin: Coin)

    @Query("DELETE FROM coins WHERE id = :id")
    suspend fun deleteCoinById(id: String)
}

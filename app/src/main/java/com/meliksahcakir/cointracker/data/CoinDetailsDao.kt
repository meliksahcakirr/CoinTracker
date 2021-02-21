package com.meliksahcakir.cointracker.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface CoinDetailsDao {

    @Query("SELECT * FROM details")
    fun observeCoinDetails(): LiveData<List<CoinDetails>>

    @Query("SELECT * FROM details WHERE id = :id")
    fun observeCoinDetailsById(id: String): LiveData<CoinDetails>

    @Query("SELECT * FROM details")
    suspend fun getCoinDetails(): List<CoinDetails>

    @Query("SELECT * FROM details WHERE id = :id")
    suspend fun getCoinDetailsById(id: String): CoinDetails?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCoinDetails(coinDetails: CoinDetails)

    @Update
    suspend fun updateCoinDetails(coinDetails: CoinDetails)

    @Delete
    suspend fun deleteCoinDetails(coinDetails: CoinDetails)

    @Query("DELETE FROM details WHERE id = :id")
    suspend fun deleteCoinById(id: String)
}

package com.meliksahcakir.cointracker.data

import com.meliksahcakir.androidutils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CoinRepository(
    private val coinDao: CoinDao,
    private val coinDetailsDao: CoinDetailsDao,
    private val apiService: ApiService
) {
    private var coinInfoListAvailable = false
    private var fetchingCoins = false
    private var fetchingCoinInfoList = false

    suspend fun fetchCoinInfoList() = withContext(Dispatchers.IO) {
        if (fetchingCoinInfoList) {
            try {
                val list = apiService.getCoinInfoList()
                if (list.isNotEmpty()) {
                    coinDao.deleteAllCoinInfo()
                    coinDao.insertAllCoinInfo(*list.toTypedArray())
                    coinInfoListAvailable = true
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        fetchingCoinInfoList = true
    }

    suspend fun searchCoins(query: String): Result<List<CoinInfo>> {
        if (!coinInfoListAvailable) {
            fetchCoinInfoList()
        }
        return try {
            val list = coinDao.searchForCoinInfo(query)
            Result.Success(list)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    fun observeCoins() = coinDao.observeCoins()

    suspend fun fetchCoins(
        size: Int = ApiService.DEFAULT_PAGE_SIZE,
        page: Int = ApiService.DEFAULT_PAGE
    ): Result<Boolean> {
        if (fetchingCoins) return Result.Success(false)
        val result = try {
            val list = apiService.getCoinsMarketData(pageSize = size, page = page)
                .map { it.mapToLocalModel() }
            coinDao.insertAllCoins(*list.toTypedArray())
            Result.Success(list.isNotEmpty())
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error(e)
        }
        fetchingCoins = false
        return result
    }

    suspend fun fetchFavoriteCoins(favorites: List<String>): Result<List<Coin>> {
        return try {
            val list = apiService.getCoinsMarketData(ids = favorites.joinToString(","))
                .map { it.mapToLocalModel() }
            Result.Success(list)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun getCoinDetailsById(id: String): Result<CoinDetails> {
        var error = false
        var details: CoinDetails? = null
        try {
            details = apiService.getCoinDetails(id).mapToLocalModel()
        } catch (e: Exception) {
            error = true
        }
        if (error || details == null) {
            details = coinDetailsDao.getCoinDetailsById(id)
        } else {
            coinDetailsDao.insertCoinDetails(details)
        }
        return if (details != null) {
            Result.Success(details)
        } else {
            Result.Error(Exception("Details cannot be found"))
        }
    }
}

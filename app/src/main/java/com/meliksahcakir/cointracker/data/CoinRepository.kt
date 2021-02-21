package com.meliksahcakir.cointracker.data

import com.meliksahcakir.androidutils.Result

class CoinRepository(
    private val coinDao: CoinDao,
    private val coinDetailsDao: CoinDetailsDao,
    private val apiService: ApiService
) {
    private var coinInfoListAvailable = false

    suspend fun fetchCoinInfoList() {
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
    ) {
        try {
            val list = apiService.getCoinsMarketData(pageSize = size, page = page)
            coinDao.insertAllCoins(*list.toTypedArray())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun fetchFavoriteCoins(favorites: List<String>): Result<List<Coin>> {
        return try {
            val list = apiService.getCoinsMarketData(ids = favorites.joinToString(","))
            Result.Success(list)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun getCoinDetailsById(id: String): Result<CoinDetails> {
        var error = false
        var details: CoinDetails? = null
        try {
            details = apiService.getCoinDetails(id)
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

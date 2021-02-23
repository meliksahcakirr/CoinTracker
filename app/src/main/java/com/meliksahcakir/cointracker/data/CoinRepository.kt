package com.meliksahcakir.cointracker.data

import com.meliksahcakir.androidutils.Result

class CoinRepository(
    private val coinDao: CoinDao,
    private val coinDetailsDao: CoinDetailsDao,
    private val apiService: ApiService
) {
    private var coinInfoListAvailable = false
    private var fetchingCoins = false
    private var fetchingCoinInfoList = false

    suspend fun fetchCoinInfoList() {
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

    suspend fun getCoins(coinIdList: List<String>): Result<List<Coin>> {
        return try {
            val list = apiService.getCoinsMarketData(ids = coinIdList.joinToString(","))
                .map { it.mapToLocalModel() }
            Result.Success(list)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun getCoinDetailsById(id: String): Result<CoinDetails> {
        try {
            val details = apiService.getCoinDetails(id).mapToLocalModel()
            coinDetailsDao.insertCoinDetails(details)
            return Result.Success(details)
        } catch (e: Exception) {
            val details = coinDetailsDao.getCoinDetailsById(id)
                ?: return Result.Error(Exception("Details cannot be found"))
            return Result.Success(details)
        }
    }
}

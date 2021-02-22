package com.meliksahcakir.cointracker.data

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("coins/list")
    suspend fun getCoinInfoList(): List<CoinInfo>

    @GET("coins/markets?order=market_cap_desc")
    suspend fun getCoinsMarketData(
        @Query("vs_currency") currency: String = DEFAULT_CURRENCY,
        @Query("ids") ids: String = "",
        @Query("per_page") pageSize: Int = DEFAULT_PAGE_SIZE,
        @Query("page") page: Int = DEFAULT_PAGE
    ): List<CoinRemote>

    @GET(
        "coins/{id}?localization=false&tickers=false&market_data=false" +
            "&community_data=false&developer_data=false"
    )
    suspend fun getCoinDetails(
        @Path("id") id: String
    ): CoinDetailsRemote

    companion object {
        const val BASE_URL = "https://api.coingecko.com/api/v3/"
        const val DEFAULT_PAGE = 1
        const val DEFAULT_PAGE_SIZE = 50
        const val DEFAULT_CURRENCY = "usd"
    }
}

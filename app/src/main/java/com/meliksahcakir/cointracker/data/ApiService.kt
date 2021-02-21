package com.meliksahcakir.cointracker.data

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

enum class CoinOrders(val value: String) {
    MARKET_CAP_DESC("market_cap_desc"),
    MARKET_CAP_ASC("market_cap_asc"),
    VOLUME_DESC("volume_desc"),
    VOLUME_ASC("volume_asc"),
    ID_ASC("id_asc"),
    ID_DESC("id_desc")
}

interface ApiService {

    @GET("coins/list")
    suspend fun getCoinInfoList(): List<CoinInfo>

    @GET("coins/market")
    suspend fun getCoinsMarketData(
        @Query("vs_currency") currency: String = DEFAULT_CURRENCY,
        @Query("ids") ids: String = "",
        @Query("order") order: String = CoinOrders.MARKET_CAP_DESC.value,
        @Query("per_page") pageSize: Int = DEFAULT_PAGE_SIZE,
        @Query("page") page: Int = DEFAULT_PAGE
    ): List<Coin>

    @GET(
        "coins/{id}?localization=false&tickers=false&market_data=false" +
            "&community_data=false&developer_data=false"
    )
    suspend fun getCoinDetails(
        @Path("id") id: String
    ): CoinDetails

    companion object {
        const val BASE_URL = "https://api.coingecko.com/api/v3/"
        const val DEFAULT_PAGE = 1
        const val DEFAULT_PAGE_SIZE = 50
        const val DEFAULT_CURRENCY = "usd"
    }
}

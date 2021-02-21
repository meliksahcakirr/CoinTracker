package com.meliksahcakir.cointracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class CoinRemote(
    val id: String,
    val symbol: String,
    val name: String,
    val image: String,
    @SerializedName("current_price")
    val currentPrice: Double,
    @SerializedName("total_volume")
    val totalVolume: Long,
    @SerializedName("price_change_percentage_24h")
    val priceChangePercentage24h: Double,
) {

    fun mapToLocalModel(): Coin {
        return Coin(
            id,
            symbol,
            name,
            image,
            currentPrice,
            totalVolume,
            priceChangePercentage24h,
            false
        )
    }
}

@Entity(tableName = "coins")
data class Coin(
    @PrimaryKey
    val id: String,
    val symbol: String,
    val name: String,
    val image: String,
    val currentPrice: Double,
    val totalVolume: Long,
    val priceChangePercentage24h: Double,
    val favorite: Boolean = false
)

package com.meliksahcakir.cointracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class CoinDetailsRemote(
    val id: String,
    val symbol: String,
    val name: String,
    @SerializedName("hashing_algorithm")
    val hashingAlgorithm: String,
    val description: CoinDetailsDescriptionRemote,
    val image: CoinDetailsImagesRemote
) {
    fun mapToLocalModel(): CoinDetails {
        return CoinDetails(
            id,
            symbol,
            name,
            hashingAlgorithm,
            description.en,
            image.thumb,
            image.small,
            image.large
        )
    }
}

data class CoinDetailsDescriptionRemote(
    val en: String = ""
)

data class CoinDetailsImagesRemote(
    val thumb: String,
    val small: String,
    val large: String
)

@Entity(tableName = "details")
data class CoinDetails(
    @PrimaryKey
    val id: String,
    val symbol: String,
    val name: String,
    val hashingAlgorithm: String,
    val description: String = "",
    val thumb: String,
    val small: String,
    val large: String
)

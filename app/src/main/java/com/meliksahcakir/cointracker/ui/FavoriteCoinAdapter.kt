package com.meliksahcakir.cointracker.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.meliksahcakir.cointracker.R
import com.meliksahcakir.cointracker.data.Coin
import com.meliksahcakir.cointracker.databinding.FavoriteCoinItemBinding
import com.meliksahcakir.cointracker.utils.setImageUrl
import java.text.NumberFormat
import java.util.Locale

class FavoriteCoinAdapter(private val listener: FavoriteCoinListener) :
    ListAdapter<Coin, FavoriteCoinViewHolder>(CoinDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteCoinViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return FavoriteCoinViewHolder(
            FavoriteCoinItemBinding.inflate(inflater, parent, false),
            listener
        )
    }

    override fun onBindViewHolder(holder: FavoriteCoinViewHolder, position: Int) {
        val coin = getItem(position)
        holder.bind(coin)
    }
}

class FavoriteCoinViewHolder(
    private val binding: FavoriteCoinItemBinding,
    private val listener: FavoriteCoinListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(coin: Coin) {
        binding.coinImageView.setImageUrl(coin.image)
        binding.coinTextView.text = coin.symbol.toUpperCase(Locale.getDefault())
        val volumeText = "Vol ${NumberFormat.getInstance().format(coin.totalVolume)}"
        binding.volumeTextView.text = volumeText
        val lastPriceText = "$${NumberFormat.getInstance().format(coin.currentPrice)}"
        binding.lastPriceTextView.text = lastPriceText
        with(binding.percentageTextView) {
            val percentage = "${"%.2f".format(coin.priceChangePercentage24h)}%"
            text = percentage
            val colorResource = when {
                coin.priceChangePercentage24h > 0f -> R.color.green
                coin.priceChangePercentage24h < 0f -> R.color.redDark
                else -> R.color.grayLight
            }
            val color = ContextCompat.getColor(itemView.context, colorResource)
            setTextColor(color)
        }
        binding.favoriteImageView.setOnClickListener {
            listener.onFavoriteClicked(coin)
        }
    }
}

interface FavoriteCoinListener {
    fun onFavoriteClicked(coin: Coin)
}

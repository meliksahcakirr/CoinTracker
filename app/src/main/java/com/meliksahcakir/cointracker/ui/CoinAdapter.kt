package com.meliksahcakir.cointracker.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.meliksahcakir.cointracker.R
import com.meliksahcakir.cointracker.data.Coin
import com.meliksahcakir.cointracker.databinding.CoinItemBinding
import com.meliksahcakir.cointracker.utils.setImageUrl
import java.text.NumberFormat

object CoinDiffCallback : DiffUtil.ItemCallback<Coin>() {
    override fun areItemsTheSame(oldItem: Coin, newItem: Coin): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Coin, newItem: Coin): Boolean {
        return oldItem == newItem
    }
}

class CoinAdapter(private val listener: CoinListener) :
    ListAdapter<Coin, CoinViewHolder>(CoinDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return CoinViewHolder(CoinItemBinding.inflate(inflater, parent, false), listener)
    }

    override fun onBindViewHolder(holder: CoinViewHolder, position: Int) {
        val coin = getItem(position)
        holder.bind(coin)
    }
}

class CoinViewHolder(
    private val binding: CoinItemBinding,
    private val listener: CoinListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(coin: Coin) {
        binding.coinImageView.setImageUrl(coin.image)
        binding.coinTextView.text = coin.symbol
        val volumeText = "Vol ${NumberFormat.getInstance().format(coin.totalVolume)}"
        binding.volumeTextView.text = volumeText
        val lastPriceText = "$${NumberFormat.getInstance().format(coin.currentPrice)}"
        binding.lastPriceTextView.text = lastPriceText
        with(binding.percentageButton) {
            val percentage = "${"%.2f".format(coin.priceChangePercentage24h)}%"
            text = percentage
            when {
                coin.priceChangePercentage24h > 0f -> setStrokeColorResource(R.color.green)
                coin.priceChangePercentage24h < 0f -> setStrokeColorResource(R.color.redDark)
                else -> setStrokeColorResource(R.color.grayLight)
            }
        }
        binding.root.setOnClickListener {
            listener.onClicked(coin)
        }
    }
}

interface CoinListener {
    fun onClicked(coin: Coin)
}

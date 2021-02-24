package com.meliksahcakir.cointracker.ui

import androidx.recyclerview.widget.DiffUtil
import com.meliksahcakir.cointracker.data.Coin

object CoinDiffCallback : DiffUtil.ItemCallback<Coin>() {
    override fun areItemsTheSame(oldItem: Coin, newItem: Coin): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Coin, newItem: Coin): Boolean {
        return oldItem == newItem
    }
}

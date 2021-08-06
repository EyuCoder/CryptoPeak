package com.codexo.cryptopeak.adapters

import androidx.recyclerview.widget.DiffUtil
import com.codexo.cryptopeak.database.CoinData

object DiffCallback : DiffUtil.ItemCallback<CoinData>() {
    override fun areItemsTheSame(oldItem: CoinData, newItem: CoinData): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: CoinData, newItem: CoinData): Boolean {
        return oldItem == newItem

    }
}
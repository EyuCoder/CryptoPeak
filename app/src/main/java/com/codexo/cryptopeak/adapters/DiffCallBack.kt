package com.codexo.cryptopeak.adapters

import androidx.recyclerview.widget.DiffUtil
import com.codexo.cryptopeak.database.CoinData

class DiffCallBack: DiffUtil.ItemCallback<CoinData>() {
    override fun areItemsTheSame(oldItem: CoinData, newItem: CoinData): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: CoinData, newItem: CoinData): Boolean {
        return oldItem.id == newItem.id
    }
}

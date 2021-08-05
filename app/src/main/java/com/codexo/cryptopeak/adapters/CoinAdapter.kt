package com.codexo.cryptopeak.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.codexo.cryptopeak.database.CoinData
import com.codexo.cryptopeak.databinding.LayoutItemBinding

class CoinAdapter :
    ListAdapter<CoinData, CoinAdapter.CoinViewHolder>(DiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinViewHolder {
        return CoinViewHolder(LayoutItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: CoinViewHolder, position: Int) {
        val coinData = getItem(position)
        holder.bind(coinData)
    }

    inner class CoinViewHolder(private val binding: LayoutItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(coinData: CoinData) {
            binding.coinData = coinData
            binding.executePendingBindings()
        }
    }
}
package com.codexo.cryptopeak.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.codexo.cryptopeak.database.CoinData
import com.codexo.cryptopeak.databinding.LayoutItemBinding

class CoinAdapter :
    ListAdapter<CoinData, CoinAdapter.CoinViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinViewHolder {
        return CoinViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: CoinViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class CoinViewHolder(private val binding: LayoutItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(coinData: CoinData) {
            binding.coinData = coinData
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): CoinViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = LayoutItemBinding.inflate(layoutInflater, parent, false)
                return CoinViewHolder(binding)
            }
        }
    }
}
package com.codexo.cryptopeak.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.codexo.cryptopeak.data.database.CoinData
import com.codexo.cryptopeak.databinding.LayoutItemBinding

class CoinAdapter(private val listener: OnItemClickListener) :
    ListAdapter<CoinData, CoinAdapter.CoinViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinViewHolder {
        val binding = LayoutItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CoinViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CoinViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CoinViewHolder(private val binding: LayoutItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(coinData: CoinData) {
            binding.coinData = coinData
            binding.ivFavorite.setOnClickListener {
                listener.onFavoriteClicked(!coinData.favorite, coinData.id)
            }
            binding.itemCointraintLayout.setOnClickListener {
                listener.onOpenDetail(coinData)
            }
            binding.executePendingBindings()
        }
    }

    interface OnItemClickListener {
        fun onFavoriteClicked(markedFavorite: Boolean, id: String)
        fun onOpenDetail(currentItem: CoinData)
    }
}
package com.codexo.cryptopeak.adapters

import android.graphics.RectF
import com.codexo.cryptopeak.data.database.CoinHistory
import com.codexo.cryptopeak.utils.TimeInterval
import com.robinhood.spark.SparkAdapter

class CoinDetailAdapter(private val coinHistory: List<CoinHistory>) : SparkAdapter() {

    var timeInterval = TimeInterval.ALL

    override fun getCount() = coinHistory.size

    override fun getItem(index: Int) = coinHistory[index]

    override fun getY(index: Int): Float {
        val selectedCoin = coinHistory[index]
        return selectedCoin.priceUsd!!.toFloat()
    }

    override fun getDataBounds(): RectF {
        val bounds = super.getDataBounds()
        if (timeInterval != TimeInterval.ALL) {
            bounds.left = count - timeInterval.numDays.toFloat()
        }
        return bounds
    }
}
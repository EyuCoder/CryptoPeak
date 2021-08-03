package com.codexo.cryptopeak.network

import java.math.BigDecimal

data class CoinData(
    val id: String,
    val rank: Int,
    val symbol: String,
    val name: String,
    val supply: BigDecimal?,
    val maxSupply: BigDecimal?,
    val marketCapUsd: BigDecimal?,
    val volumeUsd24Hr: BigDecimal?,
    val priceUsd: BigDecimal?,
    val changePercent24Hr: BigDecimal?,
    val vwap24Hr: BigDecimal?,
    val explorer: String?
)

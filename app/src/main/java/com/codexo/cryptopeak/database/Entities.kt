package com.codexo.cryptopeak.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import com.codexo.cryptopeak.network.CoinDataContainer
import java.text.DateFormat
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

@Entity(tableName = "coin_data")
@Parcelize
data class CoinData(
    @PrimaryKey
    val id: String,
    val rank: Int,
    val symbol: String,
    val name: String,
    val supply: String?,
    val maxSupply: String?,
    val marketCapUsd: String?,
    val volumeUsd24Hr: String?,
    val priceUsd: String?,
    val changePercent24Hr: String?,
    val vwap24Hr: String?,
    val explorer: String?,
    var favorite: Boolean= false
) : Parcelable {
    val priceUsdFormatted: String
        get() = formatCurrency(priceUsd?.toDouble())

    val rankFormatted get() = rank.toString()

    val vwap24HrFormatted: String
        get() = formatCurrency(vwap24Hr?.toDouble())

    val volumeUsd24HrFormatted: String
        get() = formatCurrency(volumeUsd24Hr?.toDouble())

    val changePercent24HrFormatted
        get() = formatPercentage(changePercent24Hr?.toDouble())

    val marketCapUsdFormatted: String
        get() = formatCurrency(marketCapUsd?.toDouble())

    val supplyFormatted: String
        get() = formatNumber(supply?.toDouble())

    val nameFormatted
        get() = "$rank. $name($symbol)"
}

@Parcelize
data class CoinHistory(
    val priceUsd: String?,
    val time: String?,
    val date: String?,

    ) : Parcelable {
    val timeFormatted
        get() = formatTime(time)

    val dateFormatted: String
        get() = formatDate(date.toString())

    val priceFormatted: String
        get() = formatCurrency(priceUsd?.toDouble())
}


fun CoinDataContainer.asDomainModel(): List<CoinData> {
    return data.map {
        CoinData(
            id = it.id,
            rank = it.rank,
            symbol = it.symbol,
            name = it.name,
            supply = it.supply,
            maxSupply = it.maxSupply,
            marketCapUsd = it.marketCapUsd,
            volumeUsd24Hr = it.volumeUsd24Hr,
            priceUsd = it.priceUsd,
            changePercent24Hr = it.changePercent24Hr,
            vwap24Hr = it.vwap24Hr,
            explorer = it.explorer
        )
    }
}

private fun formatCurrency(currency: Double?): String {
    return NumberFormat
        .getCurrencyInstance(Locale("en", "US"))
        .format(currency)
        .toString()
}

private fun formatPercentage(percent: Double?): String {
    return DecimalFormat("#.##")
        .apply {
            maximumFractionDigits = 2
            isDecimalSeparatorAlwaysShown = true
        }
        .format(percent?.toBigDecimal())
        .toString() + "%"
}

private fun formatNumber(number: Double?): String {
    return NumberFormat
        .getNumberInstance(Locale.US)
        .format(number)
}

private fun formatDate(date: String): String {
    val outputFormat: DateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.US)
    val inputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)

    val dateParced = inputFormat.parse(date)
    return outputFormat.format(dateParced)
}

private fun formatTime(time: String?): String {
    return DateFormat.getDateTimeInstance().format(time)
}
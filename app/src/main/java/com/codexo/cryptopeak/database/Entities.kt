package com.codexo.cryptopeak.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import com.codexo.cryptopeak.network.CoinDataContainer
import java.text.DateFormat
import java.text.DecimalFormat
import java.text.NumberFormat
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
    var favorite: Boolean = false
) : Parcelable {
    val priceUsdFormatted: String
        get() = NumberFormat
            .getCurrencyInstance(Locale("en", "US"))
            .format(priceUsd?.toDouble())
            .toString()

    val rankFormatted
        get() = rank.toString()


    val changePercent24HrFormatted
        get() = DecimalFormat("#.##")
            .apply {
                maximumFractionDigits = 2
                isDecimalSeparatorAlwaysShown = true
            }
            .format(changePercent24Hr?.toBigDecimal())
            .toString() + "%"
}

@Parcelize
data class CoinHistory(
    val priceUsd: String?,
    val time: String?,
    val date: String?,

    ) : Parcelable {
    val timeFormatted
        get() = DateFormat.getDateTimeInstance().format(time)
}

//"priceUsd": "10250.7246875711059188",
//"time": 1565913600000,
//"date": "2019-08-16T00:00:00.000Z"

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

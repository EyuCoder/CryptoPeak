package com.codexo.cryptopeak.utils

const val BASE_URL = "https://api.coincap.io/"
const val ICON_URL = "https://assets.coincap.io/assets/icons/"

enum class NetworkStatus { LOADING, ERROR, DONE }

enum class TimeInterval(val numDays: Int) {
    WEEK(7),
    MONTH(30),
    YEAR(365),
    ALL(-1)
}

package com.codexo.cryptopeak.utils

// The name of liveUpate work
const val LIVE_UPDATE_WORK_NAME = "live_update_work"

const val BASE_URL = "https://api.coincap.io/"
const val ICON_URL = "https://assets.coincap.io/assets/icons/"

enum class NetworkStatus { LOADING, ERROR, DONE }

enum class TimeInterval(val numDays: Int) {
    WEEK(7),
    MONTH(30),
    YEAR(365),
    ALL(-1)
}

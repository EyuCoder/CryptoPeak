package com.codexo.cryptopeak.viewmodels

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.codexo.cryptopeak.data.Repository
import com.codexo.cryptopeak.data.database.CoinDatabase
import com.codexo.cryptopeak.utils.NetworkStatus
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private val TAG = MainViewModel::class.java.simpleName

class MainViewModel(
    private val database: CoinDatabase,
    private val repository: Repository
) : ViewModel() {

    private val _status = MutableLiveData<NetworkStatus>()
    val status get() = _status as LiveData<NetworkStatus>

    init {
        viewModelScope.launch {
            do {
                _status.value = NetworkStatus.LOADING
                try {
                    refresh(count)
                    _status.value = NetworkStatus.DONE
                    Log.d(TAG, "Random Coin: ${repository.getRandomCoin()?.name ?: "no fave coin"}")
                } catch (e: Exception) {
                    _status.value = NetworkStatus.ERROR
                    Log.d(TAG, e.message.toString())
                }
                delay(5000)
            } while (true)
        }
    }

    private suspend fun refresh(count: Int) {
        Log.d(TAG, "refresh: $count")
        if (count > 0) {
            repository.updateCoin()
        } else {
            repository.addCoin()
        }
    }

    val coinData = repository.coinData
    val favoriteCoins = repository.favoriteCoins
    val dataCount = repository.coinCount
    var count = 0

    fun markAsFavorite(flag: Boolean, id: String) {
        viewModelScope.launch {
            repository.markAsFavorite(flag, id)
            Log.d(TAG, "markAsFavorite: clicked")
        }
    }
}

class MainViewModelFactory(
    private val database: CoinDatabase,
    private val repository: Repository
) : ViewModelProvider.Factory {
    @RequiresApi(Build.VERSION_CODES.N)
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(database, repository) as T
    }
}

package com.codexo.cryptopeak.viewmodels

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.codexo.cryptopeak.data.database.CoinDatabase
import com.codexo.cryptopeak.data.Repository
import com.codexo.cryptopeak.utils.NetworkStatus
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private val TAG = MainViewModel::class.java.simpleName

class MainViewModel(
    private val database: CoinDatabase,
    private val repository: Repository
) : ViewModel() {

    private val _status = MutableLiveData<NetworkStatus>()
    val status: LiveData<NetworkStatus>
        get() = _status

    init {
        viewModelScope.launch {
            do {
                _status.value = NetworkStatus.LOADING
                try {
                    refresh(count)
                    Log.d(TAG, "REFRESHED")
                    _status.value = NetworkStatus.DONE
                    delay(5000)
                    Log.d(TAG, "Random Coin: ${repository.getRandomCoin().name}")
                } catch (e: Exception) {
                    Log.d(TAG, "fuck " + e.message.toString())
                    _status.value = NetworkStatus.ERROR
                    delay(5000)
                }
            } while (true)
        }
    }

    private suspend fun refresh(count: Int) {
        if (count > 0) {
            Log.d(TAG, "not null here")
            repository.updateCoin()
        } else {
            Log.d(TAG, "is null here")
            repository.addCoin()
        }
    }

    val coinData = repository.coinData
    val favoriteCoin = repository.favoriteCoin
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
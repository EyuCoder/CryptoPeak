package com.codexo.cryptopeak.viewmodels

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.codexo.cryptopeak.database.CoinDatabase
import com.codexo.cryptopeak.repository.Repository
import com.codexo.cryptopeak.utils.NetworkStatus
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private val TAG = MainViewModel::class.java.simpleName

@RequiresApi(Build.VERSION_CODES.N)
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
                    repository.refreshCoin()
                    Log.d(TAG, "REFRESHED")
                    _status.value = NetworkStatus.DONE
                    delay(5000)
                } catch (e: Exception) {
                    Log.d(TAG, e.message.toString())
                    _status.value = NetworkStatus.ERROR
                    delay(10000)
                }

            } while (true)
        }
    }

    val coinData = repository.coinData
    val favoriteCoin = repository.favoriteCoin


    fun markAsFavorite(flag: Boolean, id: String) {
        viewModelScope.launch {
            repository.markAsFavorite(!flag, id)
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
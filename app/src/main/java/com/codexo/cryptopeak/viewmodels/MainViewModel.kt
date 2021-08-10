package com.codexo.cryptopeak.viewmodels

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import androidx.work.*
import com.codexo.cryptopeak.database.CoinDatabase.Companion.getDatabase
import com.codexo.cryptopeak.repository.Repository
import com.codexo.cryptopeak.utils.LIVE_UPDATE_WORK_NAME
import com.codexo.cryptopeak.workers.LiveUpdateWork
import com.codexo.cryptopeak.workers.RefreshDataWork
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

enum class NetworkStatus { LOADING, ERROR, DONE }

private val TAG = MainViewModel::class.java.simpleName
@RequiresApi(Build.VERSION_CODES.N)
class MainViewModel(application: Application) : ViewModel() {

    private val database = getDatabase(application)
    private val repository = Repository(database)

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

class MainViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    @RequiresApi(Build.VERSION_CODES.N)
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(application) as T
    }
}
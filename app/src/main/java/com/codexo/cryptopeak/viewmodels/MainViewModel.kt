package com.codexo.cryptopeak.viewmodels

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.codexo.cryptopeak.database.CoinDatabase.Companion.getDatabase
import com.codexo.cryptopeak.repository.Repository
import kotlinx.coroutines.launch

enum class NetworkStatus { LOADING, ERROR, DONE }

class MainViewModel(application: Application) : ViewModel() {

    private val database = getDatabase(application)
    private val repository = Repository(database)

    private val _status = MutableLiveData<NetworkStatus>()

    val status: LiveData<NetworkStatus>
        get() = _status

    init {
        viewModelScope.launch {
            repeat(10) {
                _status.value = NetworkStatus.LOADING
                try {
                    repository.refreshCoin()
                    _status.value = NetworkStatus.DONE
                } catch (e: Exception) {
                    _status.value = NetworkStatus.ERROR
                    Toast.makeText(application, "No Internet Connection", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    val coinData = repository.coinData
    val favoriteCoin = repository.favoriteCoin


    fun markAsFavorite(flag: Boolean, id: String) {
        viewModelScope.launch {
            repository.markAsFavorite(!flag, id)
            Log.d("CODEXOX", "markAsFavorite: clicked")
        }
    }
}

class MainViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(application) as T
    }
}
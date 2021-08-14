package com.codexo.cryptopeak.viewmodels

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.codexo.cryptopeak.database.CoinDatabase
import com.codexo.cryptopeak.database.CoinHistory
import com.codexo.cryptopeak.repository.Repository
import com.codexo.cryptopeak.utils.NetworkStatus
import kotlinx.coroutines.launch

private val TAG = DetailViewModel::class.java.simpleName

class DetailViewModel(
    private val database: CoinDatabase, private val repository: Repository
) : ViewModel() {

    var coinId = MutableLiveData<String>()

    private var _coinHistory = MutableLiveData<List<CoinHistory>>()
    val coinHistory get() = _coinHistory

    private val _status = MutableLiveData<NetworkStatus>()
    val status: LiveData<NetworkStatus>
        get() = _status

    fun refreshHistory() {
        viewModelScope.launch {
            _status.value = NetworkStatus.LOADING
            try {
                _coinHistory.value = repository.getCoinHistory(coinId.value!!)
                _status.value = NetworkStatus.DONE
            } catch (e: Exception) {
                Log.d(TAG, e.message.toString())
                _status.value = NetworkStatus.ERROR
            }
        }
    }

    class DetailViewModelFactory(
        private val database: CoinDatabase,
        private val repository: Repository
    ) : ViewModelProvider.Factory {
        @RequiresApi(Build.VERSION_CODES.N)
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return DetailViewModel(database, repository) as T
        }
    }
}
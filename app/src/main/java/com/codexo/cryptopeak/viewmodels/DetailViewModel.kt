package com.codexo.cryptopeak.viewmodels

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.codexo.cryptopeak.data.Repository
import com.codexo.cryptopeak.data.database.CoinData
import com.codexo.cryptopeak.data.database.CoinDatabase
import com.codexo.cryptopeak.data.database.CoinHistory
import com.codexo.cryptopeak.utils.NetworkStatus
import kotlinx.coroutines.launch

private val TAG = DetailViewModel::class.java.simpleName

class DetailViewModel(
    private val database: CoinDatabase,
    private val repository: Repository
) : ViewModel() {

    private lateinit var _coinDetail: LiveData<CoinData>
    val coinDetail get() = _coinDetail

    private val _coinHistory = MutableLiveData<List<CoinHistory>>()
    val coinHistory get() = _coinHistory as LiveData<List<CoinHistory>>

    private val _status = MutableLiveData<NetworkStatus>()
    val status get() = _status as LiveData<NetworkStatus>

    fun updateCoinDetails(coinId: String) {
        viewModelScope.launch {
            _coinDetail = repository.getCoinDetail(coinId)
        }
        refreshHistory(coinId)
    }

    private fun refreshHistory(coinId: String) {
        viewModelScope.launch {
            _status.value = NetworkStatus.LOADING
            try {
                _coinHistory.value = repository.getCoinHistory(coinId)
                _status.value = NetworkStatus.DONE
            } catch (e: Exception) {
                Log.d(TAG, e.message.toString())
                _status.value = NetworkStatus.ERROR
            }
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

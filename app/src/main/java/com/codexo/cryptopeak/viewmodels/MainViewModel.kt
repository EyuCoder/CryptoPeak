package com.codexo.cryptopeak.viewmodels

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.codexo.cryptopeak.data.Repository
import com.codexo.cryptopeak.data.database.CoinData
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

    private val _searchFilter = MutableLiveData<String?>()
    private val _coinDataRaw = repository.coinData

    private val _coinDataFiltered = MediatorLiveData<List<CoinData>>().apply {
        addSource(_coinDataRaw) { value = applyFilter(it, _searchFilter.value) }
        addSource(_searchFilter) { value = applyFilter(_coinDataRaw.value!!, it) }
    }
    val coinData get() = _coinDataFiltered as LiveData<List<CoinData>>

    init {
        periodicUpdateData()
    }

    private fun periodicUpdateData() {
        viewModelScope.launch {
            while (true) {
                _status.value = NetworkStatus.LOADING
                try {
                    repository.updateCoinData()
                    _status.value = NetworkStatus.DONE
                } catch (e: Exception) {
                    _status.value = NetworkStatus.ERROR
                    Log.d(TAG, e.message.toString())
                }
                delay(5000)
            }
        }
    }

    private fun applyFilter(coinData: List<CoinData>, filter: String?): List<CoinData> {
        return if (filter == null) coinData
        else coinData.filter {
            it.name.startsWith(filter, true) or
                    it.symbol.startsWith(filter, true)
        }
    }

    fun markAsFavorite(flag: Boolean, id: String) {
        viewModelScope.launch {
            repository.markAsFavorite(flag, id)
        }
    }

    fun setSearchFilter(filter: String) {
        _searchFilter.value = filter
    }

    fun clearSearchFilter() {
        _searchFilter.value = null
    }
}

class MainViewModelFactory(
    private val database: CoinDatabase,
    private val repository: Repository
) : ViewModelProvider.Factory {

    @RequiresApi(Build.VERSION_CODES.N)
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("unchecked_cast")
        return MainViewModel(database, repository) as T
    }
}

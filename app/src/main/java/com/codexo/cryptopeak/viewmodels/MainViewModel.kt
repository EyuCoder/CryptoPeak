package com.codexo.cryptopeak.viewmodels

import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.codexo.cryptopeak.database.CoinDatabase.Companion.getDatabase
import com.codexo.cryptopeak.receivers.MyReceiver
import com.codexo.cryptopeak.repository.Repository
import com.codexo.cryptopeak.ui.MainActivity
import com.codexo.cryptopeak.utils.NetworkUtil.Companion.isNetworkConnected
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class NetworkStatus { LOADING, ERROR, DONE }

@RequiresApi(Build.VERSION_CODES.N)
class MainViewModel(application: Application) : ViewModel() {

    private val database = getDatabase(application)
    private val repository = Repository(database)

    private val _status = MutableLiveData<NetworkStatus>()
    val status: LiveData<NetworkStatus>
        get() = _status

    private val _isConnected = MutableLiveData<Boolean>()
    private val isConnected: LiveData<Boolean>
        get() = _isConnected

    init {
        viewModelScope.launch {
            do {
                _status.value = NetworkStatus.LOADING
                try {
                    repository.refreshCoin()
                    _status.value = NetworkStatus.DONE
                    delay(5000)
                    initNotification(application)
                } catch (e: Exception) {
                    Log.d("CODEXOX", e.message.toString())
                    _status.value = NetworkStatus.ERROR
                    delay(10000)
                }
                _isConnected.value = isNetworkConnected
            } while (true)
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

    private lateinit var notifyPendingIntent: PendingIntent
    private val REQUEST_CODE = 0

    private fun initNotification(context: Context) {
        val notifyIntent = Intent(context, MyReceiver::class.java)
        notifyPendingIntent = PendingIntent.getBroadcast(
            context,
            REQUEST_CODE,
            notifyIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }
}

class MainViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    @RequiresApi(Build.VERSION_CODES.N)
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(application) as T
    }
}
package com.codexo.cryptopeak

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.codexo.cryptopeak.database.CoinDatabase.Companion.getDatabase
import com.codexo.cryptopeak.repository.Repository
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : ViewModel() {

    private val database = getDatabase(application)
    private val repository = Repository(database)

    init {
        viewModelScope.launch {
            repository.refreshCoin()
        }
    }

    val response = repository.coinData
}

class MainViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(application) as T
    }
}
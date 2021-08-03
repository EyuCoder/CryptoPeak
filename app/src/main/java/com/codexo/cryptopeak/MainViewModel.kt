package com.codexo.cryptopeak

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codexo.cryptopeak.network.CoinData
import com.codexo.cryptopeak.repository.Repository
import kotlinx.coroutines.launch

class MainViewModel(repository: Repository) : ViewModel() {

    private var _response: MutableLiveData<CoinData> = MutableLiveData()
    val response: MutableLiveData<CoinData>
        get() = _response

    fun getAssets(){
        viewModelScope.launch {

        }
    }
}
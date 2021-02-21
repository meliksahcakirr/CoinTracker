package com.meliksahcakir.cointracker.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meliksahcakir.androidutils.Event
import com.meliksahcakir.cointracker.data.CoinRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class SplashActivityDirections {
    LOGIN_ACTIVITY,
    MAIN_ACTIVITY
}

private const val DELAY = 2500L

class SplashViewModel(private val repository: CoinRepository) : ViewModel() {

    private var initialized = false

    private val _navigateToNextScreen = MutableLiveData<Event<SplashActivityDirections>>()
    val navigateToNextScreen: LiveData<Event<SplashActivityDirections>> = _navigateToNextScreen

    init {
        start()
        viewModelScope.launch {
            delay(DELAY)
            _navigateToNextScreen.postValue(Event(SplashActivityDirections.MAIN_ACTIVITY))
        }
    }

    private fun start() {
        if (!initialized) {
            viewModelScope.launch {
                repository.fetchCoinInfoList()
            }
            viewModelScope.launch {
                repository.fetchCoins()
            }
            initialized = true
        }
    }
}

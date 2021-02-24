package com.meliksahcakir.cointracker.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meliksahcakir.androidutils.Event
import com.meliksahcakir.androidutils.Result
import com.meliksahcakir.cointracker.data.Coin
import com.meliksahcakir.cointracker.data.CoinRepository
import com.meliksahcakir.cointracker.data.UserRemote
import com.meliksahcakir.cointracker.utils.NoConnectivityException
import kotlinx.coroutines.launch
import java.util.Timer
import java.util.TimerTask

const val FETCH_INTERVAL = 5000L

class FavoriteViewModel(
    private val repository: CoinRepository,
    private val userRemote: UserRemote
) : ViewModel() {

    private val _coins = MutableLiveData<List<Coin>>()
    val coins: LiveData<List<Coin>> = _coins

    private val _warningEvent = MutableLiveData<Event<String>>()
    val warningEvent: LiveData<Event<String>> = _warningEvent

    private val _busy = MutableLiveData<Boolean>(false)
    val busy: LiveData<Boolean> = _busy

    private var timer: Timer? = null

    private val _navigateToLoginScreen = MutableLiveData<Event<Unit>>()
    val navigateToLoginScreen: LiveData<Event<Unit>> = _navigateToLoginScreen

    init {
        viewModelScope.launch {
            fetchCoins(userRemote.favorites, true)
            startTimer()
        }
    }

    private fun fetchCoins(list: List<String>, showBusy: Boolean = false) {
        viewModelScope.launch {
            if (list.isEmpty()) {
                _coins.value = emptyList()
                return@launch
            }
            if (showBusy) {
                _busy.value = true
            }
            val result = repository.getCoins(list)
            if (result is Result.Success) {
                _coins.value = result.data
            } else {
                val exception = (result as Result.Error).exception
                if (exception is NoConnectivityException) {
                    _warningEvent.value = Event(result.exception.message ?: "")
                }
            }
            if (showBusy) {
                _busy.value = false
            }
        }
    }

    fun onFavoriteClicked(coin: Coin) {
        viewModelScope.launch {
            userRemote.removeFromFavoriteList(coin.id)
            val coinList = ArrayList<Coin>(_coins.value ?: emptyList())
            coinList.removeAll { it.id == coin.id }
            _coins.value = coinList
        }
    }

    private fun startTimer() {
        timer?.cancel()
        timer = Timer()
        timer?.scheduleAtFixedRate(
            object : TimerTask() {
                override fun run() {
                    fetchCoins(userRemote.favorites)
                }
            },
            FETCH_INTERVAL,
            FETCH_INTERVAL
        )
    }

    private fun stopTimer() {
        timer?.cancel()
        timer = null
    }

    override fun onCleared() {
        super.onCleared()
        stopTimer()
    }

    fun signOut() {
        userRemote.firebaseAuth.signOut()
        _navigateToLoginScreen.value = Event(Unit)
    }
}

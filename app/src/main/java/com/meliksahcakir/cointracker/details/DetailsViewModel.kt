package com.meliksahcakir.cointracker.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meliksahcakir.androidutils.Event
import com.meliksahcakir.androidutils.Result
import com.meliksahcakir.cointracker.data.Coin
import com.meliksahcakir.cointracker.data.CoinDetails
import com.meliksahcakir.cointracker.data.CoinRepository
import com.meliksahcakir.cointracker.data.UserRemote
import com.meliksahcakir.cointracker.utils.NoConnectivityException
import kotlinx.coroutines.launch
import java.util.Timer
import java.util.TimerTask
import java.util.concurrent.TimeUnit

private const val DEFAULT_INTERVAL = 5L

class DetailsViewModel(
    private val repository: CoinRepository,
    private val userRemote: UserRemote
) : ViewModel() {

    private val _busy = MutableLiveData<Boolean>(false)
    val busy: LiveData<Boolean> = _busy

    private val _details = MutableLiveData<CoinDetails>()
    val details: LiveData<CoinDetails> = _details

    private val _coin = MutableLiveData<Coin>()
    val coin: LiveData<Coin> = _coin

    private val _favorite = MutableLiveData<Boolean>(false)
    val favorite: LiveData<Boolean> = _favorite

    private val _warningEvent = MutableLiveData<Event<String>>()
    val warningEvent: LiveData<Event<String>> = _warningEvent

    private var coinId = ""

    private var timer: Timer? = null

    private var updateIntervalInSeconds = DEFAULT_INTERVAL

    init {
        viewModelScope.launch {
        }
    }

    fun fetchInitialData(id: String) {
        viewModelScope.launch {
            if (coinId != id) {
                coinId = id
                _favorite.value = userRemote.favorites.contains(coinId)
                fetchCoin(id)
                fetchDetails(id)
                updateTimerInterval(updateIntervalInSeconds)
            }
        }
    }

    fun updateTimerInterval(second: Long) {
        updateIntervalInSeconds = second
        timer?.cancel()
        timer = null
        timer = Timer()
        timer?.scheduleAtFixedRate(
            object : TimerTask() {
                override fun run() {
                    fetchCoin(coinId)
                }
            },
            TimeUnit.SECONDS.toMillis(second),
            TimeUnit.SECONDS.toMillis(second)
        )
    }

    fun onFavoriteButtonClicked() {
        viewModelScope.launch {
            if (userRemote.favorites.contains(coinId)) {
                userRemote.removeFromFavoriteList(coinId)
                _favorite.value = false
            } else {
                userRemote.addToFavoriteList(coinId)
                _favorite.value = true
            }
        }
    }

    private fun fetchDetails(id: String) {
        viewModelScope.launch {
            _busy.value = true
            val result = repository.getCoinDetailsById(id)
            if (result is Result.Error) {
                if (result.exception is NoConnectivityException) {
                    _warningEvent.value = Event(result.exception.message ?: "")
                }
            } else if (result is Result.Success) {
                _details.value = result.data
            }
            _busy.value = false
        }
    }

    private fun fetchCoin(id: String) {
        viewModelScope.launch {
            val result = repository.getCoins(listOf(id))
            if (result is Result.Success) {
                if (result.data.isNotEmpty()) {
                    _coin.value = result.data[0]
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        timer?.cancel()
        timer = null
    }
}

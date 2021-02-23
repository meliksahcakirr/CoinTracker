package com.meliksahcakir.cointracker.details

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.meliksahcakir.androidutils.Event
import com.meliksahcakir.androidutils.Result
import com.meliksahcakir.cointracker.R
import com.meliksahcakir.cointracker.data.Coin
import com.meliksahcakir.cointracker.data.CoinDetails
import com.meliksahcakir.cointracker.data.CoinRepository
import com.meliksahcakir.cointracker.utils.NoConnectivityException
import kotlinx.coroutines.launch
import java.util.Timer
import java.util.TimerTask
import java.util.concurrent.TimeUnit

private const val DEFAULT_INTERVAL = 5L

class DetailsViewModel(private val repository: CoinRepository, private val app: Application) :
    AndroidViewModel(app) {

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

    fun fetchInitialData(id: String) {
        if (coinId != id) {
            coinId = id
            fetchDetails(id)
            fetchCoin(id)
            updateTimerInterval(updateIntervalInSeconds)
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
        _favorite.value = !(_favorite.value ?: false)
    }

    private fun fetchDetails(id: String) {
        viewModelScope.launch {
            _busy.value = true
            val result = repository.getCoinDetailsById(id)
            if (result is Result.Error) {
                if (result.exception is NoConnectivityException) {
                    _warningEvent.value = Event(result.exception.message ?: "")
                } else {
                    _warningEvent.value = Event(app.getString(R.string.error_occurred))
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

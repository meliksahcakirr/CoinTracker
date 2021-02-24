package com.meliksahcakir.cointracker.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.meliksahcakir.androidutils.Event
import com.meliksahcakir.androidutils.Result
import com.meliksahcakir.cointracker.data.Coin
import com.meliksahcakir.cointracker.data.CoinInfo
import com.meliksahcakir.cointracker.data.CoinOrder
import com.meliksahcakir.cointracker.data.CoinRepository
import com.meliksahcakir.cointracker.utils.NoConnectivityException
import kotlinx.coroutines.launch
import java.util.Timer
import java.util.TimerTask

const val FETCH_INTERVAL = 5000L
const val SEARCH_LENGTH_THRESHOLD = 3

class MainViewModel(private val repository: CoinRepository, private val app: Application) :
    AndroidViewModel(app) {

    private var _order = MutableLiveData<CoinOrder>(CoinOrder.MARKET_CAP_DESC)

    private var searchText = ""

    private val _searchResults = MutableLiveData<List<CoinInfo>?>()
    val searchResults: LiveData<List<CoinInfo>?> = _searchResults

    val coins: LiveData<List<Coin>> = _order.switchMap { order ->
        repository.observeCoins().switchMap {
            sortCoins(it, order)
        }
    }

    private val _busy = MutableLiveData<Boolean>(false)
    val busy: LiveData<Boolean> = _busy

    private val _warningEvent = MutableLiveData<Event<String>>()
    val warningEvent: LiveData<Event<String>> = _warningEvent

    private val _navigateToDetailsPage = MutableLiveData<Event<String>>()
    val navigateToDetailsPage: LiveData<Event<String>> = _navigateToDetailsPage

    private var timer: Timer? = null

    init {
        fetchCoins(true)
    }

    private fun fetchCoins(showBusy: Boolean = false) {
        viewModelScope.launch {
            if (showBusy) {
                _busy.value = true
            }
            val result = repository.fetchCoins()
            if (result is Result.Error) {
                if (result.exception is NoConnectivityException) {
                    _warningEvent.value = Event(result.exception.message ?: "")
                }
            }
            if (showBusy) {
                _busy.value = false
            }
        }
    }

    private fun sortCoins(list: List<Coin>, order: CoinOrder): LiveData<List<Coin>> {
        val result = MutableLiveData<List<Coin>>()
        val sortedList = when (order) {
            CoinOrder.MARKET_CAP_DESC -> list.sortedByDescending { it.marketCap }
            CoinOrder.MARKET_CAP_ASC -> list.sortedBy { it.marketCap }
            CoinOrder.VOLUME_DESC -> list.sortedByDescending { it.totalVolume }
            CoinOrder.VOLUME_ASC -> list.sortedBy { it.totalVolume }
            CoinOrder.NAME_DESC -> list.sortedByDescending { it.name }
            CoinOrder.NAME_ASC -> list.sortedBy { it.name }
            CoinOrder.LAST_PRICE_DESC -> list.sortedByDescending { it.currentPrice }
            CoinOrder.LAST_PRICE_ASC -> list.sortedBy { it.currentPrice }
            CoinOrder.PERCENT_CHANGE_DESC -> list.sortedByDescending { it.priceChangePercentage24h }
            CoinOrder.PERCENT_CHANGE_ASC -> list.sortedBy { it.priceChangePercentage24h }
        }
        result.value = sortedList
        return result
    }

    fun changeOrder(index: Int) {
        if (index >= 0 && index < CoinOrder.values().size) {
            _order.value = CoinOrder.values()[index]
        }
    }

    fun search(query: String) {
        searchText = query
        if (query.length < SEARCH_LENGTH_THRESHOLD) {
            _searchResults.value = null
        } else {
            viewModelScope.launch {
                val result = repository.searchCoins(query)
                if (result is Result.Success) {
                    _searchResults.value = result.data
                } else {
                    _searchResults.value = null
                }
            }
        }
    }

    fun onCoinSelected(coinId: String) {
        _navigateToDetailsPage.value = Event(coinId)
    }

    fun startTimer() {
        timer?.cancel()
        timer = Timer()
        timer?.scheduleAtFixedRate(
            object : TimerTask() {
                override fun run() {
                    fetchCoins()
                }
            },
            FETCH_INTERVAL,
            FETCH_INTERVAL
        )
    }

    fun stopTimer() {
        timer?.cancel()
        timer = null
    }

    override fun onCleared() {
        super.onCleared()
        stopTimer()
    }
}

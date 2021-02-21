package com.meliksahcakir.cointracker.data

class CoinRepository(
    private val coinDao: CoinDao,
    private val coinDetailsDao: CoinDetailsDao
)

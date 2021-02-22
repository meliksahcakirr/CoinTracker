package com.meliksahcakir.cointracker.utils

import java.io.IOException

class NoConnectivityException(private val title: String = "") : IOException() {

    override val message: String?
        get() = title
}

package com.meliksahcakir.cointracker.utils

import android.content.Context
import com.meliksahcakir.cointracker.R
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class NetworkConnectionInterceptor(private val context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!context.isConnectedToInternet()) {
            throw NoConnectivityException(context.getString(R.string.no_internet))
        }
        val builder: Request.Builder = chain.request().newBuilder()
        return chain.proceed(builder.build())
    }
}

package com.meliksahcakir.cointracker.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.meliksahcakir.cointracker.R
import java.text.DecimalFormat

val percentageDecimalFormat = DecimalFormat("#.##%")

fun Context.isConnectedToInternet(): Boolean {
    val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networks: Array<Network> = cm.allNetworks
    var hasInternet = false
    if (networks.isNotEmpty()) {
        for (network in networks) {
            val nc = cm.getNetworkCapabilities(network)
            if (nc!!.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) hasInternet = true
        }
    }
    return hasInternet
}

fun ImageView.setImageUrl(url: String) {
    Glide
        .with(context)
        .load(url)
        .centerCrop()
        .placeholder(R.drawable.ic_monetization)
        .into(this)
}

package com.example.ktornotescompose.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

//check for an internet connection can be used anywhere in the project before calling the api/backend

fun checkForInternetConnection(context: Context): Boolean {
    val connectivityManger: ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) return connectivityManger.activeNetworkInfo?.isAvailable ?: false

    val activeNetwork = connectivityManger.activeNetwork ?: return false
    val capabilities = connectivityManger.getNetworkCapabilities(activeNetwork) ?: return false
    return when {
        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN) -> true
        else -> false
    }
}
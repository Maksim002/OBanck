package ua.ideabank.obank.core.presentation.network

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.core.content.ContextCompat

interface NetworkManagerCallback {
    fun onHasConnection(hasConnection: Boolean)
}

class NetworkManager(private val context: Context) {

    private val connectivityManager by lazy {
        ContextCompat.getSystemService(context, ConnectivityManager::class.java)
    }

    private val callback by lazy { context as NetworkManagerCallback }

    @SuppressLint("MissingPermission")
    fun register() {
        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onLost(network: Network) {
                callback.onHasConnection(false)
            }

            override fun onUnavailable() {
                callback.onHasConnection(false)
            }

            override fun onLosing(network: Network, maxMsToLive: Int) {
                callback.onHasConnection(false)
            }

            override fun onAvailable(network: Network) {
                callback.onHasConnection(true)
            }
        }
        val networkRequest = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()
        connectivityManager?.registerNetworkCallback(networkRequest, networkCallback)
    }
}
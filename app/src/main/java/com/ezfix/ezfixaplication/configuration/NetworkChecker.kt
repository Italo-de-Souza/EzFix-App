package com.ezfix.ezfixaplication.configuration

import android.app.AlertDialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

class NetworkChecker(private val connectiveManager: ConnectivityManager, val context : Context) {

    fun performActionIfConnected (action: () -> Unit){
        if (hasInternet()) {
            action();
        } else {
            AlertDialog.Builder(context)
                .setTitle("Erro!")
                .setMessage("Necessário conexão com internet.")
                .create()
                .show()
        }
    }

    private fun hasInternet():Boolean{
        val network = connectiveManager?.activeNetwork ?: return false;
        val capabilities = connectiveManager.getNetworkCapabilities(network) ?: return false;

        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)
                || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
    }
}
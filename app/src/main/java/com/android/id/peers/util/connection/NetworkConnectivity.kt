package com.android.id.peers.util.connection

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.os.Build
import java.io.File
import java.lang.Exception

class NetworkConnectivity(val context: Context) {

    public fun isNetworkConnected(): Boolean {
        var connected = true
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            try {
//                cm.registerDefaultNetworkCallback(object: ConnectivityManager.NetworkCallback() {
//                    override fun onAvailable(network: Network) {
//                        connected = true
//                    }
//
//                    override fun onLost(network: Network) {
//                        connected = false
//                    }
//                })
//                connected = false
//            } catch(error: Exception) {
//                connected = false
//            }
//        }
//        else {
            connected = cm.activeNetworkInfo != null && cm.activeNetworkInfo!!.isConnected
//        }
        return connected
    }

    public fun isConnectedOverWifi(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI)!!
        return networkInfo.isConnected
    }

    public fun deleteCache() {
        try {
            val dir = context.cacheDir;
            deleteDir(dir)
        } catch (e: Exception) {}
    }

    public fun deleteDir(dir: File): Boolean {
        when {
            dir.isDirectory -> {
                val children = dir.list()!!
                for (child in children) {
                    val success = deleteDir(File(dir, child));
                    if (!success) {
                        return false;
                    }
                }
                return dir.delete();
            }
            dir.isFile -> {
                return dir.delete();
            }
            else -> {
                return false;
            }
        }
    }
}
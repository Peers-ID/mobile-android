package com.android.id.peers.util.connection

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import android.util.Log
import java.io.File

class NetworkConnectivity() {

    companion object {
        var connected = true

        fun isNetworkConnected(context: Context): Boolean {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                try {
                    cm.registerDefaultNetworkCallback(object: ConnectivityManager.NetworkCallback() {
                        override fun onAvailable(network: Network) {
                            connected = true
                            Log.d("NetworkConnectivity", "Is Available")
                        }

                        override fun onLost(network: Network) {
                            connected = false
                            Log.d("NetworkConnectivity", "Is Lost")
                        }
                    })
//                connected = false
                } catch(error: Exception) {
//                connected = false
                }
            }
            else {
                connected = cm.activeNetworkInfo != null && cm.activeNetworkInfo!!.isConnected
            }
            return connected
        }

        public fun isConnectedOverWifi(context: Context): Boolean {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI)!!
            return networkInfo.isConnected
        }

        public fun deleteCache(context: Context) {
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
}
package com.dev.lishabora.Utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.telephony.TelephonyManager

import java.sql.DriverManager.println

/**
 * Created by Eric on 12/19/2017.
 */

class NetworkUtils(private val context: Context) {

    val isNetworkAvailable: Boolean
        get() {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            var activeNetworkInfo: NetworkInfo? = null
            if (connectivityManager != null) {
                activeNetworkInfo = connectivityManager.activeNetworkInfo
            }
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }

    companion object {
        private fun getNetworkInfo(context: Context): NetworkInfo? {
            try {
                val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                return cm.activeNetworkInfo
            } catch (e: Exception) {
                println("CheckConnectivity Exception: " + e.message)
                return null
            }

        }

        fun isConnectionFast(context: Context): Boolean {
            val info = getNetworkInfo(context)
            return info != null && info.isConnected && isConnectionFast(info.type, info.subtype)
        }

        private fun isConnectionFast(type: Int, subType: Int): Boolean {
            return if (type == ConnectivityManager.TYPE_WIFI) {
                true
            } else if (type == ConnectivityManager.TYPE_MOBILE) {
                when (subType) {
                    TelephonyManager.NETWORK_TYPE_1xRTT -> false // ~ 50-100 kbps
                    TelephonyManager.NETWORK_TYPE_CDMA -> false // ~ 14-64 kbps
                    TelephonyManager.NETWORK_TYPE_EDGE -> false // ~ 50-100 kbps
                    TelephonyManager.NETWORK_TYPE_EVDO_0 -> true // ~ 400-1000 kbps
                    TelephonyManager.NETWORK_TYPE_EVDO_A -> true // ~ 600-1400 kbps
                    TelephonyManager.NETWORK_TYPE_GPRS -> false // ~ 100 kbps
                    TelephonyManager.NETWORK_TYPE_HSDPA -> true // ~ 2-14 Mbps
                    TelephonyManager.NETWORK_TYPE_HSPA -> true // ~ 700-1700 kbps
                    TelephonyManager.NETWORK_TYPE_HSUPA -> true // ~ 1-23 Mbps
                    TelephonyManager.NETWORK_TYPE_UMTS -> true // ~ 400-7000 kbps
                    TelephonyManager.NETWORK_TYPE_EHRPD // API level 11
                    -> true // ~ 1-2 Mbps
                    TelephonyManager.NETWORK_TYPE_EVDO_B // API level 9
                    -> true // ~ 5 Mbps
                    TelephonyManager.NETWORK_TYPE_HSPAP // API level 13
                    -> true // ~ 10-20 Mbps
                    TelephonyManager.NETWORK_TYPE_IDEN // API level 8
                    -> false // ~25 kbps
                    TelephonyManager.NETWORK_TYPE_LTE // API level 11
                    -> true // ~ 10+ Mbps
                // Unknown
                    TelephonyManager.NETWORK_TYPE_UNKNOWN -> false
                    else -> false
                }
            } else {
                false
            }
        }

        fun isConnectedToWifi(context: Context): Boolean {
            val info = getNetworkInfo(context)
            return info != null && info.isConnected && info.type == ConnectivityManager.TYPE_WIFI
        }

        /**
         * Check if there is any connection to a mobile network
         *
         * @param context
         * @return
         */
        fun isConnectedToMobile(context: Context): Boolean {
            val info = getNetworkInfo(context)
            return info != null && info.isConnected && info.type == ConnectivityManager.TYPE_MOBILE
        }

        fun isConnected(context: Context, connectionType: Int): Boolean {
            when (connectionType) {
                ConnectivityManager.TYPE_WIFI -> return isConnectedToWifi(context)
                ConnectivityManager.TYPE_MOBILE -> return isConnectedToMobile(context)
                else -> return isConnected(context)
            }
        }

        /**
         * @param context
         * @return boolean
         *
         *
         * Instead use [NetworkUtil.isConnected]
         */
        @Deprecated("")
        fun isOnline(context: Context): Boolean {
            return isConnected(context)
        }

        fun isConnected(context: Context): Boolean {
            val info = getNetworkInfo(context)
            return info != null && info.isConnectedOrConnecting
        }
    }
}

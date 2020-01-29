package com.dev.zaidi.Network

//import com.android.volley.VolleyError;

//import com.android.volley.error.VolleyError;

import com.androidnetworking.error.ANError
import com.dev.zaidi.Models.NetworkAnalytics

/**
 * Created by Eric on 12/15/2017.
 */

interface RequestListener {
    fun onError(error: ANError, analytics: NetworkAnalytics)

    fun onError(error: String, analytics: NetworkAnalytics)

    fun onSuccess(response: String, analytics: NetworkAnalytics)

    // fun onAnalytics(analytics: NetworkAnalytics)
}

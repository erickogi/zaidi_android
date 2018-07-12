package com.dev.lishaboramobile.Global.Network

//import com.android.volley.VolleyError;

//import com.android.volley.error.VolleyError;

import com.androidnetworking.error.ANError

/**
 * Created by Eric on 12/15/2017.
 */

interface RequestListener {
    fun onError(error: ANError)

    fun onError(error: String)

    fun onSuccess(response: String)
}

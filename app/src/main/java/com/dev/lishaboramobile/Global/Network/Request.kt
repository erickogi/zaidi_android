package com.dev.lishaboramobile.Global.Network

import android.util.Log
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.StringRequestListener
import org.json.JSONObject
import java.util.*

class Request {


    companion object {
        fun postRequest(url: String, params: HashMap<String, String>, token: String?, listener: RequestListener) {

            var mtoken = ""
            if (token != null) {

                mtoken = token

            }

            AndroidNetworking.post(url)
                    .addBodyParameter(params)
                    .addHeaders("Authorization", "Bearer $mtoken")
                    .addHeaders("Accept", "application/json")


                    .setTag("test")
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsString(object : StringRequestListener {
                        override fun onResponse(response: String) {
                            // do anything with response
                            listener.onSuccess(response)

                        }

                        override fun onError(error: ANError) {
                            // handle error

                            Log.d("eww", error.toString())
                            listener.onError(error)
                            //  listener.onError(error)
                        }
                    })
        }

        fun getRequest(url: String, token: String?, listener: RequestListener) {
            var mtoken = ""
            if (token != null) {

                mtoken = token

            }


            AndroidNetworking.get(url)


                    .addHeaders("Authorization", "Bearer $mtoken")
                    .addHeaders("Accept", "application/json")


                    .setTag("test")
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsString(object : StringRequestListener {
                        override fun onResponse(response: String) {
                            // do anything with response
                            listener.onSuccess(response)

                        }

                        override fun onError(error: ANError) {
                            // handle error
                            listener.onError(error.errorBody)
                        }
                    })
        }


        fun putRequest(url: String, params: JSONObject, token: String?, listener: RequestListener) {
            var mtoken = ""
            if (token != null) {

                mtoken = token

            }

            Log.d("putrequest", params.toString())
            Log.d("putrequest", url)

            AndroidNetworking.put(url)


                    .addHeaders("Authorization", "Bearer $mtoken")
                    .addHeaders("Accept", "application/json")

                    //.addBodyParameter(params)
                    // .addApplicationJsonBody(params)
                    .addJSONObjectBody(params)
                    //.setContentType(ContentT)

                    .setTag("test")
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsString(object : StringRequestListener {
                        override fun onResponse(response: String) {
                            // do anything with response
                            listener.onSuccess(response)

                        }

                        override fun onError(error: ANError) {
                            // handle error
                            listener.onError(error.errorBody)
                        }
                    })
        }

        fun deleteRequest(url: String, token: String?, listener: RequestListener) {
            var mtoken = ""
            if (token != null) {

                mtoken = token

            }
            Log.d("deleterequest", url)
            //Log.d("deleterequest", url)


            AndroidNetworking.delete(url)


                    .addHeaders("Authorization", "Bearer $mtoken")
                    .addHeaders("Accept", "application/json")


                    .setTag("test")
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsString(object : StringRequestListener {
                        override fun onResponse(response: String) {
                            // do anything with response
                            listener.onSuccess(response)

                        }

                        override fun onError(error: ANError) {
                            // handle error
                            listener.onError(error.errorBody)
                        }
                    })
        }


    }

}

package com.dev.lishabora.Network

import android.util.Log
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.StringRequestListener
import com.dev.lishabora.Models.*
import com.dev.lishabora.Models.Trader.Data
import com.dev.lishabora.Utils.*
import com.google.gson.Gson
import okhttp3.OkHttpClient
import org.json.JSONArray
import org.json.JSONObject
import timber.log.Timber
import java.util.concurrent.TimeUnit

class Request {


    companion object {
        internal var responseModel = ResponseModel()
        internal var responseModelSingle = ResponseObject()
        internal var syncresponseModelSingle = SyncResponseModel()
        internal var dataresponseModelSingle = Data()


        fun getResponse(url: String, jsonObject: JSONObject, token: String, responseCallback: ResponseCallback) {
            postRequest(url, jsonObject, token, object : RequestListener {
                override fun onError(error: ANError, analytics: NetworkAnalytics) {
                    responseModel.data = null
                    responseModel.resultCode = 0
                    responseModel.resultDescription = ""

                    responseCallback.response(responseModel, analytics)

                }

                override fun onError(error: String, analytics: NetworkAnalytics) {

                    responseModel.data = null
                    responseModel.resultCode = 0
                    responseModel.resultDescription = error

                    responseCallback.response(responseModel, analytics)

                }

                override fun onSuccess(response: String, analytics: NetworkAnalytics) {
                    try {


                        val gson = Gson()
                        responseModel = gson.fromJson(response, ResponseModel::class.java)
                        // Log.d("2ReTrRe", gson.toJson(responseModel))

                        responseCallback.response(responseModel, analytics)


                    } catch (e: Exception) {
                        responseModel.data = null
                        responseModel.resultCode = 0
                        responseModel.resultDescription = e.message
                        responseCallback.response(responseModel, analytics)

                        e.printStackTrace()
                    }

                }
            })

        }


        fun getResponseSingle(url: String, jsonObject: JSONObject, token: String, responseCallback: ResponseCallback) {
            postRequest(url, jsonObject, token, object : RequestListener {
                override fun onError(error: ANError, analytics: NetworkAnalytics) {
                    responseModelSingle.data = null
                    responseModelSingle.resultCode = 0
                    responseModelSingle.resultDescription = error.toString()

                    responseCallback.response(responseModelSingle, analytics)

                }

                override fun onError(error: String, analytics: NetworkAnalytics) {

                    responseModelSingle.data = null
                    responseModelSingle.resultCode = 0
                    responseModelSingle.resultDescription = error

                    responseCallback.response(responseModelSingle, analytics)

                }

                override fun onSuccess(response: String, analytics: NetworkAnalytics) {
                    try {


                        val gson = Gson()
                        responseModelSingle = gson.fromJson(response, ResponseObject::class.java)

                        responseCallback.response(responseModelSingle, analytics)


                    } catch (e: Exception) {
                        responseModelSingle.data = null
                        responseModelSingle.resultCode = 0
                        responseModelSingle.resultDescription = e.message
                        responseCallback.response(responseModelSingle, analytics)

                        e.printStackTrace()
                    }

                }
            })

        }

        fun getResponseSyncChanges(url: String, jsonObject: JSONObject, token: String, responseCallback: SyncChangesCallback) {
            postRequest(url, jsonObject, token, object : RequestListener {
                override fun onError(error: ANError, analytics: NetworkAnalytics) {
                    responseModelSingle.data = null
                    responseModelSingle.resultCode = 0
                    responseModelSingle.resultDescription = error.toString()

                    responseCallback.onError(error.toString(), analytics)

                }

                override fun onError(error: String, analytics: NetworkAnalytics) {

                    responseModelSingle.data = null
                    responseModelSingle.resultCode = 0
                    responseModelSingle.resultDescription = error

                    responseCallback.onError(error, analytics)

                }

                override fun onSuccess(response: String, analytics: NetworkAnalytics) {
                    try {


                        val gson = Gson()
                        //SyncDownResponse r =
                        //Timber.tag("2ReTrRe").d(gson.toJson(responseModelSingle))

                        responseCallback.onSucces(gson.fromJson(response, SyncDownResponse::class.java), analytics)


                    } catch (e: Exception) {
                        responseModelSingle.data = null
                        responseModelSingle.resultCode = 0
                        responseModelSingle.resultDescription = e.message
                        responseCallback.onError("" + e.toString(), analytics)

                        e.printStackTrace()
                    }

                }
            })

        }

        fun getResponseSync(url: String, jsonObject: JSONObject, token: String, responseCallback: SyncResponseCallback) {
            postRequest(url, jsonObject, token, object : RequestListener {
                override fun onError(error: ANError, analytics: NetworkAnalytics) {

                    responseCallback.response(error.toString(), analytics)

                }

                override fun onError(error: String, analytics: NetworkAnalytics) {


                    responseCallback.response(error, analytics)

                }

                override fun onSuccess(response: String, analytics: NetworkAnalytics) {
                    try {


                        val gson = Gson()



                        syncresponseModelSingle = gson.fromJson(response, SyncResponseModel::class.java)
                        Timber.tag("2ReTrRe").d(gson.toJson(syncresponseModelSingle))
                        responseCallback.response(syncresponseModelSingle, analytics)


                    } catch (e: Exception) {
                        responseCallback.response(response, analytics)

                        e.printStackTrace()
                    }

                }
            })

        }

        fun getResponseSyncDown(url: String, jsonObject: JSONObject, token: String, responseCallback: SyncDownResponseCallback) {
            postRequest(url, jsonObject, token, object : RequestListener {
                override fun onError(error: ANError, analytics: NetworkAnalytics) {

                    responseCallback.response(error.toString(), analytics)

                }

                override fun onError(error: String, analytics: NetworkAnalytics) {


                    responseCallback.response(error, analytics)

                }

                override fun onSuccess(response: String, analytics: NetworkAnalytics) {
                    try {

                        val gson = Gson()
                        responseModelSingle = gson.fromJson(response, ResponseObject::class.java)
                        Timber.tag("2ReTrRe").d(gson.toJson(responseModelSingle))


                        dataresponseModelSingle = gson.fromJson(gson.toJson(responseModelSingle.data), Data::class.java)
                        dataresponseModelSingle.resultCode = responseModelSingle.code
                        dataresponseModelSingle.resultDescription = responseModelSingle.resultDescription

                        // Timber.tag("Syncdown").d(gson.toJson(dataresponseModelSingle))
                        responseCallback.response(dataresponseModelSingle, analytics)


                    } catch (e: Exception) {
                        responseCallback.response(response, analytics)

                        e.printStackTrace()
                    }

                }
            })

        }


        fun getRequest(url: String, token: String?, listener: RequestListener) {

            var mtoken = ""
            if (token != null) {

                mtoken = token

            }
            var analytics = NetworkAnalytics()

            val okHttpClient = OkHttpClient().newBuilder()
                    .connectTimeout(1000, TimeUnit.SECONDS)
                    .readTimeout(1000, TimeUnit.SECONDS)
                    .writeTimeout(1000, TimeUnit.SECONDS)
                    .build()



            AndroidNetworking.post(url)


                    .addHeaders("Authorization", "Bearer $mtoken")
                    .addHeaders("Accept", "application/json")


                    .setTag("get")
                    .setPriority(Priority.HIGH)
                    //.setOkHttpClient(okHttpClient)

                    .build()
                    .setAnalyticsListener { timeTakenInMillis, bytesSent, bytesReceived, isFromCache ->

                        analytics = (NetworkAnalytics(timeTakenInMillis, bytesSent, bytesReceived, isFromCache))

                    }

                    .getAsString(object : StringRequestListener {
                        override fun onResponse(response: String) {
                            // do anything with response
                            Timber.tag("ReTrRe").d(response)
                            listener.onSuccess(response, analytics)

                        }

                        override fun onError(error: ANError) {
                            // handle error

                            Timber.tag("ReTrRe").d(error.toString())
                            listener.onError(error, analytics)
                            //  listener.onError(error)
                        }
                    })
        }


        fun postRequest(url: String, params: JSONObject, token: String?, listener: RequestListener) {

            var mtoken = ""
            if (token != null) {

                mtoken = token

            }
            var analytics = NetworkAnalytics()

            val okHttpClient = OkHttpClient().newBuilder()
                    .connectTimeout(1000, TimeUnit.SECONDS)
                    .readTimeout(1000, TimeUnit.SECONDS)
                    .writeTimeout(1000, TimeUnit.SECONDS)
                    .build()



            AndroidNetworking.post(url)
                    .addJSONObjectBody(params)


                    .addHeaders("Authorization", "Bearer $mtoken")
                    .addHeaders("Accept", "application/json")


                    .setTag("test")
                    .setPriority(Priority.HIGH)
                    //.setOkHttpClient(okHttpClient)

                    .build()
                    .setAnalyticsListener { timeTakenInMillis, bytesSent, bytesReceived, isFromCache ->
                        Logs.d("Analy12Kq", " timeTakenInMillis  " , timeTakenInMillis)
                        Logs.d("Analy12Kq", " bytesSent  " , bytesSent)
                        Logs.d("Analy12Kq", " bytesReceived  " , bytesReceived)
                        Logs.d("Analy12Kq", " isFromCache  " , isFromCache)

                        analytics = (NetworkAnalytics(timeTakenInMillis, bytesSent, bytesReceived, isFromCache))

                    }

                    .getAsString(object : StringRequestListener {
                        override fun onResponse(response: String) {
                            // do anything with response
                            Timber.tag("ReTrRe").d(response)
                            listener.onSuccess(response, analytics)

                        }

                        override fun onError(error: ANError) {
                            // handle error

                            Timber.tag("ReTrRe").d(error.toString())
                            listener.onError(error, analytics)
                            //  listener.onError(error)
                        }
                    })
        }

        fun postRequest(url: String, params: JSONArray, token: String?, listener: RequestListener) {

            var mtoken = ""
            if (token != null) {

                mtoken = token

            }
            var analytics = NetworkAnalytics()

            val okHttpClient = OkHttpClient().newBuilder()
                    .connectTimeout(1000, TimeUnit.SECONDS)
                    .readTimeout(1000, TimeUnit.SECONDS)
                    .writeTimeout(1000, TimeUnit.SECONDS)
                    .build()


            AndroidNetworking.post(url)

                    .addJSONArrayBody(params)


                    .addHeaders("Authorization", "Bearer $mtoken")
                    .addHeaders("Accept", "application/json")


                    .setTag("test")
                    .setPriority(Priority.HIGH)
                    .setOkHttpClient(okHttpClient)

                    .build()


                    .setAnalyticsListener { timeTakenInMillis, bytesSent, bytesReceived, isFromCache ->
                        Log.d("Analy12Kq", " timeTakenInMillis : " + timeTakenInMillis)
                        Log.d("Analy12Kq", " bytesSent : " + bytesSent)
                        Log.d("Analy12Kq", " bytesReceived : " + bytesReceived)
                        Log.d("Analy12Kq", " isFromCache : " + isFromCache)

                        analytics = (NetworkAnalytics(timeTakenInMillis, bytesSent, bytesReceived, isFromCache))
                    }

                    .getAsString(object : StringRequestListener {
                        override fun onResponse(response: String) {
                            Timber.tag("ReTrRe").d(response)
                            listener.onSuccess(response, analytics)

                        }

                        override fun onError(error: ANError) {

                            Timber.tag("ReTrRe").d(error.toString())
                            listener.onError(error, analytics)
                        }
                    })
        }




    }

}

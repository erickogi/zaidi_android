package com.dev.lishabora.Utils.Network

import android.util.Log
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.StringRequestListener
import com.dev.lishabora.Models.ResponseModel
import com.dev.lishabora.Models.ResponseObject
import com.dev.lishabora.Utils.ResponseCallback
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

class Request {


    companion object {
        internal var responseModel = ResponseModel()
        internal var responseModelSingle = ResponseObject()

        fun getResponse(url: String, jsonObject: JSONObject, token: String, responseCallback: ResponseCallback) {
            postRequest(url, jsonObject, token, object : RequestListener {
                override fun onError(error: ANError) {
                    responseModel.data = null
                    responseModel.resultCode = 0
                    responseModel.resultDescription = ""

                    responseCallback.response(responseModel)

                }

                override fun onError(error: String) {

                    responseModel.data = null
                    responseModel.resultCode = 0
                    responseModel.resultDescription = error

                    responseCallback.response(responseModel)

                }

                override fun onSuccess(response: String) {
                    try {


                        val gson = Gson()
                        responseModel = gson.fromJson(response, ResponseModel::class.java)
                        // Log.d("2ReTrRe", gson.toJson(responseModel))

                        responseCallback.response(responseModel)


                    } catch (e: Exception) {
                        responseModel.data = null
                        responseModel.resultCode = 0
                        responseModel.resultDescription = e.message
                        responseCallback.response(responseModel)

                        e.printStackTrace()
                    }

                }
            })

        }

        fun getResponse(url: String, jsonObject: JSONArray, token: String, responseCallback: ResponseCallback) {
            postRequest(url, jsonObject, token, object : RequestListener {
                override fun onError(error: ANError) {
                    responseModel.data = null
                    responseModel.resultCode = 0
                    responseModel.resultDescription = error.toString()

                    responseCallback.response(responseModel)

                }

                override fun onError(error: String) {

                    responseModel.data = null
                    responseModel.resultCode = 0
                    responseModel.resultDescription = error

                    responseCallback.response(responseModel)

                }

                override fun onSuccess(response: String) {
                    try {


                        val gson = Gson()
                        responseModel = gson.fromJson(response, ResponseModel::class.java)
                        // Log.d("2ReTrRe", gson.toJson(responseModel))

                        responseCallback.response(responseModel)


                    } catch (e: Exception) {
                        responseModel.data = null
                        responseModel.resultCode = 0
                        responseModel.resultDescription = e.message
                        responseCallback.response(responseModel)

                        e.printStackTrace()
                    }

                }
            })

        }


        fun getResponseSingle(url: String, jsonObject: JSONObject, token: String, responseCallback: ResponseCallback) {
            postRequest(url, jsonObject, token, object : RequestListener {
                override fun onError(error: ANError) {
                    responseModelSingle.data = null
                    responseModelSingle.resultCode = 0
                    responseModelSingle.resultDescription = error.toString()

                    responseCallback.response(responseModelSingle)

                }

                override fun onError(error: String) {

                    responseModelSingle.data = null
                    responseModelSingle.resultCode = 0
                    responseModelSingle.resultDescription = error

                    responseCallback.response(responseModelSingle)

                }

                override fun onSuccess(response: String) {
                    try {


                        val gson = Gson()
                        responseModelSingle = gson.fromJson(response, ResponseObject::class.java)
                        Log.d("2ReTrRe", gson.toJson(responseModelSingle))

                        responseCallback.response(responseModelSingle)


                    } catch (e: Exception) {
                        responseModelSingle.data = null
                        responseModelSingle.resultCode = 0
                        responseModelSingle.resultDescription = e.message
                        responseCallback.response(responseModelSingle)

                        e.printStackTrace()
                    }

                }
            })

        }

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

        fun postRequest(url: String, params: JSONObject, token: String?, listener: RequestListener) {

            var mtoken = ""
            if (token != null) {

                mtoken = token

            }
            Log.d("ReTrReq", params.toString() + " Url : " + url)


            AndroidNetworking.post(url)
                    .addJSONObjectBody(params)


                    .addHeaders("Authorization", "Bearer $mtoken")
                    .addHeaders("Accept", "application/json")


                    .setTag("test")
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsString(object : StringRequestListener {
                        override fun onResponse(response: String) {
                            // do anything with response
                            Log.d("ReTrRe", response)
                            listener.onSuccess(response)

                        }

                        override fun onError(error: ANError) {
                            // handle error

                            Log.d("ReTrRe", error.toString())
                            listener.onError(error)
                            //  listener.onError(error)
                        }
                    })
        }

        fun postRequest(url: String, params: JSONArray, token: String?, listener: RequestListener) {

            var mtoken = ""
            if (token != null) {

                mtoken = token

            }
            Log.d("ReTrReq", params.toString() + " Url : " + url)


            AndroidNetworking.post(url)
                    .addJSONArrayBody(params)


                    .addHeaders("Authorization", "Bearer $mtoken")
                    .addHeaders("Accept", "application/json")


                    .setTag("test")
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsString(object : StringRequestListener {
                        override fun onResponse(response: String) {
                            // do anything with response
                            Log.d("ReTrRe", response)
                            listener.onSuccess(response)

                        }

                        override fun onError(error: ANError) {
                            // handle error

                            Log.d("ReTrRe", error.toString())
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

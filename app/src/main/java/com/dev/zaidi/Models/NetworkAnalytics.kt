package com.dev.zaidi.Models

import java.io.Serializable

class NetworkAnalytics : Serializable {

    var timeTakenInMillis: Long? = null
    var bytesSent: Long? = null
    var bytesReceived: Long? = null
    var isFromCache: Boolean? = null

    constructor()

    constructor(timeTakenInMillis: Long?, bytesSent: Long?, bytesReceived: Long?, isFromCache: Boolean?) {
        this.timeTakenInMillis = timeTakenInMillis
        this.bytesSent = bytesSent
        this.bytesReceived = bytesReceived
        this.isFromCache = isFromCache
    }
}

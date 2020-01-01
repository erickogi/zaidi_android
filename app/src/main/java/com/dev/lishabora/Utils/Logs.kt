package com.dev.lishabora.Utils

class Logs {
    companion object {

        fun d(tag: String, message: Any?) {
            try {
               // Timber.tag(tag).d( Gson().toJson(message))
            } catch (xc: Exception) {
                xc.printStackTrace()
               // Timber.tag(tag).d(xc.message)
            }
        }

        fun d(tag: String, message: String, data: Any?) {
            try {
               // Timber.tag(tag).d( message + " - " + Gson().toJson(data))
            } catch (xc: Exception) {
                xc.printStackTrace()
               // Timber.tag(tag).d(xc.message)

            }
        }
    }
}
package com.dev.lishabora.Utils

import android.text.format.DateUtils
import org.joda.time.DateTime
import org.joda.time.Period
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DateTimeUtils {
    companion object {

        var Format: String = "yyyy-MM-dd HH:mm:ss"
        fun getNowslong(): String {
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val date = Date()

            //Date d = .parse(string_date);
            val milliseconds = date.time
            //return dateFormat.format(date);
            return milliseconds.toString()
        }

        fun parseDateTime(dateString: String, originalFormat: String, outputFromat: String): String {

            val formatter = SimpleDateFormat(originalFormat, Locale.US)
            var date: Date? = null
            try {
                date = formatter.parse(dateString)

                val dateFormat = SimpleDateFormat(outputFromat, Locale("US"))

                return dateFormat.format(date)

            } catch (e: ParseException) {
                e.printStackTrace()
                return ""
            }

        }

        fun calcDiff(startDate: Date?, endDate: Date?): Period {
            val START_DT = if (startDate == null) null else DateTime(startDate)
            val END_DT = if (endDate == null) null else DateTime(endDate)

            return Period(START_DT, END_DT)

        }

        fun conver2Date(mydate: String, formatFrom: String): Date? {
            val format1 = SimpleDateFormat(formatFrom)

            var date: Date? = null
            try {
                date = format1.parse(mydate)
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            return date
        }

        fun getNow(): String {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val date = Date()

            return dateFormat.format(date)
        }

        fun getRelativeTimeSpan(dateString: String, originalFormat: String): String {

            val formatter = SimpleDateFormat(originalFormat, Locale.US)
            var date: Date? = null
            try {
                date = formatter.parse(dateString)

                return DateUtils.getRelativeTimeSpanString(date!!.time).toString()

            } catch (e: ParseException) {
                e.printStackTrace()
                return ""
            }

        }

        fun getToday(): String {

            val dateFormat = SimpleDateFormat("yyyy-MM-dd")
            val date = Date()

            return dateFormat.format(date)
        }
    }

}
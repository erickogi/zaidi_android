package com.dev.lishabora.Utils

import android.text.format.DateUtils
import android.util.Log
import com.dev.lishabora.Models.DaysDates
import com.dev.lishabora.Models.MonthsDates
import org.joda.time.DateTime
import org.joda.time.Days
import org.joda.time.LocalDate
import org.joda.time.Period
import org.joda.time.format.DateTimeFormat
import timber.log.Timber
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*



class DateTimeUtils {
    companion object {

        var Format: String = "yyyy-MM-dd HH:mm:ss"


        var FormatMinsYrSec: String = "MM-dd HH:mm"
        var FormatTime: String = "HH:mm"


        var FormatSmall: String = "yyyy-MM-dd"


        var DisplayDatePattern1 = "dd-MMM"
        var DisplayDatePattern2 = "dd-MMM-yyyy HH:mm:ss"

        fun getDateNow(): Date {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

            return Date()
        }

        fun getDateNowMill(): Date {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS")

            return Date()
        }


        fun getNowslong(): String {
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val date = Date()

            //Date d = .parse(string_date);
            val milliseconds = date.time
            //return dateFormat.format(date);
            return milliseconds.toString()
        }

        fun getDateAndTime(date1q: String): Array<String> {

            val a = ""
            val b = ""

            val originalString = date1q
            val date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(originalString)
            val newString = SimpleDateFormat("H:mm").format(date)

            val originalStringd = date1q
//            val dated = SimpleDateFormat("MM-dd HH:mm").parse(originalStringd)
            val newStringd = SimpleDateFormat("dd-MMM HH:mm").format(date)




            return arrayOf(newStringd, newString)
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

        fun isAM(time: DateTime): Boolean {
            return time.hourOfDay < 12
        }
        fun calcDiff(startDate: Date?, endDate: Date?): Period {
            val START_DT = if (startDate == null) null else DateTime(startDate)
            val END_DT = if (endDate == null) null else DateTime(endDate)

            return Period(START_DT, END_DT)

        }

        fun calcDiff(startDate: DateTime?, endDate: DateTime?): Period {
            val START_DT = if (startDate == null) null else startDate
            val END_DT = if (endDate == null) null else endDate

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

        fun conver2Date(mydate: String): DateTime? {
            try {
                val formatter = DateTimeFormat.forPattern(this.FormatSmall)
                return formatter.parseDateTime(mydate)
            } catch (hj: Exception) {
                return null
            }

        }

        fun conver2DateLong(mydate: String): DateTime? {
            val formatter = DateTimeFormat.forPattern(this.Format)
            try {
                return formatter.parseDateTime(mydate)

            } catch (hj: Exception) {
                return null
            }
        }


        fun convert2String(date: DateTime): String {
            val dt = DateTime()
            val fmt = DateTimeFormat.forPattern(this.FormatSmall)
            return fmt.print(date)
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

            val dateFormat = SimpleDateFormat(this.FormatSmall)
            val date = Date()

            return dateFormat.format(date)
        }

        fun getTodayDate(): DateTime {

            val date = DateTime()

            return date
        }

        fun getTodayDateL(): Date {

            val date = DateTime().toDate()

            return date
        }

        fun getDayOfWeek(dt: DateTime, pattern: String): String {
            val fmt = DateTimeFormat.forPattern(pattern)
            val strEnglish = fmt.print(dt)

            return strEnglish
        }

        fun getDayPrevious(dif: Int, pattern: String): String {
            val oneDayAgo = getTodayDate().minusDays(dif)
            return getDayOfWeek(oneDayAgo, pattern)


        }

        fun getDaysAndDatesBtnDates(firstDate: String, lastDate: String): List<DaysDates> {
            val fd = conver2Date(firstDate)
            val ld = conver2Date(lastDate)
            val p = calcDiff2(fd, ld)
            val daysDates = LinkedList<DaysDates>()
            for (i in 0..p) {

                val date = addDays(fd!!, i)
                val day = getDayOfWeek(date, "EEE")
                daysDates.add(DaysDates(day, convert2String(date)))


            }
            return daysDates


        }


        private fun calcDiff2(fd: DateTime?, ld: DateTime?): Int {
            return Days.daysBetween(fd!!.toLocalDate(), ld!!.toLocalDate()).days

        }

        fun getDatePrevious(i: Int): String {
            return convert2String(getTodayDate().minusDays(i))


        }

        fun addDays(date: DateTime, i: Int): DateTime {
            val day = date.plusDays(i)
            return day

        }

        fun addDaysString(date: DateTime, i: Int): String {

            val day = date.plusDays(i)
            val fmt = DateTimeFormat.forPattern(this.FormatSmall)
            return fmt.print(day)

        }

        fun isPastLastDay(endDate: String): Boolean {

            val today = getTodayDate().withHourOfDay(0)
            val enddate = conver2Date(endDate)!!.withHourOfDay(23)
            val n = today.isAfter(enddate)

            Timber.tag("isAfter").d(" Today " + today + "  Enddate " + enddate + "  Is After " + n)

            return n

        }

        fun isPastLastDay(endDate: String, allowace: Int): Boolean {

            val today = getTodayDate().withHourOfDay(0).plusDays(allowace)
            val enddate = conver2Date(endDate)!!.withHourOfDay(23)
            val n = today.isAfter(enddate)

            Timber.tag("isAfter").d(" Today " + today + "  Enddate " + enddate + "  Is After " + n)

            return n

        }

        fun getMonth(date: String): String? {


            var month = conver2Date(date)?.toString("yyyy-MMM")
            return month

        }

        fun getDisplayDate(date: String, pattern: String): String? {


            var month = conver2Date(date)?.toString(pattern)
            return month

        }

        fun getDisplayDate(date: String): String? {


            var month = conver2DateLong(date)?.toString(this.DisplayDatePattern2)
            return month

        }


        fun getMonths(limit: Int): LinkedList<MonthsDates> {

            val months = LinkedList<MonthsDates>()

            val today = getTodayDate()

            val endOfMonth = today.dayOfMonth().withMaximumValue()
            val startOfMonth = today.dayOfMonth().withMinimumValue()


            months.add(MonthsDates((LocalDate.now()).toString("MMM-yyyy"), convert2String(startOfMonth), convert2String(endOfMonth)))

            for (i in 1..limit) {


                val prevous = today.minus(Period.months(i))
                val endOfMonthPrevious = prevous.dayOfMonth().withMaximumValue()
                val startOfMonthPrevious = prevous.dayOfMonth().withMinimumValue()
                months.add(MonthsDates(prevous.toString("MMM-yyyy"), convert2String(startOfMonthPrevious), convert2String(endOfMonthPrevious)))


            }
            return months


        }

        fun getMonthsInYear(limit: Int): LinkedList<MonthsDates> {

            val months = LinkedList<MonthsDates>()
            val today = DateTime().dayOfYear().withMinimumValue()
            Log.d("datt", convert2String(today))
            //val today = DateTime().year().withMinimumValue().withTimeAtStartOfDay();

            val endOfMonth = today.dayOfMonth().withMaximumValue()
            val startOfMonth = today.dayOfMonth().withMinimumValue()


            months.add(MonthsDates((today).toString("MMM-yyyy"), convert2String(startOfMonth), convert2String(endOfMonth)))

            for (i in 1..limit) {


                val prevous = today.plus(Period.months(i))
                val endOfMonthPrevious = prevous.dayOfMonth().withMaximumValue()
                val startOfMonthPrevious = prevous.dayOfMonth().withMinimumValue()
                months.add(MonthsDates(prevous.toString("MMM-yyyy"), convert2String(startOfMonthPrevious), convert2String(endOfMonthPrevious)))


            }
            return months


        }

        fun isInMonth(date: String, month: String): Boolean {

            val dateG = conver2Date(date)!!.toString("MMM-yyyy")
            return dateG.equals(month)

        }

        fun getLongDate(dayDateTime: String): Long {
            try {
                val sdf = SimpleDateFormat(FormatSmall)
                val date = sdf.parse(dayDateTime)

                return date.time

            } catch (e: ParseException) {
                e.printStackTrace()
            }

            return 0

        }
    }

}
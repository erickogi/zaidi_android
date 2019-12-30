package com.dev.lishabora.Utils

import android.content.Context
import android.graphics.*
import android.support.design.widget.TextInputEditText
import android.text.TextUtils
import android.util.Base64
import android.util.DisplayMetrics
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.dev.lishabora.Application
import com.dev.lishaboramobile.R
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import java.util.regex.Pattern


class GeneralUtills {

    private val sNextGeneratedId = AtomicInteger(1)
    internal lateinit var path: String
    private val spinner: Spinner? = null
    private val arrayAdapter: ArrayAdapter<*>? = null
    private var downloaded = 0
    internal lateinit var context: Context
    private val bitmapString = ""


    constructor(context: Context) {
        this.context = context
    }


    companion object {

        // var isTablet: Boolean? = null
        var statusBarHeight = 0
        var density = 1f
        var displaySize = Point()
        var roundMessageSize: Int = 0
        var incorrectDisplaySizeFix: Boolean = false
        var photoSize: Int? = null
        var displayMetrics = DisplayMetrics()
        var leftBaseline: Int = 0
        var usingHardwareInput: Boolean = false
        var isInMultiwindow: Boolean = false

        var decelerateInterpolator = DecelerateInterpolator()
        var accelerateInterpolator = AccelerateInterpolator()
        var overshootInterpolator = OvershootInterpolator()

        private var isTablet: Boolean? = null
        private val adjustOwnerClassGuid = 0

        private val roundPaint: Paint? = null
        private val bitmapRect: RectF? = null

        fun createCode(farmerCode: String): String {
            return "F : " + farmerCode + "" + createCode()
        }

        fun createCode(): String {
            return "E : " + PrefrenceManager(Application.context).traderModel.code + " T : " + DateTimeUtils.getNow()
        }

        fun addCommify(no: String): String {
            var commified = "0.0"
            var number: Double? = 0.0
            try {
                number = java.lang.Double.valueOf(no)
            } catch (nfe: Exception) {

            }

            val anotherFormatU = NumberFormat.getNumberInstance(Locale.US)
            if (anotherFormatU is DecimalFormat) {
                anotherFormatU.applyPattern("#.0")
                anotherFormatU.isGroupingUsed = true
                anotherFormatU.groupingSize = 3

                commified = anotherFormatU.format(number)
            }


            return commified
        }

        fun removeCommify(no: String): String {
            val regex = "(?<=[\\d])(,)(?=[\\d])"
            val p = Pattern.compile(regex)
            var str = no
            val m = p.matcher(str)
            str = m.replaceAll("")
            return str
        }

        fun changeCOlor(value: String, view: View, type: Int) {
            var value1 = 0.0
            var txt = view as TextView

            if (type == 1) {
                try {
                    txt = view
                    value1 = java.lang.Double.valueOf(removeCommify(value))

                } catch (nm: Exception) {

                }
            } else if (type == 2) {
                try {
                    txt = view as TextInputEditText
                    value1 = java.lang.Double.valueOf(removeCommify(value))

                } catch (nm: Exception) {

                }
            }

            if (value1 < 0) {

                view.setTextColor(Application.context.resources.getColor(R.color.red))
            } else if (value1 > 0) {
                view.setTextColor(Application.context.resources.getColor(R.color.green_color_picker))

            } else {
                view.setTextColor(Application.context.resources.getColor(R.color.textblack))

            }


        }

        fun isValidPhoneNumber(value: String): Boolean {
            val inputPhoneNumber = value
            var validPhoneNumber: String? = null

            val pattern = Pattern.compile("^(?:254|\\+254|0)?(7(?:(?:[129][0-9])|(?:0[0-8])|(4[0-1]))[0-9]{6})\$")
            val matcher = pattern.matcher(inputPhoneNumber)
            if (matcher.matches()) {
                validPhoneNumber = "254" + matcher.group(1)
                return true
            }
            return true
//
        }

        fun round(value: Double, places: Int): Double {
            if (places < 0) throw IllegalArgumentException()

            var bd = BigDecimal(value)
            bd = bd.setScale(places, RoundingMode.HALF_UP)
            return bd.toDouble()
        }
        fun round(value: String, places: Int): String {

            var d: Double
            try {
                d = java.lang.Double.valueOf(value)


            } catch (ea: Exception) {

                return value
            }

            return java.lang.String.valueOf(round(d, places))
        }

        fun roundD(value: Double, places: Int): String {
            return java.lang.String.valueOf(round(value, places))
        }


        fun capitalize(txt: String): String {
            var txt = txt
            val finalTxt = ArrayList<String>()

            if (txt.contains("_")) {
                txt = txt.replace("_", " ")
            }

            if (txt.contains(" ") && txt.length > 1) {
                val tSS = txt.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                for (tSSV in tSS) {
                    finalTxt.add(capitalize(tSSV))
                }
            }

            if (finalTxt.size > 0) {
                txt = ""
                for (s in finalTxt) {
                    txt += s + " "
                }
            }

            if (txt.endsWith(" ") && txt.length > 1) {
                txt = txt.substring(0, txt.length - 1)
                return txt
            }

            try {
                txt = txt.substring(0, 1).toUpperCase() + txt.substring(1).toLowerCase()
            } catch (e: Exception) {

            }
            return txt
        }

        fun showKeyboard(view: View?) {
            if (view == null) {
                return
            }
            try {
                val inputManager = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
            } catch (e: Exception) {
                FileLog.e(e)
            }

        }

        fun isKeyboardShowed(view: View?): Boolean {
            if (view == null) {
                return false
            }
            try {
                val inputManager = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                return inputManager.isActive(view)
            } catch (e: Exception) {
                FileLog.e(e)
            }

            return false
        }

        fun hideKeyboard(view: View?) {
            if (view == null) {
                return
            }
            try {
                val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                if (!imm.isActive) {
                    return
                }
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            } catch (e: Exception) {
                FileLog.e(e)
            }

        }

        fun runOnUIThread(runnable: Runnable) {
            runOnUIThread(runnable, 0)
        }

        fun runOnUIThread(runnable: Runnable, delay: Long) {
            if (delay == 0L) {
                Application.applicationHandler.post(runnable)
            } else {
                Application.applicationHandler.postDelayed(runnable, delay)
            }
        }

        fun cancelRunOnUIThread(runnable: Runnable) {
            Application.applicationHandler.removeCallbacks(runnable)
        }

        fun isTablet(): Boolean {
            if (isTablet == null) {
                isTablet = Application.context.resources.getBoolean(R.bool.isTablet)
            }
            return isTablet as Boolean
        }

        fun isSmallTablet(): Boolean {
            val minSide = Math.min(displaySize.x, displaySize.y) / density
            return minSide <= 700
        }

        fun getMinTabletSide(): Int {
            if (!isSmallTablet()) {
                val smallSide = Math.min(displaySize.x, displaySize.y)
                var leftSide = smallSide * 35 / 100
                if (leftSide < dp(320F)) {
                    leftSide = dp(320F)
                }
                return smallSide - leftSide
            } else {
                val smallSide = Math.min(displaySize.x, displaySize.y)
                val maxSide = Math.max(displaySize.x, displaySize.y)
                var leftSide = maxSide * 35 / 100
                if (leftSide < dp(320F)) {
                    leftSide = dp(320F)
                }
                return Math.min(smallSide, maxSide - leftSide)
            }
        }

        fun dp(value: Float): Int {
            return if (value == 0f) {
                0
            } else Math.ceil((density * value).toDouble()).toInt()
        }

        fun addToClipboard(str: CharSequence) {
            try {
                val clipboard = Application.context.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                val clip = android.content.ClipData.newPlainText("label", str)
                clipboard.primaryClip = clip
            } catch (e: Exception) {
                FileLog.e(e)
            }

        }

        fun getRandon(): Int {
            val r = Random()
            return r.nextInt(9999 - 1000 + 1) + 1000
        }
    }



    fun getRandon(max: Int, min: Int): Int {
        val r = Random()
        return r.nextInt(max - min + 1) + min
    }

    fun getRandon(): Int {
        val r = Random()
        return r.nextInt(1000 - 999 + 1) + 999
    }

    /**
     * @param target
     * @return
     */
    fun isValidEmail(target: CharSequence): Boolean {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }

    /**
     * @param mobile
     * @return
     */
    fun isValidPhoneNumber(mobile: String): Boolean {
        val regEx = "^[0-9]{10}$"
        return mobile.matches(regEx.toRegex())
    }

    /**
     * @param spinner
     * @param arrayAdapter
     */
    fun setSpinner(spinner: Spinner, arrayAdapter: ArrayAdapter<*>): Spinner {

        spinner.adapter = arrayAdapter

        return spinner

    }

    /**
     * @param spinner
     * @param pos
     * @return
     */
    fun setSpinner(spinner: Spinner, pos: Int): Spinner {

        spinner.setSelection(pos)


        return spinner


    }

    /**
     * @param textInputEditText
     * @return
     */
    fun isFilledTextInputEditText(textInputEditText: TextInputEditText?): Boolean {
        if (textInputEditText == null || textInputEditText.text.toString() == "") {
            if (textInputEditText != null) {
                textInputEditText.error = "Required"
            }
            return false
        }
        return true

    }

    /**
     * @param field
     * @return
     */
    fun validateFields(field: TextInputEditText): Boolean {
        return isValidEmail(field.text.toString()) || isValidPhoneNumber(field.text.toString())

    }

    /**
     * Generate a value suitable for use in .
     * This value will not collide with ID values generated at build time by aapt for R.id.
     *
     * @return a generated ID value
     */
    fun generateViewId(): Int {
        while (true) {
            val result = sNextGeneratedId.get()
            // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
            var newValue = result + 1
            if (newValue > 0x00FFFFFF) newValue = 1 // Roll over to 1, not 0.
            if (sNextGeneratedId.compareAndSet(result, newValue)) {
                return result
            }
        }
    }

    fun isFilledTextInputEditText(edttitle: EditText?): Boolean {
        if (edttitle == null || edttitle.text.toString() == "") {
            if (edttitle != null) {
                edttitle.error = "Required"
            }
            return false
        }
        return true


    }

//    public void upload(Context context,String path, ProgressBar progressBar){
//        Ion.with(context).load( "https://koush.clockworkmod.com/test/echo")
//                .uploadProgressBar(progressBar)
//                .setMultipartFile("filename.pdf", new File(path))
//                .asJsonObject()
//                .setCallback((e, result) -> {
//
//                });
//    }

    fun sanitizePhone(phone: String): String {

        if (phone == "") {
            return ""
        }

        if ((phone.length < 11) and phone.startsWith("0")) {
            return phone.replaceFirst("^0".toRegex(), "254")
        }
        return if (phone.length == 13 && phone.startsWith("+")) {
            phone.replaceFirst("^+".toRegex(), "")
        } else phone
    }

    fun base64ToBitmap(b64: String): Bitmap? {
        try {
            val imageAsBytes = Base64.decode(b64.toByteArray(), Base64.DEFAULT)
            return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.size)
        } catch (nm: Exception) {
            return null
        }

    }

    fun bitmapToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    fun getStringImage(bmp: Bitmap?): String {
        var re = "n"
        try {
            // create lots of objects here and stash them somewhere
            val baos = ByteArrayOutputStream()
            if (bmp != null) {
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val imageBytes = baos.toByteArray()
                re = Base64.encodeToString(imageBytes, Base64.DEFAULT)
            }
        } catch (E: OutOfMemoryError) {

            // release some (all) of the above objects
        }


        //        ByteArrayOutputStream baos = new  ByteArrayOutputStream();
        //        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        //        byte [] b = baos.toByteArray();
        //        return Base64.encodeToString(b, Base64.DEFAULT);
        return re
    }

    fun getStringImage5(bmp: Bitmap): String {
        //        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        //        byte[] imageBytes = baos.toByteArray();
        //        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        //

        val baos = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
        // return encodedImage;
    }

    fun loadImagesFromStorage(path: String, context: Context): Bitmap? {
        var b: Bitmap? = null
        try {
            val f = File(path)
            b = BitmapFactory.decodeStream(FileInputStream(f))

        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            Timber.d(e.toString())
        }

        return b
    }

    fun isEmptyOrNullOrnull(s: String?, msg: String): Boolean {
        if (s != null && !s.isEmpty() && s != "null" && s != "Null") return false
        errorToast(msg)
        return true
    }

    /**
     * @param radioGroup
     * @param msg
     * @param context
     * @return
     */
    fun isSelected_RadioGroup(radioGroup: RadioGroup, msg: String): Boolean {

        if (radioGroup.checkedRadioButtonId > -1) {

            return true
        }
        errorToast(msg)
        return false

    }

    /**
     * @param spinner
     * @param errmsg
     * @return
     */
    fun isSpinnerSelected(spinner: Spinner, errmsg: String): Boolean {
        if (spinner.selectedItemPosition < 1) {
            errorToast(errmsg)
            return false
        }
        return true
    }

    /**
     * @param button
     * @param defaultText
     * @param errMsg
     *
     * @return
     */
    fun isButtonTextChanged(button: Button, defaultText: String, errMsg: String): Boolean {
        if (button.text.toString() == defaultText) {
            errorToast(errMsg)
            return false
        }
        return true
    }

    /**
     * @param msg
     */
    private fun errorToast(msg: String) {
        MyToast.errorToast(msg, context)
    }


}
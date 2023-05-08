package com.example.policyagent.util

import android.app.*
import android.content.*
import android.content.ClipboardManager
import android.graphics.*
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.OpenableColumns
import android.provider.Settings
import android.text.*
import android.util.Base64
import android.util.Base64OutputStream
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.webkit.MimeTypeMap
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.startActivity
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.example.policyagent.BuildConfig
import com.example.policyagent.R
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import java.io.*
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.math.abs
import kotlin.math.ceil


fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}


fun Context.shortToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun EditText.toText():String{
    return this.text.toString().trim()
}

fun ProgressBar.show() {
    visibility = View.VISIBLE
}

fun ProgressBar.hide() {
    visibility = View.GONE
}

fun View.show(){
    visibility = View.VISIBLE
}

fun View.hide(){
    visibility = View.GONE
}

fun View.invisible(){
    visibility = View.INVISIBLE
}

fun getRandom(range: Float, start: Float): Float {
    return (Math.random() * range).toFloat() + start
}

fun getGlideProgress(mContext: Context): CircularProgressDrawable {
    val circularProgressDrawable = CircularProgressDrawable(mContext)
    circularProgressDrawable.strokeWidth = 3f
    circularProgressDrawable.centerRadius = 20f
    circularProgressDrawable.start()
    return circularProgressDrawable
}

fun getFileChooserIntent(): Intent {
    val mimeTypes = arrayOf("image/*", "application/pdf")
    val intent = Intent(Intent.ACTION_GET_CONTENT)
    intent.addCategory(Intent.CATEGORY_OPENABLE)
    intent.type = if (mimeTypes.size == 1) mimeTypes[0] else "*/*"
    if (mimeTypes.size > 0) {
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
    }
    return intent
}

fun openDieler(number: String,context: Context){
    val intent = Intent(Intent.ACTION_DIAL)
    intent.data = Uri.parse("tel:"+number)
    context.startActivity(intent)
}

fun openMail(addresses: String,context: Context) {
    val intent = Intent(Intent.ACTION_SENDTO)
    intent.data = Uri.parse("mailto:") // only email apps should handle this
    intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(addresses))
    intent.putExtra(Intent.EXTRA_SUBJECT, "")
        context.startActivity(intent)
}

fun getFileFromURI(uri: Uri, context: Context): File? {
    val returnCursor = context.contentResolver.query(uri, null, null, null, null)
    val nameIndex =  returnCursor!!.getColumnIndex(OpenableColumns.DISPLAY_NAME)
    val sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE)
    returnCursor.moveToFirst()
    val name = returnCursor.getString(nameIndex)
    val size = returnCursor.getLong(sizeIndex).toString()
    val file = File(context.filesDir, name)
    try {
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        var read = 0
        val maxBufferSize = 1 * 1024 * 1024
        val bytesAvailable: Int = inputStream?.available() ?: 0
        //int bufferSize = 1024;
        val bufferSize = Math.min(bytesAvailable, maxBufferSize)
        val buffers = ByteArray(bufferSize)
        while (inputStream?.read(buffers).also {
                if (it != null) {
                    read = it
                }
            } != -1) {
            outputStream.write(buffers, 0, read)
        }
        Log.e("File Size", "Size " + file.length())
        inputStream?.close()
        outputStream.close()
        Log.e("File Path", "Path " + file.path)

    } catch (e: java.lang.Exception) {
        Log.e("Exception", e.message!!)
    }
    return file
}
fun loadPdf(mContext: Context,url: String){
    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    mContext.startActivity(browserIntent)
}
inline fun <reified T : Any> newIntent(context: Context): Intent = Intent(context, T::class.java)
inline fun <reified T : Any> Activity.launchActivity(
    requestCode: Int = -1,
    options: Bundle? = null,
    noinline init: Intent.() -> Unit = {},
) {
    val intent = newIntent<T>(this)
    intent.init()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
        startActivityForResult(intent, requestCode, options)
    } else {
        startActivityForResult(intent, requestCode)
    }
}

inline fun <reified T : Any> Activity.launchLoginActivity(
    requestCode: Int = -1,
    options: Bundle? = null,
    noinline init: Intent.() -> Unit = {},
) {
    val intent = newIntent<T>(this)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
    startActivity(intent)
}

fun getDeviceId(mContext: Context):String{
    var m_androidId  = ""
    if (Settings.Secure.getString(mContext.contentResolver, Settings.Secure.ANDROID_ID) != null) {
        m_androidId = Settings.Secure.getString(
            mContext.contentResolver,
            Settings.Secure.ANDROID_ID
        )
    }
    return m_androidId
}


fun getAppVersion():String{
    var m_androidId  = BuildConfig.VERSION_NAME
    return m_androidId
}

fun isValidString(str: String?):Boolean{
    return str != null && !str.isEmpty() && !str.equals("null")
}

fun View.snackbar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_LONG).also { snackbar ->
        snackbar.setAction("Ok") {
            snackbar.dismiss()
        }
    }.show()
}


fun getBitmapImage(finalBitmap: Bitmap, mContext: Context): File {
    val root = mContext.getExternalFilesDir(null)!!.absolutePath.toString()
    val myDir = File(root + AppConstants.APPDIRECTORY)
    myDir.mkdirs()
    val generator = Random()
    var n = 10000
    n = generator.nextInt(n)
    val fname = "Image-" + n + ".jpg"
    val file = File(myDir, fname)
    // if (file.exists()) file.delete();
    try {
        val out = FileOutputStream(file)
        finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
        out.flush()
        out.close()
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return file
}

fun getModelValuesFromString(body: String, java: Class<*>): Any {
    val gson = Gson()
    val category = gson.fromJson(body, java::class.java)
    Log.d(java.javaClass.simpleName, " : " + category)
    return category
}


val EMAIL_REGEX = "[a-zA-Z0-9._-]+@[a-z]+\\.[a-z]+"
fun isEmailValid(email: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

val ACCOUNT_NUMBER_REGEX = "^[0-9]{9,18}"
fun isValidAccountNumber(str: String): Boolean {
    return isRegexMatched(str, ACCOUNT_NUMBER_REGEX)
}

val IFSC_CODE_REGEX = "^[A-Za-z]{4}[A-Z0-9]{7}\$"
fun isIFSCCodeValid(str: String): Boolean {
    return isRegexMatched(str, IFSC_CODE_REGEX)
}

val PAN_NUMBER_REGEX = "^[A-Z]{5}[0-9]{4}[A-Z]{1}"
fun isPanNumberValid(str: String): Boolean {
    return isRegexMatched(str, PAN_NUMBER_REGEX)
}

fun isRegexMatched(str:String,regex:String):Boolean{
    val p = Pattern.compile(regex)

    // If the string is empty
    // return false

    // If the string is empty
    // return false
    if (str.isEmpty()) {
        return false
    }

    // Pattern class contains matcher()
    // method to find matching between
    // the given string and
    // the regular expression.

    // Pattern class contains matcher()
    // method to find matching between
    // the given string and
    // the regular expression.
    val m: Matcher = p.matcher(str)
    // Return if the string
    // matched the ReGex
    return m.matches();
}


fun isValidPhoneNumber(phoneNumber: String): Boolean {
    return if (!TextUtils.isEmpty(phoneNumber)) {
        Patterns.PHONE.matcher(phoneNumber).matches()
    } else false
}

/*fun isVisibleOrNot(editText: EditText, imageView: ImageView) {
    Log.e("Check : ", editText.inputType.toString())
    if (editText.inputType == InputType.TYPE_CLASS_NUMBER) {
        editText.inputType = InputType.TYPE_NUMBER_VARIATION_NORMAL
//        editText.inputType = InputType.TYPE_CLASS_NUMBER
        editText.transformationMethod = PasswordTransformationMethod()
        editText.setSelection(editText.text.length)
        imageView.setImageResource(R.drawable.eyes)
    } else {
        editText.inputType = InputType.TYPE_CLASS_NUMBER
        editText.transformationMethod = null
        editText.setSelection(editText.text.length)
        imageView.setImageResource(R.drawable.close_eyes)
    }
}*/

fun showAlertMessage(mContext: Context, message: String) {
    // build alert dialog
    val dialogBuilder = AlertDialog.Builder(mContext)

    // set message of alert dialog
    dialogBuilder.setMessage(message)
        // if the dialog is cancelable
        .setCancelable(false)
        // positive button text and action
        .setPositiveButton("Ok", DialogInterface.OnClickListener { dialog, id ->
            dialog.cancel()
        })
    // create dialog box
    val alert = dialogBuilder.create()
    // set title for alert dialog box
    alert.setTitle("Alert")
    // com.example.policyagent.data.util.com.example.policyagent.data.util.show alert dialog
    alert.show()
}

fun convertToBitmap(view: View, defaultColor: Int): Bitmap {
    val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    canvas.drawColor(defaultColor)
    view.draw(canvas)
    return bitmap
}

fun saveBitmapImage(mContext: Context, myBitmapImage: Bitmap): String {
    val myBitmap =   getResizedBitmap(myBitmapImage, AppConstants.MAX_SIZE)
//    val myBitmap = Bitmap.createScaledBitmap(myBitmap, 800, 800, false)
    val bytes = ByteArrayOutputStream()
    myBitmap.compress(Bitmap.CompressFormat.JPEG, 60, bytes)
    val imagePAth =
        mContext.getExternalFilesDir(null)!!.absolutePath.toString() + AppConstants.APPDIRECTORY
    val wallpaperDirectory = File(imagePAth)
    // have the object build the directory structure, if needed.
    Log.d("fee", wallpaperDirectory.toString())
    if (!wallpaperDirectory.exists()) {
        wallpaperDirectory.mkdirs()
    }
    try {
        Log.d("heel", wallpaperDirectory.toString())
        val f = File(
            wallpaperDirectory, Calendar.getInstance()
                .timeInMillis.toString() + ".jpg"
        )
        f.createNewFile()
        val fo = FileOutputStream(f)
        fo.write(bytes.toByteArray())
        MediaScannerConnection.scanFile(
            mContext,
            arrayOf(f.path),
            arrayOf("image/jpeg"), null
        )
        fo.close()
        mContext.toast("File Saved::--->" + f.absolutePath)
        Log.d("TAG", "File Saved::--->" + f.absolutePath)

        return f.absolutePath
    } catch (e1: IOException) {
        e1.printStackTrace()
    }

    return ""
}



fun showErrorMessage(message: String, context: Context) {
    // build alert dialog
    val dialogBuilder = AlertDialog.Builder(context)
    // set message of alert dialog
    dialogBuilder.setMessage(message)
        // if the dialog is cancelable
        .setCancelable(false)
        // positive button text and action
        .setPositiveButton(android.R.string.ok, DialogInterface.OnClickListener { dialog, id ->
            dialog.cancel()
        })
    // create dialog box
    val alert = dialogBuilder.create()
    // set title for alert dialog box
    alert.setTitle(android.R.string.dialog_alert_title)
    // com.example.policyagent.data.util.com.example.policyagent.data.util.show alert dialog
    alert.show()
}

fun share(text: String?, context: Context) {
    val i = Intent(Intent.ACTION_SEND)
    i.type = "text/plain"
    i.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.app_name))
    i.putExtra(Intent.EXTRA_TEXT, text)
    context.startActivity(Intent.createChooser(i, "Share via"))
}

fun setClipboard(context: Context, text: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("Reciept Detail", text)
    clipboard.setPrimaryClip(clip)
    printLog("clipboard", clipboard.primaryClip!!.toString())
}

fun printLog(s: String, toString: String) {
    if (BuildConfig.DEBUG){
        Log.e(s + "  :  ", toString)
    }
}


fun rateApp(mContext: Context) {
    try {
        val playstoreuri1: Uri = Uri.parse("market://details?id=" + mContext.packageName)
        val playstoreIntent1: Intent = Intent(Intent.ACTION_VIEW, playstoreuri1)
        mContext.startActivity(playstoreIntent1)
        
    } catch (exp: Exception) {
        val playstoreuri2: Uri =
            Uri.parse("http://play.google.com/store/apps/details?id=" + mContext.packageName)
        val playstoreIntent2: Intent = Intent(Intent.ACTION_VIEW, playstoreuri2)
        mContext.startActivity(playstoreIntent2)
    }
}


fun isValidMail(email: String): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

fun isValidMobile(phone: String): Boolean {
    return Patterns.PHONE.matcher(phone).matches()
}
fun getResizedBitmap(image: Bitmap, maxSize: Int):Bitmap {
    var width = image.width
    var height = image.height
    val bitmapRatio = width.toFloat() / height.toFloat()
    if (bitmapRatio > 1)
    {
//        width = maxSize
        height = (width / bitmapRatio).toInt()
    }
    else
    {
        height = maxSize
        width = (height * bitmapRatio).toInt()
    }
    return Bitmap.createScaledBitmap(image, width, height, true)
}

fun getLocationMode(context: Context):Int {
    return Settings.Secure.getInt(context.contentResolver, Settings.Secure.LOCATION_MODE)
}
fun getAge(year: Int, month: Int, day: Int): Int {
    //calculating age from dob
    val dob = Calendar.getInstance()
    val today = Calendar.getInstance()
    dob[year, month] = day
    var age = today[Calendar.YEAR] - dob[Calendar.YEAR]
    if (today[Calendar.DAY_OF_YEAR] < dob[Calendar.DAY_OF_YEAR]) {
        age--
    }
    return age
}

fun dateDifference(startDate: Date, endDate: Date): Int {
    var year = abs((startDate.time - endDate.time) / 86400000 / 365)
    return ceil(year.toDouble()).toInt()
}

fun strinToBitmap(str: String):Bitmap{
    val decodedString: ByteArray = android.util.Base64.decode(str, android.util.Base64.DEFAULT)
    val decodedByte: Bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
    return decodedByte
}



fun RotateBitmap(source: Bitmap, angle: Float):Bitmap {
    val matrix = Matrix()
    matrix.postRotate(angle)
    return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
}

fun turnGPSOn(mContext: Context) {
     try{
         val intent = Intent("android.location.GPS_ENABLED_CHANGE")
         intent.putExtra("enabled", true)
         mContext.sendBroadcast(intent)
     }catch (ex: Exception){
         ex.printStackTrace()
     }
//    val provider = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED)
//    if (!provider.contains("gps"))
//    { //if gps is disabled
//        val poke = Intent()
//        poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider")
//        poke.addCategory(Intent.CATEGORY_ALTERNATIVE)
//        poke.setData(Uri.parse("3"))
//        mContext.sendBroadcast(poke)
//    }
}

fun turnGPSOff(mContext: Context) {
    val provider = Settings.Secure.getString(
        mContext.contentResolver,
        Settings.Secure.LOCATION_PROVIDERS_ALLOWED
    )
    if (provider.contains("gps"))
    { //if gps is enabled
        val poke = Intent()
        poke.setClassName(
            "com.android.settings",
            "com.android.settings.widget.SettingsAppWidgetProvider"
        )
        poke.addCategory(Intent.CATEGORY_ALTERNATIVE)
        poke.data = Uri.parse("3")
        mContext.sendBroadcast(poke)
    }
}

fun openWebUrl(mContext: Context, url: String){
    try{
        var mFinalUrl = url
        if (!mFinalUrl.startsWith("http://") && !mFinalUrl.startsWith("https://"))
            mFinalUrl = "https://" + mFinalUrl;
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(mFinalUrl))
        mContext.startActivity(browserIntent)
    }catch (ex: java.lang.Exception){
        ex.printStackTrace()
    }
}

fun calculatePercentage(obtained: Double, total: Double): Double {
    return total * obtained / 100
}

fun getPercentFromValue(commmitionValue: Double, totalValue: Double): Double {
    return commmitionValue*(100/totalValue)
}

fun calculatePercentageInString(obtained: Double, total: Double): Double {
    return total * obtained / 100
}

fun getTwoDecimalString(value: Double):String{
    return value.toBigDecimal().setScale(3, RoundingMode.UP).toString()
}

fun getTwoDecimalRechargeString(value: Double):String{
    return value.toBigDecimal().setScale(2, RoundingMode.UP).toString()
}

fun getTwoDecimalDouble(value: Double):Double{
    return value.toBigDecimal().setScale(2, RoundingMode.UP).toDouble()
}

// Function to remove duplicates from an ArrayList
fun <T> removeDuplicates(list: ArrayList<T>):ArrayList<T> {
     // Create a new LinkedHashSet
     val set = LinkedHashSet<T>()
     // Add the elements to set
     set.addAll(list)
     // Clear the list
     list.clear()
     // add the elements of set
     // with no duplicates to the list
     list.addAll(set)
     // return the list
     return list
 }

fun distance(lat1: Double, lon1: Double, lat2: Double, lon2: Double):Double {
    val theta = lon1 - lon2
    var dist = ((Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2))) + (Math.cos(deg2rad(lat1))
            * Math.cos(deg2rad(lat2))
            * Math.cos(deg2rad(theta))))
    dist = Math.acos(dist)
    dist = rad2deg(dist)
    dist = dist * 60.0 * 1.1515
    return (dist)
}

private fun deg2rad(deg: Double):Double {
    return (deg * Math.PI / 180.0)
}

private fun rad2deg(rad: Double):Double {
    return (rad * 180.0 / Math.PI)
}

fun callFromUtil(mContext: Context, mobile: String){
//Dialer intent
    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Uri.encode(mobile)))
    mContext.startActivity(intent)
}


fun round(value: Double, numberOfDigitsAfterDecimalPoint: Int):Double {
    var bigDecimal = BigDecimal(value)
    bigDecimal = bigDecimal.setScale(
        numberOfDigitsAfterDecimalPoint,
        BigDecimal.ROUND_HALF_UP
    )
    return bigDecimal.toDouble()
}

@Suppress("DEPRECATION")
fun changeLanguage(mContext: Context, language: String){
    val locale = Locale(language)
    Locale.setDefault(locale)
    val resources = mContext.resources
    val configuration = resources.configuration
    configuration.locale = locale
    resources.updateConfiguration(configuration, resources.displayMetrics)
}

fun sendMessage(msg: String, mContext: Context) {
    try
    {
        val smsIntent = Intent(Intent.ACTION_VIEW)
        smsIntent.type = "vnd.android-dir/mms-sms"
        smsIntent.putExtra("sms_body", msg)
        mContext.startActivity(smsIntent)
    }
    catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(mContext, "No SIM Found", Toast.LENGTH_LONG).show()
    }
}

/** Returns the consumer friendly device name */
fun getDeviceName():String {
    val manufacturer = Build.MANUFACTURER
    val model = Build.MODEL
    val base_operating_system =  "${Build.VERSION.BASE_OS}, ${Build.VERSION.CODENAME}, ${Build.VERSION.RELEASE}, ${Build.VERSION.SDK_INT}, ${Build.VERSION_CODES.BASE}, ${Build.DISPLAY}"
    if (model.startsWith(manufacturer))
    {
        return capitalize(model)
    }
    return capitalize(manufacturer) + ", " + model + ", "+base_operating_system
}

private fun capitalize(str: String):String {
    if (TextUtils.isEmpty(str))
    {
        return str
    }
    val arr = str.toCharArray()
    var capitalizeNext = true
    val phrase = StringBuilder()
    for (c in arr)
    {
        if (capitalizeNext && Character.isLetter(c))
        {
            phrase.append(Character.toUpperCase(c))
            capitalizeNext = false
            continue
        }
        else if (Character.isWhitespace(c))
        {
            capitalizeNext = true
        }
        phrase.append(c)
    }
    return phrase.toString()
}




fun isValidPhone(phone: String): Boolean{
    var isVAlid = true
    if (phone.isEmpty() || phone.length < 10 || phone.length > 10){
        isVAlid = false
    }
    return isVAlid
}

fun isValidPhoneWihoutZero(phone: String): Boolean{
    var isVAlid = false
    if (!phone.isEmpty() && !phone.startsWith("0")){
        isVAlid = true
    }
    return isVAlid
}

fun isValidPassword(password: String):Boolean{
    var isVAlid = true
    if (password.isEmpty() || password.trim().length < 6){
        isVAlid = false
    }
    return isVAlid
}


fun getMimeType(url: String?): String? {
    var type: String? = null
    val extension: String = MimeTypeMap.getFileExtensionFromUrl(url)
    if (extension != null) {
        type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
    }
    return type
}

fun encode(bitmap: String): String {
    val base64 =  convertImageFileToBase64(File(bitmap))
//    com.example.policyagent.data.util.com.example.policyagent.data.util.printLog("com.example.policyagent.data.util.com.example.policyagent.data.util.encode","  ->   "+base64)
    return base64
}

fun convertImageFileToBase64(imageFile: File): String {
    return FileInputStream(imageFile).use { inputStream ->
        ByteArrayOutputStream().use { outputStream ->
            Base64OutputStream(outputStream, Base64.DEFAULT).use { base64FilterStream ->
                inputStream.copyTo(base64FilterStream)
                base64FilterStream.close() // This line is required, see comments
                outputStream.toString()
            }
        }
    }
}



/*fun openDateOfBirthCalendar(
    typeDob: String,
    mContext: Context,
    listener: DateImageListener,
    dateFormat: String
) {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val dateDlg = DatePickerDialog(mContext, object : DatePickerDialog.OnDateSetListener {
        override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int) {
            val chosenDate = Time()
            chosenDate.set(dayOfMonth, monthOfYear, year)
            val dtDob = chosenDate.toMillis(true)
            val strDate = DateFormat.format(dateFormat, dtDob)
            if (typeDob.equals(AppConstants.TYPE_DOB)) {
                val currentYear = calendar.get(Calendar.YEAR)
                val currentMonth = calendar.get(Calendar.MONTH)
                val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
                val actualYear = currentYear - year
                val actualMOnth = currentMonth - monthOfYear
                val actualDay = currentDay - dayOfMonth
                if (actualYear > 18) {
                    listener.onDateSelected(strDate.toString())
                    printLog(
                        "Age1 : ",
                        "  ->  ${currentYear}-${currentMonth}-${currentDay}  -> ${strDate} "
                    )
                } else if (actualYear >= 18 && actualMOnth > 0) {
                    listener.onDateSelected(strDate.toString())
                    printLog(
                        "Age2 : ",
                        "  ->  ${currentYear}-${currentMonth}-${currentDay}  -> ${strDate} "
                    )
                } else if (actualYear >= 18 && actualMOnth == 0 && actualDay >= 0) {
                    listener.onDateSelected(strDate.toString())
                    printLog(
                        "Age3 : ",
                        "  ->  ${currentYear}-${currentMonth}-${currentDay}  -> ${strDate} "
                    )
                } else {
                    printLog(
                        "Age4 : ",
                        "  ->  ${currentYear}-${currentMonth}-${currentDay}  -> ${strDate} "
                    )
                    showAppErrorMessage(
                        mContext.getString(R.string.minimum_age_should_be),
                        mContext
                    )
                }
            } else {
                listener.onDateSelected(strDate.toString())
            }
        }
    }, year, month, day)
    if (typeDob.toString().equals(TYPE_EXPIRY)){
        dateDlg.datePicker.minDate = System.currentTimeMillis() - 1000
    }else if (typeDob.toString().equals(TYPE_DOB)){
        dateDlg.datePicker.maxDate = System.currentTimeMillis() - 1000
    }
    dateDlg.show()
}*/

/*fun openDateOfBirthCalendar1(
    typeDob: String,
    mContext: Context,
    listener: DateImageListener,
    dateFormat: String
) {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val dateDlg = DatePickerDialog(mContext, object : DatePickerDialog.OnDateSetListener {
        override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int) {
            val chosenDate = Time()
            chosenDate.set(dayOfMonth, monthOfYear, year)
            val dtDob = chosenDate.toMillis(true)
            val strDate = DateFormat.format(dateFormat, dtDob)
            listener.onDateSelected(strDate.toString())
        }
    }, year, month, day)
    if (typeDob.toString().equals(TYPE_EXPIRY)){
        dateDlg.datePicker.minDate = System.currentTimeMillis() - 1000
    }else if (typeDob.toString().equals(TYPE_DOB)){
        dateDlg.datePicker.maxDate = System.currentTimeMillis() - 1000
    }
    dateDlg.show()
}*/

/*@SuppressLint("SimpleDateFormat")
fun openTimePicker(context: Context, format: String, listenr: DateImageListener){
    val cal = Calendar.getInstance()
    val startHours = cal.get(Calendar.HOUR_OF_DAY)
    val startMinute = cal.get(Calendar.MINUTE)
    val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
        cal.set(Calendar.HOUR_OF_DAY, hour)
        cal.set(Calendar.MINUTE, minute)
        val simpleDateTimeFormat = SimpleDateFormat(format)
        val time = simpleDateTimeFormat.format(cal.time)
        val timeInDate = simpleDateTimeFormat.parse(time)!!
        *//*Always Open*//*
        listenr.onTimeSelected(time)

    }
    TimePickerDialog(
        context,
        timeSetListener,
        startHours,
        startMinute,
        false
    ).show()

}*/

fun getStringSizeLengthFile(size: Long):String {
    val df = DecimalFormat("0.00")
    val sizeKb = 1024.0f
    val sizeMb = sizeKb * sizeKb
    val sizeGb = sizeMb * sizeKb
    val sizeTerra = sizeGb * sizeKb
    if (size < sizeMb)
        return df.format(size / sizeKb) + " Kb"
    else if (size < sizeGb)
        return df.format(size / sizeMb) + " Mb"
    else if (size < sizeTerra)
        return df.format(size / sizeGb) + " Gb"
    return ""
}

fun getSizeinKB(size: Long):String {
    val df = DecimalFormat("0.00")
    val sizeKb = 1024.0f
    val sizeMb = sizeKb * sizeKb
    val sizeGb = sizeMb * sizeKb
    val sizeTerra = sizeGb * sizeKb
//    if (size < sizeMb)
        return df.format(size / sizeKb)
//    else if (size < sizeGb)
//        return df.format(size / sizeMb) + " Mb"
//    else if (size < sizeTerra)
//        return df.format(size / sizeGb) + " Gb"
//    return ""
}

fun hideKeyboard(activity: Activity) {
    try {
        val view = activity.currentFocus
        val methodManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (view!!.windowToken != null) {
            methodManager.hideSoftInputFromWindow(
                view.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }catch (ex: java.lang.Exception){
        ex.printStackTrace()
    }
}

fun showKeyboard(activity: Activity) {
    try{
    val view = activity.currentFocus
    val methodManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    methodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }catch (ex: java.lang.Exception){
        ex.printStackTrace()
    }
}

/*fun goToWhatsApp(number: String, mContext: Context){
    try {
        val whatSAppUrl = "https://api.whatsapp.com/send/?phone=${number}&text&app_absent=0"
        openCustomWebIntent(mContext, whatSAppUrl)
    }catch (ex: java.lang.Exception){
            if (ex.message != null && !ex.message!!.isEmpty()){
                showAppErrorMessage(ex.message!!, mContext)
            }
        ex.printStackTrace()
    }
}*/



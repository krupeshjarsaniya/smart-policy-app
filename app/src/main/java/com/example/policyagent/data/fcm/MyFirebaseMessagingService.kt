/*
package com.example.policyagent.data.fcm
import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.provider.Settings
import android.widget.ImageView
import androidx.annotation.Nullable
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.example.policyagent.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.recharge.taka24.R
import com.recharge.taka24.data.preferences.com.example.policyagent.data.preferences.PreferenceProvider
import com.recharge.taka24.data.responses.NoticeItemData
import com.recharge.taka24.data.responses.NotificationCounter
import com.recharge.taka24.data.responses.WebView.FreechargeResponseNew
import com.recharge.taka24.data.responses.home.Categories
import com.recharge.taka24.data.responses.home.HomeData
import com.recharge.taka24.ui.activity.HomeActivity
import com.recharge.taka24.ui.factory.MainViewModelFactory
import com.recharge.taka24.ui.listener.HomeFragmentListener
import com.recharge.taka24.ui.viewmodel.HomeFragmentViewModel
import com.recharge.taka24.util.com.example.policyagent.data.util.AppConstants
import com.recharge.taka24.util.com.example.policyagent.data.util.com.example.policyagent.data.util.isValidString
import com.recharge.taka24.util.com.example.policyagent.data.util.com.example.policyagent.data.util.printLog
import org.json.JSONObject
import org.kodein.di.KodeinAware

import java.util.*


open class MyFirebaseMessagingService : FirebaseMessagingService(), HomeFragmentListener,
    KodeinAware {
    override val kodein by kodein()
    private var notificationManager : NotificationManager?=null
    private var CHANNELID = "All"
    private val factory: MainViewModelFactory by instance()
    private lateinit var viewModel: HomeFragmentViewModel

    companion object{
        var myFirebaseMessagingService : MyFirebaseMessagingService?=null
    }

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
        viewModel = ViewModelProvider(HomeActivity.homeActivity, factory).get(HomeFragmentViewModel::class.java)
        viewModel.authListener = this
        myFirebaseMessagingService = this
        try{
//            viewModel.hitApi()
            viewModel.getNotificationCounter()
            notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            com.example.policyagent.data.util.com.example.policyagent.data.util.printLog("onData : ", "" + Gson().toJson(p0.data) + "\n\n")
            com.example.policyagent.data.util.com.example.policyagent.data.util.printLog("onNotification : ", "" + Gson().toJson(p0.notification) + "\n\n")
            if (p0.data != null) {
                val data = Gson().toJson(p0.data)
                com.example.policyagent.data.util.com.example.policyagent.data.util.printLog("onData : ", "" + data + "\n\n")
                    val jsonObject = JSONObject(data.toString())
                if (jsonObject.has("type")) {
                    if (jsonObject.getString("type").equals("custom_message") && com.example.policyagent.data.util.com.example.policyagent.data.util.isValidString(
                            jsonObject.getString("image")
                        )
                    ) {
                        showImageNotification(jsonObject)
                    }else{
                        com.example.policyagent.data.util.com.example.policyagent.data.util.showNotification(jsonObject, BitmapFactory.decodeResource(resources, R.drawable.ic_logo),null)
                    }
                }else {
                    com.example.policyagent.data.util.com.example.policyagent.data.util.showNotification(jsonObject, BitmapFactory.decodeResource(resources, R.drawable.ic_logo),null)
                }
            }
        }catch (ex: Exception){
            ex.printStackTrace()
        }
    }

    fun getInstance(): MyFirebaseMessagingService {
        if (myFirebaseMessagingService != null){
            return myFirebaseMessagingService!!
        }else{
            return MyFirebaseMessagingService()
        }
    }

    private fun showImageNotification(data: JSONObject){
        var url = ""
        if(!data.getString("image").toString().startsWith("http")){
            url =  com.example.policyagent.data.util.AppConstants.getImageBaseUrl() + data.getString("image")
        }else{
            url =  data.getString("image")
        }

        com.example.policyagent.data.util.com.example.policyagent.data.util.printLog("Image Notification", " ->  ${url}")
        Glide.with(this).asBitmap().load(url).into(object : CustomTarget<Bitmap?>() {
            override fun onResourceReady(
                resource: Bitmap,
                transition: com.bumptech.glide.request.transition.Transition<in Bitmap?>?
            ) {
                com.example.policyagent.data.util.com.example.policyagent.data.util.showNotification(data,BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher),resource)
            }
            override fun onLoadCleared(@Nullable placeholder: Drawable?) {

            }
        })
    }

    private fun com.example.policyagent.data.util.com.example.policyagent.data.util.showNotification(data: JSONObject,bitmap: Bitmap,imageBitmap: Bitmap?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNELID,
                CHANNELID,
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationChannel.enableLights(true)
            notificationChannel.enableVibration(true)
            notificationChannel.setVibrationPattern(longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400))
            notificationChannel.setShowBadge(true)
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC)
            notificationManager!!.createNotificationChannel(notificationChannel)
            val list = ArrayList<NotificationChannelGroup>()
            list.add(NotificationChannelGroup(CHANNELID, CHANNELID))
            notificationManager!!.createNotificationChannelGroups(list)
        }
        val intent = Intent(this, HomeActivity::class.java)
        intent.putExtra(com.example.policyagent.data.util.AppConstants.FROM_NOTIFICATION, com.example.policyagent.data.util.AppConstants.FROM_NOTIFICATION)
        if (data.has("type")) {
            if (data.getString("type").equals("admin_chat")) {
                intent.putExtra(com.example.policyagent.data.util.AppConstants.FROM, com.example.policyagent.data.util.AppConstants.CHAT)
            }
        }
//        intent.putExtra(com.example.policyagent.data.util.AppConstants.FROM_NOTIFICATION,data.get("messageId"))
//        intent.putExtra(com.example.policyagent.data.util.AppConstants.NOTIFICATION_ID,getRequestCode(data.get("messageId")))
        val contentIntent = PendingIntent.getActivity(this, getRequestCode(CHANNELID), intent, 0)
//        val uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.restaurant_receive_order)
        val pattern = longArrayOf(500, 500, 500, 500, 500, 500, 500, 500, 500)

        //Assign BigText style notification
        //Assign BigText style notification
        val bigText = NotificationCompat.BigTextStyle()
        if (data.has("text")) {
            if(data.getString("text") != null) {
                bigText.bigText(data.getString("text"))
            }
            else{
                bigText.bigText("")
            }
        }
        if(bigText.setSummaryText(data.getString("title")) != null){
            bigText.setSummaryText(data.getString("title"))
        }
        else{
            bigText.setSummaryText("")
        }

        val bigImageStyle = NotificationCompat.BigPictureStyle()
        if (imageBitmap != null) {
            bigImageStyle.bigPicture(imageBitmap)
        }
        if(bigImageStyle.setBigContentTitle(data.getString("title")) != null){
            bigImageStyle.setBigContentTitle(data.getString("title"))
        }
        else{
            bigImageStyle.setBigContentTitle("")
        }


        val notification = NotificationCompat.Builder(this, CHANNELID)
            .setContentTitle(data.getString("title"))
            .setContentText(data.getString("text"))
            .setContentIntent(contentIntent)
             .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(Notification.DEFAULT_ALL)
            .setAutoCancel(true)
            .setStyle(bigText)
            .setLights(Color.BLUE, 500, 500)
            .setVibrate(pattern)
//            .setSound(uri)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setLargeIcon(bitmap)
                if (imageBitmap != null) {
                    notification.setStyle(bigImageStyle)
                }

        notificationManager!!.notify(getRequestCode(CHANNELID), notification.build())
    }

    fun getChannel() : MutableList<NotificationChannel>? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return  notificationManager!!.notificationChannels
        } else {

        }

        return null!!
    }

    fun clearNotification(id: Int){
        try {
            notificationManager!!.cancel(id)
        }catch (ex: Exception){
            ex.printStackTrace()
        }
    }

    fun deleteChannel(channel_id: String){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager!!.deleteNotificationChannel("channel_id")
        }
    }

    fun goToChannelIntent(channel_id: String){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val intent = Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS)
            intent.putExtra(Settings.EXTRA_CHANNEL_ID, channel_id)
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
            startActivity(intent)
        }

    }

    fun getRequestCode(get: String?):Int{
        if (get!!.equals(CHANNELID)) {
            val r = Random()
            val i1 = r.nextInt(45 - 28) + 28
            return i1
        }else if (get.equals(CHANNELID)){
            return 36
        }
        else if (get.equals(CHANNELID)){
            return 37
        }else{
            return 38
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        com.example.policyagent.data.preferences.PreferenceProvider(this).setStringValue(com.example.policyagent.data.util.AppConstants.FCM_TOKEN, token)
        com.example.policyagent.data.util.com.example.policyagent.data.util.printLog(this::class.java.simpleName, "Token -> " + token)
    }

    override fun onStarted() {
        
    }

    override fun onSuccess(homeData: HomeData) {
        
    }

    override fun onSuccessBillPay(homeData: ArrayList<Categories>) {
        
    }

    override fun onCategoryClick(position: Int, subPosition: Int) {
        
    }

    override fun onStaticCategoryClick(position: Int) {
        
    }

    override fun onFailure(message: String) {
        
    }

    override fun onToastMessage(message: String) {
        
    }

    override fun onLogOutError(message: String) {
        
    }

    override fun onLoadImage(imageUrl: String, imageView: ImageView) {
        
    }

    override fun onGetActiveNotices(it: ArrayList<NoticeItemData>?) {
        
    }

    override fun onArrowClick(type: String) {
        
    }

    override fun onResponse(type: FreechargeResponseNew) {
        
    }

    override fun onResponseNotification(notification: NotificationCounter) {

    }

    override fun onClickShare() {
        
    }


}
*/

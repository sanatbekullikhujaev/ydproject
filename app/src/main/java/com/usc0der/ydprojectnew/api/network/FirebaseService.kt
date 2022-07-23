package com.usc0der.ydprojectnew.api.network

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_ONE_SHOT
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.usc0der.ydprojectnew.api.di.MyHiltApp
import com.usc0der.ydprojectnew.R
import com.usc0der.ydprojectnew.SplashActivity
import com.usc0der.ydprojectnew.app.App
import com.usc0der.ydprojectnew.utils.SharedPref
import kotlin.random.Random


private const val CHANNEL_ID = "channelId"
private const val CHANNEL_NAME = "channelName"

class FirebaseService : FirebaseMessagingService() {
    private lateinit var pref: SharedPref
    override fun onNewToken(newToken: String) {
        super.onNewToken(newToken)
        pref = SharedPref(MyHiltApp().baseContext)
        pref.device_token = newToken
    }
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        pref = SharedPref(App.mApp)
        var title = ""
        var body = ""
        var type = ""
        type = message.data["type"].toString()
        body = message.data["body"].toString()
        title = message.data["title"].toString()
        pref.deviceDate=title
//        EventBus.getDefault().post(title)


        val intent = Intent(this, SplashActivity::class.java)
        val requestCode: Int = (0..10).random()
        val pendingIntent = PendingIntent.getActivity(this, requestCode, intent, FLAG_ONE_SHOT)
        val bigStyle = NotificationCompat.BigTextStyle()
        bigStyle.setBigContentTitle(title)
            .bigText(body)

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher_round))
            .setStyle(bigStyle)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            createNotificationChannel(manager)
        val notificationId = Random.nextInt()
        manager.notify(notificationId, notification)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(manager: NotificationManager) {
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "new-age"
            lightColor = Color.RED
            enableLights(true)
        }
        manager.createNotificationChannel(channel)
    }
}
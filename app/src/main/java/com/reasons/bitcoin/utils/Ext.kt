package com.reasons.bitcoin.utils

import android.annotation.SuppressLint
import android.app.*
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.reasons.bitcoin.view.MainActivity
import com.reasons.bitcoin.R
import com.reasons.bitcoin.service.BitconService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit



fun getRetrofitObject(): Retrofit.Builder {
    return Retrofit.Builder()
}

fun getBitcoinService(): BitconService {
    val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()
    val retrofit = getRetrofitObject()
    return retrofit.baseUrl("https://api.coindesk.com")
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .addConverterFactory(MoshiConverterFactory.create())
        .client(okHttpClient)
        .build()
        .create(BitconService::class.java)

}

fun getCurrentTimeStamp() = System.currentTimeMillis()
fun getTargetReachedNotificationTitle() = "Hurry!! Target reached"
fun getTargetReachedNotificationText() = "Updates available now !!"

fun getNotificationTitle() = "Check latest updates!!"
fun getNotificationText() = "Check out right now !!"

fun getNotificationChannelDescription() = "BitCoin Channel description"

fun Service.notificationForOldVersionDialog(isTargetReached: Boolean = false) {
    val pi = PendingIntent.getActivity(this, 0, Intent(this, MainActivity::class.java), 0)


    if (isTargetReached) {
        val notification: Notification = NotificationCompat.Builder(this)
            .setTicker(getString(R.string.app_name))
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle(getTargetReachedNotificationTitle())
            .setContentText(getTargetReachedNotificationText())
            .setContentIntent(pi)
            .build()

        val notificationManager =
            getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager


        notificationManager.notify(1, notification)
    } else {
        val notification: Notification = NotificationCompat.Builder(this)
            .setTicker(getString(R.string.app_name))
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle(getNotificationTitle())
            .setContentText(getNotificationText())
            .setContentIntent(pi)
            .build()
        startForeground(1, notification)

    }
}

fun Service.notificationForNewVersionDialog(
    isTargetReached: Boolean = false
) {
    val notificationManager = getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager
    val NOTIFICATION_CHANNEL_ID = "bitcoin_1"
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        @SuppressLint("WrongConstant") val notificationChannel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            "Bitcoin Notifications",
            NotificationManager.IMPORTANCE_MAX
        )
        // Configure the notification channel.
        notificationChannel.description = getNotificationChannelDescription()
        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.RED
        notificationChannel.vibrationPattern = longArrayOf(0, 1000, 500, 1000)
        if (isTargetReached)
            notificationChannel.enableVibration(true)
        else
            notificationChannel.enableVibration(false)

        notificationManager.createNotificationChannel(notificationChannel)
    }
    val pi = PendingIntent.getActivity(this, 0, Intent(this, MainActivity::class.java), 0)

    if (isTargetReached) {
        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
        notificationBuilder.setAutoCancel(true)
            .setDefaults(Notification.DEFAULT_ALL)
            .setWhen(getCurrentTimeStamp())
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setTicker(getString(R.string.app_name))
            .setContentIntent(pi)
            .setContentTitle(getTargetReachedNotificationTitle())
            .setContentText(getTargetReachedNotificationText())
            .setContentInfo("Information")
        val foreGroundNotification = notificationBuilder.build()


        notificationManager.notify(1, foreGroundNotification)
    } else {
        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
        notificationBuilder.setAutoCancel(true)
            .setDefaults(Notification.DEFAULT_ALL)
            .setWhen(getCurrentTimeStamp())
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setTicker(getString(R.string.app_name))
            .setContentIntent(pi)
            .setContentTitle(getNotificationTitle())
            .setContentText(getNotificationText())
            .setContentInfo("Information")
        val foreGroundNotification = notificationBuilder.build()
        startForeground(1, foreGroundNotification)

    }
}

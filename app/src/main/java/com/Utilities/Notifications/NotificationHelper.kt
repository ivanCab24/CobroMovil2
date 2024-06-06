package com.Utilities.Notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.innovacion.eks.beerws.R
import kotlin.random.Random

class NotificationHelper(base: Context?) : ContextWrapper(base) {

    private val CHANNEL_NAME = "CobroMovil Tickets No Insertados"
    private val CHANNEL_ID = "com.Utilities.Notifications $CHANNEL_NAME"

    init {
        initNotificationChannel()
    }

    private fun initNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            createChannels()

        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannels() {

        val notificationChannel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            enableLights(true)
            enableVibration(true)
            description = "Esta es la descripcion"
            lightColor = Color.RED
            lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        }

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.apply {
            createNotificationChannel(notificationChannel)
        }

    }

    fun sendHighPriorityNotification(title: String, body: String) {

        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID).apply {
            setContentTitle(title)
            setContentText(body)
            setSmallIcon(R.drawable.ic_baseline_notifications)
            setAutoCancel(true)
            priority = NotificationCompat.PRIORITY_HIGH
            setStyle(
                NotificationCompat.BigTextStyle()
                    .setBigContentTitle(title)
                    .bigText(body)
            )

        }.build()

        val random = Random.nextInt()

        NotificationManagerCompat.from(this).notify(random, notification)

    }


}
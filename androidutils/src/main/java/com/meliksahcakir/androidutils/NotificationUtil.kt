package com.meliksahcakir.androidutils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.DrawableRes
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

private const val CHANNEL_ID = "default"
private const val NOTIFICATION_ID = 42

class NotificationUtil(
    context: Context,
    channelName: String? = null,
    channelDescription: String? = null
) {

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val defaultName = context.getString(R.string.channel_name)
            val defaultDescription = context.getString(R.string.channel_description)
            val name = channelName ?: defaultName
            val descriptionText = channelDescription ?: defaultDescription
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showNotification(
        context: Context,
        title: String,
        message: String,
        @DrawableRes icon: Int? = null
    ): Notification {
        val drawable = icon ?: R.drawable.library_notification_icon
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(drawable)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        return builder.build().apply {
            NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, this)
        }
    }
}

package com.example.alarmapplication

import android.app.Notification.DEFAULT_SOUND
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.media.MediaPlayer
import androidx.core.app.NotificationCompat
import kotlin.random.Random

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        makeAlarmNotification(context)

        val mediaPlayer = MediaPlayer.create(context, R.raw.padenie_truby)
        mediaPlayer.start()
    }

    private fun makeAlarmNotification(context: Context?) {
        val intent = Intent(
            context,
            AlarmActivity::class.java,
        ).apply {
            setFlags(FLAG_ACTIVITY_NEW_TASK)
        }

        val pendingIntent = PendingIntent.getActivity(
            context!!,
            Random.nextInt(),
            intent,
            PendingIntent.FLAG_IMMUTABLE,
        )

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(
            "channel_id",
            "channel_name",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "channel_description"
        }
        notificationManager.createNotificationChannel(channel)

        var builder = NotificationCompat.Builder(context, "channel_id")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("Alarm title")
            .setContentText("Alarm text")
            .setFullScreenIntent(pendingIntent, true)
            .setAutoCancel(true)

        val notification = builder.build().apply {
            defaults = defaults or DEFAULT_SOUND.inv()
        }

        notificationManager.notify(99, notification)
    }
}
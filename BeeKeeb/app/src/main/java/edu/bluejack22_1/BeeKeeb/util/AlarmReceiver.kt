package edu.bluejack22_1.BeeKeeb.util

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

import com.example.beekeeb.MainActivity
import com.example.beekeeb.R

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        sendNotification(context)

    }

    private fun sendNotification(context: Context?) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

        val builder = NotificationCompat.Builder(context!!, "beekeebNotif")
            .setSmallIcon(R.drawable.ic_baseline_notifications_24)
            .setContentTitle("Beekeb Notification")
            .setContentText("Have you seen the news today ?")
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)

        with(NotificationManagerCompat.from(context)) {
            notify(1, builder.build())
        }
    }
}
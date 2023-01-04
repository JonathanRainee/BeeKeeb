package edu.bluejack22_1.BeeKeeb.util

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

import com.example.beekeeb.MainActivity
import com.example.beekeeb.R

const val code = 1
const val channelID = "channel1"

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        sendNotification(context)

    }

    companion object {
        fun sendNotification(context: Context?) {
            val intent = Intent(context, MainActivity::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, code, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

            val builder = NotificationCompat.Builder(context!!, "beekeebNotif")
                .setSmallIcon(R.drawable.ic_baseline_notifications_24)
                .setContentTitle("Beekeb Notification")
                .setContentText("Have you seen the news today ?")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)

            with(NotificationManagerCompat.from(context)) {
                notify(code, builder.build())
            }
//            val notifManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//            notifManager.notify(code, builder.build())
        }
    }

}
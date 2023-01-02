package com.example.beekeeb

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.beekeeb.databinding.ActivityHomeBinding
import com.google.firebase.auth.FirebaseAuth
import edu.bluejack22_1.BeeKeeb.util.AlarmReceiver
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val userInstance = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root);

        createNotificationChannel()
        setNotification()

        if(userInstance.currentUser != null) {
            this.finish()
            val mainPageActivity = Intent(this, MainPageActivity::class.java)
            startActivity(mainPageActivity)
        }


        binding.btnSignIn.setOnClickListener{
            AlarmReceiver.sendNotification(this)
            val intentSignIn = Intent(this, LoginOptionActivity::class.java)
            startActivityForResult(intentSignIn, 1)
        }


        binding.btnRegister.setOnClickListener{
            val intentSignIn = Intent(this, LoginOptionActivity::class.java)
            val intentRegis = Intent(this, RegisterActivity::class.java)
            val intentRegister = Intent(this, RegisterActivity::class.java)
            startActivity(intentRegis)
        }
    }

    private fun createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name: CharSequence = "BeekeebNotifChannel"
            val description = "Channel notification"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("notif1", name, importance)
            channel.description = description

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun setNotification() {
        val notifID = 1
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)

        val pendingIntent = PendingIntent.getBroadcast(this, notifID, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
        }


        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY, pendingIntent)

    }

}
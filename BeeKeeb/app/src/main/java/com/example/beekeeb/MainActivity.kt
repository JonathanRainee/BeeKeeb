package com.example.beekeeb

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.beekeeb.databinding.ActivityHomeBinding
import edu.bluejack22_1.BeeKeeb.util.AlarmReceiver
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root);

        createNotificatioChannel()
        setNotification()

        binding.btnSignIn.setOnClickListener{
            val intentSignIn = Intent(this, LoginOptionActivity::class.java)
            startActivity(intentSignIn)
        }


        binding.btnRegister.setOnClickListener{
            Log.d("debugg", "start debug")
            val intentSignIn = Intent(this, LoginOptionActivity::class.java)
            val intentRegis = Intent(this, RegisterActivity::class.java)
            val intentRegister = Intent(this, RegisterActivity::class.java)
            startActivity(intentRegis)
        }
    }

    private fun createNotificatioChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name: CharSequence = "BeekeebNotifChannel"
            val description = "Channel notification"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("beekeebNotif", name, importance)
            channel.description = description

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun setNotification() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(this, 0, intent, 0)
        }

        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 15)
            set(Calendar.MINUTE, 48)
        }

        Log.d("currdate", Calendar.getInstance().get(Calendar.HOUR_OF_DAY).toString())
        Log.d("currdate", Calendar.getInstance().get(Calendar.MINUTE).toString())

        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            intent
        )

    }
}
package cz.dzubera.prosebe.android.utils

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import cz.dzubera.prosebe.android.AlarmReceiver
import cz.dzubera.prosebe.android.MainActivity
import cz.dzubera.prosebe.android.TaskInfo
import java.util.*
import java.util.concurrent.TimeUnit

object AlarmUtils {
    const val CHANNEL_ID = "156633"

    fun setAlarm(taskInfo: TaskInfo, context: Context) {
        // creating alarmManager instance
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        // adding intent and pending intent to go to AlarmReceiver Class in future
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra("task_info", taskInfo)
        val pendingIntent =
            PendingIntent.getBroadcast(
                context,
                taskInfo.id,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        // when using setAlarmClock() it displays a notification until alarm rings and when pressed it takes us to mainActivity
        val mainActivityIntent = Intent(context, MainActivity::class.java)
        val basicPendingIntent = PendingIntent.getActivity(
            context,
            taskInfo.id,
            mainActivityIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        // creating clockInfo instance
        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, taskInfo.hour)
            set(Calendar.MINUTE, taskInfo.minute)
        }
        if (calendar.timeInMillis <= System.currentTimeMillis()) {
            println("old time add 24h")

            calendar.add(Calendar.HOUR_OF_DAY, 24)
        }
        if (calendar.timeInMillis > System.currentTimeMillis()) {
            // setting the alarm
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
            println("Set alarm " + taskInfo.id + " " + taskInfo.description + " " + calendar.timeInMillis.toString())
        }


    }

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "ProSebe channel"
            val descriptionText = "Application notification channel"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            }

            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}


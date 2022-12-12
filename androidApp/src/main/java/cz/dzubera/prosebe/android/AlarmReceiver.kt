package cz.dzubera.prosebe.android

import android.app.PendingIntent
import android.app.PendingIntent.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import cz.dzubera.prosebe.android.utils.AlarmUtils
import cz.dzubera.prosebe.repository.Repository
import java.io.Serializable


class AlarmReceiver : BroadcastReceiver() {


    override fun onReceive(p0: Context?, p1: Intent?) {
        p0?.let {


            val taskInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                p1?.getSerializableExtra("task_info", TaskInfo::class.java)
            } else {
                p1?.getSerializableExtra("task_info") as? TaskInfo
            }

            println(
                "received " + (taskInfo?.id
                    ?: "") + " " + taskInfo?.description + "" + taskInfo?.hour + " " + taskInfo?.minute
            )

            val tapResultIntent = Intent(it, MainActivity::class.java)
            tapResultIntent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            AlarmUtils.createNotificationChannel(it)
            showSimpleNotification(
                it,
                AlarmUtils.CHANNEL_ID,
                taskInfo?.id ?: 0,
                taskInfo?.description ?: "unknown"
            )

        }


    }
}

data class TaskInfo(
    var id: Int,
    var description: String,
    var hour: Int,
    var minute: Int,
) : Serializable


// shows notification with a title and one-line content text
fun showSimpleNotification(
    context: Context,
    channelId: String,
    notificationId: Int,
    textTitle: String,
    priority: Int = NotificationCompat.PRIORITY_HIGH
) {

    val fullScreenIntent = Intent(context, AlarmActivity::class.java)
    fullScreenIntent.flags =
        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
    val mBundle = Bundle()
    mBundle.putInt("id", notificationId)
    mBundle.putString("title", textTitle)

    fullScreenIntent.putExtras(mBundle)
    val fullScreenPendingIntent = PendingIntent.getActivity(
        context, 2004, fullScreenIntent,
        FLAG_UPDATE_CURRENT or FLAG_IMMUTABLE
    )

    val intent = Intent(context, MainActivity::class.java).apply {
        flags =
            Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK

    }
    val pendingIntent: PendingIntent =
        getActivity(context, notificationId, intent, FLAG_UPDATE_CURRENT or FLAG_IMMUTABLE)

    val builder = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.drawable.ic_app_notification)
        .setContentTitle("Ahoj " + App.repository.getUserName())
        .setContentIntent(pendingIntent)
        .setContentText(textTitle)
        .setPriority(NotificationCompat.PRIORITY_MAX)
        .setCategory(NotificationCompat.CATEGORY_ALARM)
        .setFullScreenIntent(fullScreenPendingIntent, true)
        .setAutoCancel(true)
    println("notify")

    with(NotificationManagerCompat.from(context)) {
        notify(notificationId, builder.build())
    }
}
package cz.dzubera.prosebe.android

import android.R
import android.app.Notification
import android.app.PendingIntent.*
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import cz.dzubera.prosebe.android.utils.AlarmUtils.CHANNEL_ID


class DeviceOnReceiverService : Service() {

    var receiver: BroadcastReceiver? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(554433,
            buildForegroundNotification(this));
        val intentFilter = IntentFilter(Intent.ACTION_SCREEN_ON)
        receiver = DeviceOnReceiver()
        registerReceiver(receiver, intentFilter)
        Log.d("ERVICE", "started service")
        return START_STICKY
    }

    override fun onDestroy() {
        receiver?.let {
            unregisterReceiver(it)

        }
        super.onDestroy()
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    private fun buildForegroundNotification(context: Context): Notification? {
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(cz.dzubera.prosebe.android.R.drawable.ic_app_notification)
            .setContentTitle("Pro sebe")
            .setContentText("")
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setAutoCancel(true)
        println("notify")

        return builder.build()
    }


}
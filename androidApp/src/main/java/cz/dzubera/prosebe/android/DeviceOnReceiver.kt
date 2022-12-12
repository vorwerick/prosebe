package cz.dzubera.prosebe.android

import android.app.PendingIntent
import android.app.PendingIntent.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import cz.dzubera.prosebe.android.utils.AlarmUtils
import cz.dzubera.prosebe.repository.Repository
import java.io.Serializable


class DeviceOnReceiver : BroadcastReceiver() {


    override fun onReceive(p0: Context?, p1: Intent?) {
        Log.d("ACTION", p1?.action.toString())
        if(p1?.action == Intent.ACTION_SCREEN_ON){
            p0?.let {
                val fullScreenIntent = Intent(it, AchieveActivity::class.java)
                fullScreenIntent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_NEW_TASK

                val fullScreenPendingIntent = PendingIntent.getActivity(
                    p0, 2005, fullScreenIntent,
                    FLAG_UPDATE_CURRENT or FLAG_IMMUTABLE
                )
                fullScreenPendingIntent.send()
                Log.d("KKT","Tak coje?")

            }
        }

    }
}
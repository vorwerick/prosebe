package cz.dzubera.prosebe.android.widget

import android.app.Service
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.os.IBinder
import android.widget.RemoteViews
import cz.dzubera.prosebe.android.App
import cz.dzubera.prosebe.android.R


class WidgetUpdateService : Service() {

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // generates random number
        val sb =StringBuilder()
        App.repository.getAchieves().forEach {
            sb.insert(0, it.name +"\n")
        }
        // Reaches the view on widget and displays the number
        val view = RemoteViews(packageName, R.layout.widget_content)

        view.setTextViewText(R.id.affirmation_text, sb.toString())
        val theWidget = ComponentName(this, CustomAppWidgetProvider::class.java)
        val manager = AppWidgetManager.getInstance(this)
        manager.updateAppWidget(theWidget, view)
        return super.onStartCommand(intent, flags, startId)
    }
}
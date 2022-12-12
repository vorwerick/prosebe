package cz.dzubera.prosebe.android.widget

import android.app.AlarmManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.widget.RemoteViews
import cz.dzubera.prosebe.android.R


class CustomAppWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray?
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)

        val manager: AlarmManager =
            context!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val i = Intent(context, WidgetUpdateService::class.java)

        manager.setRepeating(
            AlarmManager.ELAPSED_REALTIME,
            SystemClock.elapsedRealtime(),
            60000,
            PendingIntent.getService(context, 945, i, PendingIntent.FLAG_IMMUTABLE)
        )
    }

    private fun updateAppWidget(
        context: Context?, appWidgetManager: AppWidgetManager?,
        appWidgetId: Int
    ) {
        println("WIDGET UPDATE")
        context?.let {
            // Construct the RemoteViews object
            val views = RemoteViews(it.packageName, R.layout.widget_content)
            views.setTextViewText(R.id.affirmation_text, System.currentTimeMillis().toString());
            // Instruct the widget manager to update the widget
            appWidgetManager?.updateAppWidget(appWidgetId, views)
        }

    }
}
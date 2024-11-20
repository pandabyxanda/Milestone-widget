package com.example.milestone_widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import android.app.PendingIntent
import android.content.Intent
import com.example.milestone_widget.db.DBHelper

class xwidget : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

internal fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
    val views = RemoteViews(context.packageName, R.layout.xwidget)
    val db = DBHelper(context, null)
    val items = db.getAllItems()

    views.removeAllViews(R.id.widget_container)

    // Create a RemoteViews object for the header
    val headerView = RemoteViews(context.packageName, R.layout.widget_header)
    headerView.setTextViewText(R.id.widget_header, "Dynamic Header")

//    // Set up the update button
//    val updateIntent = Intent(context, xwidget::class.java).apply {
//        action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
//        putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, intArrayOf(appWidgetId))
//    }
//    val updatePendingIntent = PendingIntent.getBroadcast(context, appWidgetId, updateIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
//    headerView.setOnClickPendingIntent(R.id.update_button, updatePendingIntent)

    views.addView(R.id.widget_container, headerView)

    items?.let {
        if (it.moveToFirst()) {
            do {
                val itemName = it.getString(it.getColumnIndexOrThrow(DBHelper.NAME_COL))
                val buttonView = RemoteViews(context.packageName, R.layout.widget_button)
                buttonView.setTextViewText(R.id.widget_button, itemName)

                val intent = Intent(context, WidgetButtonReceiver::class.java).apply {
                    action = "com.example.milestone_widget.BUTTON_CLICK"
                    putExtra("item_name", itemName)
                }
                val pendingIntent = PendingIntent.getBroadcast(context, itemName.hashCode(), intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
                buttonView.setOnClickPendingIntent(R.id.widget_button, pendingIntent)

                views.addView(R.id.widget_container, buttonView)
            } while (it.moveToNext())
        }
    }

    appWidgetManager.updateAppWidget(appWidgetId, views)
}
package com.example.milestone_widget

import android.appwidget.AppWidgetManager
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews

class WidgetButtonReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("WidgetButtonReceiver", "onReceive")
        Log.d("WidgetButtonReceiver", intent.action.toString())
        if (intent.action == "com.example.milestone_widget.UPDATE_DB") {
            val db = DBHelper(context, null)
            val name = "widget_click"
            val age = "0" // or any other value you want to store
            db.addName(name, age)
            Log.d("WidgetButtonReceiver", "Database updated from widget button click")

            // Update the widget button text
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val remoteViews = RemoteViews(context.packageName, R.layout.xwidget)
//            val clickCount = (1..6).random()
            val numberRows = db.getCountByName("widget_click")
            val newText = "Clicked $numberRows times"
//            val newText = "Clicked $clickCount times"
            remoteViews.setTextViewText(R.id.appwidget_button, newText)

            // Get all widget IDs and update them
            val thisWidget = ComponentName(context, xwidget::class.java)
            val allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget)
            for (widgetId in allWidgetIds) {
                appWidgetManager.updateAppWidget(widgetId, remoteViews)
            }
        }
    }
}
package com.example.milestone_widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import android.app.PendingIntent
import android.content.Intent

/**
 * Implementation of App Widget functionality.
 */
class xwidget : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        // There may be multiple widgets active, so update all of them
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
//    val widgetText = context.getString(R.string.appwidget_text)
    // Construct the RemoteViews object
//    val views = RemoteViews(context.packageName, R.layout.xwidget)
//    val newText = "Clicked $clickCount times"
    val clickCount = (1..6).random()
    val newText = "Clicked $clickCount times wow"

//    val intent = Intent(context, WidgetButtonReceiver::class.java).apply {
//        action = "com.example.milestone_widget.UPDATE_DB"
//    }
//    val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
//    views.setOnClickPendingIntent(R.id.appwidget_button, pendingIntent)

//    val pendingIntent: PendingIntent = PendingIntent.getActivity(
//        /* context = */ context,
//        /* requestCode = */  0,
//        /* intent = */ Intent(context, SecondActivity::class.java),
//        /* flags = */ PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//    )
//
//    // Get the layout for the widget and attach an onClick listener to
//    // the button.
//    val views: RemoteViews = RemoteViews(
//        context.packageName,
//        R.layout.xwidget,
//    ).apply {
//        setOnClickPendingIntent(R.id.appwidget_button, pendingIntent)
//    }
    val views = RemoteViews(context.packageName, R.layout.xwidget)

    val intent = Intent(context, WidgetButtonReceiver::class.java).apply {
        action = "com.example.milestone_widget.UPDATE_DB"
    }
    val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
    views.setOnClickPendingIntent(R.id.appwidget_button, pendingIntent)


//    if (intent.action == "com.example.milestone_widget.UPDATE_DB") {
////            val db = DBHelper(context, null)
////            val name = "widget_click"
////            val age = "0" // or any other value you want to store
////            db.addName(name, age)
////            Log.d("WidgetButtonReceiver", "Database updated from widget button click")
////        }
////    }

    // Update the button text
    views.setTextViewText(R.id.appwidget_button, newText)
//    views.setTextViewText(R.id.appwidget_text, widgetText)

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}


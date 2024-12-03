package com.example.milestone_widget.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.example.milestone_widget.MainActivity
import com.example.milestone_widget.R
import com.example.milestone_widget.db.DataBase
import com.example.milestone_widget.getCurrentHour
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

internal fun updateAppWidgetInternal(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    val views = RemoteViews(context.packageName, R.layout.main_widget)
    val db = DataBase(context, null)
    val items = db.getAllItems()

    val startingHour = "06"
    var date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    val hour = getCurrentHour()
    if (hour < startingHour.toInt()) {
        date = SimpleDateFormat(
            "yyyy-MM-dd",
            Locale.getDefault()
        ).format(Date().time - 24 * 60 * 60 * 1000)
    }

    // Intent to launch MainActivity
    val launchAppIntent = Intent(context, MainActivity::class.java)
    val launchAppPendingIntent = PendingIntent.getActivity(
        context,
        0,
        launchAppIntent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
    views.setOnClickPendingIntent(R.id.widget_container, launchAppPendingIntent)
    views.removeAllViews(R.id.widget_container)
    items?.let {
        if (it.moveToFirst()) {
            do {
                val itemName = it.getString(it.getColumnIndexOrThrow(DataBase.NAME_COL))
                val itemShortName = it.getString(it.getColumnIndexOrThrow(DataBase.SHORT_NAME_COL))
                val itemId = it.getInt(it.getColumnIndexOrThrow(DataBase.ITEM_ID_COL))
                val actionCount = db.getActionCountByItemIdAndDate(itemId, date)
                val isActive = it.getInt(it.getColumnIndexOrThrow(DataBase.ACTIVE_COL))
                val buttonView = RemoteViews(context.packageName, R.layout.widget_button)
                val displayName = if (itemShortName.isNullOrEmpty()) itemName else itemShortName
                if (isActive == 1) {
                    buttonView.setTextViewText(R.id.widget_button_line1, displayName)
                    buttonView.setTextViewText(R.id.widget_button_line2, "$actionCount")
                    val intent = Intent(context, WidgetButtonReceiver::class.java).apply {
                        action = "com.example.milestone_widget.BUTTON_CLICK"
                        putExtra("item_name", itemName)
                    }
                    val pendingIntent = PendingIntent.getBroadcast(
                        context,
                        itemName.hashCode(),
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )
                    buttonView.setOnClickPendingIntent(R.id.widget_button_layout, pendingIntent)
                    views.addView(R.id.widget_container, buttonView)
                }
            } while (it.moveToNext())
        }
    }
    appWidgetManager.updateAppWidget(appWidgetId, views)
}
package com.example.milestone_widget.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.example.milestone_widget.R
import com.example.milestone_widget.db.DataBase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Xwidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidgetInternal(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

internal fun updateAppWidgetInternal(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    val views = RemoteViews(context.packageName, R.layout.xwidget)
    val db = DataBase(context, null)
    val items = db.getAllItems()
    val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    views.removeAllViews(R.id.widget_container)

    // Create a RemoteViews object for the header
    val headerView = RemoteViews(context.packageName, R.layout.widget_header)
//    headerView.setTextViewText(R.id.widget_header, context.getString(R.string.widget_header_name))

    // Set up the update button
    val updateIntent = Intent(context, Xwidget::class.java).apply {
        action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, intArrayOf(appWidgetId))
    }
    val updatePendingIntent = PendingIntent.getBroadcast(
        context,
        appWidgetId,
        updateIntent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
    headerView.setOnClickPendingIntent(R.id.update_button, updatePendingIntent)


    views.addView(R.id.widget_container, headerView)

    items?.let {
        if (it.moveToFirst()) {
            do {
                val itemName = it.getString(it.getColumnIndexOrThrow(DataBase.NAME_COL))
                val itemShortName = it.getString(it.getColumnIndexOrThrow(DataBase.SHORT_NAME_COL))
                val itemId = it.getInt(it.getColumnIndexOrThrow(DataBase.ITEM_ID_COL))
                val actionCount = db.getActionCountByItemIdAndDate(itemId, date)
                val isActive = it.getInt(it.getColumnIndexOrThrow(DataBase.ACTIVE_COL))
                val buttonView = RemoteViews(context.packageName, R.layout.widget_button)
//                val displayName = itemShortName ?: itemName
                val displayName = if (itemShortName.isNullOrEmpty()) itemName else itemShortName
//                Log.d("Xwidget", "displayName: $displayName")
//                Log.d("Xwidget", "itemName: $itemName")
//                Log.d("Xwidget", "itemShortName: $itemShortName")
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
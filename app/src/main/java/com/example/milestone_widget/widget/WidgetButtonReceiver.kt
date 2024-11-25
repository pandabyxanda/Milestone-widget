package com.example.milestone_widget.widget

import android.appwidget.AppWidgetManager
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.milestone_widget.R
import com.example.milestone_widget.db.DataBase


class WidgetButtonReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "com.example.milestone_widget.BUTTON_CLICK") {
            val itemName = intent.getStringExtra("item_name")
//            Log.d("WidgetButtonReceiver", "Button clicked: $itemName")
            val db = DataBase(context, null)
            val cursor = db.getAllItems()
            cursor?.use {
                if (it.moveToFirst()) {
                    do {
                        val name = it.getString(it.getColumnIndexOrThrow(DataBase.NAME_COL))
                        if (name == itemName) {
                            val itemId = it.getInt(it.getColumnIndexOrThrow(DataBase.ITEM_ID_COL))
                            db.addItemAction(itemId)
//                            Log.d("WidgetButtonReceiver", "Item action added for item ID: $itemId")
                            break
                        }
                    } while (it.moveToNext())
                }
            }
            // Update the widget
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val componentName = ComponentName(context, MainWidget::class.java)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(componentName)
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_container)
            for (appWidgetId in appWidgetIds) {
                updateAppWidgetInternal(context, appWidgetManager, appWidgetId)
            }
        }
    }
}
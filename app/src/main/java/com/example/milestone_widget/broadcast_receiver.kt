package com.example.milestone_widget

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.milestone_widget.db.DataBase
import android.appwidget.AppWidgetManager
import android.content.ComponentName


class WidgetButtonReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "com.example.milestone_widget.BUTTON_CLICK") {
            val itemName = intent.getStringExtra("item_name")
            Log.d("WidgetButtonReceiver", "Button clicked: $itemName")

            // Handle the button click event
            val db = DataBase(context, null)
            val cursor = db.getAllItems()
            cursor?.use {
                if (it.moveToFirst()) {
                    do {
                        val name = it.getString(it.getColumnIndexOrThrow(DataBase.NAME_COL))
                        if (name == itemName) {
                            val itemId = it.getInt(it.getColumnIndexOrThrow(DataBase.ITEM_ID_COL))
                            db.addItemAction(itemId)
                            Log.d("WidgetButtonReceiver", "Item action added for item ID: $itemId")
                            break
                        }
                    } while (it.moveToNext())
                }
            }

            // Update the widget
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val componentName = ComponentName(context, Xwidget::class.java)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(componentName)
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_container)
            for (appWidgetId in appWidgetIds) {
                updateAppWidgetInternal(context, appWidgetManager, appWidgetId)
            }
        }
    }
}

//class WidgetButtonReceiver : BroadcastReceiver() {
//    override fun onReceive(context: Context, intent: Intent) {
//        if (intent.action == "com.example.milestone_widget.BUTTON_CLICK") {
//            val itemName = intent.getStringExtra("item_name")
//            Log.d("WidgetButtonReceiver", "Button clicked: $itemName")
//            // Handle the button click event
//            //        if (intent.action == "com.example.milestone_widget.UPDATE_DB") {
////            val db = DBHelper(context, null)
////            val name = "widget_click"
////            val age = "0" // or any other value you want to store
////            db.addName(name, age)
////            Log.d("WidgetButtonReceiver", "Database updated from widget button click")
////
////            // Update the widget button text
////            val appWidgetManager = AppWidgetManager.getInstance(context)
////            val remoteViews = RemoteViews(context.packageName, R.layout.xwidget)
//////            val clickCount = (1..6).random()
////            val numberRows = db.getCountByName("widget_click")
////            val newText = "Clicked $numberRows times"
//////            val newText = "Clicked $clickCount times"
////            remoteViews.setTextViewText(R.id.appwidget_button, newText)
////
////            // Get all widget IDs and update them
////            val thisWidget = ComponentName(context, xwidget::class.java)
////            val allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget)
////            for (widgetId in allWidgetIds) {
////                appWidgetManager.updateAppWidget(widgetId, remoteViews)
////            }
////        }
//        }
//    }
//}
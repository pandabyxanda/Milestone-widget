package com.example.milestone_widget.widget

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent

fun updateWidget(context: Context) {
    val intent = Intent(context, MainWidget::class.java).apply {
        action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        val ids = AppWidgetManager.getInstance(context)
            .getAppWidgetIds(ComponentName(context, MainWidget::class.java))
        putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
    }
    context.sendBroadcast(intent)
}
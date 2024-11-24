package com.example.milestone_widget.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context

class MainWidget : AppWidgetProvider() {
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
        // functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // functionality for when the last widget is disabled
    }
}

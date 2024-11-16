//package com.example.milestone_widget
//
//import android.content.BroadcastReceiver
//import android.content.Context
//import android.content.Intent
//import android.util.Log
//
//class WidgetButtonReceiver : BroadcastReceiver() {
//    override fun onReceive(context: Context, intent: Intent) {
//        if (intent.action == "com.example.milestone_widget.UPDATE_DB") {
//            val db = DBHelper(context, null)
//            val name = "widget_click"
//            val age = "0" // or any other value you want to store
//            db.addName(name, age)
//            Log.d("WidgetButtonReceiver", "Database updated from widget button click")
//        }
//    }
//}
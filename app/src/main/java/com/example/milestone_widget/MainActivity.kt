package com.example.milestone_widget

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.milestone_widget.db.DataBase
import com.example.milestone_widget.db.Item
import com.example.milestone_widget.main_page.MainPage
import com.example.milestone_widget.main_page.refreshItemList
import com.example.milestone_widget.other_pages.ItemPageCreate
import com.example.milestone_widget.other_pages.ItemPageUpdate
import com.example.milestone_widget.other_pages.OnItemAddedListener
import com.example.milestone_widget.ui.theme.Milestone_widgetTheme
import com.example.milestone_widget.widget.updateWidget
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun getCurrentHour(): Int {
    val calendar = Calendar.getInstance()
    return calendar.get(Calendar.HOUR_OF_DAY)
}

class MainActivity : ComponentActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MainActivity", "onCreate")
        sharedPreferences =
            getSharedPreferences("com.example.milestone_widget", Context.MODE_PRIVATE)
        setContent {
            Milestone_widgetTheme {
                MainScreen(sharedPreferences)
            }
        }

    }

    override fun onPause() {
        super.onPause()
        updateWidget(this)
    }
}

@Composable
fun MainScreen(sharedPreferences: SharedPreferences) {
    val navController = rememberNavController()
    val startingHour = "06"
    val selectedDate = remember {
        mutableStateOf(SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()))
    }
    val hour = getCurrentHour()
    if (hour < startingHour.toInt()) {
        selectedDate.value = SimpleDateFormat(
            "yyyy-MM-dd",
            Locale.getDefault()
        ).format(Date().time - 24 * 60 * 60 * 1000)
    }
    val itemList = remember { mutableStateListOf<Item>() }
    val context = LocalContext.current
    val db = DataBase(context, null)
    val onItemAddedListener = object : OnItemAddedListener {
        override fun onItemAdded() {
            refreshItemList(db, itemList, selectedDate.value)
        }
    }
    val durationMillis = 700
    Column {
        NavHost(
            navController = navController,
            startDestination = "main",
            enterTransition = { fadeIn(animationSpec = tween(durationMillis)) },
            exitTransition = { fadeOut(animationSpec = tween(durationMillis)) },
            popEnterTransition = { fadeIn(animationSpec = tween(durationMillis)) },
            popExitTransition = { fadeOut(animationSpec = tween(durationMillis)) }
        ) {
            composable("main") { MainPage(navController, selectedDate, itemList) }
            composable("ItemPageCreate") {
                ItemPageCreate(
                    navController,
                    onItemAddedListener
                )
            }
            composable("ItemPageUpdate/{id}/{name}/{shortName}/{description}/{dateCreated}/{isActive}") { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id")?.toInt()
                val name = backStackEntry.arguments?.getString("name")
                val shortName = backStackEntry.arguments?.getString("shortName")
                val description = backStackEntry.arguments?.getString("description")
                val dateCreated = backStackEntry.arguments?.getString("dateCreated")
                val isActive = backStackEntry.arguments?.getString("isActive")?.toInt()
                ItemPageUpdate(
                    navController,
                    id,
                    name,
                    shortName,
                    description,
                    dateCreated,
                    isActive,
                    selectedDate,
                    onItemAddedListener
                )
            }
        }
    }
}

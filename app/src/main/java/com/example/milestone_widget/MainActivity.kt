package com.example.milestone_widget

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.milestone_widget.db.DataBase
import com.example.milestone_widget.db.Item
import com.example.milestone_widget.ui.theme.Milestone_widgetTheme
import com.example.milestone_widget.widget.updateWidget
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class MainActivity : ComponentActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MainActivity", "onCreate")
        sharedPreferences =
            getSharedPreferences("com.example.milestone_widget", Context.MODE_PRIVATE)

        setContent {
            Milestone_widgetTheme {
//                DiceRollerApp(sharedPreferences)
                MainScreen(sharedPreferences)
            }
        }

    }

    override fun onPause() {
        super.onPause()
//        // Send broadcast to update the widget
//        val intent = Intent(this, Xwidget::class.java).apply {
//            action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
//            putExtra(
//                AppWidgetManager.EXTRA_APPWIDGET_IDS,
//                AppWidgetManager.getInstance(this@MainActivity)
//                    .getAppWidgetIds(ComponentName(this@MainActivity, Xwidget::class.java))
//            )
//        }
//        sendBroadcast(intent)
        updateWidget(this)
    }


    override fun onResume() {
        super.onResume()
    }
}

@Composable
fun MainScreen(sharedPreferences: SharedPreferences) {
    val navController = rememberNavController()
    val selectedDate = remember {
        mutableStateOf(SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()))
    }
    val itemList = remember { mutableStateListOf<Item>() }
    val context = LocalContext.current
    val db = DataBase(context, null)

    val onItemAddedListener = object : OnItemAddedListener {
        override fun onItemAdded() {
            refreshItemList(db, itemList, selectedDate.value)
        }
    }

    NavHost(navController = navController, startDestination = "main") {
        composable("main") { MainContent(navController, sharedPreferences, selectedDate, itemList) }
        composable("ItemPageCreate") {
            ItemPageCreate(
                navController,
                itemList,
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
                selectedDate
            )
        }
    }
}

@Preview
@Composable
fun MainApp(
    sharedPreferences: SharedPreferences = androidx.compose.ui.platform.LocalContext.current.getSharedPreferences(
        "com.example.milestone_widget",
        Context.MODE_PRIVATE
    )
) {
    MainScreen(sharedPreferences)

}






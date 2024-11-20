package com.example.milestone_widget

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.milestone_widget.ui.theme.Milestone_widgetTheme


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
        // Send broadcast to update the widget
        val intent = Intent(this, Xwidget::class.java).apply {
            action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,
                AppWidgetManager.getInstance(this@MainActivity)
                    .getAppWidgetIds(ComponentName(this@MainActivity, Xwidget::class.java))
            )
        }
        sendBroadcast(intent)
    }


    override fun onResume() {
        super.onResume()
    }
}


//@Composable
//fun MainScreen(sharedPreferences: SharedPreferences) {
//    val navController = rememberNavController()
//    NavHost(navController = navController, startDestination = "main") {
//        composable("main") { MainContent(navController, sharedPreferences) }
//        composable("newPage") { NewPage(navController) }
//    }
//}
//@Suppress("DEPRECATION")
//inline fun <reified T : Serializable> Bundle.customGetSerializable(key: String): T? {
//    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) getSerializable(key, T::class.java)
//    else getSerializable(key) as? T
//}

@Composable
fun MainScreen(sharedPreferences: SharedPreferences) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "main") {
        composable("main") { MainContent(navController, sharedPreferences) }
        composable("ItemPageCreate") { ItemPageCreate(navController) }
        composable("ItemPageUpdate/{id}/{name}/{shortName}/{description}/{dateCreated}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toInt()
            val name = backStackEntry.arguments?.getString("name")
            val shortName = backStackEntry.arguments?.getString("shortName")
            val description = backStackEntry.arguments?.getString("description")
            val dateCreated = backStackEntry.arguments?.getString("dateCreated")
            ItemPageUpdate(navController, id, name, shortName, description, dateCreated)
        }
    }
}

@Preview
@Composable
fun DiceRollerApp(
    sharedPreferences: SharedPreferences = androidx.compose.ui.platform.LocalContext.current.getSharedPreferences(
        "com.example.milestone_widget",
        Context.MODE_PRIVATE
    )
) {
    Box(modifier = Modifier.fillMaxSize()) {
        MainScreen(sharedPreferences = sharedPreferences)
    }
}






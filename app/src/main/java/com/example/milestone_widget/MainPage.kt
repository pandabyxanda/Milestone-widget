package com.example.milestone_widget

import android.content.SharedPreferences
import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.milestone_widget.db.DBHelper


@Composable
fun MainList(modifier: Modifier = Modifier, itemsList: List<String>) {
    LazyColumn(
        modifier = modifier
            .padding(10.dp)
            .fillMaxSize()
    ) {
        items(itemsList) { item ->
            Card(
                modifier = Modifier
                    .padding(vertical = 6.dp) // Adjust the vertical padding to change the gap
                    .fillMaxWidth()
                    .border(2.dp, Color.Black, shape = RoundedCornerShape(16.dp))
                    .clickable {
                        // Handle card click
                        Log.d("MainActivity", "Card clicked: $item")
                    }
            ) {
                Text(
                    text = item,
                    color = Color.Black,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

@Composable
fun MainContent(navController: NavHostController, sharedPreferences: SharedPreferences) {
    val db = DBHelper(LocalContext.current, null)
    val itemNameList = remember { mutableStateListOf<String>() }
    val currentBackStackEntry = navController.currentBackStackEntryAsState()

    LaunchedEffect(currentBackStackEntry.value) {
        if (currentBackStackEntry.value?.destination?.route == "main") {
            itemNameList.clear()
            val rows = db.getAllItems()
            rows?.let {
                if (it.moveToFirst()) {
                    do {
                        val name = it.getString(it.getColumnIndexOrThrow(DBHelper.NAME_COL))
                        itemNameList.add(name)
                    } while (it.moveToNext())
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            CustomTopBar()
            MainList(
                modifier = Modifier
                    .fillMaxSize(),
                itemsList = itemNameList
            )
        }
        FloatingActionButton(
            onClick = {
                navController.navigate("newPage")
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Text("+")
        }
    }
}
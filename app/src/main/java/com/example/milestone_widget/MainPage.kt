package com.example.milestone_widget

import android.content.SharedPreferences
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
import com.example.milestone_widget.db.DataBase
import com.example.milestone_widget.db.Item


@Composable
fun MainList(navController: NavHostController, modifier: Modifier = Modifier, itemList: List<Item>) {
    LazyColumn(
        modifier = modifier
            .padding(10.dp)
            .fillMaxSize()
    ) {
        items(itemList) { item ->
            Card(
                modifier = Modifier
                    .padding(vertical = 6.dp)
                    .fillMaxWidth()
                    .border(2.dp, Color.Black, shape = RoundedCornerShape(16.dp))
                    .clickable {
                        navController.navigate("ItemPageUpdate/${item.id}/${item.name}/${item.shortName}/${item.description}/${item.dateCreated}")
                    }
            ) {
                Text(
                    text = "${item.id} ${item.name}${if (item.shortName.isNotEmpty()) " (${item.shortName})" else ""} - (${item.actionCount})",
                    color = Color.Black,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

@Composable
fun MainContent(navController: NavHostController, sharedPreferences: SharedPreferences) {
    val db = DataBase(LocalContext.current, null)
//    val itemNameList = remember { mutableStateListOf<String>() }
    val itemList = remember { mutableStateListOf<Item>() }
    val currentBackStackEntry = navController.currentBackStackEntryAsState()

    LaunchedEffect(currentBackStackEntry.value) {
        if (currentBackStackEntry.value?.destination?.route == "main") {
//            itemNameList.clear()
            itemList.clear()
            val rows = db.getAllItems()
            rows?.let {
                if (it.moveToFirst()) {
                    do {
                        val id = it.getInt(it.getColumnIndexOrThrow(DataBase.ITEM_ID_COL))
                        val name = it.getString(it.getColumnIndexOrThrow(DataBase.NAME_COL))
                        val shortName = it.getString(it.getColumnIndexOrThrow(DataBase.SHORT_NAME_COL))
                        val description = it.getString(it.getColumnIndexOrThrow(DataBase.DESCRIPTION_COL))
                        val dateCreated = it.getString(it.getColumnIndexOrThrow(DataBase.DATE_CREATED_COL))
                        val actionCount = db.getActionsByItemId(id)?.count ?: 0
                        itemList.add(Item(id, name, shortName, description, dateCreated, actionCount))
                    } while (it.moveToNext())
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            CustomTopBar()
            MainList(
                navController = navController,
                modifier = Modifier
                    .fillMaxSize(),
                itemList = itemList
            )
        }
        FloatingActionButton(
            onClick = {
                navController.navigate("ItemPageCreate")
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Text("+")
        }
    }
}
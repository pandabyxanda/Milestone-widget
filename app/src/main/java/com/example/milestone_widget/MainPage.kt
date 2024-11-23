package com.example.milestone_widget

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.milestone_widget.db.DataBase
import com.example.milestone_widget.db.Item
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MainContent(
    navController: NavHostController,
    sharedPreferences: SharedPreferences,
    selectedDate: MutableState<String>,
    itemList: MutableList<Item>
) {
    val db = DataBase(LocalContext.current, null)

    val currentBackStackEntry = navController.currentBackStackEntryAsState()

    Log.d("MainContent selectedDate", selectedDate.value)
    LaunchedEffect(currentBackStackEntry.value, selectedDate.value, itemList) {
        Log.d("LaunchedEffect", "LaunchedEffect trigger ${currentBackStackEntry.value} ${selectedDate.value}")
        if (currentBackStackEntry.value?.destination?.route == "main") {
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
                        val actionCount = db.getActionsByItemIdAndDate(id, selectedDate.value)?.count ?: 0
                        val isActive = it.getInt(it.getColumnIndexOrThrow(DataBase.ACTIVE_COL))
                        itemList.add(Item(id, name, shortName, description, dateCreated, actionCount, isActive))
                    } while (it.moveToNext())
                }
            }
            Log.d("MainList", "itemList 88: $itemList")
        }
    }

//    // Listen for result from ItemPageCreate
//    navController.currentBackStackEntry?.savedStateHandle?.getLiveData<Boolean>("itemCreated")?.observe(LocalLifecycleOwner.current) { result ->
//        if (result) {
//            // Trigger the update
//            itemList.clear()
//            val rows = db.getAllItems()
//            rows?.let {
//                if (it.moveToFirst()) {
//                    do {
//                        val id = it.getInt(it.getColumnIndexOrThrow(DataBase.ITEM_ID_COL))
//                        val name = it.getString(it.getColumnIndexOrThrow(DataBase.NAME_COL))
//                        val shortName = it.getString(it.getColumnIndexOrThrow(DataBase.SHORT_NAME_COL))
//                        val description = it.getString(it.getColumnIndexOrThrow(DataBase.DESCRIPTION_COL))
//                        val dateCreated = it.getString(it.getColumnIndexOrThrow(DataBase.DATE_CREATED_COL))
//                        val actionCount = db.getActionsByItemIdAndDate(id, selectedDate.value)?.count ?: 0
//                        val isActive = it.getInt(it.getColumnIndexOrThrow(DataBase.ACTIVE_COL))
//                        itemList.add(Item(id, name, shortName, description, dateCreated, actionCount, isActive))
//                    } while (it.moveToNext())
//                }
//            }
//        }
//    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            TopBarMain(selectedDate = selectedDate)
            MainList(
                navController = navController,
                modifier = Modifier.fillMaxSize(),
                itemList = itemList
            )
        }
        FloatingActionButton(
            onClick = {
                navController.navigate("ItemPageCreate")
            },
            modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp)
        ) {
            Text("+")
        }
    }
}

//@Composable
//fun MainContent(
//    navController: NavHostController,
//    sharedPreferences: SharedPreferences,
//    selectedDate: MutableState<String>
//) {
//    val db = DataBase(LocalContext.current, null)
////    val itemNameList = remember { mutableStateListOf<String>() }
//    val itemList = remember { mutableStateListOf<Item>() }
//    val currentBackStackEntry = navController.currentBackStackEntryAsState()
//
//    Log.d("MainContent selectedDate", selectedDate.value)
//    LaunchedEffect(currentBackStackEntry.value, selectedDate.value) {
//        Log.d("LaunchedEffect", "LaunchedEffect trigger ${currentBackStackEntry.value} ${selectedDate.value}")
//        if (currentBackStackEntry.value?.destination?.route == "main") {
////            itemNameList.clear()
//            Log.d("selectedDate", "selectedDate after if: ${selectedDate.value}")
//            itemList.clear()
//            val rows = db.getAllItems()
//            rows?.let {
//                if (it.moveToFirst()) {
//                    do {
//                        val id = it.getInt(it.getColumnIndexOrThrow(DataBase.ITEM_ID_COL))
//                        val name = it.getString(it.getColumnIndexOrThrow(DataBase.NAME_COL))
//                        val shortName =
//                            it.getString(it.getColumnIndexOrThrow(DataBase.SHORT_NAME_COL))
//                        val description =
//                            it.getString(it.getColumnIndexOrThrow(DataBase.DESCRIPTION_COL))
//                        val dateCreated =
//                            it.getString(it.getColumnIndexOrThrow(DataBase.DATE_CREATED_COL))
//                        val actionCount =
//                            db.getActionsByItemIdAndDate(id, selectedDate.value)?.count ?: 0
//                        val isActive = it.getInt(it.getColumnIndexOrThrow(DataBase.ACTIVE_COL))
//                        itemList.add(
//                            Item(
//                                id,
//                                name,
//                                shortName,
//                                description,
//                                dateCreated,
//                                actionCount,
//                                isActive
//                            )
//                        )
//                    } while (it.moveToNext())
//                }
//            }
//            Log.d("MainList", "itemList 88: $itemList")
//        }
//    }
//
//    Box(modifier = Modifier.fillMaxSize()) {
//        Column {
//            TopBarMain(selectedDate = selectedDate)
//            MainList(
//                navController = navController,
//                modifier = Modifier
//                    .fillMaxSize(),
//                itemList = itemList
//            )
//        }
//        FloatingActionButton(
//            onClick = {
//                navController.navigate("ItemPageCreate")
//            },
//            modifier = Modifier
//                .align(Alignment.BottomEnd)
//                .padding(16.dp)
//        ) {
//            Text("+")
//        }
//    }
//}


@Composable
fun MainList(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    itemList: MutableList<Item>
) {
    val context = LocalContext.current
    val db = DataBase(context, null)
//    val itemListState = remember { mutableStateListOf<Item>().apply { addAll(itemList) } }
//    val itemListState = remember { mutableStateListOf(*itemList.toTypedArray()) }
//    Log.d("MainList", "itemListState: $itemListState")
    Log.d("MainList", "itemList: $itemList")
//    LaunchedEffect(itemListState) {
//        itemListState.clear()
//        itemListState.addAll(itemList)
//    }
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
                        navController.navigate("ItemPageUpdate/${item.id}/${item.name}/${item.shortName}/${item.description}/${item.dateCreated}/${item.isActive}")
                    }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${item.name}${if (item.shortName.isNotEmpty()) " (${item.shortName})" else ""} (${item.actionCount})",
                        color = Color.Black,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "x",
                        color = Color.Red,
                        modifier = Modifier
                            .clickable {
                                db.deleteItem(item.id)
                                itemList.remove(item)
                            }
                            .padding(start = 8.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainContentPreview() {
    val navController = rememberNavController()
    val selectedDate = remember {
        mutableStateOf(SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()))
    }
    MainContent(
        navController = navController,
        sharedPreferences = LocalContext.current.getSharedPreferences("prefs", Context.MODE_PRIVATE),
        selectedDate = selectedDate,
        itemList = mutableListOf()
    )
}
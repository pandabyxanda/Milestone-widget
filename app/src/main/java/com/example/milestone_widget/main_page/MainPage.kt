package com.example.milestone_widget.main_page

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.milestone_widget.R
import com.example.milestone_widget.db.DataBase
import com.example.milestone_widget.db.Item
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun MainPage(
    navController: NavHostController,
    selectedDate: MutableState<String>,
    itemList: MutableList<Item>
) {
    val db = DataBase(LocalContext.current, null)

    val currentBackStackEntry = navController.currentBackStackEntryAsState()

    Log.d("MainContent selectedDate", selectedDate.value)
    LaunchedEffect(currentBackStackEntry.value, selectedDate.value, itemList) {
        Log.d(
            "LaunchedEffect",
            "LaunchedEffect trigger ${currentBackStackEntry.value} ${selectedDate.value}"
        )
        if (currentBackStackEntry.value?.destination?.route == "main") {
            refreshItemList(db, itemList, selectedDate.value)
        }
    }
    val refreshMainPage = {
        refreshItemList(db, itemList, selectedDate.value)
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.main_background))
    ) {
        Column {
            TopBarMain(selectedDate, onRefresh = refreshMainPage)
            MainList(
                navController = navController,
                modifier = Modifier.fillMaxSize(),
                itemList = itemList,
                refreshItemList = { refreshItemList(db, itemList, selectedDate.value) }
            )
        }
        FloatingActionButton(
            onClick = {
                navController.navigate("ItemPageCreate") {
                    popUpTo("main") { inclusive = true }
                }
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = colorResource(id = R.color.button_add_item_background)

        ) {
            Text(
                text = "+",

                fontSize = 30.sp,
            )
        }
    }
}

fun refreshItemList(db: DataBase, itemList: MutableList<Item>, selectedDate: String) {
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
                val actionCount = db.getActionsByItemIdAndDate(id, selectedDate)?.count ?: 0
                val isActive = it.getInt(it.getColumnIndexOrThrow(DataBase.ACTIVE_COL))
                itemList.add(
                    Item(
                        id,
                        name,
                        shortName,
                        description,
                        dateCreated,
                        actionCount,
                        isActive
                    )
                )
            } while (it.moveToNext())
        }
    }
    Log.d("MainList", "itemList 88")
}


@Composable
fun MainList(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    itemList: MutableList<Item>,
    refreshItemList: () -> Unit
) {
    val context = LocalContext.current
    val db = DataBase(context, null)
    val (showDialog, setShowDialog) = remember { mutableStateOf(false) }
    val (itemToDelete, setItemToDelete) = remember { mutableStateOf<Item?>(null) }
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
                    },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.main_list_card_background)),

                ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "${item.name}${if (item.shortName.isNotEmpty()) " | ${item.shortName}" else ""}",
                            color = Color.Black,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Text(
                            text = item.actionCount.toString(),
                            color = Color.Black,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    Text(
                        text = "x",
                        color = Color.Red,
                        modifier = Modifier
                            .clickable {
                                setItemToDelete(item)
                                setShowDialog(true)
                            }
                            .padding(start = 8.dp),
                    )
                }
            }
        }
    }

    if (showDialog && itemToDelete != null) {
        AlertDialog(
            onDismissRequest = { setShowDialog(false) },
            title = { Text(text = "Confirm Delete") },
            text = { Text(text = "Are you sure you want to delete '${itemToDelete.name}'?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        db.deleteItem(itemToDelete.id)
                        refreshItemList()
                        setShowDialog(false)
                    }
                ) {
                    Text("Delete", color = Color.Black)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { setShowDialog(false) }
                ) {
                    Text("Cancel", color = Color.Black)
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainContentPreview() {
    val navController = rememberNavController()
    val selectedDate = remember {
        mutableStateOf(SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()))
    }
    val itemList = remember { mutableStateListOf<Item>() }
    val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    val item1 = Item(
        1,
        "Beer",
        "\uD83C\uDF7A",
        "Sample Description",
        date,
        0,
        1
    )
    val item2 = Item(
        1,
        "Water",
        "",
        "Sample Description",
        date,
        5,
        1
    )
    val item3 = Item(
        1,
        "Trainings",
        "T",
        "Sample Description",
        date,
        0,
        0
    )
    itemList.add(item1)
    itemList.add(item2)
    itemList.add(item3)
    MainPage(
        navController = navController,
        selectedDate = selectedDate,
        itemList = itemList
    )
}
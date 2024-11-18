package com.example.milestone_widget

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.milestone_widget.ui.theme.Milestone_widgetTheme
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton

// TODO: make button click add number to total and save it 

// TODO: make widget click save data somewhere
// TODO: make widget click change text on widget


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
    }

    override fun onResume() {
        super.onResume()
    }
}

@Composable
fun CustomTopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Blue)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = { /* Handle menu click */ }) {
            Icon(Icons.Filled.Menu, contentDescription = "Menu", tint = Color.White)
        }
        Text(text = "Main Screen", color = Color.White)
        IconButton(onClick = { /* Handle search click */ }) {
            Icon(Icons.Filled.Search, contentDescription = "Search", tint = Color.White)
        }
    }
}

@Composable
fun MainScreen(sharedPreferences: SharedPreferences) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            CustomTopBar()
            DiceWithButtonAndImage(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center),
                sharedPreferences = sharedPreferences
            )
        }
        FloatingActionButton(
            onClick = {
                // Logic to create a new item
                Log.d("MainActivity", "New item created")
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Text("+")
        }
    }
}

@Composable
fun DiceWithButtonAndImage(modifier: Modifier = Modifier, sharedPreferences: SharedPreferences) {
    var result by remember { mutableStateOf(1) }
//    var count by remember { mutableStateOf(0) }
    var count = sharedPreferences.getInt("count", 0)

    var itemsList by remember { mutableStateOf((0..0).toList()) }

    val allEntries: Map<String, *> = sharedPreferences.getAll()

    val itemList = ArrayList<String>()

    for ((key, value) in allEntries) {
        itemList.add(value.toString())
    }
//    println("Updated List: $itemsList")
    Log.d("MainActivity", "itemList: $itemList")
    Log.d("MainActivity", "DiceWithButtonAndImage function called!")

    val myEdit = sharedPreferences.edit()


    val imageResource = when (result) {
        1 -> R.drawable.dice_1
        2 -> R.drawable.dice_2
        3 -> R.drawable.dice_3
        4 -> R.drawable.dice_4
        5 -> R.drawable.dice_5
        else -> R.drawable.dice_6
    }

    val db = DBHelper(LocalContext.current, null)
    val name = "hey"
    val age = count.toString()
    db.addName(name, age)

//    var names = db.getName()
    val rows = db.getLastRows(3)
    // print the last row of the database
    rows?.let {
        if (it.moveToLast()) {
            do {
                val id = it.getInt(it.getColumnIndexOrThrow(DBHelper.ID_COL))
                val name = it.getString(it.getColumnIndexOrThrow(DBHelper.NAME_COl))
                val age = it.getString(it.getColumnIndexOrThrow(DBHelper.AGE_COL))
                val timestamp = it.getString(it.getColumnIndexOrThrow(DBHelper.TIMESTAMP))
                Log.d("MainActivity", "ID: $id, Name: $name, Age: $age, Timestamp: $timestamp")
            } while (it.moveToPrevious())
        }
    }
//    row?.let {
//        if (it.moveToFirst()) {
//            val id = it.getInt(it.getColumnIndexOrThrow(DBHelper.ID_COL))
//            val name = it.getString(it.getColumnIndexOrThrow(DBHelper.NAME_COl))
//            val age = it.getString(it.getColumnIndexOrThrow(DBHelper.AGE_COL))
//            val timestamp = it.getString(it.getColumnIndexOrThrow(DBHelper.TIMESTAMP))
//            Log.d("MainActivity", "ID: $id, Name: $name, Age: $age, Timestamp: $timestamp")
//        }
//    }
//    Log.d("MainActivity", "names: $names")


    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Image(
            painter = painterResource(imageResource),
            contentDescription = "1",
            alignment = Alignment.TopCenter
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            result = (1..6).random()
            count++
            myEdit.putInt("count", count)
            myEdit.apply()
            itemsList = itemsList + (itemsList.size)
            Log.d("MainActivity", "Button clicked!")

        }) {
            Text(
                text = "pressed $count times",
                fontSize = 12.sp,
                color = Color.Cyan,

                modifier = Modifier
                    .padding(16.dp)
            )
        }
        LazyColumn {
            items(itemsList) {
                Text(
                    text = "Item is $it",
                    color = Color.Cyan,
                )
            }
        }

    }
}


//@Preview
//@Composable
//fun DiceRollerApp(
//    sharedPreferences: SharedPreferences = androidx.compose.ui.platform.LocalContext.current.getSharedPreferences("com.example.milestone_widget", Context.MODE_PRIVATE)) {
//    DiceWithButtonAndImage(
//        modifier = Modifier
//            .fillMaxSize()
//            .wrapContentSize(Alignment.Center),
//        sharedPreferences = sharedPreferences
//    )
//}
@Preview
@Composable
fun DiceRollerApp(
    sharedPreferences: SharedPreferences = androidx.compose.ui.platform.LocalContext.current.getSharedPreferences(
        "com.example.milestone_widget",
        Context.MODE_PRIVATE
    )
) {
    Box(modifier = Modifier.fillMaxSize()) {
        DiceWithButtonAndImage(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center),
            sharedPreferences = sharedPreferences
        )
        FloatingActionButton(
            onClick = {
                // Logic to create a new item
//                val db = DBHelper(LocalContext.current, null)
//                val name = "new_item"
//                val age = "0"
//                db.addName(name, age)
                Log.d("MainActivity", "New item created")
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Text("+")
        }
    }
}





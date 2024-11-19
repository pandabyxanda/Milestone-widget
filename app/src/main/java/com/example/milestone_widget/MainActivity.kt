package com.example.milestone_widget

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.milestone_widget.ui.theme.Milestone_widgetTheme
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager

//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.foundation.VerticalScrollbar
//import androidx.compose.foundation.rememberScrollbarAdapter


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
fun TextFieldWithPlaceholder(
    value: String,
    onValueChange: (String) -> Unit,
    placeholderText: String,
    labelText: String? = null,
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholderText, color = Color.Gray) },
//        label = { Text(labelText) }
    )
}

@Composable
fun CustomAlertDialog(
    onDismissRequest: () -> Unit,
    title: @Composable () -> Unit,
    text: @Composable () -> Unit,
    confirmButton: @Composable () -> Unit,
    dismissButton: @Composable () -> Unit,
    items: List<Int>
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(1f)
                .padding(16.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White)
                .padding(16.dp)
        ) {
            Column {
                Box(modifier = Modifier.focusRequester(focusRequester)) {
                    title()
                }
                Spacer(modifier = Modifier.height(8.dp))
                text()
                Spacer(modifier = Modifier.height(8.dp))
                LazyColumn {
                    items(items) { item ->
                        Text(text = "This is da $item", modifier = Modifier.padding(8.dp))
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    dismissButton()
                    Spacer(modifier = Modifier.width(8.dp))
                    confirmButton()
                }
            }
        }
    }
}


@Composable
fun MainScreen(sharedPreferences: SharedPreferences) {
    val db = DBHelper(LocalContext.current, null)
    val itemsList = remember { mutableStateListOf<Int>() }
    val showDialog = remember { mutableStateOf(false) }
    val textState = remember { mutableStateOf("") }
    val titleState = remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        val rows = db.getLastRows(50)
//        rows?.let {
//            if (it.moveToLast()) {
//                do {
//                    val id = it.getInt(it.getColumnIndexOrThrow(DBHelper.ID_COL))
//                    itemsList.add(id)
//                } while (it.moveToPrevious())
//            }
//        }
        rows?.let {
            if (it.moveToFirst()) {
                do {
                    val id = it.getInt(it.getColumnIndexOrThrow(DBHelper.ID_COL))
                    itemsList.add(id)
                } while (it.moveToNext())
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            CustomTopBar()
            MainList(
                modifier = Modifier
                    .fillMaxSize(),
                itemsList = itemsList
            )
        }
        FloatingActionButton(
            onClick = {
//                val name = "hey"
////                val age = count.toString()
//                val age = "300"
//                db.addName(name, age)
//                itemsList.add(0, 999)
                Log.d("MainActivity", "New item created")
                showDialog.value = true
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Text("+")
        }
    }

    if (showDialog.value) {
        CustomAlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = {
//                OutlinedTextField(
//                    value = textState.value,
//                    onValueChange = { textState.value = it },
//                    label = { Text("Text") }
//                )
                TextFieldWithPlaceholder(
                    value = titleState.value,
                    onValueChange = { titleState.value = it },
                    placeholderText = "Title",
                )
            },
            text = {
//                OutlinedTextField(
//                    value = textState.value,
//                    onValueChange = { textState.value = it },
//                    label = { Text("Text") }
//                )
                TextFieldWithPlaceholder(
                    value = textState.value,
                    onValueChange = { textState.value = it },
                    placeholderText = "Note",
                )
            },
            confirmButton = {
                Button(onClick = {
                    // Handle the text input
                    Log.d("MainActivity", "Text entered: ${textState.value}")
                    showDialog.value = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog.value = false }) {
                    Text("Cancel")
                }
            },
//            items = itemsList
            items = (0..10).toList()
        )
    }
}

@Composable
fun MainList(modifier: Modifier = Modifier, itemsList: List<Int>) {
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
                    text = "Item is $item",
                    color = Color.Black,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}
//@Composable
//fun MainList(modifier: Modifier = Modifier, itemsList: List<Int>) {
//    val scrollState = rememberScrollState()
//
//    Box(modifier = modifier.verticalScroll(scrollState)) {
//        LazyColumn(
//            modifier = Modifier
//                .padding(8.dp)
//                .fillMaxSize()
//        ) {
//            items(itemsList) { item ->
//                Card(
//                    modifier = Modifier
//                        .padding(vertical = 4.dp) // Adjust the vertical padding to change the gap
//                        .fillMaxWidth()
//                        .border(2.dp, Color.Black, shape = RoundedCornerShape(16.dp))
//                        .clickable {
//                            // Handle card click
//                            Log.d("MainActivity", "Card clicked: $item")
//                        }
//                ) {
//                    Text(
//                        text = "Item is $item",
//                        color = Color.Cyan,
//                        modifier = Modifier.padding(16.dp)
//                    )
//                }
//            }
//        }
//        VerticalScrollbar(
//            modifier = Modifier.align(Alignment.CenterEnd),
//            adapter = rememberScrollbarAdapter(scrollState)
//        )
//    }
//}

//@Composable
//fun DiceWithButtonAndImage(modifier: Modifier = Modifier, sharedPreferences: SharedPreferences) {
//    var result by remember { mutableStateOf(1) }
////    var count by remember { mutableStateOf(0) }
//    var count = sharedPreferences.getInt("count", 0)
//
//    var itemsList by remember { mutableStateOf((0..0).toList()) }
//
//    val allEntries: Map<String, *> = sharedPreferences.getAll()
//
//    val itemList = ArrayList<String>()
//
//    for ((key, value) in allEntries) {
//        itemList.add(value.toString())
//    }
////    println("Updated List: $itemsList")
//    Log.d("MainActivity", "itemList: $itemList")
//    Log.d("MainActivity", "DiceWithButtonAndImage function called!")
//
//    val myEdit = sharedPreferences.edit()
//
//
//    val imageResource = when (result) {
//        1 -> R.drawable.dice_1
//        2 -> R.drawable.dice_2
//        3 -> R.drawable.dice_3
//        4 -> R.drawable.dice_4
//        5 -> R.drawable.dice_5
//        else -> R.drawable.dice_6
//    }
//
//    val db = DBHelper(LocalContext.current, null)
//    val name = "hey"
//    val age = count.toString()
//    db.addName(name, age)
//
////    var names = db.getName()
//    val rows = db.getLastRows(3)
//    // print the last row of the database
//    rows?.let {
//        if (it.moveToLast()) {
//            do {
//                val id = it.getInt(it.getColumnIndexOrThrow(DBHelper.ID_COL))
//                val name = it.getString(it.getColumnIndexOrThrow(DBHelper.NAME_COl))
//                val age = it.getString(it.getColumnIndexOrThrow(DBHelper.AGE_COL))
//                val timestamp = it.getString(it.getColumnIndexOrThrow(DBHelper.TIMESTAMP))
//                Log.d("MainActivity", "ID: $id, Name: $name, Age: $age, Timestamp: $timestamp")
//            } while (it.moveToPrevious())
//        }
//    }
////    row?.let {
////        if (it.moveToFirst()) {
////            val id = it.getInt(it.getColumnIndexOrThrow(DBHelper.ID_COL))
////            val name = it.getString(it.getColumnIndexOrThrow(DBHelper.NAME_COl))
////            val age = it.getString(it.getColumnIndexOrThrow(DBHelper.AGE_COL))
////            val timestamp = it.getString(it.getColumnIndexOrThrow(DBHelper.TIMESTAMP))
////            Log.d("MainActivity", "ID: $id, Name: $name, Age: $age, Timestamp: $timestamp")
////        }
////    }
////    Log.d("MainActivity", "names: $names")
//
//
//    Column(
//        modifier = modifier,
//        horizontalAlignment = Alignment.CenterHorizontally
//
//    ) {
//        Image(
//            painter = painterResource(imageResource),
//            contentDescription = "1",
//            alignment = Alignment.TopCenter
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Button(onClick = {
//            result = (1..6).random()
//            count++
//            myEdit.putInt("count", count)
//            myEdit.apply()
//            itemsList = itemsList + (itemsList.size)
//            Log.d("MainActivity", "Button clicked!")
//
//        }) {
//            Text(
//                text = "pressed $count times",
//                fontSize = 12.sp,
//                color = Color.Cyan,
//
//                modifier = Modifier
//                    .padding(16.dp)
//            )
//        }
//        LazyColumn {
//            items(itemsList) {
//                Text(
//                    text = "Item is $it",
//                    color = Color.Cyan,
//                )
//            }
//        }
//
//    }
//}


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
        MainScreen(sharedPreferences = sharedPreferences)
    }
}
//    Box(modifier = Modifier.fillMaxSize()) {
//        DiceWithButtonAndImage(
//            modifier = Modifier
//                .fillMaxSize()
//                .wrapContentSize(Alignment.Center),
//            sharedPreferences = sharedPreferences
//        )
//        FloatingActionButton(
//            onClick = {
//                // Logic to create a new item
////                val db = DBHelper(LocalContext.current, null)
////                val name = "new_item"
////                val age = "0"
////                db.addName(name, age)
//                Log.d("MainActivity", "New item created")
//            },
//            modifier = Modifier
//                .align(Alignment.BottomEnd)
//                .padding(16.dp)
//        ) {
//            Text("+")
//        }
//    }






package com.example.milestone_widget

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.milestone_widget.db.DataBase
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import com.example.milestone_widget.db.Action

//@Composable
//fun ItemPage(itemName: String?) {
//    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//        Text(text = "Item: $itemName", color = Color.Black, modifier = Modifier.padding(16.dp))
//    }
//}

@Composable
fun ItemPageUpdate(
    navController: NavHostController,
    id: Int?,
    name: String?,
    shortName: String?,
    description: String?,
    dateCreated: String?
) {
    val context = LocalContext.current
    val db = DataBase(context, null)
    val actionList = remember { mutableStateListOf<Action>() }

    LaunchedEffect(id) {
        id?.let {
            val rows = db.getActionsByItemId(it)
            rows?.let {
                if (it.moveToFirst()) {
                    do {
                        val actionId = it.getInt(it.getColumnIndexOrThrow("action_id"))
                        val actionDate = it.getString(it.getColumnIndexOrThrow("date"))
                        actionList.add(Action(actionId, actionDate))
                    } while (it.moveToNext())
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            CustomTopBarItem(navController = navController)
            Spacer(modifier = Modifier.height(16.dp))
            TextFieldWithPlaceholderItemPage(
                value = name ?: "",
                onValueChange = { /* handle name change */ },
                placeholderText = "Name",
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                items(actionList) { action ->
                    Text(
                        text = action.date,
                        color = Color.Black,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }
        }
    }
}

//@Composable
//fun ItemPage(
//    navController: NavHostController,
//    id: Int?,
//    name: String?,
//    shortName: String?,
//    description: String?,
//    dateCreated: String?
//) {
//    var newName: String
//    if (name != null) {
//        newName = name
//    } else {
//        newName = ""
//    }
//    Box(modifier = Modifier.fillMaxSize()) {
//        Column {
//            CustomTopBarItem(navController = navController)
//            Spacer(modifier = Modifier.height(16.dp))
//            TextFieldWithPlaceholderItemPage(
//                value = newName,
//                onValueChange = { newName = it },
//                placeholderText = "Name",
//                modifier = Modifier
//                    .fillMaxWidth(),
//            )
////            Spacer(modifier = Modifier.height(8.dp))
////            TextFieldWithPlaceholderItemPage(
////                value = shortNameState.value,
////                onValueChange = { shortNameState.value = it },
////                placeholderText = "Short Name (shown in the widget)",
////                modifier = Modifier.fillMaxWidth(),
////            )
////            Spacer(modifier = Modifier.height(8.dp))
////            TextFieldWithPlaceholderItemPage(
////                value = descriptionState.value,
////                onValueChange = { descriptionState.value = it },
////                placeholderText = "Description",
////                modifier = Modifier.fillMaxWidth(),
////            )
//        }
//    }
//}
//@Composable
//fun TextFieldWithPlaceholderItemPage(
//    value: String,
//    onValueChange: (String) -> Unit,
//    placeholderText: String,
//    modifier: Modifier = Modifier,
//    labelText: String? = null,
//) {
//    TextField(
//        value = value,
//        onValueChange = onValueChange,
//        placeholder = { Text(placeholderText, color = Color.Gray) },
//        modifier = modifier
//    )
//}
@Composable
fun TextFieldWithPlaceholderItemPage(
    value: String,
    onValueChange: (String) -> Unit,
    placeholderText: String,
    modifier: Modifier = Modifier,
    labelText: String? = null,
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholderText, color = Color.Gray) },
        modifier = modifier
    )
}
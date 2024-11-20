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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.milestone_widget.db.DBHelper

//@Composable
//fun ItemPage(itemName: String?) {
//    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//        Text(text = "Item: $itemName", color = Color.Black, modifier = Modifier.padding(16.dp))
//    }
//}

@Composable
fun ItemPage(navController: NavHostController, itemName: String?) {
    val context = LocalContext.current
    val db = DBHelper(context, null)
    val nameState = remember { mutableStateOf(itemName ?: "") }
    val shortNameState = remember { mutableStateOf("") }
    val descriptionState = remember { mutableStateOf("") }

    DisposableEffect(Unit) {
        onDispose {
            if (nameState.value.isNotEmpty()) {
                db.addItem(nameState.value, shortNameState.value, descriptionState.value)
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            CustomTopBarItem(navController = navController)
            Spacer(modifier = Modifier.height(16.dp))
            TextFieldWithPlaceholderItemPage(
                value = nameState.value,
                onValueChange = { nameState.value = it },
                placeholderText = "Name",
                modifier = Modifier
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextFieldWithPlaceholderItemPage(
                value = shortNameState.value,
                onValueChange = { shortNameState.value = it },
                placeholderText = "Short Name (shown in the widget)",
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextFieldWithPlaceholderItemPage(
                value = descriptionState.value,
                onValueChange = { descriptionState.value = it },
                placeholderText = "Description",
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

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
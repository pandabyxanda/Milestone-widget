package com.example.milestone_widget

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.example.milestone_widget.db.Action
import com.example.milestone_widget.db.DataBase

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
                    Button(
                        onClick = { /* handle button click */ },
                        colors = ButtonDefaults.buttonColors(Color.LightGray),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Text(text = action.date, color = Color.Black)
                    }
                }
            }
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
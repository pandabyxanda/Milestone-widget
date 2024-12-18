package com.example.milestone_widget.other_pages

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.milestone_widget.R
import com.example.milestone_widget.db.Action
import com.example.milestone_widget.db.DataBase


@Composable
fun ItemPageUpdate(
    navController: NavHostController,
    id: Int?,
    name: String?,
    shortName: String?,
    description: String?,
    dateCreated: String?,
    isActive: Int?,
    selectedDate: MutableState<String>,
    onItemAddedListener: OnItemAddedListener
) {
    val context = LocalContext.current
    val db = DataBase(context, null)
    val actionList = remember { mutableStateListOf<Action>() }
    val nameState = remember { mutableStateOf(name ?: "") }
    val shortNameState = remember { mutableStateOf(shortName ?: "") }
    val descriptionState = remember { mutableStateOf(description ?: "") }
    val isActiveState = remember { mutableStateOf(isActive == 1) }
    LaunchedEffect(id) {
        id?.let { it ->
            val rows = db.getActionsByItemIdAndDate(it, selectedDate.value)
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

    DisposableEffect(Unit) {
        onDispose {
            id?.let {
                if (nameState.value.isNotEmpty()) {
                    db.updateItem(
                        it,
                        nameState.value,
                        shortNameState.value,
                        descriptionState.value,
                        if (isActiveState.value) 1 else 0
                    )
//                updateWidget(context)
                    onItemAddedListener.onItemAdded()
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.main_background))
    ) {
        Column {
            TopBarItemPage(
                navController = navController,
                text = "Update ${nameState.value}"
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextFieldWithPlaceholder(
                value = nameState.value,
                onValueChange = { nameState.value = it },
                placeholderText = "Name",
                modifier = Modifier
                    .fillMaxWidth(),
                textSize = 24,
                placeholderTextSize = 24,
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextFieldWithPlaceholder(
                value = shortNameState.value,
                onValueChange = { shortNameState.value = it },
                placeholderText = "Short Name (shown in the widget)",
                modifier = Modifier.fillMaxWidth(),
                textSize = 18,
                placeholderTextSize = 18,
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextFieldWithPlaceholder(
                value = descriptionState.value,
                onValueChange = { descriptionState.value = it },
                placeholderText = "Description",
                modifier = Modifier.fillMaxWidth(),
                textSize = 18,
                placeholderTextSize = 18,
                multiLines = true,
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Checkbox(
                    checked = isActiveState.value,
                    onCheckedChange = { isActiveState.value = it },
                    colors = CheckboxDefaults.colors(
                        checkedColor = colorResource(id = R.color.button_add_item_background),
                        uncheckedColor = Color.Red,
                        checkmarkColor = Color.White
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Show in widget", color = Color.Black)
            }

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(thickness = 1.dp, color = Color.Gray)
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 8.dp,
                        end = 8.dp,
                        top = 0.dp,
                        bottom = 0.dp
                    )
            ) {

                items(actionList) { action ->
                    Button(
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(Color.LightGray),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = action.date,
                                color = Color.Black,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = "x",
                                color = Color.Red,
                                modifier = Modifier
                                    .clickable {
                                        db.deleteAction(action.id)
                                        actionList.remove(action)
                                    }
                                    .padding(start = 8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ItemPageUpdatePreview() {
    val navController = rememberNavController()
    val selectedDate = remember { mutableStateOf("2024-11-22") }
    ItemPageUpdate(
        navController = navController,
        id = 1,
        name = "Sample Item",
        shortName = "Sample",
        description = "",
        dateCreated = "2024-11-22",
        isActive = 1,
        selectedDate = selectedDate,
        object : OnItemAddedListener {
            override fun onItemAdded() {
            }
        }
    )
}
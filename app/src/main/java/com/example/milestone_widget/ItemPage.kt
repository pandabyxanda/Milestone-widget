package com.example.milestone_widget

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun NewPage(navController: NavHostController) {
    val textState = remember { mutableStateOf("") }
    val titleState = remember { mutableStateOf("") }
    val titleFocusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        titleFocusRequester.requestFocus()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            CustomTopBarItem(navController = navController)
            Spacer(modifier = Modifier.height(16.dp))
            TextFieldWithPlaceholder(
                value = titleState.value,
                onValueChange = { titleState.value = it },
                placeholderText = "Title (shown in the widget)",
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(titleFocusRequester),
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextFieldWithPlaceholder(
                value = textState.value,
                onValueChange = { textState.value = it },
                placeholderText = "Description",
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
fun TextFieldWithPlaceholder(
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
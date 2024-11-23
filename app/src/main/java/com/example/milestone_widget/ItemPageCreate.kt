package com.example.milestone_widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.milestone_widget.db.DataBase
import com.example.milestone_widget.db.Item
import com.example.milestone_widget.widget.updateWidget
import java.time.format.TextStyle

interface OnItemAddedListener {
    fun onItemAdded()
}

@Composable
fun ItemPageCreate(
    navController: NavHostController,
    itemList: MutableList<Item>,
    onItemAddedListener: OnItemAddedListener
) {
    val context = LocalContext.current
    val db = DataBase(context, null)
    val nameState = remember { mutableStateOf("") }
    val shortNameState = remember { mutableStateOf("") }
    val descriptionState = remember { mutableStateOf("") }
    val nameFocusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        nameFocusRequester.requestFocus()
    }

    DisposableEffect(Unit) {
        onDispose {
            if (nameState.value.isNotEmpty()) {
                db.addItem(nameState.value, shortNameState.value, descriptionState.value)
                updateWidget(context)
                onItemAddedListener.onItemAdded()
            }
        }
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(colorResource(id = R.color.main_background))
    ) {
        Column {
            TopBarItemPage(
                navController = navController,
                text = "Create new item"
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextFieldWithPlaceholder(
                value = nameState.value,
                onValueChange = { nameState.value = it },
                placeholderText = "Name",
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(nameFocusRequester),
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
    textSize: Int = 18,
    placeholderTextSize: Int = 18,
    multiLines: Boolean = false,
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = placeholderText,
                color = Color.Gray,
                fontSize = placeholderTextSize.sp,
                )
        },
        modifier = modifier,
        colors = OutlinedTextFieldDefaults.colors(
//            unfocusedContainerColor = Color.Gray,
//            focusedLabelColor = Color.Transparent,
//            unfocusedBorderColor = Color.Green,
            unfocusedBorderColor = Color.Transparent,
            focusedBorderColor = Color.Transparent
        ),
        shape = RoundedCornerShape(16.dp),
        textStyle = androidx.compose.ui.text.TextStyle(fontSize = textSize.sp),
        singleLine = !multiLines,
    )
}

@Preview(showBackground = true)
@Composable
fun ItemPageCreatePreview() {
    val navController = rememberNavController()
    ItemPageCreate(
        navController = navController,
        itemList = mutableListOf(),
        object : OnItemAddedListener {
            override fun onItemAdded() {
                // Do nothing
            }
        }
    )
}
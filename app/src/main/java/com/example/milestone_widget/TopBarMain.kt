package com.example.milestone_widget

import android.app.DatePickerDialog
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.util.Calendar


@Composable
fun TopBarMain(selectedDate: MutableState<String>) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    if (selectedDate.value.isNotEmpty()) {
        val parts = selectedDate.value.split("-")
        if (parts.size == 3) {
            calendar.set(parts[0].toInt(), parts[1].toInt() - 1, parts[2].toInt())
        }
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Blue)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
//        IconButton(onClick = { /* Handle menu click */ }) {
//            Icon(Icons.Filled.Menu, contentDescription = "Menu", tint = Color.White)
//        }
//        Text(text = "Main Screen", color = Color.White)

        IconButton(onClick = {
            val datePickerDialog = DatePickerDialog(
                context,
                { _, year, month, dayOfMonth ->
                    selectedDate.value = "$year-${month + 1}-$dayOfMonth"
                    Log.d("TopBarMain", "Selected date: ${selectedDate.value}")
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
        }) {
            Icon(Icons.Filled.DateRange, contentDescription = "Select Date", tint = Color.White)
//            Spacer(modifier = Modifier.width(8.dp))
//            Text(text = "Select Date")
        }

//        IconButton(onClick = { /* Handle search click */ }) {
//            Icon(Icons.Filled.Search, contentDescription = "Search", tint = Color.White)
//        }
    }
}


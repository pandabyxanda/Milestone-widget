package com.example.milestone_widget

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class SecondActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("SecondActivity", "SecondActivity onCreate")
//        setContentView(R.layout.xwidget)

//        // Find the Go Back button
//        val buttonGoBack = findViewById<Button>(R.id.buttonGoBack)
//
//        // Set up the click listener to go back to MainActivity
//        buttonGoBack.setOnClickListener {
//            finish() // Ends this activity and returns to the previous one
//        }
    }
}
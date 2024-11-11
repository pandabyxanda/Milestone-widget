package com.example.milestone_widget

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.milestone_widget.ui.theme.Milestone_widgetTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.Surface
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Milestone_widgetTheme {
                // A surface container using the 'background' color from the theme
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {
//                    Greeting(name = "Sam", from = "From Emma")
//                }
                GreetingImage(
                    name = "Sam",
                    from = "From Emma",
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}

//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContent {
//            Milestone_widgetTheme {
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    Greeting(
//                        name = "Panda main",
//                        modifier = Modifier.padding(innerPadding)
//                    )
//                }
//            }
//        }
//    }
//}

@Composable
fun GreetingText(name: String, from: String = "default", modifier: Modifier = Modifier) {
//    Surface(color = Color.Magenta) {
        Column (
//            verticalArrangement = Arrangement.Center,

            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally,

//            modifier = modifier
            modifier = Modifier
                .fillMaxSize()
        )
        {

            Text(
                text = "Hi, my name is $name!",
                fontSize = 36.sp,
                lineHeight = 40.sp,
                //            modifier = modifier.padding(24.dp),

                textAlign = TextAlign.Center,

                modifier = Modifier.background(color = Color.Green)
            )
            Text(
                text = "from $from",
                fontSize = 12.sp,
                color = Color.Cyan,
//                textAlign = TextAlign.Center,

                modifier = Modifier
                    .padding(16.dp)
                    .align(alignment = Alignment.End)

//                textAlign =
            )
        }
//    }
}
@Composable
fun GreetingImage(name: String, from: String, modifier: Modifier = Modifier) {
    val image = painterResource(R.drawable.androidparty)
    Box(modifier) {
        Image(
            painter = image,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            alpha = 0.5F
        )

        GreetingText(
            name = name,
            from = from
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Milestone_widgetTheme {
        GreetingImage(
            name = stringResource(R.string.happy_birthday_text),
            from = "hhh"
        )
    }
}

//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    Milestone_widgetTheme {
//        Greeting("Panda", "Br")
//    }
//}

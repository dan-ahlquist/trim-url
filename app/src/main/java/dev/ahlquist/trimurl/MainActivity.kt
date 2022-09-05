package dev.ahlquist.trimurl

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import dev.ahlquist.trimurl.ui.theme.TrimURLTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val data = intent.getStringExtra(Intent.EXTRA_PROCESS_TEXT)

        setContent {
            TrimURLTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Greeting(data)
                }
            }
        }
    }
}

@Composable
fun Greeting(data: String?) {
    data?.let { Text(text = "Intent data:\n$data") }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview2() {
    TrimURLTheme {
        Greeting("2")
    }
}
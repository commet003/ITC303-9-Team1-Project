package com.csu_itc303_team1.adhdtaskmanager


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.webkit.WebView
import androidx.activity.ComponentActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material3.*
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpScreen() {
    val context = LocalContext.current
    var viewManualClicked by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Welcome to Help Page")

                }
            )
            //Spacer(modifier = Modifier.height(46.dp))
        }

    ) {

        Column(
            modifier = Modifier
                .fillMaxSize() // Make the Column fill the available space
                .padding(79.dp)

        ) {
            Text(
                text = "Click on One Of the Options: ",
                style = TextStyle(fontSize = 18.sp, fontStyle = FontStyle.Italic),
                color = Color.Blue
            )

            Spacer(modifier = Modifier.height(15.dp))



            // "View Manual" row
            Row(
                modifier = Modifier.clickable {
                    viewManualClicked = true
                    coroutineScope.launch {
                        //openUserManual(context)
                        //For simplicity,launching an Intent to open a web URL.

                        val intent = Intent(context, WebViewActivity::class.java)
                        context.startActivity(intent)
                    }
                }



            ) {
                Text(
                    text = "- View Manual",
                    style = TextStyle(fontSize = 20.sp),
                    textDecoration = if (viewManualClicked) TextDecoration.Underline else TextDecoration.None
                )

            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "- View Architecture Diagram", style = TextStyle(fontSize = 20.sp))
        }
    }
}



// Function to open the user manual in a WebView
private fun openUserManual(context: ComponentActivity) {
    val intent = Intent(context, WebViewActivity::class.java)
    context.startActivity(intent)
}

// WebViewActivity that loads the HTML content in a WebView
class WebViewActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val webView = WebView(this)
        setContentView(webView)

        // Load the user manual HTML file from the assets folder
        webView.loadUrl("file:///android_asset/Manual.html")

    }
}

@Preview
@Composable
fun HelpScreenPreview() {
    HelpScreen()
}

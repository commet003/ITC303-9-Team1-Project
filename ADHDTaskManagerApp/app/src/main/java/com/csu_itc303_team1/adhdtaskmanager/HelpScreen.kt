package com.csu_itc303_team1.adhdtaskmanager


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHostState
import androidx.compose.material3.*
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)

//@Composable
//fun HelpScreen() {
//    val context = LocalContext.current
//    var viewManualClicked by remember { mutableStateOf(false) }
//    val coroutineScope = rememberCoroutineScope()
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = {
//                    Text(text = "Welcome to Help Page")
//
//                }
//            )
//            //Spacer(modifier = Modifier.height(46.dp))
//        }
//
//    ) {
//
//        Column(
//            modifier = Modifier
//                .fillMaxSize() // Make the Column fill the available space
//                .padding(79.dp)
//
//        ) {
//            Text(
//                text = "Click on One Of the Options: ",
//                style = TextStyle(fontSize = 18.sp, fontStyle = FontStyle.Italic),
//                color = Color.Blue
//            )
//
//            Spacer(modifier = Modifier.height(15.dp))
//
//
//
//            // "View Manual" row
//            Row(
//                modifier = Modifier.clickable {
//                    viewManualClicked = true
//                    coroutineScope.launch {
//                        //openUserManual(context)
//                        //For simplicity,launching an Intent to open a web URL.
//
//                        val intent = Intent(context, WebViewActivity::class.java)
//                        context.startActivity(intent)
//                    }
//                }
//
//
//
//            ) {
//                Text(
//                    text = "- View Manual",
//                    style = TextStyle(fontSize = 20.sp),
//                    textDecoration = if (viewManualClicked) TextDecoration.Underline else TextDecoration.None
//                )
//
//            }
//            Spacer(modifier = Modifier.height(16.dp))
//
//
//            //Text(text = "- View Architecture Diagram", style = TextStyle(fontSize = 20.sp))
//
//            // "View Architecture Diagram" row
//            Row(
//                modifier = Modifier.clickable {
//                    viewManualClicked = true
//                    coroutineScope.launch {
//                        //open Architecture Diagram (context)
//                        //For simplicity,launching an Intent to open a web URL.
//
//                        val intent = Intent(context, WebViewActivity::class.java)
//                        context.startActivity(intent)
//                    }
//                }
//
//
//
//            ) {
//                Text(
//                    text = "- View Architecture Diagram",
//                    style = TextStyle(fontSize = 20.sp),
//                    textDecoration = if (viewManualClicked) TextDecoration.Underline else TextDecoration.None
//                )
//
//            }
//            // "View the Video" row
//            Row(
//                modifier = Modifier.clickable {
//                    viewManualClicked = true
//                    coroutineScope.launch {
//                        //open Video (context)
//                        //For simplicity,launching an Intent to open a web URL.
//
//                        val intent = Intent(context, WebViewActivity::class.java)
//                        context.startActivity(intent)
//                    }
//                }
//
//
//
//            ) {
//                Text(
//                    text = "- View the Demo Video",
//                    style = TextStyle(fontSize = 20.sp),
//                    textDecoration = if (viewManualClicked) TextDecoration.Underline else TextDecoration.None
//                )
//
//            }
//        }
//    }
//}

@Composable
fun HelpScreen() {
    val context = LocalContext.current
    var viewManualClicked by remember { mutableStateOf(false) }
    var viewArchitectureClicked by remember { mutableStateOf(false) }
    var viewVideoClicked by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Welcome to the Help Page")
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
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
                    viewArchitectureClicked = false
                    viewVideoClicked = false
                    coroutineScope.launch {
                        val intent = Intent(context, WebViewActivity::class.java)
                        intent.putExtra("URL", "file:///android_asset/Manual.html")
                        context.startActivity(intent)
                    }
                }
            ) {
                Text(
                    text = "- View Manual",
                    style = TextStyle(
                        fontSize = 20.sp,
                        textDecoration = if (viewManualClicked) TextDecoration.Underline else TextDecoration.None
                    )
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            // "View Architecture Diagram" row
            Row(
                modifier = Modifier.clickable {
                    viewManualClicked = false
                    viewArchitectureClicked = true
                    viewVideoClicked = false
                    coroutineScope.launch {
                        val intent = Intent(context, WebViewActivity::class.java)
                        intent.putExtra("URL", "file:///android_asset/Developers Delight.html")
                        context.startActivity(intent)
                    }
                }
            ) {
                Text(
                    text = "- Developers Resources",
                    style = TextStyle(
                        fontSize = 20.sp,
                        textDecoration = if (viewArchitectureClicked) TextDecoration.Underline else TextDecoration.None
                    )
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            // "View the Video" row
            Row(
                modifier = Modifier.clickable {
                    viewManualClicked = false
                    viewArchitectureClicked = false
                    viewVideoClicked = true
                    coroutineScope.launch {
                        val intent = Intent(context, WebViewActivity::class.java)
                        //intent.putExtra("URL", "file:///android_asset/p.mov")
                        intent.putExtra("URL", "https://youtu.be/H0hJHFFbrB0")
                        context.startActivity(intent)
                    }
                }
            ) {
                Text(
                    text = "- View the Demo Video",
                    style = TextStyle(
                        fontSize = 20.sp,
                        textDecoration = if (viewVideoClicked) TextDecoration.Underline else TextDecoration.None
                    )
                )
            }
        }
    }
}




// Function to open the user manual in a WebView
//private fun openUserManual(context: ComponentActivity) {
//    val intent = Intent(context, WebViewActivity::class.java)
//    context.startActivity(intent)
//}

// WebViewActivity that loads the HTML content in a WebView
//class WebViewActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        val webView = WebView(this)
//        setContentView(webView)
//
//        // Load the user manual HTML file from the assets folder
//        webView.loadUrl("file:///android_asset/Manual.html")
//
//    }
//}

class WebViewActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val webView = WebView(this)
        setContentView(webView)

        // Retrieve the URL from the Intent extras
        val url = intent.getStringExtra("URL")

        // Enable JavaScript to play videos
        webView.settings.javaScriptEnabled = true

        // Load the URL in the WebView
        if (url != null) {
            webView.loadUrl(url)
        } else {
            // Handle the case where the URL is null or not provided.
            // You can show an error message or load a default URL here.
            webView.loadUrl("file:///android_asset/Error.html")

        }

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                // Load assets from the app package instead of treating them as file URLs
                //view?.loadUrl(request?.url?.toString())
                val url = request?.url?.toString() ?: ""
                view?.loadUrl(url)
                return true
            }
        }
        // Set a custom WebChrome client to handle video playback
        webView.webChromeClient = WebChromeClient()

        // Load the video from the assets folder in the WebView
        //webView.loadUrl("https://youtu.be/H0hJHFFbrB0")
    }
}



@Preview
@Composable
fun HelpScreenPreview() {
    HelpScreen()
}

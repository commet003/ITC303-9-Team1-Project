package com.csu_itc303_team1.adhdtaskmanager.ui.help_screen


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.csu_itc303_team1.adhdtaskmanager.ui.ui_components.MainTopAppBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)




@Composable
fun HelpScreen(
    scope: CoroutineScope,
    drawerState: DrawerState
) {
    val context = LocalContext.current
    var viewManualClicked by remember { mutableStateOf(false) }
    var viewArchitectureClicked by remember { mutableStateOf(false) }
    var viewVideoClicked by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val text = context.assets.open("UserManual.txt").bufferedReader().use { it.readText() }

    Scaffold(
        topBar = { MainTopAppBar(scope = scope, drawerState = drawerState)}
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                //.padding(79.dp)
                .padding(10.dp, 80.dp, 10.dp, 10.dp)
        ) {
//            Text(
//                text = "Click on One Of the Options: ",
//                style = TextStyle(fontSize = 18.sp, fontStyle = FontStyle.Italic),
//                color = Color.Blue
//            )
//
//            Spacer(modifier = Modifier.height(15.dp))
//
//            // "View Manual" row
//            Row(
//                modifier = Modifier.clickable {
//                    viewManualClicked = true
//                    viewArchitectureClicked = false
//                    viewVideoClicked = false
//                    coroutineScope.launch {
//                        val intent = Intent(context, WebViewActivity::class.java)
//                        intent.putExtra("URL", "file:///android_asset/Manual.html")
//                        context.startActivity(intent)
//                    }
//                }
//            ) {
//                Text(
//                    text = "- View Manual",
//                    style = TextStyle(
//                        fontSize = 20.sp,
//                        textDecoration = if (viewManualClicked) TextDecoration.Underline else TextDecoration.None
//                    )
//                )
//            }
//            Spacer(modifier = Modifier.height(16.dp))
//
//            // "View Architecture Diagram" row
//            Row(
//                modifier = Modifier.clickable {
//                    viewManualClicked = false
//                    viewArchitectureClicked = true
//                    viewVideoClicked = false
//                    coroutineScope.launch {
//                        val intent = Intent(context, WebViewActivity::class.java)
//                        intent.putExtra("URL", "file:///android_asset/Developers Delight.html")
//                        context.startActivity(intent)
//                    }
//                }
//            ) {
//                Text(
//                    text = "- Developers Resources",
//                    style = TextStyle(
//                        fontSize = 20.sp,
//                        textDecoration = if (viewArchitectureClicked) TextDecoration.Underline else TextDecoration.None
//                    )
//                )
//            }
//            Spacer(modifier = Modifier.height(16.dp))
//
//            // "View the Video" row
//            Row(
//                modifier = Modifier.clickable {
//                    viewManualClicked = false
//                    viewArchitectureClicked = false
//                    viewVideoClicked = true
//                    coroutineScope.launch {
//                        val intent = Intent(context, WebViewActivity::class.java)
//                        //intent.putExtra("URL", "file:///android_asset/p.mov")
//                        intent.putExtra("URL", "https://youtu.be/H0hJHFFbrB0")
//                        context.startActivity(intent)
//                    }
//                }
//            ) {
//                Text(
//                    text = "- View the Demo Video",
//                    style = TextStyle(
//                        fontSize = 20.sp,
//                        textDecoration = if (viewVideoClicked) TextDecoration.Underline else TextDecoration.None
//                    )
//                )
//            }
            LazyColumn {
                item {
                    Text(
                        text = text,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }


}






class WebViewActivity : ComponentActivity() {
    private lateinit var webView: WebView
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
    }
}
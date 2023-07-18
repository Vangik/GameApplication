package com.example.gameapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.CookieManager
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.example.gameapplication.navigation.SetupNavHost
import com.example.gameapplication.repository.makeServerRequest
import com.example.gameapplication.ui.theme.GameApplicationTheme
import com.example.gameapplication.viewModel.FarmGameViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val viewModel: FarmGameViewModel by viewModels()

    private var webViewVisible = false

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            webViewVisible = savedInstanceState.getBoolean("webview_visible", false)
        }

        if (!webViewVisible) {
            lifecycleScope.launch {
                val result = makeServerRequest()

                if (result) {
                    showWebView()
                }
            }
        }
        setContent {
            GameApplicationTheme {
                App()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("webview_visible", webViewVisible)
    }

    @Composable
    private fun App() {
        if (webViewVisible) {
            WebViewContainer()
        } else {
            val navController = rememberNavController()
            SetupNavHost(navController = navController, viewModel)
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Composable
    private fun WebViewContainer() {
        val context = LocalContext.current

        AndroidView(factory = { WebView(context) }, modifier = Modifier.fillMaxSize()) { webView ->
            webView.settings.javaScriptEnabled = true

            webView.webChromeClient = WebChromeClient()
            webView.webViewClient = WebViewClient()

            val cookieManager = CookieManager.getInstance()
            cookieManager.setAcceptCookie(true)

            webView.loadUrl("https://www.google.com")
        }
    }

    private fun showWebView() {
        webViewVisible = true
    }
}

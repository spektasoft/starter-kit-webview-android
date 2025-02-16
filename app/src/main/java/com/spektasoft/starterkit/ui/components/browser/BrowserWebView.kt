package com.spektasoft.starterkit.ui.components.browser

import android.annotation.SuppressLint
import android.os.Build
import android.view.ViewGroup
import android.webkit.WebView
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun BrowserWebView(
    modifier: Modifier = Modifier,
    baseUrl: String,
    isRefreshing: Boolean,
    onSetRefreshed: () -> Unit,
    onUpdateProgress: (Int) -> Unit
) {
    var webView: WebView? = null

    BackHandler {
        webView?.let {
            if (it.canGoBack()) it.goBack()
        }
    }

    AndroidView(
        modifier = modifier,
        factory = {
            WebView(it).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                webViewClient = BrowserWebViewClientCompat(baseUrl, it)
                webChromeClient = BrowserWebChromeClient(onUpdateProgress)
                with(this.settings) {
                    domStorageEnabled = true
                    javaScriptEnabled = true

                    setSupportZoom(false)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        isAlgorithmicDarkeningAllowed = true
                    }
                }
                this.loadUrl(baseUrl)
                webView = this
            }
        },
        update = {
            if (isRefreshing) {
                it.reload()
                onSetRefreshed()
            }

            webView = it
        }
    )
}
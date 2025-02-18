package com.spektasoft.starterkit.ui.components.browser

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.webkit.WebView
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.children
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun BrowserWebView(
    modifier: Modifier = Modifier,
    baseUrl: String,
    onUpdateProgress: (Int) -> Unit
) {
    var webView: WebView? = null

    var progress by rememberSaveable { mutableIntStateOf(0) }

    BackHandler {
        webView?.let {
            if (it.canGoBack()) it.goBack()
        }
    }

    AndroidView(
        modifier = modifier,
        factory = {
            val mWebView = WebView(it).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                webViewClient = BrowserWebViewClientCompat(baseUrl, it)
                webChromeClient = BrowserWebChromeClient { p ->
                    progress = p
                    onUpdateProgress(p)
                }
                with(this.settings) {
                    domStorageEnabled = true
                    javaScriptEnabled = true

                    setSupportZoom(false)
                }
                this.loadUrl(baseUrl)
                webView = this
            }

            val mSwipeRefreshLayout = SwipeRefreshLayout(it).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                addView(mWebView)
                setOnRefreshListener {
                    webView?.reload()
                }
            }

            mSwipeRefreshLayout
        },
        update = {
            if (progress == 100) {
                it.isRefreshing = false
            }
            webView = it.children.first { c ->
                c is WebView
            } as? WebView
        }
    )
}
package com.spektasoft.starterkit.ui.components.browser

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.webkit.WebView
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.spektasoft.starterkit.R

@SuppressLint("SetJavaScriptEnabled", "InflateParams")
@Composable
fun BrowserWebView(
    baseUrl: String,
    modifier: Modifier = Modifier,
    onUpdateProgress: (Int) -> Unit
) {
    if (LocalInspectionMode.current) return

    var webView: WebView? = null

    var bundle by rememberSaveable { mutableStateOf<Bundle?>(null) }
    var progress by rememberSaveable { mutableIntStateOf(0) }

    BackHandler {
        webView?.let {
            if (it.canGoBack()) it.goBack()
        }
    }

    DisposableEffect(LocalLifecycleOwner.current) {
        onDispose {
            bundle = Bundle().also { bundle ->
                webView?.saveState(bundle)
            }
        }
    }

    AndroidView(
        modifier = modifier,
        factory = {
            val view = LayoutInflater.from(it).inflate(R.layout.browser_web_view, null)
            view.findViewById<WebView>(R.id.webView).apply {
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
                bundle?.let { b -> restoreState(b) } ?: this.loadUrl(baseUrl)
                webView = this
            }
            view.findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayout).apply {
                setOnRefreshListener {
                    webView?.reload()
                }
            }
            view
        },
        update = {
            val mCircularProgressIndicator =
                it.findViewById<CircularProgressIndicator>(R.id.circularProgressIndicator)
            val mWebView = it.findViewById<WebView>(R.id.webView)
            val mSwipeRefreshLayout = it.findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayout)

            if (progress == 100) {
                mSwipeRefreshLayout.isRefreshing = false
                mWebView.visibility = VISIBLE
                mCircularProgressIndicator.visibility = INVISIBLE
            } else {
                mWebView.visibility = INVISIBLE
                if (!mSwipeRefreshLayout.isRefreshing) {
                    mCircularProgressIndicator.visibility = VISIBLE
                }
            }

            webView = mWebView
        }
    )
}

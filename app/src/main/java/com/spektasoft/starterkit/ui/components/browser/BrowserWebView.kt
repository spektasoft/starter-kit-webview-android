package com.spektasoft.starterkit.ui.components.browser

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.webkit.WebView
import android.widget.LinearLayout
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.spektasoft.starterkit.R
import com.spektasoft.starterkit.ui.components.browser.config.BrowserWebChromeClientConfig
import com.spektasoft.starterkit.ui.components.browser.config.BrowserWebViewClientCompatConfig

@SuppressLint("SetJavaScriptEnabled", "InflateParams")
@Composable
fun BrowserWebView(
    baseUrl: String,
    modifier: Modifier = Modifier,
    webChromeClientConfig: BrowserWebChromeClientConfig = BrowserWebChromeClientConfig(),
    webViewClientCompatConfig: BrowserWebViewClientCompatConfig = BrowserWebViewClientCompatConfig(),
) {
    if (LocalInspectionMode.current) return

    var webView: WebView? = null

    var bundle by rememberSaveable { mutableStateOf<Bundle?>(null) }
    var canGoBack by rememberSaveable { mutableStateOf(false) }
    var progress by rememberSaveable { mutableIntStateOf(0) }
    var openLinkDialog by remember { mutableStateOf<String?>(null) }

    val mWebChromeClientConfig = webChromeClientConfig.copy(
        progressChangedHandler = { p ->
            progress = p
            webChromeClientConfig.progressChangedHandler(p)
        }
    )

    val mWebViewClientCompatConfig = webViewClientCompatConfig.copy(
        shouldOverrideUrlLoadingHandler = { _, request ->
            if (request.url.toString().startsWith(baseUrl)) {
                return@copy false
            }

            openLinkDialog = request.url.toString()

            true
        }
    )

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
                webChromeClient = BrowserWebChromeClient(mWebChromeClientConfig)
                webViewClient = BrowserWebViewClientCompat(mWebViewClientCompatConfig)
                with(this.settings) {
                    domStorageEnabled = true
                    javaScriptEnabled = true

                    setSupportZoom(false)
                }
                addJavascriptInterface(BrowserInterface(it), "Android")
                bundle?.let { b -> restoreState(b) } ?: this.loadUrl(baseUrl)
            }
            view.findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayout).apply {
                setOnRefreshListener {
                    webView?.reload()
                }
            }
            view
        },
        update = {
            val mCircularProgressContainer =
                it.findViewById<LinearLayout>(R.id.circularProgressContainer)
            val mWebView = it.findViewById<WebView>(R.id.webView)
            val mSwipeRefreshLayout = it.findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayout)

            if (progress == 100) {
                mSwipeRefreshLayout.isRefreshing = false
                mWebView.visibility = VISIBLE
                mCircularProgressContainer.visibility = INVISIBLE
            } else {
                mWebView.visibility = INVISIBLE
                if (!mSwipeRefreshLayout.isRefreshing) {
                    mCircularProgressContainer.visibility = VISIBLE
                }
            }

            canGoBack = mWebView.canGoBack()
            webView = mWebView
        }
    )

    if (canGoBack) {
        BackHandler {
            webView?.let {
                if (it.canGoBack()) it.goBack()
            }
        }
    }

    val context = LocalContext.current
    openLinkDialog?.let {
        OpenLinkDialog(
            onConfirm = {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
                context.startActivity(intent)
                openLinkDialog = null
            },
            onCancel = { openLinkDialog = null },
            link = it
        )
    }
}

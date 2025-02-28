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
import androidx.activity.compose.LocalActivity
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.spektasoft.starterkit.R
import com.spektasoft.starterkit.ui.components.browser.config.BrowserInterfaceConfig
import com.spektasoft.starterkit.ui.components.browser.config.BrowserWebChromeClientConfig
import com.spektasoft.starterkit.ui.components.browser.config.BrowserWebViewClientCompatConfig
import com.spektasoft.starterkit.util.setLanguage
import kotlinx.coroutines.launch

@SuppressLint("SetJavaScriptEnabled", "InflateParams")
@Composable
fun BrowserWebView(
    baseUrl: String,
    modifier: Modifier = Modifier,
    browserInterfaceConfig: BrowserInterfaceConfig = BrowserInterfaceConfig(),
    browserWebChromeClientConfig: BrowserWebChromeClientConfig = BrowserWebChromeClientConfig(),
    browserWebViewClientCompatConfig: BrowserWebViewClientCompatConfig = BrowserWebViewClientCompatConfig(),
) {
    if (LocalInspectionMode.current) return

    val activity = LocalActivity.current
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var webView: WebView? = null

    var bundle by rememberSaveable { mutableStateOf<Bundle?>(null) }
    var canGoBack by rememberSaveable { mutableStateOf(false) }
    var progress by rememberSaveable { mutableIntStateOf(0) }
    var openLinkDialog by remember { mutableStateOf<String?>(null) }

    val mBrowserInterfaceConfig = browserInterfaceConfig.copy(
        onSwitchLanguage = {
            browserInterfaceConfig.onSwitchLanguage(it)
            lifecycleOwner.lifecycleScope.launch {
                activity?.run {
                    if (setLanguage(it)) {
                        recreate()
                    }
                }
            }
        }
    )

    val mBrowserWebChromeClientConfig = browserWebChromeClientConfig.copy(
        progressChangedHandler = { p ->
            browserWebChromeClientConfig.progressChangedHandler(p)
            progress = p
        }
    )

    val mBrowserWebViewClientCompatConfig = browserWebViewClientCompatConfig.copy(
        shouldOverrideUrlLoadingHandler = { view, request ->
            val isOverridden =
                browserWebViewClientCompatConfig.shouldOverrideUrlLoadingHandler(view, request)

            if (isOverridden) {
                return@copy true
            }

            if (request.url.toString().startsWith(baseUrl)) {
                return@copy false
            }

            openLinkDialog = request.url.toString()

            true
        }
    )

    DisposableEffect(lifecycleOwner) {
        onDispose {
            bundle = Bundle().also { bundle ->
                webView?.saveState(bundle)
            }
        }
    }

    val colorPrimary = MaterialTheme.colorScheme.primary
    val colorSecondary = MaterialTheme.colorScheme.secondary
    val colorTertiary = MaterialTheme.colorScheme.tertiary
    AndroidView(
        modifier = modifier,
        factory = {
            val view = LayoutInflater.from(it).inflate(R.layout.browser_web_view, null)
            view.findViewById<CircularProgressIndicator>(R.id.circularProgressIndicator).apply {
                setIndicatorColor(colorPrimary.toArgb())
            }
            view.findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayout).apply {
                setColorSchemeColors(
                    colorPrimary.toArgb(),
                    colorSecondary.toArgb(),
                    colorTertiary.toArgb()
                )
                setOnRefreshListener {
                    webView?.reload()
                }
            }
            view.findViewById<WebView>(R.id.webView).apply {
                webChromeClient = BrowserWebChromeClient(mBrowserWebChromeClientConfig)
                webViewClient = BrowserWebViewClientCompat(mBrowserWebViewClientCompatConfig)
                with(this.settings) {
                    domStorageEnabled = true
                    javaScriptEnabled = true

                    setSupportZoom(false)
                }
                addJavascriptInterface(
                    BrowserInterface(mBrowserInterfaceConfig), "Android"
                )
                bundle?.let { b -> restoreState(b) } ?: this.loadUrl(baseUrl)
            }
            view
        },
        update = {
            val mCircularProgressContainer =
                it.findViewById<LinearLayout>(R.id.circularProgressContainer)
            val mWebView = it.findViewById<WebView>(R.id.webView)
            val mSwipeRefreshLayout = it.findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayout)

            if (progress <= 10 || progress == 100) {
                mWebView.visibility = VISIBLE
                mCircularProgressContainer.visibility = INVISIBLE
                if (progress == 100) {
                    mSwipeRefreshLayout.isRefreshing = false
                }
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

package com.spektasoft.starterkit.ui.components.browser.config

import android.webkit.WebResourceRequest
import android.webkit.WebView
import androidx.webkit.WebResourceErrorCompat

data class BrowserWebViewClientCompatConfig(
    val shouldOverrideUrlLoadingHandler: (
        WebView,
        WebResourceRequest
    ) -> Boolean = { _, _ -> false },
    val onReceivedErrorHandler: (
        view: WebView,
        request: WebResourceRequest,
        error: WebResourceErrorCompat
    ) -> Unit = { _, _, _ -> }
)

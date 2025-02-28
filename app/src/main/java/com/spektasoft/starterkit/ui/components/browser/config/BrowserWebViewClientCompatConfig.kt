package com.spektasoft.starterkit.ui.components.browser.config

import android.webkit.WebResourceRequest
import android.webkit.WebView

data class BrowserWebViewClientCompatConfig(
    val shouldOverrideUrlLoadingHandler: (
        WebView,
        WebResourceRequest
    ) -> Boolean = { _, _ -> false },
)

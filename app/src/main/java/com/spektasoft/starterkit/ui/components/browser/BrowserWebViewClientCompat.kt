package com.spektasoft.starterkit.ui.components.browser

import android.webkit.WebResourceRequest
import android.webkit.WebView
import androidx.webkit.WebViewClientCompat
import com.spektasoft.starterkit.ui.components.browser.config.BrowserWebViewClientCompatConfig

class BrowserWebViewClientCompat(private val config: BrowserWebViewClientCompatConfig) :
    WebViewClientCompat() {
    override fun shouldOverrideUrlLoading(
        view: WebView,
        request: WebResourceRequest
    ): Boolean {
        return config.shouldOverrideUrlLoadingHandler(view, request)
    }
}
package com.spektasoft.starterkit.ui.components.browser

import android.content.Context
import android.content.Intent
import android.webkit.WebResourceRequest
import android.webkit.WebView
import androidx.webkit.WebViewClientCompat

class BrowserWebViewClientCompat(private val baseUrl: String, private val context: Context): WebViewClientCompat() {
    override fun shouldOverrideUrlLoading(
        view: WebView,
        request: WebResourceRequest
    ): Boolean {
        if (request.url.toString().startsWith(baseUrl)) {
            return false
        }
        Intent(Intent.ACTION_VIEW, request.url).apply {
            context.startActivity(this, null)
        }
        return true
    }
}
package com.spektasoft.starterkit.ui.components.browser

import android.webkit.WebChromeClient
import android.webkit.WebView

class BrowserWebChromeClient(
    private val onUpdateProgress: (Int) -> Unit
): WebChromeClient() {
    override fun onProgressChanged(view: WebView?, newProgress: Int) {
        super.onProgressChanged(view, newProgress)
        onUpdateProgress(newProgress)
    }
}
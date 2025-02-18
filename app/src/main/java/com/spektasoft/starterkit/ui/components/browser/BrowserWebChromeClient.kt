package com.spektasoft.starterkit.ui.components.browser

import android.net.Uri
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import com.spektasoft.starterkit.ui.components.browser.config.BrowserWebChromeClientConfig

class BrowserWebChromeClient(private val config: BrowserWebChromeClientConfig) : WebChromeClient() {
    override fun onProgressChanged(view: WebView?, newProgress: Int) {
        super.onProgressChanged(view, newProgress)
        config.progressChangedHandler(newProgress)
    }

    override fun onShowFileChooser(
        webView: WebView?,
        filePathCallback: ValueCallback<Array<Uri>>?,
        fileChooserParams: FileChooserParams?
    ): Boolean {
        return config.showFileChooserHandler(webView, filePathCallback, fileChooserParams)
    }
}
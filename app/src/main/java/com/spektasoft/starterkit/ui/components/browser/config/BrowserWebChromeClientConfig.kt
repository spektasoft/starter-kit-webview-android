package com.spektasoft.starterkit.ui.components.browser.config

import android.net.Uri
import android.webkit.ValueCallback
import android.webkit.WebChromeClient.FileChooserParams
import android.webkit.WebView

data class BrowserWebChromeClientConfig(
    val progressChangedHandler: (Int) -> Unit = {},
    val showFileChooserHandler: (WebView?, ValueCallback<Array<Uri>>?, FileChooserParams?) -> Boolean = { _, _, _ -> false }
)

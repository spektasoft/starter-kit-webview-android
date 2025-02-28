package com.spektasoft.starterkit.ui.components.browser

import android.webkit.JavascriptInterface
import com.spektasoft.starterkit.ui.components.browser.config.BrowserInterfaceConfig

class BrowserInterface(private val config: BrowserInterfaceConfig) {
    @JavascriptInterface
    fun switchLanguage(language: String) {
        config.onSwitchLanguage(language)
    }
}
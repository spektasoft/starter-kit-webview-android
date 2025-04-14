package com.spektasoft.starterkit.ui.components.browser

import android.content.Context
import android.webkit.JavascriptInterface
import com.spektasoft.starterkit.ui.components.browser.config.BrowserInterfaceConfig

class BrowserInterface(private val config: BrowserInterfaceConfig, private val context: Context) {
    @JavascriptInterface
    fun getPackageName(): String? {
        return config.getPackageName(context)
    }

    @JavascriptInterface
    fun getVersionCode(): Long? {
        return config.getVersionCode(context)
    }

    @JavascriptInterface
    fun navigate() {
        config.onNavigate()
    }

    @JavascriptInterface
    fun navigated() {
        config.onNavigated()
    }

    @JavascriptInterface
    fun switchLanguage(language: String) {
        config.onSwitchLanguage(language)
    }
}
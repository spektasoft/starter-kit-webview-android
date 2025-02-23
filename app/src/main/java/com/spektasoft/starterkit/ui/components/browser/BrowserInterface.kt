package com.spektasoft.starterkit.ui.components.browser

import android.content.Context
import android.webkit.JavascriptInterface
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.spektasoft.starterkit.util.setLanguage
import kotlinx.coroutines.launch

class BrowserInterface(private val context: Context) {
    @JavascriptInterface
    fun switchLanguage(language: String) {
        val activity = context as? ComponentActivity ?: return

        activity.lifecycleScope.launch {
            activity.setLanguage(language)
            activity.recreate()
        }
    }
}
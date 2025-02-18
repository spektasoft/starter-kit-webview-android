package com.spektasoft.starterkit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.spektasoft.starterkit.ui.components.browser.Browser
import com.spektasoft.starterkit.ui.components.browser.config.BrowserWebChromeClientConfig
import com.spektasoft.starterkit.ui.theme.AppTheme
import com.spektasoft.starterkit.util.loadEnvProperties

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val env = loadEnvProperties(this)
        val appUrl = env.getProperty("APP_URL")

        val webChromeClientConfig = BrowserWebChromeClientConfig()

        setContent {
            AppTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .safeDrawingPadding(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Browser(appUrl, webChromeClientConfig)
                }
            }
        }
    }
}
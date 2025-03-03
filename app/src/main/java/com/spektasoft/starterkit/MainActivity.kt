package com.spektasoft.starterkit

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.spektasoft.starterkit.extensions.setLocale
import com.spektasoft.starterkit.ui.components.browser.Browser
import com.spektasoft.starterkit.ui.theme.AppTheme
import com.spektasoft.starterkit.util.getLanguage
import com.spektasoft.starterkit.util.loadEnvProperties
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {
    override fun attachBaseContext(newBase: Context) {
        val language = runBlocking {
            newBase.getLanguage()
        }
        super.attachBaseContext(newBase.setLocale(language))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AppTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .safeDrawingPadding(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val env = loadEnvProperties()
                    val appUrl = env.getProperty("APP_URL")
                    Browser(appUrl)
                }
            }
        }
    }
}
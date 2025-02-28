package com.spektasoft.starterkit.ui.components.browser

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.spektasoft.starterkit.extensions.withDefaultShowFileChooserHandler
import com.spektasoft.starterkit.ui.components.browser.config.BrowserInterfaceConfig
import com.spektasoft.starterkit.ui.components.browser.config.BrowserWebChromeClientConfig
import com.spektasoft.starterkit.ui.theme.AppTheme
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun Browser(baseUrl: String) {
    val lifecycleOwner = LocalLifecycleOwner.current

    var progress by rememberSaveable { mutableIntStateOf(0) }

    var isNavigating by remember { mutableStateOf(false) }
    var navigateJob by remember { mutableStateOf<Job?>(null) }
    val browserInterfaceConfig = BrowserInterfaceConfig(
        onNavigate = {
            isNavigating = true
            progress = 10
            navigateJob?.cancel()
            navigateJob = lifecycleOwner.lifecycleScope.launch {
                while (progress < 99) {
                    if (progress + 10 > 99) {
                        progress = 99
                    } else {
                        progress += 10
                    }
                    delay(1000)
                }
            }
        },
        onNavigated = {
            progress = 99
            navigateJob?.cancel()
            isNavigating = false
            progress = 100
        },
    )

    val browserWebChromeClientConfig = BrowserWebChromeClientConfig(
        progressChangedHandler = { p ->
            if (!isNavigating) {
                progress = p
            }
        }
    ).withDefaultShowFileChooserHandler()

    Box(
        Modifier
            .fillMaxSize()
    ) {
        BrowserWebView(
            baseUrl = baseUrl,
            browserInterfaceConfig = browserInterfaceConfig,
            browserWebChromeClientConfig = browserWebChromeClientConfig
        )
        AnimatedVisibility(
            modifier = Modifier.fillMaxWidth(), visible = progress in 0..99
        ) {
            LinearProgressIndicator(progress = { progress.toFloat() / 100 })
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun BrowserPreview() {
    AppTheme {
        Browser("")
    }
}
package com.spektasoft.starterkit.ui.components.browser

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun Browser(baseUrl: String) {
    var progress by rememberSaveable { mutableIntStateOf(0) }

    Box(
        Modifier
            .fillMaxSize()
    ) {
        BrowserWebView(
            baseUrl = baseUrl,
            onUpdateProgress = { progress = it }
        )
        AnimatedVisibility(
            modifier = Modifier.fillMaxWidth(), visible = progress in 1..99
        ) {
            LinearProgressIndicator(progress = { progress.toFloat() / 100 })
        }
    }
}
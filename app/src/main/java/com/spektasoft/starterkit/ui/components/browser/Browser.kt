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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.spektasoft.starterkit.ui.theme.AppTheme

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
//        AnimatedVisibility(
//            modifier = Modifier.fillMaxSize(), visible = progress in 0..99,
//        ) {
//            Column(
//                modifier = Modifier
//                    .fillMaxSize(),
//                verticalArrangement = Arrangement.Center,
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                CircularProgressIndicator()
//            }
//        }
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
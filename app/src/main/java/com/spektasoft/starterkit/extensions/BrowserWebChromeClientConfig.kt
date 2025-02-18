package com.spektasoft.starterkit.extensions

import android.net.Uri
import android.webkit.ValueCallback
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.spektasoft.starterkit.ui.components.browser.config.BrowserWebChromeClientConfig

@Composable
fun BrowserWebChromeClientConfig.withDefaultShowFileChooserHandler(): BrowserWebChromeClientConfig {
    var filePathsCallback by remember {
        mutableStateOf<ValueCallback<Array<Uri>>?>(null)
    }
    var fileChooserCallbackFunction by remember {
        mutableStateOf<((Array<Uri>?) -> Unit)?>(null)
    }
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.PickMultipleVisualMedia()) { result ->
            fileChooserCallbackFunction?.invoke(result.toTypedArray())
            fileChooserCallbackFunction = null
        }

    return copy(
        showFileChooserHandler = { _, webFilePathCallback, _ ->
            filePathsCallback?.onReceiveValue(null)
            filePathsCallback = webFilePathCallback

            try {
                fileChooserCallbackFunction = { uris ->
                    webFilePathCallback?.onReceiveValue(uris)
                    filePathsCallback = null
                }

                launcher.launch(
                    PickVisualMediaRequest(
                        mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
                    )
                )
                true
            } catch (e: Exception) {
                webFilePathCallback?.onReceiveValue(null)
                filePathsCallback = null
                false
            }
        }
    )
}
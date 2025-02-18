package com.spektasoft.starterkit.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import java.io.InputStream
import java.util.Properties

@Composable
fun loadEnvProperties(): Properties {
    val context = LocalContext.current
    val properties = Properties()
    val inputStream: InputStream = context.assets.open("env.properties")
    properties.load(inputStream)
    inputStream.close()
    return properties
}
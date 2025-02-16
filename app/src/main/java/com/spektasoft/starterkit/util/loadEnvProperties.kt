package com.spektasoft.starterkit.util

import android.content.Context
import java.io.InputStream
import java.util.Properties

fun loadEnvProperties(context: Context): Properties {
    val properties = Properties()
    val inputStream: InputStream = context.assets.open("env.properties")
    properties.load(inputStream)
    inputStream.close()
    return properties
}
package com.spektasoft.starterkit.util

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.spektasoft.starterkit.extensions.settingsDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val LANGUAGE = stringPreferencesKey("language")

suspend fun Context.getLanguage(): String {
    return settingsDataStore.data.map {
        it[LANGUAGE] ?: "en"
    }.first()
}

suspend fun Context.setLanguage(language: String) {
    settingsDataStore.edit {
        it[LANGUAGE] = language
    }
}
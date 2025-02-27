package com.spektasoft.starterkit.extensions

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.spektasoft.starterkit.util.setLanguage
import kotlinx.coroutines.runBlocking
import java.util.Locale

val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

fun Context.setLocale(language: String): Context? {
    runBlocking {
        setLanguage(language)
    }
    val resources = resources
    val configuration = resources.configuration
    val locale = Locale(language)
    Locale.setDefault(locale)
    configuration.setLocale(locale)
    return createConfigurationContext(configuration)
}
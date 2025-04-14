package com.spektasoft.starterkit.ui.components.browser.config

import android.content.Context

data class BrowserInterfaceConfig(
    val getPackageName: (Context) -> String? = { _ -> null },
    val getVersionCode: (Context) -> Long? = { _ -> null },
    val onNavigate: () -> Unit = {},
    val onNavigated: () -> Unit = {},
    val onSwitchLanguage: (String) -> Unit = {}
)

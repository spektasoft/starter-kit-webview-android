package com.spektasoft.starterkit.ui.components.browser.config

data class BrowserInterfaceConfig(
    val onNavigate: () -> Unit = {},
    val onNavigated: () -> Unit = {},
    val onSwitchLanguage: (String) -> Unit = {}
)

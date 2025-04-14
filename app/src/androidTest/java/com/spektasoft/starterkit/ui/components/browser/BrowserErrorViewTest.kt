package com.spektasoft.starterkit.ui.components.browser

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.platform.app.InstrumentationRegistry
import com.spektasoft.starterkit.R
import org.junit.Rule
import org.junit.Test

class BrowserErrorViewTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun displaysErrorMessageAndRetryButton() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val resources = context.resources

        composeTestRule.setContent {
            BrowserErrorView(onRetry = {})
        }

        composeTestRule.onNodeWithText(resources.getString(R.string.error_message)).assertExists()
        composeTestRule.onNodeWithText(resources.getString(R.string.retry)).assertExists()
        composeTestRule.onNodeWithContentDescription(resources.getString(R.string.error_icon_description))
            .assertExists()
    }

    @Test
    fun retryButtonCallsOnRetryCallback() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val resources = context.resources

        var retryClicked = false

        composeTestRule.setContent {
            BrowserErrorView(onRetry = { retryClicked = true })
        }

        composeTestRule.onNodeWithText(resources.getString(R.string.retry)).performClick()
        assert(retryClicked) { "Retry button click should trigger onRetry callback" }
    }
}
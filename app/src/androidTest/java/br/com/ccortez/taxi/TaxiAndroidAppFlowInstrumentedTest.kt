package br.com.ccortez.taxi

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextReplacement
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Mirrors the smoke scenarios described in ANDROID_INSTRUMENTED_TESTS_SPEC.md
 * (`native-appium-demo`, package `AndroidAppTests`).
 *
 * Compose node tags match `setTagAndId` in the feature modules.
 */
@RunWith(AndroidJUnit4::class)
class TaxiAndroidAppFlowInstrumentedTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun travelRequestScreenShowsTitleLiterallyTravelRequest() {
        waitUntilNodeDisplayedWithTag(TAG_SCREEN_TITLE_REQUEST)

        composeTestRule
            .onNodeWithTag(TAG_SCREEN_TITLE_REQUEST, useUnmergedTree = true)
            .assertIsDisplayed()
            .assertTextEquals(TITLE_TRAVEL_REQUEST)
    }

    @Test
    fun fillTravelRequest_submit_navigatesToAvailableRidersWithExpectedTitle() {
        waitUntilNodeDisplayedWithTag(TAG_SCREEN_TITLE_REQUEST)

        composeTestRule.onNodeWithTag(TAG_FIELD_USER_ID, useUnmergedTree = true).performTextReplacement(FLOW_USER_ID)
        composeTestRule.onNodeWithTag(TAG_FIELD_ORIGIN, useUnmergedTree = true).performTextReplacement(FLOW_ORIGIN)
        composeTestRule.onNodeWithTag(TAG_FIELD_DESTINY, useUnmergedTree = true).performTextReplacement(FLOW_DESTINY)

        composeTestRule.onNodeWithTag(TAG_SUBMIT_BUTTON, useUnmergedTree = true).performClick()

        waitUntilNodeDisplayedWithTag(TAG_SCREEN_TITLE_AVAILABLE)
        composeTestRule
            .onNodeWithTag(TAG_SCREEN_TITLE_AVAILABLE, useUnmergedTree = true)
            .assertIsDisplayed()
            .assertTextEquals(TITLE_AVAILABLE_RIDERS)
    }

    /** Same intent as Compose `visibilityOfElementLocated`-style waits in the Appium mirrors. */
    private fun waitUntilNodeDisplayedWithTag(testTag: String, timeoutMillis: Long = TITLE_NAVIGATION_TIMEOUT_MS) {
        val deadline = System.nanoTime() + timeoutMillis * 1_000_000L
        while (System.nanoTime() < deadline) {
            composeTestRule.waitForIdle()
            try {
                composeTestRule.onNodeWithTag(testTag, useUnmergedTree = true).assertIsDisplayed()
                return
            } catch (_: AssertionError) {
                Thread.sleep(50)
            }
        }
        composeTestRule.onNodeWithTag(testTag, useUnmergedTree = true).assertIsDisplayed()
    }

    private companion object {
        const val TITLE_TRAVEL_REQUEST = "Travel Request"
        const val TITLE_AVAILABLE_RIDERS = "Available Riders"

        const val TAG_SCREEN_TITLE_REQUEST = "travelRequestTitle"
        const val TAG_SCREEN_TITLE_AVAILABLE = "availableRidersTitle"
        const val TAG_SUBMIT_BUTTON = "requestTravelButton"

        const val TAG_FIELD_USER_ID = "Id do usuário"
        const val TAG_FIELD_ORIGIN = "Endereço de origem"
        const val TAG_FIELD_DESTINY = "Endereço de destino"

        const val FLOW_USER_ID = "12345"
        const val FLOW_ORIGIN =
            "Av. Brasil, 2033 - Jardim America, São Paulo - SP, 01431-001"
        const val FLOW_DESTINY =
            "Av. Paulista, 1538 - Bela Vista, São Paulo - SP, 01310-200"

        const val TITLE_NAVIGATION_TIMEOUT_MS = 15_000L
    }
}

package hsmida.exam.fdj

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsFocused
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsNotFocused
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import hsmida.exam.fdj.compose.components.SearchBar
import org.junit.Rule
import org.junit.Test

class SearchBarTest {
    
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun searchBar_verifyInitialState() {
        composeTestRule.setContent { 
            SearchBar(title = "")
        }
        composeTestRule
            .onNodeWithText("Search leagues")
            .assertIsDisplayed()
            .assertIsNotEnabled()
            .assertIsNotFocused()
    }

    @Test
    fun searchBar_verifyTitle() {
        composeTestRule.setContent {
            SearchBar(title = "Test")
        }
        composeTestRule
            .onNodeWithText("Test")
            .assertIsDisplayed()
    }

    @Test
    fun searchBar_verifyCanEdit() {
        composeTestRule.setContent {
            SearchBar(title = "", canEdit = true)
        }
        composeTestRule
            .onNodeWithText("Search leagues")
            .assertIsEnabled()

        composeTestRule
            .onNodeWithText("Close")
            .assertIsDisplayed()
    }

}
package com.csu_itc303_team1.adhdtaskmanager

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FirstTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    privatelateinit var rewardViewModel: RewardViewModel

    @Test
    fun checkScreenShows() {
        composeTestRule.setContent {

            RewardScreen(rewardViewModel = )
        }
    }

}
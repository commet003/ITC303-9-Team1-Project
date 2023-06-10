package com.csu_itc303_team1.adhdtaskmanager

import org.junit.Assert.*
import org.junit.Before

class RewardViewModelTest {

    private lateinit var rewardViewModel: RewardViewModel

    @Before
    fun setUp() {
        rewardViewModel = RewardViewModel(RewardRepo())
    }

}
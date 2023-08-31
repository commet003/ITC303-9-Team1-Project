package com.csu_itc303_team1.adhdtaskmanager.ui.navigation

import com.csu_itc303_team1.adhdtaskmanager.R


sealed class Screen(var route: String, var icon: Int, val title: String) {
    object TodoScreen : Screen("todo_screen", R.drawable.ic_home, "Home")
    object TodoDetailScreen : Screen("todo_detail_screen", R.drawable.ic_todo_detail, "Todo Detail")

    object AddTodoScreen : Screen("add_todo_screen", R.drawable.ic_add_todo, "Add Todo")
    object EditTodoScreen : Screen("edit_todo_screen", R.drawable.ic_edit_todo, "Edit Todo")
    object SettingsScreen : Screen("settings_screen", R.drawable.ic_settings, "Settings")
    object LeaderboardScreen : Screen("leaderboard_screen", R.drawable.ic_leaderboard, "Leaderboard")

    object RewardsScreen : Screen("reward_screen", R.drawable.ic_rewards, "Rewards")
    object SignInScreen : Screen("sign_in_screen", R.drawable.ic_sign_in, "Sign In")
    object HelpScreen : Screen("help_screen", R.drawable.ic_help, "Help")
    object PomodoroTimerScreen : Screen("pomodoro_timer_screen", R.drawable.ic_pomodoro_timer, "Pomodoro Timer")

    companion object {
        val items = listOf(
            TodoScreen,
            TodoDetailScreen,
            AddTodoScreen,
            EditTodoScreen,
            SettingsScreen,
            LeaderboardScreen,
            RewardsScreen,
            SignInScreen,
            HelpScreen,
            PomodoroTimerScreen
        )
    }
}

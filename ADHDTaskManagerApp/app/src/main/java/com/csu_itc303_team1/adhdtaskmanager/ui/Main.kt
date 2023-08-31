package com.csu_itc303_team1.adhdtaskmanager.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.csu_itc303_team1.adhdtaskmanager.ui.theme.ADHDTaskManagerTheme
import com.csu_itc303_team1.adhdtaskmanager.ui.todo_detail.TodoDetailScreen
import com.csu_itc303_team1.adhdtaskmanager.ui.todo_detail.TodoDetailViewModel
import com.csu_itc303_team1.adhdtaskmanager.ui.todo_screen.TodosScreen

@Composable
@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
fun Main() {
    ADHDTaskManagerTheme {
        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = "todos",
        ) {
            composable("todos") {
                TodosScreen(
                    hiltViewModel(),
                    onTodoClick = { todoId ->
                        navController.navigate("detail/$todoId")
                    },
                    onAddTodoClick = {
                        // TODO
                    },
                    onArchiveClick = {
                        // TODO
                    },
                    onSettingsClick = {
                        // TODO
                    }
                )
            }
            composable(
                route = "detail/{todoId}",
                arguments = listOf(navArgument("todoId") { type = NavType.LongType })
            ) { backStackEntry ->
                TodoDetailScreen(
                    hiltViewModel<TodoDetailViewModel>().also {
                        it.getTodoDetails()
                        it.setTodoId(backStackEntry.arguments?.getLong("todoId") ?: 0L)
                    },
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onEditClick = {
                        navController.navigate("edit/")
                    },
                    onArchiveClick = {
                        // TODO
                    },
                    onSettingsClick = {
                        // TODO
                    }
                )
            }
        }
    }
}

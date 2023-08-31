package com.csu_itc303_team1.adhdtaskmanager

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.csu_itc303_team1.adhdtaskmanager.TodoDestinationsArgs.TITLE_ARG
import com.csu_itc303_team1.adhdtaskmanager.TodoDestinationsArgs.TODO_ID_ARG
import com.csu_itc303_team1.adhdtaskmanager.TodoDestinationsArgs.USER_MESSAGE_ARG
import com.csu_itc303_team1.adhdtaskmanager.TodoScreens.ADD_EDIT_TODO_SCREEN
import com.csu_itc303_team1.adhdtaskmanager.TodoScreens.TODOS_SCREEN
import com.csu_itc303_team1.adhdtaskmanager.TodoScreens.TODO_DETAIL_SCREEN

/**
 * Screens used in [TodoDestinations]
 */
private object TodoScreens {
    const val TODOS_SCREEN = "todos"
    const val TODO_DETAIL_SCREEN = "todo"
    const val ADD_EDIT_TODO_SCREEN = "addEditTodo"
}

/**
 * Arguments used in [TodoDestinations] routes
 */
object TodoDestinationsArgs {
    const val USER_MESSAGE_ARG = "userMessage"
    const val TODO_ID_ARG = "todoId"
    const val TITLE_ARG = "title"
}


object TodoDestinations {
    const val TODOS_ROUTE = "$TODOS_SCREEN?$USER_MESSAGE_ARG={$USER_MESSAGE_ARG}"
    const val TODO_DETAIL_ROUTE = "$TODO_DETAIL_SCREEN/{$TODO_ID_ARG}"
    const val ADD_EDIT_TODO_ROUTE = "$ADD_EDIT_TODO_SCREEN/{$TITLE_ARG}?$TODO_ID_ARG={$TODO_ID_ARG}"
}

/**
 * Models the navigation actions in the app.
 */
class TodoNavigationActions(private val navController: NavHostController) {

    fun navigateToTodos(userMessage: Int = 0) {
        val navigatesFromDrawer = userMessage == 0
        navController.navigate(
            TODOS_SCREEN.let {
                if (userMessage != 0) "$it?$USER_MESSAGE_ARG=$userMessage" else it
            }
        ) {
            popUpTo(navController.graph.findStartDestination().id) {
                inclusive = !navigatesFromDrawer
                saveState = navigatesFromDrawer
            }
            launchSingleTop = true
            restoreState = navigatesFromDrawer
        }
    }

    fun navigateToTodoDetail(todoId: String) {
        navController.navigate("$TODO_DETAIL_SCREEN/$todoId")
    }

    fun navigateToAddEditTodo(title: Int, todoId: String?) {
        navController.navigate(
            "$ADD_EDIT_TODO_SCREEN/$title".let {
                if (todoId != null) "$it?$TODO_ID_ARG=$todoId" else it
            }
        )
    }
}
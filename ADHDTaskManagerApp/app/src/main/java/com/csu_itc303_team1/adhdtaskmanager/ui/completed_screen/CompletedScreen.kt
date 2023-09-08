package com.csu_itc303_team1.adhdtaskmanager.ui.completed_screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.csu_itc303_team1.adhdtaskmanager.ui.todo_screen.TodoViewModel
import com.csu_itc303_team1.adhdtaskmanager.ui.ui_components.CompletedTaskCard
import com.csu_itc303_team1.adhdtaskmanager.utils.todo_utils.SortOrder


@Composable
fun CompletedScreen(todoViewModel: TodoViewModel) {
    val todos = todoViewModel.todos.collectAsStateWithLifecycle(emptyList())


    LazyColumn(
        modifier = Modifier,
        contentPadding = PaddingValues(0.dp, 20.dp, 0.dp)
    ) {

        todoViewModel.filterSortTodos(
            todos = todos.value,
            showCompleted = true,
            showUncompleted = false,
            sortOrder = SortOrder.BY_DEADLINE
        ).forEach { todo ->
            item {
                CompletedTaskCard(todo = todo)
            }
        }
    }
}
package com.csu_itc303_team1.adhdtaskmanager.ui.completed_screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.csu_itc303_team1.adhdtaskmanager.ui.ui_components.CompletedTaskCard
import com.csu_itc303_team1.adhdtaskmanager.utils.states.TodoState


@Composable
fun CompletedScreen(state: TodoState) {

    LazyColumn(
        modifier = Modifier,
        contentPadding = PaddingValues(0.dp, 20.dp, 0.dp)
    ) {
        items(state.todos, {it.id}) { todo ->
            if (todo.userId == state.userId && todo.isCompleted){
                CompletedTaskCard(todo)
            }
        }
    }
}
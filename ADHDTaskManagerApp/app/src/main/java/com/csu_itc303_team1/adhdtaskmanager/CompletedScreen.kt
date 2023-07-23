package com.csu_itc303_team1.adhdtaskmanager

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun CompletedScreen(state: TodoState, event: (TodoEvent) -> Unit) {

    event(TodoEvent.sortBy(SortType.BY_COMPLETED))

    LazyColumn(
        modifier = Modifier,
        contentPadding = PaddingValues(0.dp, 20.dp, 0.dp)
    ) {
        items(state.todos, {it.id}) { todo ->
            println(todo.title)
            CompletedTaskCard(todo)
        }
    }
}
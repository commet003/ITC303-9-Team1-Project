package com.csu_itc303_team1.adhdtaskmanager

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.csu_itc303_team1.adhdtaskmanager.database.local.Todo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
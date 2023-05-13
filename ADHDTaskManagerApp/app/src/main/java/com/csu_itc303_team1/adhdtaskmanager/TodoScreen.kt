package com.csu_itc303_team1.adhdtaskmanager


import androidx.compose.animation.expandHorizontally
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.unit.dp

@Composable
fun TodoScreen(
    state: TodoState,
    onEvent: (TodoEvent) -> Unit,
) {

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {

                onEvent(TodoEvent.showDialog)
            },  Modifier.padding(0.dp,0.dp,43.dp,0.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Todo"
                )
            }
        }
    ) { paddingValues ->

        if (state.showDialog){
            AddTodoDialog(
                state = state,
                onEvent = onEvent,
            )

        }

        LazyColumn(
            contentPadding = paddingValues,
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ){
            items(state.todos){ todo ->
                TodoCard(todo = todo, onEvent = onEvent)
            }
        }
    }
}
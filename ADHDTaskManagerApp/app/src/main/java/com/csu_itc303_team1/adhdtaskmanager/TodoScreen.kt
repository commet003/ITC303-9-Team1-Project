package com.csu_itc303_team1.adhdtaskmanager


import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.csu_itc303_team1.adhdtaskmanager.pomodoro.MainTimer

@Composable
fun TodoScreen(
    state: TodoState,
    onEvent: (TodoEvent) -> Unit,
) {
    val context = LocalContext.current
    Scaffold(
        floatingActionButton = {
            Column {
                FloatingActionButton(onClick = {
                    onEvent(TodoEvent.showDialog)
                }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Todo"
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                FloatingActionButton(onClick = {
                    context.startActivity(Intent(context, MainTimer::class.java))
                }) {
                    Icon(
                        imageVector = Icons.Default.Face,
                        contentDescription = "Go To Timer"
                    )
                }
            }
        },

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
                TodoCard(
                    todo = todo,
                    onEvent = onEvent
                ) {
                    Log.e("onclickCard", "OnclickCard")
                    val sharedPreferences =
                        context.getSharedPreferences("timer_shared", Context.MODE_PRIVATE)
                    val editor = sharedPreferences?.edit()
                    editor?.putString("task", todo.title)
                    editor?.apply()

                    context.startActivity(Intent(context, MainTimer::class.java))
                }
            }
        }
    }
}
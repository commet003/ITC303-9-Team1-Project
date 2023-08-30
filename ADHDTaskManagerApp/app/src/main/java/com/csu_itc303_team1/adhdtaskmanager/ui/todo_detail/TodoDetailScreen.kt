package com.csu_itc303_team1.adhdtaskmanager.ui.todo_detail

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun TodoDetailScreen(
    viewModel: TodoDetailViewModel
) {
    val padding = WindowInsets.systemBars.asPaddingValues()
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
    ) {
        Text(text = "todo ${viewModel.todoId}")
    }
}
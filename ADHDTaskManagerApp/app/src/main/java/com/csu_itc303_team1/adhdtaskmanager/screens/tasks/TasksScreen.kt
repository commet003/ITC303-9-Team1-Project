package com.csu_itc303_team1.adhdtaskmanager.screens.tasks

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.csu_itc303_team1.adhdtaskmanager.common.composable.ActionToolbar
import com.csu_itc303_team1.adhdtaskmanager.common.ext.smallSpacer
import com.csu_itc303_team1.adhdtaskmanager.common.ext.toolbarActions
import com.csu_itc303_team1.adhdtaskmanager.R.string as AppText

@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
@ExperimentalMaterialApi
fun TasksScreen(
    openScreen: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TasksViewModel = hiltViewModel()
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onAddClick(openScreen) },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = CircleShape,
                modifier = modifier.padding(16.dp)
            ) {
                Icon(Icons.Filled.Add, "Add")
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) {
        val tasks = viewModel.tasks.collectAsStateWithLifecycle(emptyList())

        Column(modifier = Modifier.fillMaxWidth().fillMaxHeight()) {
            ActionToolbar(
                title = AppText.tasks,
                modifier = Modifier.toolbarActions(),
                onNavClick = {

                }
            )

            Spacer(modifier = Modifier.smallSpacer())

            LazyColumn {
                items(tasks.value, key = { it.id }) { taskItem ->
                    TaskItem(
                        task = taskItem,
                        onActionClick = { action -> viewModel.onTaskActionClick(openScreen, taskItem, action) }
                    )
                }
            }
        }
    }

    LaunchedEffect(viewModel) { viewModel.loadTaskOptions() }
}
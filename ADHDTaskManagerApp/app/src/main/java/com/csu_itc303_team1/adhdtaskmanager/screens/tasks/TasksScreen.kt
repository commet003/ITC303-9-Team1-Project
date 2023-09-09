package com.csu_itc303_team1.adhdtaskmanager.screens.tasks

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.csu_itc303_team1.adhdtaskmanager.AppState
import com.csu_itc303_team1.adhdtaskmanager.common.composable.NavToolbar
import com.csu_itc303_team1.adhdtaskmanager.common.ext.smallSpacer
import com.csu_itc303_team1.adhdtaskmanager.common.ext.toolbarActions
import kotlinx.coroutines.launch
import com.csu_itc303_team1.adhdtaskmanager.R.drawable as AppIcon
import com.csu_itc303_team1.adhdtaskmanager.R.string as AppText

@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
@ExperimentalMaterialApi
fun TasksScreen(
    openScreen: (String) -> Unit,
    appState: AppState,
    modifier: Modifier = Modifier,
    viewModel: TasksViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    var menuClicked by remember{ mutableStateOf(false)}

    LaunchedEffect(key1 = menuClicked){
        if (appState.getDrawerState().isClosed) scope.launch { appState.openDrawer() }
        else scope.launch { appState.closeDrawer() }
        Log.d("TasksScreen", "clicked: ${appState.getDrawerState().currentValue}")
    }

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
        backgroundColor = MaterialTheme.colorScheme.background,
        topBar = {
            NavToolbar(
                title = AppText.tasks,
                navActionIcon = AppIcon.ic_nav_menu,
                modifier = Modifier.toolbarActions(),
                navAction = {
                    menuClicked = !menuClicked
                }
            )
        },
    ) {
        val tasks = viewModel.tasks.collectAsStateWithLifecycle(emptyList())

        Column(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()) {

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
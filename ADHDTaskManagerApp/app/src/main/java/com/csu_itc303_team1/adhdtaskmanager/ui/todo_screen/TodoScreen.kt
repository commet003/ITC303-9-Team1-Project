package com.csu_itc303_team1.adhdtaskmanager.ui.todo_screen


//noinspection UsingMaterialAndMaterial3Libraries
import android.annotation.SuppressLint
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuItemColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.csu_itc303_team1.adhdtaskmanager.ui.dialogs.AddEditTodoDialog
import com.csu_itc303_team1.adhdtaskmanager.ui.ui_components.CustomToastMessage
import com.csu_itc303_team1.adhdtaskmanager.ui.ui_components.TodoItem
import com.csu_itc303_team1.adhdtaskmanager.ui.ui_components.lottieLoaderAnimation
import com.csu_itc303_team1.adhdtaskmanager.utils.firestore_utils.FirestoreViewModel
import com.csu_itc303_team1.adhdtaskmanager.utils.states.TodoState
import com.csu_itc303_team1.adhdtaskmanager.utils.todo_utils.SortOrder
import com.csu_itc303_team1.adhdtaskmanager.utils.todo_utils.TodoEvent
import kotlinx.coroutines.launch




@RequiresApi(34)
@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun TodoScreen(
    state: TodoState,
    onEvent: (TodoEvent) -> Unit,
    todoViewModel: TodoViewModel,
    firestoreViewModel: FirestoreViewModel
) {


    val sheetState = rememberStandardBottomSheetState(
        initialValue = SheetValue.Hidden,
        skipHiddenState = false
    )
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = sheetState
    )
    val scope = rememberCoroutineScope()

    val showToast = remember { mutableStateOf(false) }
    var sortOrder = remember {SortOrder.NONE}
    val todos = todoViewModel.todos.collectAsStateWithLifecycle(emptyList())





    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        sheetSwipeEnabled = false,
        sheetDragHandle = {},
        content = {
            Scaffold(
                floatingActionButton = {
                    FloatingActionButton(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        onClick = {
                            onEvent(TodoEvent.showDialog)
                            scope.launch {
                                sheetState.expand()
                            }
                        },
                        shape = CircleShape
                    ) {
                        Icon(
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            imageVector = Icons.Default.Add,
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.primaryContainer)
                                .height(32.dp)
                                .width(32.dp),
                            contentDescription = "Add Todo"
                        )
                    }
                }
            ) { paddingValues ->
                var expanded by remember { mutableStateOf(false) }

                if (state.showEditTodoDialog){
                    scope.launch {
                        sheetState.expand()
                    }
                }

                Column (
                    verticalArrangement = Arrangement.SpaceAround
                ){
                    // TODO: This is where the task are filtered
                    // If you only want the completed task to show, then you can set
                    // sortType to SortType.BY_COMPLETED
                    ExposedDropdownMenuBox(
                        modifier = Modifier
                            .align(Alignment.End)
                            .padding(end = 10.dp),
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded}
                    )
                    {
                        IconButton(
                            modifier = Modifier
                                .menuAnchor()
                                .align(Alignment.CenterHorizontally)
                            ,
                            onClick = { }) {
                            Icon(
                                tint = MaterialTheme.colorScheme.onBackground,
                                imageVector = Icons.Filled.List,
                                contentDescription = "Filter"
                            )
                        }

                        ExposedDropdownMenu(
                            modifier = Modifier
                                .width(150.dp)
                                .background(MaterialTheme.colorScheme.surface),
                            expanded = expanded,
                            onDismissRequest = { expanded = false }) {
                                DropdownMenuItem(
                                    modifier = Modifier.clip(MaterialTheme.shapes.medium),
                                    colors = MenuItemColors(
                                            textColor = MaterialTheme.colorScheme.onSurface,
                                            leadingIconColor = MaterialTheme.colorScheme.onSurface,
                                            trailingIconColor = MaterialTheme.colorScheme.onSurface,
                                            disabledTextColor = MaterialTheme.colorScheme.surfaceVariant,
                                            disabledLeadingIconColor = MaterialTheme.colorScheme.surfaceVariant,
                                            disabledTrailingIconColor = MaterialTheme.colorScheme.surfaceVariant,
                                        ),
                                    enabled = sortOrder != SortOrder.NONE,
                                    onClick = {
                                        expanded = false
                                        sortOrder = SortOrder.NONE
                                    },
                                    text = { Text("None") }
                                )
                                DropdownMenuItem(
                                    modifier = Modifier.clip(MaterialTheme.shapes.medium),
                                    colors = MenuItemColors(
                                        textColor = MaterialTheme.colorScheme.onSurface,
                                        leadingIconColor = MaterialTheme.colorScheme.onSurface,
                                        trailingIconColor = MaterialTheme.colorScheme.onSurface,
                                        disabledTextColor = MaterialTheme.colorScheme.surfaceVariant,
                                        disabledLeadingIconColor = MaterialTheme.colorScheme.surfaceVariant,
                                        disabledTrailingIconColor = MaterialTheme.colorScheme.surfaceVariant,
                                    ),
                                    enabled = sortOrder != SortOrder.BY_DEADLINE,
                                    onClick = {
                                        expanded = false
                                        sortOrder = SortOrder.BY_DEADLINE
                                    },
                                    text = { Text("Deadline") }
                                )
                                DropdownMenuItem(
                                    modifier = Modifier.clip(MaterialTheme.shapes.medium),
                                    colors = MenuItemColors(
                                        textColor = MaterialTheme.colorScheme.onSurface,
                                        leadingIconColor = MaterialTheme.colorScheme.onSurface,
                                        trailingIconColor = MaterialTheme.colorScheme.onSurface,
                                        disabledTextColor = MaterialTheme.colorScheme.surfaceVariant,
                                        disabledLeadingIconColor = MaterialTheme.colorScheme.surfaceVariant,
                                        disabledTrailingIconColor = MaterialTheme.colorScheme.surfaceVariant,
                                    ),
                                    enabled = sortOrder != SortOrder.BY_PRIORITY,
                                    onClick = {
                                        expanded = false
                                        sortOrder = SortOrder.BY_PRIORITY
                                    },
                                    text = { Text("Priority") }
                                )
                                DropdownMenuItem(
                                    modifier = Modifier.clip(MaterialTheme.shapes.medium),
                                    colors = MenuItemColors(
                                        textColor = MaterialTheme.colorScheme.onSurface,
                                        leadingIconColor = MaterialTheme.colorScheme.onSurface,
                                        trailingIconColor = MaterialTheme.colorScheme.onSurface,
                                        disabledTextColor = MaterialTheme.colorScheme.surfaceVariant,
                                        disabledLeadingIconColor = MaterialTheme.colorScheme.surfaceVariant,
                                        disabledTrailingIconColor = MaterialTheme.colorScheme.surfaceVariant,
                                    ),
                                    enabled = sortOrder != SortOrder.BY_DEADLINE_AND_PRIORITY,
                                    onClick = {
                                        expanded = false
                                        sortOrder = SortOrder.BY_DEADLINE_AND_PRIORITY
                                    },
                                    text = { Text("Deadline and Priority") }
                                )
                                DropdownMenuItem(
                                    modifier = Modifier.clip(MaterialTheme.shapes.medium),
                                    colors = MenuItemColors(
                                        textColor = MaterialTheme.colorScheme.onSurface,
                                        leadingIconColor = MaterialTheme.colorScheme.onSurface,
                                        trailingIconColor = MaterialTheme.colorScheme.onSurface,
                                        disabledTextColor = MaterialTheme.colorScheme.surfaceVariant,
                                        disabledLeadingIconColor = MaterialTheme.colorScheme.surfaceVariant,
                                        disabledTrailingIconColor = MaterialTheme.colorScheme.surfaceVariant,
                                    ),
                                    enabled = sortOrder != SortOrder.BY_CATEGORY,
                                    onClick = {
                                        expanded = false
                                        sortOrder = SortOrder.BY_CATEGORY
                                    },
                                    text = { Text("Category") }
                                )
                                DropdownMenuItem(
                                    modifier = Modifier.clip(MaterialTheme.shapes.medium),
                                    colors = MenuItemColors(
                                        textColor = MaterialTheme.colorScheme.onSurface,
                                        leadingIconColor = MaterialTheme.colorScheme.onSurface,
                                        trailingIconColor = MaterialTheme.colorScheme.onSurface,
                                        disabledTextColor = MaterialTheme.colorScheme.surfaceVariant,
                                        disabledLeadingIconColor = MaterialTheme.colorScheme.surfaceVariant,
                                        disabledTrailingIconColor = MaterialTheme.colorScheme.surfaceVariant,
                                    ),
                                    enabled = sortOrder != SortOrder.BY_DEADLINE_AND_CATEGORY,
                                    onClick = {
                                        expanded = false
                                        sortOrder = SortOrder.BY_DEADLINE_AND_CATEGORY
                                    },
                                    text = { Text("Deadline and Category") }
                                )
                            }
                    }


                    LazyColumn(
                        contentPadding = paddingValues,
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(1.dp)
                    ) {

                        todoViewModel.filterSortTodos(
                            todos = todos.value,
                            showCompleted = false,
                            sortOrder = sortOrder
                        ).forEach {
                            item {
                                TodoItem(
                                    todo = it,
                                    showToast = showToast,
                                    onEvent = onEvent,
                                    firestoreViewModel = firestoreViewModel,
                                )
                            }
                        }
                    }


                }


                lottieLoaderAnimation(isVisible = showToast.value)

                CustomToastMessage(
                    message = "Congrats on completing a task!",
                    isVisible = showToast.value,
                )




            }
        },
        sheetPeekHeight = 0.dp,
        sheetContent = {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                if (state.showDialog || state.showEditTodoDialog) {
                    AddEditTodoDialog(state = state, onEvent = onEvent, scope = scope, sheetState = sheetState)
                }
            }
        }
    )

}







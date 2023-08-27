package com.csu_itc303_team1.adhdtaskmanager.ui.todo_screen


//noinspection UsingMaterialAndMaterial3Libraries
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.csu_itc303_team1.adhdtaskmanager.ui.dialogs.AddEditTodoDialog
import com.csu_itc303_team1.adhdtaskmanager.ui.reward_screen.RewardViewModel
import com.csu_itc303_team1.adhdtaskmanager.ui.ui_components.CustomToastMessage
import com.csu_itc303_team1.adhdtaskmanager.ui.ui_components.TodoCard
<<<<<<< HEAD
import com.csu_itc303_team1.adhdtaskmanager.utils.firebase.UserData
import com.csu_itc303_team1.adhdtaskmanager.utils.firestore_utils.FirestoreViewModel
=======
import com.csu_itc303_team1.adhdtaskmanager.ui.ui_components.lottieLoaderAnimation
import com.csu_itc303_team1.adhdtaskmanager.utils.firestore_utils.UsersViewModel
>>>>>>> parent of 25e01d5 (Simplified Firestore and Rewards systems)
import com.csu_itc303_team1.adhdtaskmanager.utils.states.TodoState
import com.csu_itc303_team1.adhdtaskmanager.utils.todo_utils.SortType
import com.csu_itc303_team1.adhdtaskmanager.utils.todo_utils.TodoEvent
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.IntSize
import androidx.core.view.drawToBitmap
import com.csu_itc303_team1.adhdtaskmanager.utils.blurBitmap
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.draw
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas




@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun TodoScreen(
    state: TodoState,
<<<<<<< HEAD
    firestoreViewModel: FirestoreViewModel,
    currentUser: UserData,
    onEvent: (TodoEvent) -> Unit
=======
    onEvent: (TodoEvent) -> Unit,
    rewardViewModel: RewardViewModel,
    usersViewModel: UsersViewModel
>>>>>>> parent of 25e01d5 (Simplified Firestore and Rewards systems)
) {

    rewardViewModel.allRewards.observeAsState(listOf())

    val sheetState = rememberStandardBottomSheetState(
        initialValue = SheetValue.Hidden,
        skipHiddenState = false
    )
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = sheetState
    )
    val scope = rememberCoroutineScope()

    val showToast = remember { mutableStateOf(false) }



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
                        }
                    ) {
                        Icon(
                            tint = MaterialTheme.colorScheme.onPrimary,
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
                            SortType.values().forEach { sortType ->
                                DropdownMenuItem(
                                    modifier = Modifier.clip(MaterialTheme.shapes.medium),
                                    onClick = {
                                        expanded = false
                                        onEvent(TodoEvent.sortBy(sortType))
                                    },
                                    text = {
                                        when (sortType.name) {
                                            "BY_PRIORITY" -> {
                                                Text(
                                                    text = "By Priority",
                                                    color = MaterialTheme.colorScheme.onSurface
                                                )
                                            }
                                            "BY_DATE_TIME" -> {
                                                Text(
                                                    text = "By Date",
                                                    color = MaterialTheme.colorScheme.onSurface
                                                )
                                            }
                                            "BY_COMPLETED" -> {
                                                Text(
                                                    text = "By Completed",
                                                    color = MaterialTheme.colorScheme.onSurface
                                                )
                                            }
                                            "BY_NOT_COMPLETED" -> {
                                                Text(
                                                    text = "By Not Completed",
                                                    color = MaterialTheme.colorScheme.onSurface
                                                )
                                            }
                                        }
                                    }
                                )
                            }
                        }


                    }

                    LazyColumn(
                        contentPadding = paddingValues,
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(1.dp)
                    ) {

                        items(state.todos) { todo ->
                            if (todo.userID == state.userId) {

                                TodoCard(
                                    todo = todo,
                                    todoState = state,
                                    onEvent = onEvent,
                                    index = state.todos.indexOf(todo),
                                    rewardViewModel = rewardViewModel,
                                    usersViewModel = usersViewModel,
                                    showToast = showToast

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
            var priorityExpandedMenu by remember { mutableStateOf(false) }
            var prioritySelection by remember { mutableStateOf("") }
            var titleError by remember { mutableStateOf(false) }
            var descriptionError by remember { mutableStateOf(false) }

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







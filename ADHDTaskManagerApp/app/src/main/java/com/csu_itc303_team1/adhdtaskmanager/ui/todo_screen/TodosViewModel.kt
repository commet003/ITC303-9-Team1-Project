package com.csu_itc303_team1.adhdtaskmanager.ui.todo_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csu_itc303_team1.adhdtaskmanager.data.TodoStatus
import com.csu_itc303_team1.adhdtaskmanager.data.TodoSummary
import com.csu_itc303_team1.adhdtaskmanager.data.User
import com.csu_itc303_team1.adhdtaskmanager.usecase.GetOngoingTodoSummariesUseCase
import com.csu_itc303_team1.adhdtaskmanager.usecase.ToggleTodoStarStateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.time.Clock
import javax.inject.Inject

@HiltViewModel
class TodosViewModel @Inject constructor(
    private val currentUser: User,
    val clock: Clock,
    getOngoingTodoSummariesUseCase: GetOngoingTodoSummariesUseCase,
    private val toggleTodoStarStateUseCase: ToggleTodoStarStateUseCase
) : ViewModel() {

    private val statusExpanded = MutableStateFlow(TodoStatus.values().associateWith { true })

    val statusGroups = combine(
        statusExpanded,
        getOngoingTodoSummariesUseCase(currentUser.id)
    ) { statuses, summaries ->
        statuses.mapValues { (status, expanded) ->
            TodoStatusGroup(
                expanded = expanded,
                summaries = summaries.filter { it.status == status }
            )
        }
    }

    fun toggleTodoStarState(todoId: Long) {
        viewModelScope.launch {
            toggleTodoStarStateUseCase(todoId, currentUser)
        }
    }

    fun toggleStatusExpanded(status: TodoStatus) {
        statusExpanded.value = statusExpanded.value.mapValues { (s, expanded) ->
            if (status == s) !expanded else expanded
        }
    }
}

class TodoStatusGroup(
    val expanded: Boolean,
    val summaries: List<TodoSummary>
)
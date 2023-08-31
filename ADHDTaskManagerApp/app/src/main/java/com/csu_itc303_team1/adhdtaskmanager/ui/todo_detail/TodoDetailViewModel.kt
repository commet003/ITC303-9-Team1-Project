package com.csu_itc303_team1.adhdtaskmanager.ui.todo_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csu_itc303_team1.adhdtaskmanager.data.User
import com.csu_itc303_team1.adhdtaskmanager.usecase.FindTodoDetailUseCase
import com.csu_itc303_team1.adhdtaskmanager.usecase.ToggleTodoStarStateUseCase
import com.csu_itc303_team1.adhdtaskmanager.utils.WhileUiSubscribed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TodoDetailViewModel @Inject constructor(
    private val findTodoDetailUseCase: FindTodoDetailUseCase,
    private val toggleTodoStarStateUseCase: ToggleTodoStarStateUseCase,
    private val currentUser: User
) : ViewModel() {

    var todoId = MutableStateFlow(0L)

    @OptIn(ExperimentalCoroutinesApi::class)
    val detail = todoId.transformLatest { id ->
        emitAll(findTodoDetailUseCase(id))
    }.stateIn(viewModelScope, WhileUiSubscribed, null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val starred = detail.mapLatest { detail ->
        detail?.starUsers?.contains(currentUser)
    }.stateIn(viewModelScope, WhileUiSubscribed, false)


    fun setTodoId(id: Long) {
        todoId.value = id
    }

    fun getTodoDetails() {
        if (todoId.value <= 0L) return
        viewModelScope.launch {
            findTodoDetailUseCase(todoId.value)
        }
    }

    fun toggleTodoStarState() {
        if (todoId.value <= 0L) return
        viewModelScope.launch {
            toggleTodoStarStateUseCase(todoId.value, currentUser)
        }
    }
}


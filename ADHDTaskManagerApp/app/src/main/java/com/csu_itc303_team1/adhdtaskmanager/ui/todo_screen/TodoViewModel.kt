package com.csu_itc303_team1.adhdtaskmanager.ui.todo_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csu_itc303_team1.adhdtaskmanager.Priority
import com.csu_itc303_team1.adhdtaskmanager.SortType
import com.csu_itc303_team1.adhdtaskmanager.Todo
import com.csu_itc303_team1.adhdtaskmanager.TodoDao
import com.csu_itc303_team1.adhdtaskmanager.TodoEvent
import com.csu_itc303_team1.adhdtaskmanager.TodoState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class TodoViewModel(
    private val todoDao: TodoDao
): ViewModel() {

    private val _state = MutableStateFlow(TodoState())
    private val _sortType = MutableStateFlow(SortType.BY_DATE)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _todos = _sortType
        .flatMapLatest { sortType ->
            when (sortType) {
                SortType.BY_DATE -> todoDao.sortByDueDate()
                SortType.BY_PRIORITY -> todoDao.sortByPriority()
                SortType.BY_TIME -> todoDao.sortByDueTime()
                SortType.BY_COMPLETED -> todoDao.sortByCompleted()
                SortType.BY_NOT_COMPLETED -> todoDao.sortByNotCompleted()
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val state = combine(_state, _sortType, _todos) { state, sortType, todos ->
        state.copy(
            todos = todos,
            sortType = sortType
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TodoState())

    fun onEvent(event: TodoEvent) {
        when (event) {
            // Update Todo
            is TodoEvent.updateTodo -> {
                val title = state.value.title
                val description = state.value.description
                val priority = state.value.priority
                val dueDate = state.value.dueDate
                val dueTime = state.value.dueTime
                val userId = state.value.userId
                val id = state.value.id

                if (title.isBlank() || description.isBlank()) {
                    return
                }

                val todo = Todo(
                    id = id,
                    title = title,
                    description = description,
                    priority = priority,
                    dueDate = dueDate,
                    dueTime = dueTime,
                    userID = userId
                )

                viewModelScope.launch {
                    todoDao.updateTodo(todo)
                }

                _state.update {
                    it.copy(
                        title = "",
                        description = "",
                        priority = Priority.LOW,
                        dueDate = "",
                        dueTime = "",
                        showDialog = false,
                        showEditTodoDialog = false
                    )
                }
            }

            // Toggle isClicked state of the todo
            is TodoEvent.toggleIsClicked -> {
                viewModelScope.launch {
                    todoDao.updateTodo(
                        event.todo.copy(
                            isClicked = !event.todo.isClicked
                        )
                    )
                }
            }


            // Delete Todo
            is TodoEvent.deleteTodo -> {
                viewModelScope.launch {
                    todoDao.deleteTodo(event.todo)
                }
            }

            TodoEvent.showDialog -> {
                _state.update {
                    it.copy(showDialog = true)
                }
            }

            TodoEvent.hideDialog -> {
                _state.update {
                    it.copy(showDialog = false)
                }
            }

            // Show edit date
            TodoEvent.showEditDateSelector -> {
                _state.update {
                    it.copy(showEditDateSelector = true)
                }
            }

            // Hide edit date
            TodoEvent.hideEditDateSelector -> {
                _state.update {
                    it.copy(showEditDateSelector = false)
                }
            }

            // Show edit time
            TodoEvent.showEditTimeSelector -> {
                _state.update {
                    it.copy(showEditTimeSelector = true)
                }
            }

            // Hide edit time
            TodoEvent.hideEditTimeSelector -> {
                _state.update {
                    it.copy(showEditTimeSelector = false)
                }
            }


            TodoEvent.saveTodo -> {
                val title = state.value.title
                val description = state.value.description
                val priority = state.value.priority
                val dueDate = state.value.dueDate
                val dueTime = state.value.dueTime
                val userId = state.value.userId

                if (title.isBlank() || description.isBlank()) {
                    return
                }

                val todo = Todo(
                    title = title,
                    description = description,
                    priority = priority,
                    dueDate = dueDate,
                    dueTime = dueTime,
                    userID = userId
                )

                viewModelScope.launch {
                    todoDao.insertTodo(todo)
                }

                _state.update {
                    it.copy(
                        title = "",
                        description = "",
                        priority = Priority.LOW,
                        dueDate = "",
                        dueTime = "",
                        showDialog = false,
                        showEditTodoDialog = false
                    )
                }
            }

            is TodoEvent.setDescription -> {
                _state.update {
                    it.copy(description = event.description)
                }
            }

            is TodoEvent.setDueDate -> {
                _state.update {
                    it.copy(dueDate = event.dueDate)
                }
            }

            is TodoEvent.setDueTime -> {
                _state.update {
                    it.copy(dueTime = event.dueTime)
                }
            }

            is TodoEvent.setPriority -> {
                _state.update {
                    it.copy(priority = event.priority)
                }
            }

            is TodoEvent.setTitle -> {
                _state.update {
                    it.copy(title = event.title)
                }
            }

            is TodoEvent.sortBy -> {
                _sortType.value = event.sortType
            }

            TodoEvent.hideEditTodoDialog -> {
                _state.update {
                    it.copy(showEditTodoDialog = false)
                }
            }

            TodoEvent.showEditTodoDialog -> {
                _state.update {
                    it.copy(showEditTodoDialog = true)
                }
            }

            TodoEvent.hideDateSelector -> {
                _state.update {
                    it.copy(showDateSelector = false)
                }
            }

            TodoEvent.showDateSelector -> {
                _state.update {
                    it.copy(showDateSelector = true)
                }
            }

            TodoEvent.hideTimeSelector -> {
                _state.update {
                    it.copy(showTimeSelector = false)
                }
            }

            TodoEvent.showTimeSelector -> {
                _state.update {
                    it.copy(showTimeSelector = true)
                }
            }

            // Toggle the completed state of the todo
            is TodoEvent.toggleCompleted -> {
                viewModelScope.launch {
                    todoDao.updateTodo(
                        event.todo.copy(
                            isCompleted = !event.todo.isCompleted,
                            completionDate = if (event.todo.isCompleted) {
                                ""
                            } else {
                                LocalDateTime.now().toString()
                            }
                        )
                    )
                }
            }

            is TodoEvent.setUserId -> {
                _state.update {
                    it.copy(userId = event.userId)
                }
            }

            is TodoEvent.getTodoById -> {
                viewModelScope.launch {
                    todoDao.getTodoById(event.id)
                }
            }

            else -> {}
        }
    }
}
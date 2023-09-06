package com.csu_itc303_team1.adhdtaskmanager.ui.todo_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csu_itc303_team1.adhdtaskmanager.utils.database_dao.TodoDao
import com.csu_itc303_team1.adhdtaskmanager.utils.states.TodoState
import com.csu_itc303_team1.adhdtaskmanager.utils.todo_utils.Priority
import com.csu_itc303_team1.adhdtaskmanager.utils.todo_utils.SortOrder
import com.csu_itc303_team1.adhdtaskmanager.utils.todo_utils.Todo
import com.csu_itc303_team1.adhdtaskmanager.utils.todo_utils.TodoEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class TodoViewModel(
    private val todoDao: TodoDao
): ViewModel() {

    private val _state = MutableStateFlow(TodoState())
    private var _sortOrder = MutableStateFlow(SortOrder.BY_DEADLINE)

    init {
        viewModelScope.launch {
            todoDao.getAllTodos().collect {
                _state.value.todos = filterSortTasks(
                    todos = it,
                    showCompleted = false,
                    sortOrder = _sortOrder.value)
            }
        }
    }

    private fun filterSortTasks(
        todos: List<Todo>,
        showCompleted: Boolean,
        sortOrder: SortOrder
    ): List<Todo> {
        // filter the tasks
        val filteredTasks = if (showCompleted) {
            todos
        } else {
            todos.filter { !it.completed }
        }
        // sort the tasks
        return when (sortOrder) {
            SortOrder.NONE -> filteredTasks
            SortOrder.BY_DEADLINE -> filteredTasks.sortedWith(
                compareByDescending<Todo>{ it.dueTime }.thenBy{ it.dueDate }
            )
            SortOrder.BY_PRIORITY -> filteredTasks.sortedBy { it.priority }
            SortOrder.BY_DEADLINE_AND_PRIORITY -> filteredTasks.sortedWith(
                compareByDescending<Todo>
                { it.dueTime }.thenBy { it.dueDate}.thenBy { it.priority }
            )
            SortOrder.BY_CATEGORY -> filteredTasks.sortedBy { it.category }
            SortOrder.BY_DEADLINE_AND_CATEGORY -> filteredTasks.sortedWith(
                compareByDescending<Todo>
                { it.dueTime }.thenBy { it.dueDate }.thenBy { it.category }
            )
        }
    }

    val state = _state

    fun onEvent(event: TodoEvent) {
        when (event) {
            // Todo Title Error
            is TodoEvent.titleError -> {
                _state.update {
                    it.copy(
                        titleError = event.error
                    )
                }
            }

            is TodoEvent.ToggleLottieAnimation -> {
                _state.value = _state.value.copy(showLottieAnimation = event.show)
            }

            // Todo Description Error
            is TodoEvent.descriptionError -> {
                _state.update {
                    it.copy(
                        descriptionError = event.error
                    )
                }
            }

            // Update Todo
            is TodoEvent.updateTodo -> {
                viewModelScope.launch {
                    todoDao.updateTodo(
                        event.todo.copy(
                            title = _state.value.title,
                            description = _state.value.description,
                            priority = _state.value.priority,
                            category = _state.value.category,
                            dueDate = _state.value.dueDate,
                            dueTime = _state.value.dueTime,
                            userId = _state.value.userId
                        )
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
                val title = _state.value.title
                val description = _state.value.description
                val priority = _state.value.priority
                val category = _state.value.category
                val dueDate = _state.value.dueDate
                val dueTime = _state.value.dueTime
                val userId = _state.value.userId

                if (title.isEmpty()) {
                    TodoEvent.titleError(true)
                    return
                }
                if (description.isEmpty()) {
                    TodoEvent.descriptionError(true)
                    return
                }

                val todo = Todo(
                    title = title,
                    description = description,
                    priority = priority,
                    category = category,
                    dueDate = dueDate,
                    dueTime = dueTime,
                    userId = userId
                )

                viewModelScope.launch {
                    todoDao.insertTodo(todo)
                }

                _state.update {
                    it.copy(
                        title = "",
                        description = "",
                        priority = Priority.Low.value,
                        category = "",
                        dueDate = "",
                        dueTime = "",
                        showDialog = false,
                        showEditTodoDialog = false,
                        titleError = false,
                        descriptionError = false
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
                    it.copy(priority = event.priority.value)
                }
            }

            is TodoEvent.setCategory -> {
                _state.update {
                    it.copy(category = event.category.name)
                }
            }

            is TodoEvent.setTitle -> {
                _state.update {
                    it.copy(title = event.title)
                }
            }

            is TodoEvent.sortBy -> {
                _sortOrder.value = event.sortOrder
                filterSortTasks(
                    todos = _state.value.todos,
                    showCompleted = false,
                    sortOrder = _sortOrder.value
                )
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
                            completed = !event.todo.completed,
                            completedDate = if (event.todo.completed) {
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

            // Reset State Todos to values from database
            TodoEvent.resetTodos -> {
                viewModelScope.launch {
                    todoDao.getAllTodos().collect {
                        _state.value.todos = filterSortTasks(
                            todos = it,
                            showCompleted = false,
                            sortOrder = _sortOrder.value)
                    }
                }
            }

            // Reset State
            TodoEvent.resetState -> {
                _state.update {
                    it.copy(
                        title = "",
                        description = "",
                        priority = Priority.Low.value,
                        category = "",
                        dueDate = "",
                        dueTime = "",
                        showDialog = false,
                        showEditTodoDialog = false,
                        titleError = false,
                        descriptionError = false
                    )
                }
            }


            else -> {}
        }
    }
}